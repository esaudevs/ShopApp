package com.esaudev.shopapp.domain.repository

import com.esaudev.shopapp.domain.model.CartItem
import com.esaudev.shopapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductById(id: Int): Result<Product>
    suspend fun addToCart(product: Product)
    fun observeCart(): Flow<List<CartItem>>
    suspend fun deleteFromCart(id: Int)
    suspend fun addQuantityToCart(id: Int)
    suspend fun removeQuantityToCart(id: Int)
    suspend fun completeOrder()
}