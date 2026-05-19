package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.data.remote.dto.response.ProductResponse
import com.example.randtexpress.domain.repository.CartRepository
import com.example.randtexpress.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: ProductResponse? = null,
    val errorMessage: String? = null,
    val quantity: Int = 1,
    val isFavorite: Boolean = false
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    product = null,
                    errorMessage = null,
                    quantity = 1,
                    isFavorite = false
                )
            }

            try {
                val product = productRepository.getProductById(productId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = product
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Không thể tải sản phẩm"
                    )
                }
            }
        }
    }

    fun increaseQuantity() {
        _uiState.update { state ->
            state.copy(quantity = state.quantity + 1)
        }
    }

    fun decreaseQuantity() {
        _uiState.update { state ->
            state.copy(quantity = if (state.quantity > 1) state.quantity - 1 else 1)
        }
    }

    fun toggleFavorite() {
        _uiState.update { state ->
            state.copy(isFavorite = !state.isFavorite)
        }
    }

    fun addToCart() {
        val product = _uiState.value.product ?: return
        val quantity = _uiState.value.quantity

        viewModelScope.launch {
            repeat(quantity) {
                cartRepository.addToCart(
                    productId = product.id,
                    name = product.name ?: "",
                    imageUrl = product.imageUrl,
                    price = product.price ?: 0L
                )
            }
        }
    }
}
