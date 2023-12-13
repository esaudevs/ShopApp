package com.esaudev.shopapp.ui.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.shopapp.domain.model.User
import com.esaudev.shopapp.domain.repository.AuthRepository
import com.esaudev.shopapp.domain.usecase.NETWORK_ERROR
import com.esaudev.shopapp.domain.usecase.SignUpValidatorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpValidatorUseCase: SignUpValidatorUseCase,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiEvent = Channel<SignUpUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState.Idle)
    val uiState: StateFlow<SignUpUiState> = _uiState

    private val _passwordValidatorState: MutableStateFlow<PasswordValidatorState> = MutableStateFlow(
        PasswordValidatorState()
    )
    val passwordValidatorState: StateFlow<PasswordValidatorState> = _passwordValidatorState

    fun onPasswordsChanged(password: String, confPassword: String) {
        viewModelScope.launch {
            _passwordValidatorState.update { passwordValidatorState ->
                passwordValidatorState.copy(
                    passwordsMatch = password.isNotBlank() &&
                            confPassword.isNotBlank() &&
                            password == confPassword,
                    passwordLength = password.length >= 8,
                    passwordHasNumber = password.any { it.isDigit() }
                )
            }
        }
    }

    fun signUp(user: User, password: String, confPassword: String) {
        viewModelScope.launch {
            _uiState.value = SignUpUiState.Loading
            val signUpValidatorRequest = signUpValidatorUseCase(
                user = user,
                password = password,
                confPassword = confPassword,
            )

            if (signUpValidatorRequest.isSuccess) {
                val userRegistration = authRepository.signUp(user, password)
                if (userRegistration.isSuccess) {
                    _uiEvent.send(SignUpUiEvent.AccountCreated)
                } else {
                    _uiEvent.send(SignUpUiEvent.Error(NETWORK_ERROR))
                }
                _uiState.value = SignUpUiState.Idle
            } else {
                _uiState.value = SignUpUiState.Idle
                _uiEvent.send(SignUpUiEvent.Error(errorCode = signUpValidatorRequest.exceptionOrNull()!!.message!!))
            }
        }
    }
}

data class PasswordValidatorState(
    val passwordsMatch: Boolean = false,
    val passwordLength: Boolean = false,
    val passwordHasNumber: Boolean = false
) {
    fun allRequirementsMet(): Boolean = passwordsMatch && passwordLength && passwordHasNumber
}

sealed class SignUpUiState {
    data object Loading: SignUpUiState()
    data object Idle: SignUpUiState()
}

sealed class SignUpUiEvent {
    data object AccountCreated : SignUpUiEvent()
    data class Error(val errorCode: String): SignUpUiEvent()
}