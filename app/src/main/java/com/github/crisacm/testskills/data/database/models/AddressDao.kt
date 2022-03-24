package com.github.crisacm.testskills.data.database.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AddressDao {

    @Query("SELECT * FROM addresses")
    suspend fun getAll(): List<Address>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(address: Address): Long
}