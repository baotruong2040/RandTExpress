package com.example.randtexpress.di

import com.example.randtexpress.data.remote.NetworkConfig
import com.example.randtexpress.data.remote.dto.response.BooleanDeserializer
import com.example.randtexpress.data.remote.dto.response.CategoryListResponse
import com.example.randtexpress.data.remote.dto.response.CategoryListResponseDeserializer
import com.example.randtexpress.data.remote.dto.response.PaginatedNotifications
import com.example.randtexpress.data.remote.dto.response.PaginatedNotificationsDeserializer
import com.example.randtexpress.data.remote.dto.response.ProductListResponse
import com.example.randtexpress.data.remote.dto.response.ProductListResponseDeserializer
import com.example.randtexpress.data.remote.dto.response.UserListResponse
import com.example.randtexpress.data.remote.dto.response.UserListResponseDeserializer
import com.example.randtexpress.data.preferences.UserPreferences
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(
        userPreferences: UserPreferences
    ): Interceptor {
        return Interceptor { chain ->
            val sessionToken = runBlocking {
                userPreferences.sessionData.first().token
            }
            val request = if (sessionToken.isNullOrBlank()) {
                chain.request()
            } else {
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $sessionToken")
                    .build()
            }
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, BooleanDeserializer())
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanDeserializer())
            .registerTypeAdapter(ProductListResponse::class.java, ProductListResponseDeserializer())
            .registerTypeAdapter(CategoryListResponse::class.java, CategoryListResponseDeserializer())
            .registerTypeAdapter(PaginatedNotifications::class.java, PaginatedNotificationsDeserializer())
            .registerTypeAdapter(UserListResponse::class.java, UserListResponseDeserializer())
            .create()
        return Retrofit.Builder()
            .baseUrl(NetworkConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
