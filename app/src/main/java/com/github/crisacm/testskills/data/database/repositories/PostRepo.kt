package com.github.crisacm.testskills.data.database.repositories

import com.github.crisacm.testskills.data.database.models.Post
import com.github.crisacm.testskills.data.network.ApiResult
import com.github.crisacm.testskills.data.network.models.PostsResponse
import kotlinx.coroutines.flow.Flow

interface PostRepo {

    suspend fun insert(mutableList: MutableList<Post>)

    suspend fun getBy(idServer: Long): Post?

    fun getAllByOnFlow(userId: Long): Flow<List<Post>>

    suspend fun syncPostsByUser(userId: Long): ApiResult<List<PostsResponse?>>
}