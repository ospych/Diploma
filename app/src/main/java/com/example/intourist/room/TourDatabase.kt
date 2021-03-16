package com.example.intourist.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.intourist.room.data.TourRoom

@Database(entities = [TourRoom::class], version = 1, exportSchema = false)
abstract class TourDatabase: RoomDatabase() {
    abstract fun toursDao(): ToursDao

    companion object {
        @Volatile
        private var INSTANCE: TourDatabase? = null

        fun getDatabase(context: Context): TourDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TourDatabase::class.java,
                        "favourite"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}