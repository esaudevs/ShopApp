package com.esaudev.shopapp.log_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.shopapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun logIn(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val token = authRepository.logIn(username, password)
            if (token.isSuccess) {
                _uiEvent.send(LoginUiEvent.LoggedIn(token.getOrNull()!!))
                _uiState.value = LoginUiState.Idle
            } else {
                _uiEvent.send(LoginUiEvent.Error(Exception()))
                _uiState.value = LoginUiState.Idle
            }
        }
    }
}

sealed class LoginUiState {
    data object Idle: LoginUiState()
    data object Loading: LoginUiState()
}

sealed class LoginUiEvent {
    data class LoggedIn(val token: String): LoginUiEvent()
    data class Error(val exception: Throwable): LoginUiEvent()
}