package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.domain.model.CartItem
import com.example.randtexpress.domain.repository.CartRepository
import com.example.randtexpress.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val fullName: String = "",
    val phone: String = "",
    val address: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Long = 0L,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val createdOrderId: Int? = null
) {
    val isCartEmpty: Boolean
        get() = cartItems.isEmpty()

    val canPlaceOrder: Boolean
        get() = !isLoading && fullName.isNotBlank() && phone.isNotBlank() && address.isNotBlank() && cartItems.isNotEmpty()
}

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
        observeCartItems()
    }

    private fun observeCartItems() {
        viewModelScope.launch {
            cartRepository.observeCartItems().collect { items ->
                _uiState.update {
                    it.copy(
                        cartItems = items,
                        totalPrice = items.sumOf { cartItem -> cartItem.lineTotal }
                    )
                }
            }
        }
    }

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, errorMessage = null) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, errorMessage = null) }
    }

    fun onAddressChange(value: String) {
        _uiState.update { it.copy(address = value, errorMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun placeOrder(name: String, phone: String, address: String) {
        val currentState = uiState.value
        val normalizedName = name.trim()
        val normalizedPhone = phone.trim()
        val normalizedAddress = address.trim()

        if (currentState.cartItems.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Giỏ hàng đang trống.") }
            return
        }

        if (normalizedName.isBlank() || normalizedPhone.isBlank() || normalizedAddress.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin giao hàng.") }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                createdOrderId = null
            )
        }

        viewModelScope.launch {
            try {
                val response = orderRepository.createOrder(
                    deliveryAddress = normalizedAddress,
                    items = currentState.cartItems.map { item -> item.productId to item.quantity }
                )
                cartRepository.clearCart()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        createdOrderId = response.orderId
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Không thể tạo đơn hàng."
                    )
                }
            }
        }
    }
}
