package com.example.randtexpress.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val productId: Int,
    val name: String,
    val imageUrl: String?,
    val price: Long,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
)
