package com.github.crisacm.testskills.di

import android.content.Context
import com.github.crisacm.testskills.data.database.DatabaseInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun databaseInstanceProvider(
        @ApplicationContext context: Context
    ): DatabaseInstance = DatabaseInstance.getInstance(context)
}