package com.github.crisacm.testskills.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class Address(
    @PrimaryKey(autoGenerate = true) var uid: Long? = null,
    var street: String = "",
    var suite: String = "",
    var city: String = "",
    var zipcode: String = "",
    var lat: Double? = null,
    var lng: Double? = null
)
