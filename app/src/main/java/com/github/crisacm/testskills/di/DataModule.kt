package com.github.crisacm.testskills.di

import com.github.crisacm.testskills.data.database.DatabaseInstance
import com.github.crisacm.testskills.data.database.repositories.*
import com.github.crisacm.testskills.data.network.WebServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun userRepoProvider(
        webServiceApi: WebServiceApi,
        databaseInstance: DatabaseInstance
    ): UserRepo = UserRepoImp(webServiceApi, databaseInstance.userDao())

    @Provides
    @Singleton
    fun addressRepoProvider(
        databaseInstance: DatabaseInstance
    ): AddressRepo = AddressRepoImp(databaseInstance.addressDao())

    @Provides
    @Singleton
    fun companyRepoProvider(
        databaseInstance: DatabaseInstance
    ): CompanyRepo = CompanyRepoImp(databaseInstance.companyDao())

    @Provides
    @Singleton
    fun postRepoProvider(
        webServiceApi: WebServiceApi,
        databaseInstance: DatabaseInstance
    ): PostRepo = PostRepoImp(webServiceApi, databaseInstance.postDao())
}