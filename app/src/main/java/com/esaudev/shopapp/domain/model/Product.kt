package com.esaudev.shopapp.domain.model

data class Product (
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
)