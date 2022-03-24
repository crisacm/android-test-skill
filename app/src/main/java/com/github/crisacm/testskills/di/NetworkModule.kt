package com.github.crisacm.testskills.di

import com.github.crisacm.testskills.data.network.WebServiceApi
import com.github.crisacm.testskills.data.network.WebServiceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun retrofitInstanceProvider(): Retrofit = WebServiceHelper.getRetrofitInstance()

    @Provides
    @Singleton
    fun webServiceApiProvider(
        retrofit: Retrofit
    ): WebServiceApi {
        return retrofit.create(WebServiceApi::class.java)
    }
}