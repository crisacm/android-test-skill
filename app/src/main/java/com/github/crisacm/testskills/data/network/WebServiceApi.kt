package com.github.crisacm.testskills.data.network

import com.github.crisacm.testskills.data.network.models.PostsResponse
import com.github.crisacm.testskills.data.network.models.UsersResponse
import com.github.crisacm.testskills.utils.UrlHelper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServiceApi {

    @GET(UrlHelper.USERS)
    fun getUsers(): Call<List<UsersResponse?>?>

    @GET(UrlHelper.POSTS)
    fun getPostsByUser(
        @Query("userId") id: String
    ): Call<List<PostsResponse?>?>
}