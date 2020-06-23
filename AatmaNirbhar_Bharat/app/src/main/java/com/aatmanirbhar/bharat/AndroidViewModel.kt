package com.aatmanirbhar.bharat

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class AndroidViewModel:ViewModel() {
    private val mService  = RetrofitService()
    fun getApplist(): MutableLiveData<ApiResponse>? {
        return mService.getApplist()
    }
}