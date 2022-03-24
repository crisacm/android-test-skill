package com.github.crisacm.testskills.data.database.repositories

import com.github.crisacm.testskills.data.database.models.User
import com.github.crisacm.testskills.data.database.models.UserDao
import com.github.crisacm.testskills.data.network.ApiResult
import com.github.crisacm.testskills.data.network.WebServiceApi
import com.github.crisacm.testskills.data.network.models.UsersResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepoImp constructor(
    private val mWebServiceApi: WebServiceApi,
    private val mUserDao: UserDao
) : UserRepo {

    override suspend fun insert(mutableList: MutableList<User>) = mUserDao.insert(mutableList)

    override suspend fun getBy(idServer: Long): User? = mUserDao.getBy(idServer)

    override fun getAllOnFlow(): Flow<List<User>> = mUserDao.getAllOnFlow()

    override suspend fun syncUsers(): ApiResult<List<UsersResponse?>> {
        return suspendCoroutine {
            mWebServiceApi.getUsers().enqueue(object : Callback<List<UsersResponse?>?> {
                override fun onResponse(call: Call<List<UsersResponse?>?>, response: Response<List<UsersResponse?>?>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            it.resume(ApiResult.Success(response.body()!!))
                        } else {
                            it.resume(ApiResult.Failure("Body are null"))
                        }
                    } else {
                        it.resume(ApiResult.Failure("Response not are successful"))
                    }
                }

                override fun onFailure(call: Call<List<UsersResponse?>?>, t: Throwable) {
                    t.printStackTrace()
                    it.resume(ApiResult.Error(t))
                }
            })
        }
    }
}