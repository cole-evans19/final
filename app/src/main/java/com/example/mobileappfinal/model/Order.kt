package com.example.mobileappfinal.model

data class Order(
    val restaurant: String,
    val food: String,
    val address: String,
    val dateTime: String,
    val price: Double,
    val quantity: Int
)
