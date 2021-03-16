package com.example.intourist.retrofit.model

data class Tour(
    val id: Int,
    val title: String,
    val price: Int,
    val date: String,
    val image: String,
    val places: String
)
