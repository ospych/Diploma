package com.example.intourist.ui.favourites

import android.app.Application
import androidx.lifecycle.*
import com.example.intourist.room.RepositoryRoom
import com.example.intourist.room.TourDatabase
import com.example.intourist.room.data.TourRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel (application: Application): AndroidViewModel(application){
    val readAll: LiveData<List<TourRoom>>
    private val repository: RepositoryRoom

    init {
        val itemDao = TourDatabase.getDatabase(application).toursDao()
        repository = RepositoryRoom(itemDao)
        readAll = repository.allItems
    }
    fun deleteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(id)
        }
    }

    fun deleteItems(tours: TourRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(tours)
        }
    }

    fun addItems(tours: TourRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertItem(tours)
        }
    }
}