package com.esaudev.shopapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.esaudev.shopapp.data.local.room.dao.CartDao
import com.esaudev.shopapp.data.local.room.model.CartItemEntity

@Database(
    entities = [
        CartItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ShopAppDatabase: RoomDatabase() {

    abstract fun cartDao(): CartDao
}