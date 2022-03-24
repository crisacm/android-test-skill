package com.github.crisacm.testskills.data.database.repositories

import com.github.crisacm.testskills.data.database.models.Address
import com.github.crisacm.testskills.data.database.models.AddressDao

class AddressRepoImp constructor(
    private val mAddressDao: AddressDao
) : AddressRepo {

    override suspend fun insert(address: Address): Long = mAddressDao.insert(address)
}