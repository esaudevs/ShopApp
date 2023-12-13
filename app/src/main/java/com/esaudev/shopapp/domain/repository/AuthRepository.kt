package com.esaudev.shopapp.domain.repository

import com.esaudev.shopapp.domain.model.User

interface AuthRepository {
    suspend fun logIn(username: String, password: String): Result<String>
    suspend fun signUp(user: User, password: String): Result<Int>
}