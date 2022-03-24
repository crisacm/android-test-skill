package com.github.crisacm.testskills.data.database.repositories

import com.github.crisacm.testskills.data.database.models.Post
import com.github.crisacm.testskills.data.database.models.PostDao
import com.github.crisacm.testskills.data.network.ApiResult
import com.github.crisacm.testskills.data.network.WebServiceApi
import com.github.crisacm.testskills.data.network.models.PostsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PostRepoImp constructor(
    private val mWebServiceApi: WebServiceApi,
    private val mPostDao: PostDao
) : PostRepo {

    override suspend fun insert(mutableList: MutableList<Post>) = mPostDao.insert(mutableList)

    override suspend fun getBy(idServer: Long): Post? = mPostDao.getBy(idServer)

    override fun getAllByOnFlow(userId: Long): Flow<List<Post>> = mPostDao.getAllByOnFLow(userId)

    override suspend fun syncPostsByUser(userId: Long): ApiResult<List<PostsResponse?>> {
        return suspendCoroutine {
            mWebServiceApi.getPostsByUser(userId.toString()).enqueue(object : Callback<List<PostsResponse?>?> {
                override fun onResponse(call: Call<List<PostsResponse?>?>, response: Response<List<PostsResponse?>?>) {
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

                override fun onFailure(call: Call<List<PostsResponse?>?>, t: Throwable) {
                    t.printStackTrace()
                    it.resume(ApiResult.Error(t))
                }
            })
        }
    }
}