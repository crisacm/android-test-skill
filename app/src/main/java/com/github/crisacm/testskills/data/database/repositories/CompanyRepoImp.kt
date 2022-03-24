package com.github.crisacm.testskills.data.database.repositories

import com.github.crisacm.testskills.data.database.models.Company
import com.github.crisacm.testskills.data.database.models.CompanyDao

class CompanyRepoImp constructor(
    private val mCompanyDao: CompanyDao
) : CompanyRepo {

    override suspend fun insert(company: Company): Long = mCompanyDao.insert(company)
}