package com.example.intourist.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.intourist.room.data.TourRoom

@Dao
interface ToursDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tours: TourRoom)

    @Delete()
    suspend fun delete(tours: TourRoom)

    @Query("DELETE FROM favourite WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM favourite")
    fun getAllItems(): LiveData<List<TourRoom>>

//    @Query("SELECT title FROM favourite WHERE id = :id")
//    fun test(id: Int): LiveData<TourRoom>
}