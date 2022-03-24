package com.github.crisacm.testskills.data.database.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE  idServer = :idServer LIMIT 1")
    suspend fun getBy(idServer: Long): User?

    @Query("SELECT * FROM users")
    fun getAllOnFlow(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mutableList: MutableList<User>)
}