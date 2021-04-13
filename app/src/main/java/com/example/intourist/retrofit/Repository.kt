package com.example.intourist.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.intourist.retrofit.model.Category
import com.example.intourist.retrofit.model.Tour
import com.example.intourist.retrofit.model.TourDetail
import retrofit2.Response
import java.lang.Exception
import java.time.LocalDate
import java.util.*

class Repository {
    suspend fun getTours(): Response<List<Tour>> {
        return RetrofitInstance.api.getTours()
    }

    suspend fun getCategories(): Response<List<Category>> {
        return RetrofitInstance.api.getCategories()
    }

    suspend fun getSingleTour(number: Int): Response<TourDetail> {
        return RetrofitInstance.api.getSingleTour(number)
    }

    suspend fun getFilteredTour(category : String, date: String) : Response<List<Tour>> {
        return RetrofitInstance.api.getFilteredTours(category, date)
    }
}