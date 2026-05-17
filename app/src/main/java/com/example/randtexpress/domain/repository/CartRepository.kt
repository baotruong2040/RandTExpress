package com.example.randtexpress.domain.repository

import com.example.randtexpress.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCartItems(): Flow<List<CartItem>>

    suspend fun addToCart(
        productId: Int,
        name: String,
        imageUrl: String?,
        price: Long
    )

    suspend fun updateQuantity(productId: Int, quantity: Int)

    suspend fun removeFromCart(productId: Int)

    suspend fun clearCart()
}
