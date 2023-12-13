package com.esaudev.shopapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.shopapp.domain.model.Product
import com.esaudev.shopapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {

    private val _product : MutableLiveData<ProductDetailUiState> = MutableLiveData()
    val product : LiveData<ProductDetailUiState>
        get() = _product

    private val _uiEvent = Channel<ProductDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun getProductDetail(id: Int) {
        viewModelScope.launch {
            _product.value = ProductDetailUiState.Loading
            val productDetailCall = productRepository.getProductById(id)
            _product.value = ProductDetailUiState.Idle(product = productDetailCall.getOrThrow())
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            _product.value = ProductDetailUiState.AddingToCart
            productRepository.addToCart(product)
            _uiEvent.send(ProductDetailUiEvent.AddedToCart)
            _product.value = ProductDetailUiState.Idle(product)
        }
    }
}

sealed class ProductDetailUiState {
    data object Loading: ProductDetailUiState()
    data object AddingToCart: ProductDetailUiState()
    data class Idle(val product: Product): ProductDetailUiState()
}

sealed class ProductDetailUiEvent {
    data object AddedToCart: ProductDetailUiEvent()
    data class Error(val exception: Throwable): ProductDetailUiEvent()
}