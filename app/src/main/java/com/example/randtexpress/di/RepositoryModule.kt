package com.example.randtexpress.di

import com.example.randtexpress.data.repository.AuthRepositoryImpl
import com.example.randtexpress.data.repository.CategoryRepositoryImpl
import com.example.randtexpress.data.repository.NotificationRepositoryImpl
import com.example.randtexpress.data.repository.OrderRepositoryImpl
import com.example.randtexpress.data.repository.ProductRepositoryImpl
import com.example.randtexpress.data.repository.UserRepositoryImpl
import com.example.randtexpress.domain.repository.AuthRepository
import com.example.randtexpress.domain.repository.CategoryRepository
import com.example.randtexpress.domain.repository.NotificationRepository
import com.example.randtexpress.domain.repository.OrderRepository
import com.example.randtexpress.domain.repository.ProductRepository
import com.example.randtexpress.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Singleton
    @Binds
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Singleton
    @Binds
    abstract fun bindOrderRepository(
        impl: OrderRepositoryImpl
    ): OrderRepository

    @Singleton
    @Binds
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}
