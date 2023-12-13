package com.esaudev.shopapp.data.repository

import com.esaudev.shopapp.data.remote.api.FakeStoreApi
import com.esaudev.shopapp.data.remote.dto.auth.AuthDto
import com.esaudev.shopapp.data.remote.dto.auth.NameDto
import com.esaudev.shopapp.data.remote.dto.auth.SignUpDto
import com.esaudev.shopapp.domain.model.User
import com.esaudev.shopapp.domain.repository.AuthRepository
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val fakeStoreApi: FakeStoreApi,
): AuthRepository {
    override suspend fun logIn(username: String, password: String): Result<String> {
        return try {
            val response = fakeStoreApi.logIn(
                AuthDto(
                    username = username,
                    password = password,
                )
            )
            if(response.isSuccessful) {
                Result.success(response.body()!!.token)
            } else {
                Result.failure(Exception())
            }
        } catch (e: Exception) {
            Result.failure(Exception())
        }
    }

    override suspend fun signUp(user: User, password: String): Result<Int> {
        return try {
            val response = fakeStoreApi.signUp(
                signUpBody = SignUpDto(
                    email = user.email,
                    password = password,
                    username = user.userName,
                    name = NameDto(
                        firstname = user.firstName,
                        lastName = user.lastName,
                    ),
                    address = null,
                    phone = null
                )
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!.id)
            } else {
                Result.failure(Exception())
            }
        } catch (e: Exception) {
            Result.failure(Exception())
        }
    }
}