package com.example.intourist.room

import androidx.lifecycle.LiveData
import com.example.intourist.room.data.TourRoom

class RepositoryRoom(private val dao: ToursDao) {
    suspend fun insertItem(tours: TourRoom) {
        dao.insert(tours)
    }

    suspend fun deleteItem(tours: TourRoom) {
        dao.delete(tours)
    }

    suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }

//    suspend fun testR(id: Int) {
//        dao.test(id)
//    }

    val allItems: LiveData<List<TourRoom>> = dao.getAllItems()
}