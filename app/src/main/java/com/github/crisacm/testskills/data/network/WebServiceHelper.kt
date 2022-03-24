package com.github.crisacm.testskills.data.network

import android.util.Log
import com.github.crisacm.testskills.BuildConfig
import com.github.crisacm.testskills.utils.UrlHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WebServiceHelper {

    private const val TAG = "WebserviceHelper"

    private val interceptor = HttpLoggingInterceptor { Log.v(TAG, it) }
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    fun getRetrofitInstance(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(UrlHelper.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        if (BuildConfig.DEBUG) {
            retrofit.client(client)
        }

        return retrofit.build()
    }
}