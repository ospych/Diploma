package com.example.intourist.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intourist.retrofit.Repository
import com.example.intourist.retrofit.model.Category
import com.example.intourist.retrofit.model.Tour
import com.example.intourist.retrofit.model.TourDetail
import com.example.intourist.utils.log
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDate
import java.util.*

class RetrofitViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<List<Tour>>> = MutableLiveData()
    val myResponseSingle: MutableLiveData<Response<TourDetail>> = MutableLiveData()
    val myResponseCategory: MutableLiveData<Response<List<Category>>> = MutableLiveData()
    val myResponseFiltered: MutableLiveData<Response<List<Tour>>> = MutableLiveData()

    fun getTours() {
        viewModelScope.launch {
            val response: Response<List<Tour>> = repository.getTours()
            myResponse.value = response
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            val response: Response<List<Category>> = repository.getCategories()
            try {
                myResponseCategory.value = response
            } catch (e :Exception){
                log(e.toString())
            }
        }
    }

    fun getSingleTour(number: Int) {
        viewModelScope.launch {
            val response: Response<TourDetail> = repository.getSingleTour(number)
            myResponseSingle.value = response
        }
    }

    fun getFilteredTour(category : String ,date: String) {
        viewModelScope.launch {
            val response: Response<List<Tour>> = repository.getFilteredTour(category, date)
            myResponseFiltered.value = response
        }
    }

}