package com.example.intourist.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.intourist.room.data.TourRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application): AndroidViewModel(application){
    val readAllItems: LiveData<List<TourRoom>>
    private val repository: RepositoryRoom

    init {
        val itemDao = TourDatabase.getDatabase(application).toursDao()
        repository = RepositoryRoom(itemDao)
        readAllItems = repository.allItems
    }

    fun addItems(tours: TourRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertItem(tours)
        }
    }

    fun deleteItems(tours: TourRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(tours)
        }
    }

    fun deleteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(id)
        }
    }

//    fun testRR(id: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.testR(id)
//        }
//    }
}