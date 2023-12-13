package com.esaudev.shopapp.domain.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val userName: String,
    val email: String,
)
