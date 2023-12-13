package com.esaudev.shopapp.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.shopapp.domain.model.CartItem
import com.esaudev.shopapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {

    private val _cart : MutableLiveData<CartUiState> = MutableLiveData()
    val cart : LiveData<CartUiState>
        get() = _cart

    private val _uiEvent = Channel<CartUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun listenCartChanges() {
        viewModelScope.launch {
            productRepository.observeCart().collect { cartItems ->
                if (cartItems.isNotEmpty()) {
                    _cart.value = CartUiState.WithItems(items = cartItems)
                } else {
                    _cart.value = CartUiState.Empty
                }
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            productRepository.deleteFromCart(id)
        }
    }

    fun addQuantity(id: Int) {
        viewModelScope.launch {
            productRepository.addQuantityToCart(id)
        }
    }

    fun removeQuantity(id: Int) {
        viewModelScope.launch {
            productRepository.removeQuantityToCart(id)
        }
    }

    fun completeOrder() {
        viewModelScope.launch {
            productRepository.completeOrder()
            _uiEvent.send(CartUiEvent.OrderCompleted)
        }
    }
}

sealed class CartUiState {
    data object Empty: CartUiState()
    data class WithItems(val items: List<CartItem>): CartUiState()
}

sealed class CartUiEvent {
    data object OrderCompleted: CartUiEvent()
    data class Error(val exception: Throwable): CartUiEvent()
}