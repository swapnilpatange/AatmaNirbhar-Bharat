package com.aatmanirbhar.bharat

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    val liveUserResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    companion object Factory {
        var gson = GsonBuilder().setLenient().create()
        fun create(): ApiInterface {
            Log.e("retrofit","create")
            val retrofit = Retrofit.Builder()
                .baseUrl("http://dietprovider.in/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }

    fun getApplist(): MutableLiveData<ApiResponse>? {
        val retrofitCall  = create().getApplist()
        retrofitCall.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable?) {
                Log.e("on Failure :", "retrofit error")
            }
            override fun onResponse(call: Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                val list  = response.body()

                liveUserResponse?.value = list

            }
        })
        return liveUserResponse
    }
}