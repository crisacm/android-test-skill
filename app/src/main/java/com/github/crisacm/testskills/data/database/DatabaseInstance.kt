package com.github.crisacm.testskills.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.crisacm.testskills.data.database.models.*

@Database(
    entities = [
        User::class,
        Address::class,
        Company::class,
        Post::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class DatabaseInstance : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun addressDao(): AddressDao
    abstract fun companyDao(): CompanyDao
    abstract fun postDao(): PostDao

    companion object {
        private const val DATABASE_NAME = "database.db"

        @Volatile
        private var INSTANCE: DatabaseInstance? = null

        fun getInstance(context: Context): DatabaseInstance =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context): DatabaseInstance =
            Room.databaseBuilder(
                context.applicationContext,
                DatabaseInstance::class.java,
                DATABASE_NAME
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .setJournalMode(JournalMode.TRUNCATE)
                .build()
    }
}