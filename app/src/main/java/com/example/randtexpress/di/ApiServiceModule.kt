package com.example.randtexpress.di

import com.example.randtexpress.data.remote.api.AuthApiService
import com.example.randtexpress.data.remote.api.CategoryApiService
import com.example.randtexpress.data.remote.api.NotificationApiService
import com.example.randtexpress.data.remote.api.OrderApiService
import com.example.randtexpress.data.remote.api.ProductApiService
import com.example.randtexpress.data.remote.api.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Singleton
    @Provides
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideProductApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideCategoryApiService(retrofit: Retrofit): CategoryApiService {
        return retrofit.create(CategoryApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService {
        return retrofit.create(OrderApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideNotificationApiService(retrofit: Retrofit): NotificationApiService {
        return retrofit.create(NotificationApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }
}
