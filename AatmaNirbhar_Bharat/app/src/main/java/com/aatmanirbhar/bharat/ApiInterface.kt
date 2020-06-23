package com.aatmanirbhar.bharat

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("appdata.json")
    fun getApplist(): Call<ApiResponse>

}