package com.esaudev.courseessentials.data.remote.dto.product

import com.esaudev.shopapp.data.remote.dto.product.RatingDto
import com.esaudev.shopapp.domain.model.Product
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDto(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "price") val price: Double,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "category")  val category: String,
    @field:Json(name = "image") val imageUrl: String,
    @field:Json(name = "rating") val rating: RatingDto,
)

fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        price = price,
    )
}
