package com.esaudev.shopapp.domain.model

data class CartItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val price: Double,
    val quantity: Int
)
