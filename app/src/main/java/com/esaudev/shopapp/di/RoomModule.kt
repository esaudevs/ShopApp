package com.esaudev.shopapp.di

import android.content.Context
import androidx.room.Room
import com.esaudev.shopapp.data.local.room.ShopAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ShopAppDatabase::class.java,
        "shop_app_database"
    )
        .build()

    @Singleton
    @Provides
    fun providesCartDao(
        database: ShopAppDatabase
    ) = database.cartDao()
}