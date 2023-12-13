package com.esaudev.shopapp.data.remote.dto.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddressDto(
    @field:Json(name = "city") val city: String,
    @field:Json(name = "street") val street: String,
    @field:Json(name = "number") val number: Int,
    @field:Json(name = "zipCode") val zipCode: String,
    @field:Json(name = "geolocation")  val geolocation: GeolocationDto?,
)

@JsonClass(generateAdapter = true)
data class GeolocationDto(
    @field:Json(name = "lat") val lat: String,
    @field:Json(name = "long") val long: String,
)
