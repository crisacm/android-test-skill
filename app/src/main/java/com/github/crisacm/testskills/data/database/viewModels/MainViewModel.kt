package com.github.crisacm.testskills.data.database.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.crisacm.testskills.data.database.models.Address
import com.github.crisacm.testskills.data.database.models.Company
import com.github.crisacm.testskills.data.database.models.User
import com.github.crisacm.testskills.data.database.repositories.AddressRepo
import com.github.crisacm.testskills.data.database.repositories.CompanyRepo
import com.github.crisacm.testskills.data.database.repositories.UserRepo
import com.github.crisacm.testskills.data.network.ApiResult
import com.github.crisacm.testskills.data.network.models.UsersResponse
import com.github.crisacm.testskills.ui.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mUserRepo: UserRepo,
    private val mAddressRepo: AddressRepo,
    private val mCompanyRepo: CompanyRepo
) : ViewModel() {

    private val _uiEvent: MutableSharedFlow<SyncEvent<Nothing>> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    sealed class SyncEvent<out R> {
        object Syncing : SyncEvent<Nothing>()
        object SyncSuccessful : SyncEvent<Nothing>()
        data class SyncFailed(val msg: String) : SyncEvent<Nothing>()
    }

    fun syncAndValidateUsers() {
        viewModelScope.launch {
            when (val result = mUserRepo.syncUsers()) {
                is ApiResult.Success -> validateAndSaveUsers(result.data)
                is ApiResult.Failure -> Log.w(MainActivity.TAG, "Failure: ${result.message}")
                is ApiResult.Error -> result.t.printStackTrace()
            }
        }
    }

    fun validateAndSaveUsers(list: List<UsersResponse?>) {
        viewModelScope.launch {
            _uiEvent.emit(SyncEvent.Syncing)
            val newUsers = mutableListOf<User>()

            list.forEach { _usersResponse ->
                _usersResponse?.also {
                    it.id?.let { _id ->
                        if (mUserRepo.getBy(_id) == null) {
                            var addressUid: Long? = null

                            it.address?.let { _address ->
                                Address().apply {
                                    this.street = _address.street
                                    this.suite = _address.suite
                                    this.city = _address.city
                                    this.lat = _address.geo?.lat
                                    this.lng = _address.geo?.lng
                                }.also { it2 -> addressUid = mAddressRepo.insert(it2) }
                            }

                            var companyUid: Long? = null

                            it.company?.let { _company ->
                                Company().apply {
                                    this.name = _company.name
                                    this.catchPhrase = _company.catchPhrase
                                    this.bs = _company.bs
                                }.also { it2 -> companyUid = mCompanyRepo.insert(it2) }
                            }

                            User().apply {
                                it.id?.let { it2 -> this.idServer = it2 }
                                this.name = it.name
                                this.username = it.username
                                this.email = it.email
                                this.addressId = addressUid
                                this.phone = it.phone
                                this.website = it.website
                                this.companyId = companyUid
                            }.also { _user -> newUsers.add(_user) }
                        }
                    }
                }
            }

            if (newUsers.isNotEmpty()) {
                mUserRepo.insert(newUsers)
            }

            _uiEvent.emit(SyncEvent.SyncSuccessful)
        }
    }

    fun getAllUsers(): Flow<List<User>> = mUserRepo.getAllOnFlow()
}