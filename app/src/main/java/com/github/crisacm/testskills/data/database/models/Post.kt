package com.github.crisacm.testskills.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) var uid: Long? = null,
    var userId: Long = 0,
    var idServer: Long = 0,
    var title: String = "",
    var body: String = ""
)
