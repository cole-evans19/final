package com.example.mobileappfinal.model

data class Restaurant(
    val name: String,
    val location: String,
    val photos: List<String>,
    val foodItems: Map<String, Double>
)


