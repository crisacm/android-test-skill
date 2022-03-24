package com.github.crisacm.testskills.data.network.models

import com.google.gson.annotations.SerializedName

data class PostsResponse(
    @SerializedName("userId") var userId: Long? = null,
    @SerializedName("id") var id: Long? = null,
    @SerializedName("title") var title: String = "",
    @SerializedName("body") var body: String = ""
)