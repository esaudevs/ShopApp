package com.esaudev.shopapp.di

import com.esaudev.shopapp.data.remote.api.FakeStoreApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton
import retrofit2.converter.moshi.MoshiConverterFactory

const val FAKE_API_BASE_URL = "https://fakestoreapi.com"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(
        moshi: Moshi
    ): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun providePokemonApi(
        client: OkHttpClient,
        converterFactory: MoshiConverterFactory
    ): FakeStoreApi {
        return Retrofit.Builder()
            .baseUrl(FAKE_API_BASE_URL)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create()
    }
}