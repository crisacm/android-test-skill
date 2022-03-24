package com.github.crisacm.testskills.data.database.repositories

import com.github.crisacm.testskills.data.database.models.User
import com.github.crisacm.testskills.data.network.ApiResult
import com.github.crisacm.testskills.data.network.models.UsersResponse
import kotlinx.coroutines.flow.Flow

interface UserRepo {

    suspend fun insert(mutableList: MutableList<User>)

    suspend fun getBy(idServer: Long): User?

    fun getAllOnFlow(): Flow<List<User>>

    suspend fun syncUsers(): ApiResult<List<UsersResponse?>>
}