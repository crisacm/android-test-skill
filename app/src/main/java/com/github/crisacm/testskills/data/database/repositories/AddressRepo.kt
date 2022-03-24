package com.github.crisacm.testskills.data.database.repositories

import com.github.crisacm.testskills.data.database.models.Address

interface AddressRepo {

    suspend fun insert(address: Address): Long
}