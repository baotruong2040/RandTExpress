package com.example.randtexpress.data.repository

import com.example.randtexpress.data.local.dao.CartDao
import com.example.randtexpress.data.local.entity.CartItemEntity
import com.example.randtexpress.data.remote.ImageUrlResolver
import com.example.randtexpress.domain.model.CartItem
import com.example.randtexpress.domain.repository.CartRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {
    override fun observeCartItems(): Flow<List<CartItem>> {
        return cartDao.observeCartItems().map { items ->
            items.map { item ->
                CartItem(
                    productId = item.productId,
                    name = item.name,
                    imageUrl = ImageUrlResolver.resolve(item.imageUrl),
                    price = item.price,
                    quantity = item.quantity
                )
            }
        }
    }

    override suspend fun addToCart(
        productId: Int,
        name: String,
        imageUrl: String?,
        price: Long
    ) {
        val currentItem = cartDao.getCartItem(productId)
        if (currentItem == null) {
            cartDao.upsertCartItem(
                CartItemEntity(
                    productId = productId,
                    name = name,
                    imageUrl = ImageUrlResolver.resolve(imageUrl),
                    price = price,
                    quantity = 1
                )
            )
            return
        }

        cartDao.updateQuantity(
            productId = productId,
            quantity = currentItem.quantity + 1
        )
    }

    override suspend fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            cartDao.removeFromCart(productId)
            return
        }
        cartDao.updateQuantity(productId = productId, quantity = quantity)
    }

    override suspend fun removeFromCart(productId: Int) {
        cartDao.removeFromCart(productId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}
