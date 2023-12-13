package com.esaudev.shopapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.shopapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userToken: MutableLiveData<String> = MutableLiveData()
    val userToken: LiveData<String>
        get() = _userToken

    private val _hasSplashFinished: MutableLiveData<Unit> = MutableLiveData()
    val hasSplashFinished: LiveData<Unit>
        get() = _hasSplashFinished

    fun getUserToken() {
        viewModelScope.launch {
            _userToken.value = authRepository.getUserToken().first()
        }
    }

    fun splashFinished() {
        viewModelScope.launch {
            _hasSplashFinished.value = Unit
        }
    }
}