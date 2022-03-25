package com.github.crisacm.testskills.data.database.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts WHERE userId = :userId")
    fun getAllByOnFLow(userId: Long): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE idServer = :idServer LIMIT 1")
    suspend fun getBy(idServer: Long): Post?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: List<Post>)
}