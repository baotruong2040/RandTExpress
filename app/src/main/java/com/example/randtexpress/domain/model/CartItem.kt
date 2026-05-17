package com.example.randtexpress.domain.model

data class CartItem(
    val productId: Int,
    val name: String,
    val imageUrl: String?,
    val price: Long,
    val quantity: Int
) {
    val lineTotal: Long
        get() = price * quantity
}
