package com.github.crisacm.testskills.data.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.crisacm.testskills.data.database.models.Address
import com.github.crisacm.testskills.data.database.models.Company
import com.github.crisacm.testskills.data.database.models.User
import com.github.crisacm.testskills.data.database.repositories.AddressRepo
import com.github.crisacm.testskills.data.database.repositories.CompanyRepo
import com.github.crisacm.testskills.data.database.repositories.UserRepo
import com.github.crisacm.testskills.data.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val mUserRepo: UserRepo,
    private val mAddressRepo: AddressRepo,
    private val mCompanyRepo: CompanyRepo
) : ViewModel() {

    private val _syncEvent: MutableSharedFlow<SyncEvent<Nothing>> = MutableSharedFlow()
    val syncEvent = _syncEvent.asSharedFlow()

    sealed class SyncEvent<out R> {
        object Syncing : SyncEvent<Nothing>()
        object SyncSuccessful : SyncEvent<Nothing>()
        data class SyncFailed(val msg: String) : SyncEvent<Nothing>()
    }

    init {
        viewModelScope.launch {
            when (val result = mUserRepo.syncUsers()) {
                is ApiResult.Success -> {
                    _syncEvent.emit(SyncEvent.Syncing)
                    val newUsers = mutableListOf<User>()

                    result.data.forEach { _usersResponse ->
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

                    _syncEvent.emit(SyncEvent.SyncSuccessful)
                }
                is ApiResult.Failure -> _syncEvent.emit(SyncEvent.SyncFailed("Failure: ${result.message}"))
                is ApiResult.Error -> _syncEvent.emit(SyncEvent.SyncFailed("Error: ${result.t.message}"))
            }
        }
    }

    fun getAllUsers(): Flow<List<User>> = mUserRepo.getAllOnFlow()
}