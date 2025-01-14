package com.example.creddemo

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val retrofit by lazy{


        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // connection timeout
            .readTimeout(60, TimeUnit.SECONDS)    // read timeout
            .build()

        Retrofit.Builder().baseUrl("https://api.mocklets.com/p6764/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val apiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }


}