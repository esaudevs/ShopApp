package com.esaudev.shopapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.esaudev.shopapp.data.local.room.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Upsert
    suspend fun upsert(cartItemEntity: CartItemEntity)

    @Query("SELECT * FROM cart_items")
    fun observeAll(): Flow<List<CartItemEntity>>

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE id = :id")
    suspend fun addQuantityById(id: Int)

    @Query("UPDATE cart_items SET quantity = quantity - 1 WHERE id = :id")
    suspend fun removeQuantityById(id: Int)

    @Query("DELETE FROM cart_items")
    suspend fun truncateCart()

}