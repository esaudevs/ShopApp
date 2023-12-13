package com.esaudev.shopapp.domain.usecase

import com.esaudev.shopapp.domain.model.User
import javax.inject.Inject

const val EMPTY_USERNAME_ERROR = "1"
const val EMPTY_EMAIL_ERROR = "2"
const val PASSWORDS_NOT_MATCH_ERROR = "3"
const val EMPTY_PASSWORD_ERROR = "4"
const val NETWORK_ERROR = "5"

class SignUpValidatorUseCase @Inject constructor() {

    operator fun invoke(user: User, password: String, confPassword: String): Result<Unit> {
        if(user.userName.isEmpty()) {
            return Result.failure(Exception(EMPTY_USERNAME_ERROR))
        }

        if (user.email.isEmpty()) {
            return Result.failure(Exception(EMPTY_EMAIL_ERROR))
        }

        if (password.isEmpty()) {
            return Result.failure(Exception(EMPTY_PASSWORD_ERROR))
        }

        if (password != confPassword) {
            return Result.failure(Exception(PASSWORDS_NOT_MATCH_ERROR))
        }

        return Result.success(Unit)
    }
}