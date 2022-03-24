package com.github.crisacm.testskills.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class Company(
    @PrimaryKey(autoGenerate = true) var uid: Long? = null,
    var name: String = "",
    var catchPhrase: String = "",
    var bs: String = ""
)
