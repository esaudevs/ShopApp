package com.esaudev.shopapp.data.remote.dto.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserIdDto(
    @field:Json(name = "id") val id: Int
)
