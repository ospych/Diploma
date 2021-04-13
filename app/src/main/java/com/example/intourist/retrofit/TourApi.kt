package com.example.intourist.retrofit

import androidx.lifecycle.LiveData
import com.example.intourist.retrofit.model.Category
import com.example.intourist.retrofit.model.Tour
import com.example.intourist.retrofit.model.TourDetail
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.util.*

interface TourApi {

    @GET("/")
    suspend fun getTours(): Response<List<Tour>>

    @GET("/category")
    suspend fun getCategories(): Response<List<Category>>

    @GET("/category")
    fun getCategory(): Call<MutableList<Category>>

    @GET("/{id}")
    suspend fun getSingleTour(
        @Path("id") number: Int
    ) : Response<TourDetail>

    @GET("/{id}")
    fun getTour(
        @Path("id") number: Int
    ) : Call<TourDetail>

    @GET("/")
    suspend fun getFilteredTours(
        @Query("category") category: String,
        @Query("date") date: String
    ) : Response<List<Tour>>

}