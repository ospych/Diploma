package com.example.intourist.retrofit

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://osmchab.pythonanywhere.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TourApi by lazy {
        retrofit.create(TourApi::class.java)
    }
}