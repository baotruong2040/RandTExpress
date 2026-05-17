package com.example.randtexpress.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.randtexpress.data.local.dao.CartDao
import com.example.randtexpress.data.local.entity.CartItemEntity

@Database(
    entities = [CartItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
