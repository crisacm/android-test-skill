package com.github.crisacm.testskills.data.database.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompanyDao {

    @Query("SELECT * FROM companies")
    suspend fun getAll(): List<Company>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(company: Company): Long
}