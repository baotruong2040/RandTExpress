package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.domain.model.CartItem
import com.example.randtexpress.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val totalPrice: Long = 0L
) {
    val isEmpty: Boolean
        get() = items.isEmpty()
}

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        observeCartItems()
    }

    private fun observeCartItems() {
        viewModelScope.launch {
            cartRepository.observeCartItems().collect { items ->
                _uiState.update {
                    it.copy(
                        items = items,
                        totalPrice = items.sumOf { cartItem -> cartItem.lineTotal }
                    )
                }
            }
        }
    }

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            cartRepository.updateQuantity(item.productId, item.quantity + 1)
        }
    }

    fun decreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            cartRepository.updateQuantity(item.productId, item.quantity - 1)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }
}
