package com.esaudev.shopapp.data.remote.dto.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthDto(
    @field:Json(name = "username") val username: String,
    @field:Json(name = "password") val password: String
)
