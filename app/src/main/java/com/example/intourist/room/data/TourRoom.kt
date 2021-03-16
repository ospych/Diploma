package com.example.intourist.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favourite")
data class TourRoom(
        @PrimaryKey
        val id: Int,
        val title: String,
        val price: Int,
        val date: String,
        val image: String,
        val places: String
) : Serializable
