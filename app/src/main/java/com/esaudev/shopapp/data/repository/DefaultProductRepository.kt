package com.esaudev.shopapp.data.repository

import com.esaudev.shopapp.data.local.room.dao.CartDao
import com.esaudev.shopapp.data.local.room.model.CartItemEntity
import com.esaudev.shopapp.data.remote.dto.product.ProductDto
import com.esaudev.shopapp.data.remote.dto.product.toProduct
import com.esaudev.shopapp.domain.model.CartItem
import com.esaudev.shopapp.domain.model.Product
import com.esaudev.shopapp.data.remote.api.FakeStoreApi
import com.esaudev.shopapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val storeApi: FakeStoreApi,
    private val cartDao: CartDao
) : ProductRepository {
    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val result = storeApi.fetchProducts()
            if (result.isSuccessful) {
                val products = result.body()!!.map(ProductDto::toProduct)
                Result.success(products)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val result = storeApi.fetchProductById(id)
            if (result.isSuccessful) {
                val product = result.body()!!.toProduct()
                Result.success(product)
            } else {
                Result.failure(Exception())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addToCart(product: Product) {
        cartDao.upsert(
            cartItemEntity = CartItemEntity(
                id = product.id,
                title = product.title,
                description = product.description,
                price = product.price,
                imageUrl = product.imageUrl
            )
        )
    }

    override fun observeCart(): Flow<List<CartItem>> {
        return cartDao.observeAll().map { cartItemEntityList ->
            cartItemEntityList.map { cartItemEntity ->
                CartItem(
                    id = cartItemEntity.id,
                    title = cartItemEntity.title,
                    imageUrl = cartItemEntity.imageUrl,
                    price = cartItemEntity.price,
                    quantity = cartItemEntity.quantity
                )
            }
        }
    }

    override suspend fun deleteFromCart(id: Int) {
        cartDao.deleteById(id)
    }

    override suspend fun addQuantityToCart(id: Int) {
        cartDao.addQuantityById(id)
    }

    override suspend fun removeQuantityToCart(id: Int) {
        cartDao.removeQuantityById(id)
    }

    override suspend fun completeOrder() {
        cartDao.truncateCart()
    }
}