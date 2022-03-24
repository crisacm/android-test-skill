package com.github.crisacm.testskills.data.database.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) var uid: Long? = null,
    var idServer: Long = 0,
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var addressId: Long? = null,
    var phone: String = "",
    var website: String = "",
    var companyId: Long? = null
) : Parcelable
