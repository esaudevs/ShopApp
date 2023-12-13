package com.esaudev.shopapp.data.remote.api

import com.esaudev.shopapp.data.remote.dto.auth.AuthDto
import com.esaudev.shopapp.data.remote.dto.auth.SignUpDto
import com.esaudev.shopapp.data.remote.dto.auth.TokenDto
import com.esaudev.shopapp.data.remote.dto.auth.UserIdDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FakeStoreApi {

    @POST("/auth/login")
    suspend fun logIn(
        @Body authBody: AuthDto
    ): Response<TokenDto>

    @POST("/users")
    suspend fun signUp(
        @Body signUpBody: SignUpDto
    ): Response<UserIdDto>

}