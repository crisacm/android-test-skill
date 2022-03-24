package com.github.crisacm.testskills.data.database.repositories

import com.github.crisacm.testskills.data.database.models.Company

interface CompanyRepo {

    suspend fun insert(company: Company): Long
}