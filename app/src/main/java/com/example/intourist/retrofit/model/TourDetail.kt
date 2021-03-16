package com.example.intourist.retrofit.model

data class TourDetail(
    val category: String,
    val date: String,
    val desc: String,
    val id: Int,
    val image: String,
    val payment: String,
    val places: String,
    val price: Int,
    val shots: List<Shot>,
    val title: String
)