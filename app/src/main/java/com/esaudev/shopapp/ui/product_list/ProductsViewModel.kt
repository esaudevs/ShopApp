package com.esaudev.shopapp.ui.product_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.shopapp.domain.model.Product
import com.esaudev.shopapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {

    private val _products : MutableLiveData<List<Product>> = MutableLiveData()
    val products : LiveData<List<Product>>
        get() = _products

    fun getProducts() {
        viewModelScope.launch {
            val productsCall = productRepository.getProducts()
            println(productsCall)
            _products.value = productsCall.getOrDefault(emptyList())
        }
    }
}