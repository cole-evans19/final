package com.example.mobileappfinal.model

import java.util.Date

data class Order(
    val date: Date,
    val restaurant: String,
    val price: Double,
    val quantity: Int,
    val address: String

)