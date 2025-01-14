package com.example.creddemo

import com.example.creddemo.DataClass.Data
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("test_mint")
        fun getData() : Call<Data>


}