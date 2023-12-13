package com.esaudev.shopapp.data.remote.dto.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignUpDto(
    @field:Json(name = "email") val email: String,
    @field:Json(name = "username") val username: String,
    @field:Json(name = "password") val password: String,
    @field:Json(name = "name") val name: NameDto?,
    @field:Json(name = "address") val address: AddressDto?,
    @field:Json(name = "phone") val phone: String?
)

@JsonClass(generateAdapter = true)
data class NameDto(
    val firstname: String,
    val lastName: String,
)

