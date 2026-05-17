# Prompt cho AI Agent: Xây dựng ứng dụng R&T Express (Android Studio + Jetpack Compose)

---

## 🎯 NHIỆM VỤ TỔNG QUAN

Bạn là một Android Developer chuyên nghiệp. Hãy xây dựng **ứng dụng đặt đồ ăn nhanh R&T Express** (Customer App) hoàn chỉnh theo kiến trúc **MVVM + Clean Architecture**, sử dụng:

- **Kotlin + Jetpack Compose** cho toàn bộ UI
- **Retrofit 2** để gọi REST API backend
- **Room Database** để cache dữ liệu cục bộ và lưu giỏ hàng offline
- **Hilt** để Dependency Injection
- **DataStore** để lưu JWT token và thông tin phiên đăng nhập

> **Base URL API:** `https://api.rtexpress.com/` (placeholder — agent cần khai báo dưới dạng constant có thể thay đổi)

---

## 🎨 HỆ THỐNG THIẾT KẾ (Design System)

### Bảng màu
```kotlin
// ui/theme/Color.kt
val BrandRed = Color(0xFFD32F2F)
val BrandRedDark = Color(0xFFB71C1C)
val BrandYellow = Color(0xFFFFC107)
val BrandOrange = Color(0xFFFF8F00)
val BackgroundLight = Color(0xFFFAFAFA)
val CardBackground = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
val StarYellow = Color(0xFFFFC107)
val GradientStart = Color(0xFFD32F2F)
val GradientEnd = Color(0xFFFF8F00)
val SurfaceOrange = Color(0xFFFFF3E0)
val GreenCart = Color(0xFF4CAF50)
```

### Shape & Spacing
- Card nhỏ: `RoundedCornerShape(12.dp)` | Bottom sheet: `RoundedCornerShape(topStart=24.dp, topEnd=24.dp)`
- Nút CTA: `RoundedCornerShape(50.dp)` (pill) | Padding màn hình: `16.dp`

---

## 📁 CẤU TRÚC DỰ ÁN (MVVM + Clean Architecture)

```
app/src/main/java/com/example/rtexpress/
│
├── di/
│   ├── AppModule.kt              ← Hilt: cung cấp Retrofit, DB, DataStore
│   ├── NetworkModule.kt          ← OkHttpClient + AuthInterceptor
│   └── RepositoryModule.kt       ← Bind interface → implementation
│
├── data/
│   ├── remote/
│   │   ├── api/
│   │   │   ├── AuthApiService.kt
│   │   │   ├── ProductApiService.kt
│   │   │   ├── OrderApiService.kt
│   │   │   └── NotificationApiService.kt
│   │   └── dto/                  ← Data Transfer Objects (JSON mapping)
│   │       ├── request/
│   │       │   ├── LoginRequest.kt
│   │       │   ├── RegisterRequest.kt
│   │       │   └── CreateOrderRequest.kt
│   │       └── response/
│   │           ├── ApiResponse.kt        ← Generic envelope wrapper
│   │           ├── AuthResponse.kt
│   │           ├── ProductResponse.kt
│   │           ├── CategoryResponse.kt
│   │           ├── OrderResponse.kt
│   │           └── NotificationResponse.kt
│   │
│   ├── local/
│   │   ├── AppDatabase.kt        ← Room Database
│   │   ├── dao/
│   │   │   ├── ProductDao.kt
│   │   │   ├── CategoryDao.kt
│   │   │   ├── CartDao.kt
│   │   │   └── NotificationDao.kt
│   │   └── entity/               ← Room Entities
│   │       ├── ProductEntity.kt
│   │       ├── CategoryEntity.kt
│   │       ├── CartItemEntity.kt
│   │       └── NotificationEntity.kt
│   │
│   ├── repository/
│   │   ├── AuthRepository.kt (interface) + AuthRepositoryImpl.kt
│   │   ├── ProductRepository.kt (interface) + ProductRepositoryImpl.kt
│   │   ├── OrderRepository.kt (interface) + OrderRepositoryImpl.kt
│   │   ├── CartRepository.kt (interface) + CartRepositoryImpl.kt
│   │   └── NotificationRepository.kt (interface) + NotificationRepositoryImpl.kt
│   │
│   └── preferences/
│       └── UserPreferences.kt    ← DataStore: lưu token, userId, role
│
├── domain/
│   └── model/                    ← Domain models (UI dùng trực tiếp)
│       ├── User.kt
│       ├── Product.kt
│       ├── Category.kt
│       ├── CartItem.kt
│       ├── Order.kt
│       └── Notification.kt
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── navigation/
│   │   └── AppNavigation.kt
│   ├── screens/
│   │   ├── auth/
│   │   │   ├── AuthViewModel.kt
│   │   │   ├── LoginScreen.kt
│   │   │   └── RegisterScreen.kt
│   │   ├── home/
│   │   │   ├── HomeViewModel.kt
│   │   │   └── HomeScreen.kt
│   │   ├── detail/
│   │   │   ├── ProductDetailViewModel.kt
│   │   │   └── ProductDetailScreen.kt
│   │   ├── cart/
│   │   │   ├── CartViewModel.kt
│   │   │   ├── CartScreen.kt          ← giỏ trống
│   │   │   └── CartFilledScreen.kt    ← giỏ có hàng
│   │   ├── notification/
│   │   │   ├── NotificationViewModel.kt
│   │   │   └── NotificationScreen.kt
│   │   └── profile/
│   │       ├── ProfileViewModel.kt
│   │       └── ProfileScreen.kt
│   └── components/
│       ├── BottomNavigationBar.kt
│       ├── ProductCard.kt
│       ├── BannerSlider.kt
│       ├── StarRating.kt
│       ├── QuantitySelector.kt
│       └── UiStateWrapper.kt         ← Composable xử lý Loading/Error/Success
│
└── MainActivity.kt
```

---

## ⚙️ DEPENDENCIES (`build.gradle.kts` — app level)

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    compileSdk = 34
    defaultConfig { minSdk = 24; targetSdk = 34 }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.10" }
}

dependencies {
    // ─── Compose BOM ───────────────────────────────────────────
    val composeBom = platform("androidx.compose:compose-bom:2024.04.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // ─── Navigation ────────────────────────────────────────────
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ─── Lifecycle / ViewModel ─────────────────────────────────
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // ─── Hilt (Dependency Injection) ───────────────────────────
    implementation("com.google.dagger:hilt-android:2.51")
    ksp("com.google.dagger:hilt-android-compiler:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // ─── Retrofit + OkHttp + Gson ──────────────────────────────
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ─── Room (Local Database) ─────────────────────────────────
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // ─── DataStore (Token storage) ─────────────────────────────
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // ─── Coil (Image loading) ──────────────────────────────────
    implementation("io.coil-kt:coil-compose:2.6.0")

    // ─── Accompanist Pager (Banner slider) ────────────────────
    implementation("com.google.accompanist:accompanist-pager:0.34.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.34.0")

    // ─── Coroutines ────────────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
}
```

`build.gradle.kts` (project level):
```kotlin
plugins {
    id("com.google.dagger.hilt.android") version "2.51" apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
}
```

---

## 🌐 TẦNG NETWORK (Remote)

### 1. Generic API Response Wrapper

```kotlin
// data/remote/dto/response/ApiResponse.kt
data class ApiResponse<T>(
    val status: String,         // "success" | "error"
    val message: String,
    val data: T?
)
```

### 2. DTOs (Request & Response)

```kotlin
// ── REQUEST DTOs ──────────────────────────────────────────────

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val full_name: String,
    val email: String,
    val phone: String,
    val address: String? = null
)

data class OrderItemRequest(
    val product_id: Int,
    val quantity: Int
)

data class CreateOrderRequest(
    val delivery_address: String,
    val items: List<OrderItemRequest>
)

// ── RESPONSE DTOs ─────────────────────────────────────────────

data class AuthData(
    val token: String,
    val user_id: Int,
    val role: String          // "CUSTOMER" | "STAFF" | "ADMIN"
)

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val image_url: String?,
    val category_id: Int,
    val is_available: Boolean
)

data class CategoryDto(
    val id: Int,
    val name: String,
    val description: String?,
    val image_url: String?
)

data class OrderDto(
    val order_id: Int,
    val total_amount: Double,
    val status: String        // PENDING|PREPARING|READY|DELIVERING|DELIVERED|CANCELLED
)

data class NotificationDto(
    val id: Int,
    val title: String,
    val message: String,
    val is_read: Boolean,
    val created_at: String
)
```

### 3. API Services (Retrofit Interfaces)

```kotlin
// data/remote/api/AuthApiService.kt
interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<ApiResponse<AuthData>>

    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<ApiResponse<Map<String, Any>>>
}

// data/remote/api/ProductApiService.kt
interface ProductApiService {
    @GET("api/products")
    suspend fun getProducts(): Response<ApiResponse<List<ProductDto>>>

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ApiResponse<ProductDto>>

    @GET("api/categories")
    suspend fun getCategories(): Response<ApiResponse<List<CategoryDto>>>
}

// data/remote/api/OrderApiService.kt
interface OrderApiService {
    @POST("api/orders")
    suspend fun createOrder(@Body body: CreateOrderRequest): Response<ApiResponse<OrderDto>>

    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") id: Int): Response<ApiResponse<OrderDto>>
}

// data/remote/api/NotificationApiService.kt
interface NotificationApiService {
    @GET("api/notifications")
    suspend fun getNotifications(): Response<ApiResponse<List<NotificationDto>>>

    @PUT("api/notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: Int): Response<ApiResponse<Map<String, Any>>>
}
```

### 4. Auth Interceptor (tự động đính JWT vào header)

```kotlin
// di/NetworkModule.kt
class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val token = runBlocking { userPreferences.getToken() }
        val request = chain.request().newBuilder().apply {
            if (token != null) addHeader("Authorization", "Bearer $token")
        }.build()
        return chain.proceed(request)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.rtexpress.com/"

    @Provides @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApiService = retrofit.create(ProductApiService::class.java)

    @Provides @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApiService = retrofit.create(OrderApiService::class.java)

    @Provides @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApiService = retrofit.create(NotificationApiService::class.java)
}
```

---

## 🗄️ TẦNG LOCAL (Room Database)

### 1. Entities

```kotlin
// data/local/entity/ProductEntity.kt
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String?,
    val categoryId: Int,
    val isAvailable: Boolean,
    val cachedAt: Long = System.currentTimeMillis()   // TTL cache
)

// data/local/entity/CategoryEntity.kt
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?
)

// data/local/entity/CartItemEntity.kt
// Giỏ hàng lưu cục bộ, đồng bộ remote khi login
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: Int,
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String?,
    var quantity: Int,
    var notes: String = ""
)

// data/local/entity/NotificationEntity.kt
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String
)
```

### 2. DAOs

```kotlin
// data/local/dao/ProductDao.kt
@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isAvailable = 1")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Int): ProductEntity?

    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND isAvailable = 1")
    fun getProductsByCategory(categoryId: Int): Flow<List<ProductEntity>>

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearAll()
}

// data/local/dao/CartDao.kt
@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartItemCount(): Flow<Int>

    @Query("SELECT SUM(quantity * productPrice) FROM cart_items")
    fun getCartTotal(): Flow<Double?>

    @Upsert
    suspend fun upsertItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun removeItem(productId: Int)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: Int, quantity: Int)

    @Query("UPDATE cart_items SET notes = :notes WHERE productId = :productId")
    suspend fun updateNotes(productId: Int, notes: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

// data/local/dao/NotificationDao.kt
@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Upsert
    suspend fun upsertAll(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)

    @Query("DELETE FROM notifications")
    suspend fun clearAll()
}
```

### 3. AppDatabase

```kotlin
// data/local/AppDatabase.kt
@Database(
    entities = [ProductEntity::class, CategoryEntity::class,
                CartItemEntity::class, NotificationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun cartDao(): CartDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        const val DATABASE_NAME = "rt_express.db"
    }
}
```

---

## 💾 DATASTORE (Token & Session)

```kotlin
// data/preferences/UserPreferences.kt
@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.createDataStoreWithDefaults()

    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USER_ID_KEY = intPreferencesKey("user_id")
        val USER_ROLE_KEY = stringPreferencesKey("user_role")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
    }

    suspend fun saveSession(token: String, userId: Int, role: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId
            prefs[USER_ROLE_KEY] = role
        }
    }

    suspend fun getToken(): String? =
        dataStore.data.first()[TOKEN_KEY]

    fun isLoggedIn(): Flow<Boolean> =
        dataStore.data.map { it[TOKEN_KEY] != null }

    suspend fun clearSession() = dataStore.edit { it.clear() }
}

// Extension để tạo DataStore
fun Context.createDataStoreWithDefaults() = createDataStore(name = "user_prefs")
```

---

## 🔄 TẦNG REPOSITORY

### Pattern chuẩn (áp dụng cho tất cả repository):

```kotlin
// Sealed class kết quả — tránh dùng Exception thô
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

### AuthRepositoryImpl

```kotlin
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<AuthData> {
        return try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body()?.status == "success") {
                val authData = response.body()!!.data!!
                userPreferences.saveSession(authData.token, authData.user_id, authData.role)
                Result.Success(authData)
            } else {
                Result.Error(response.body()?.message ?: "Đăng nhập thất bại", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Lỗi kết nối: ${e.localizedMessage}")
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful && response.body()?.status == "success")
                Result.Success(Unit)
            else
                Result.Error(response.body()?.message ?: "Đăng ký thất bại", response.code())
        } catch (e: Exception) {
            Result.Error("Lỗi kết nối: ${e.localizedMessage}")
        }
    }

    override suspend fun logout() = userPreferences.clearSession()
}
```

### ProductRepositoryImpl (Offline-first với Room cache)

```kotlin
// Chiến lược: đọc Room trước → trả về → fetch API → cập nhật Room
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) : ProductRepository {

    // Flow từ Room — UI tự cập nhật khi Room thay đổi
    override fun getProducts(): Flow<List<Product>> =
        productDao.getAllProducts().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshProducts(): Result<Unit> {
        return try {
            val response = api.getProducts()
            if (response.isSuccessful) {
                val products = response.body()?.data ?: emptyList()
                productDao.clearAll()
                productDao.upsertAll(products.map { it.toEntity() })
                Result.Success(Unit)
            } else {
                Result.Error("Không thể tải sản phẩm", response.code())
            }
        } catch (e: Exception) {
            // Không có mạng → Room cache vẫn hoạt động, không crash
            Result.Error("Không có kết nối mạng")
        }
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        val cached = productDao.getProductById(id)
        if (cached != null) return Result.Success(cached.toDomain())
        return try {
            val response = api.getProductById(id)
            if (response.isSuccessful)
                Result.Success(response.body()!!.data!!.toDomain())
            else
                Result.Error("Không tìm thấy sản phẩm", response.code())
        } catch (e: Exception) {
            Result.Error("Lỗi kết nối")
        }
    }
}
```

### CartRepositoryImpl (Offline-first — Room là nguồn sự thật)

```kotlin
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val orderApi: OrderApiService
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> =
        cartDao.getCartItems().map { it.map { e -> e.toDomain() } }

    override fun getCartTotal(): Flow<Double> =
        cartDao.getCartTotal().map { it ?: 0.0 }

    override fun getCartItemCount(): Flow<Int> = cartDao.getCartItemCount()

    override suspend fun addOrUpdateItem(product: Product, quantity: Int) {
        cartDao.upsertItem(CartItemEntity(
            productId = product.id,
            productName = product.name,
            productPrice = product.price,
            productImageUrl = product.imageUrl,
            quantity = quantity
        ))
    }

    override suspend fun removeItem(productId: Int) = cartDao.removeItem(productId)
    override suspend fun updateQuantity(productId: Int, quantity: Int) = cartDao.updateQuantity(productId, quantity)
    override suspend fun updateNotes(productId: Int, notes: String) = cartDao.updateNotes(productId, notes)
    override suspend fun clearCart() = cartDao.clearCart()

    // Gọi API tạo đơn hàng, sau đó xoá giỏ hàng local nếu thành công
    override suspend fun placeOrder(
        deliveryAddress: String,
        cartItems: List<CartItem>
    ): Result<OrderDto> {
        return try {
            val body = CreateOrderRequest(
                delivery_address = deliveryAddress,
                items = cartItems.map { OrderItemRequest(it.productId, it.quantity) }
            )
            val response = orderApi.createOrder(body)
            if (response.isSuccessful && response.body()?.status == "success") {
                val order = response.body()!!.data!!
                cartDao.clearCart()   // Xoá giỏ hàng sau khi đặt thành công
                Result.Success(order)
            } else {
                Result.Error(response.body()?.message ?: "Đặt hàng thất bại", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Lỗi kết nối: ${e.localizedMessage}")
        }
    }
}
```

### NotificationRepositoryImpl

```kotlin
class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApiService,
    private val dao: NotificationDao
) : NotificationRepository {

    override fun getNotifications(): Flow<List<Notification>> =
        dao.getAllNotifications().map { it.map { e -> e.toDomain() } }

    override fun getUnreadCount(): Flow<Int> = dao.getUnreadCount()

    override suspend fun refresh(): Result<Unit> {
        return try {
            val response = api.getNotifications()
            if (response.isSuccessful) {
                dao.upsertAll(response.body()!!.data!!.map { it.toEntity() })
                Result.Success(Unit)
            } else Result.Error("Không thể tải thông báo")
        } catch (e: Exception) { Result.Error("Lỗi kết nối") }
    }

    override suspend fun markAsRead(id: Int) {
        dao.markAsRead(id)          // Optimistic update local trước
        try { api.markAsRead(id) } catch (_: Exception) {}   // Sync remote sau
    }
}
```

---

## 🧠 VIEWMODELS

### AuthViewModel

```kotlin
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = userPreferences.isLoggedIn()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _uiState.value = when (val result = authRepository.login(username, password)) {
                is Result.Success -> AuthUiState.LoginSuccess
                is Result.Error -> AuthUiState.Error(result.message)
                else -> AuthUiState.Idle
            }
        }
    }

    fun register(username: String, password: String, fullName: String, email: String, phone: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = RegisterRequest(username, password, fullName, email, phone)
            _uiState.value = when (val result = authRepository.register(request)) {
                is Result.Success -> AuthUiState.RegisterSuccess
                is Result.Error -> AuthUiState.Error(result.message)
                else -> AuthUiState.Idle
            }
        }
    }

    fun logout() = viewModelScope.launch { authRepository.logout() }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object LoginSuccess : AuthUiState()
    object RegisterSuccess : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
```

### HomeViewModel

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    // Đọc từ Room (offline-first), auto-update khi Room thay đổi
    val products: StateFlow<List<Product>> = productRepository.getProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val popularProducts: StateFlow<List<Product>> = products
        .map { it.filter { p -> p.categoryId != 3 }.take(6) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val comboProducts: StateFlow<List<Product>> = products
        .map { it.filter { p -> p.name.contains("Combo", ignoreCase = true) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init { refreshProducts() }

    fun refreshProducts() = viewModelScope.launch {
        _isRefreshing.value = true
        val result = productRepository.refreshProducts()
        if (result is Result.Error) _errorMessage.value = result.message
        _isRefreshing.value = false
    }
}
```

### CartViewModel

```kotlin
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartTotal: StateFlow<Double> = cartRepository.getCartTotal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartItemCount: StateFlow<Int> = cartRepository.getCartItemCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _orderState = MutableStateFlow<OrderUiState>(OrderUiState.Idle)
    val orderState: StateFlow<OrderUiState> = _orderState.asStateFlow()

    fun addToCart(product: Product, quantity: Int = 1) =
        viewModelScope.launch { cartRepository.addOrUpdateItem(product, quantity) }

    fun removeFromCart(productId: Int) =
        viewModelScope.launch { cartRepository.removeItem(productId) }

    fun updateQuantity(productId: Int, quantity: Int) = viewModelScope.launch {
        if (quantity <= 0) cartRepository.removeItem(productId)
        else cartRepository.updateQuantity(productId, quantity)
    }

    fun updateNotes(productId: Int, notes: String) =
        viewModelScope.launch { cartRepository.updateNotes(productId, notes) }

    fun placeOrder(deliveryAddress: String) = viewModelScope.launch {
        _orderState.value = OrderUiState.Loading
        val items = cartItems.value
        if (items.isEmpty()) { _orderState.value = OrderUiState.Error("Giỏ hàng trống"); return@launch }
        _orderState.value = when (val result = cartRepository.placeOrder(deliveryAddress, items)) {
            is Result.Success -> OrderUiState.Success(result.data.order_id)
            is Result.Error -> OrderUiState.Error(result.message)
            else -> OrderUiState.Idle
        }
    }
}

sealed class OrderUiState {
    object Idle : OrderUiState()
    object Loading : OrderUiState()
    data class Success(val orderId: Int) : OrderUiState()
    data class Error(val message: String) : OrderUiState()
}
```

### NotificationViewModel

```kotlin
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val notifications: StateFlow<List<Notification>> = notificationRepository.getNotifications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadCount: StateFlow<Int> = notificationRepository.getUnreadCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init { viewModelScope.launch { notificationRepository.refresh() } }

    fun markAsRead(id: Int) = viewModelScope.launch { notificationRepository.markAsRead(id) }

    fun clearAll() = viewModelScope.launch { /* gọi dao.clearAll() */ }
}
```

---

## 📱 MÀN HÌNH UI — KẾT NỐI VIEWMODEL

### Quy tắc chung cho tất cả màn hình:

```kotlin
// Collect StateFlow đúng cách trong Compose
val items by viewModel.products.collectAsStateWithLifecycle()
val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
```

### UiStateWrapper (component tái sử dụng)

```kotlin
@Composable
fun <T> UiStateWrapper(
    isLoading: Boolean,
    errorMessage: String?,
    data: T?,
    onRetry: () -> Unit,
    content: @Composable (T) -> Unit
) {
    when {
        isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = BrandRed)
        }
        errorMessage != null -> Column(
            Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(errorMessage, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = BrandRed)) {
                Text("Thử lại")
            }
        }
        data != null -> content(data)
    }
}
```

---

### 1. 🔐 LoginScreen — gọi AuthViewModel

```kotlin
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Điều hướng khi thành công
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.LoginSuccess) onLoginSuccess()
    }

    // Hiển thị lỗi bằng Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Error)
            snackbarHostState.showSnackbar((uiState as AuthUiState.Error).message)
    }

    // ── UI ──────────────────────────────────────────────────────
    // [Xem mô tả UI đầy đủ bên dưới — giữ nguyên thiết kế cũ]
    // Khi nhấn nút Login:
    //   viewModel.login(username, password)
    // Khi nhấn Sign up:
    //   viewModel.register(username, password, fullName, email, phone)
    // Nút disabled + CircularProgressIndicator khi uiState == Loading
}
```

---

### 2. 🏠 HomeScreen — gọi HomeViewModel + CartViewModel

```kotlin
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    navController: NavController
) {
    val popularProducts by homeViewModel.popularProducts.collectAsStateWithLifecycle()
    val comboProducts by homeViewModel.comboProducts.collectAsStateWithLifecycle()
    val isRefreshing by homeViewModel.isRefreshing.collectAsStateWithLifecycle()
    val cartItemCount by cartViewModel.cartItemCount.collectAsStateWithLifecycle()
    val errorMessage by homeViewModel.errorMessage.collectAsStateWithLifecycle()

    // PullRefresh để kéo làm mới
    // Khi nhấn "+" trên ProductCard:
    //   cartViewModel.addToCart(product)
    // Khi nhấn card sản phẩm:
    //   navController.navigate("detail/${product.id}")
    // Icon giỏ hàng hiển thị badge = cartItemCount
}
```

---

### 3. 📋 ProductDetailScreen — gọi ProductDetailViewModel + CartViewModel

```kotlin
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val productId: Int = checkNotNull(savedStateHandle["productId"])

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = productRepository.getProductById(productId)) {
                is Result.Success -> _product.value = result.data
                else -> { /* handle error */ }
            }
            _isLoading.value = false
        }
    }
}

// Trong ProductDetailScreen:
// Khi nhấn "Add to cart":
//   cartViewModel.addToCart(product, quantity)
//   navController.navigate("cart") hoặc hiển thị Snackbar xác nhận
```

---

### 4. 🛒 CartFilledScreen — gọi CartViewModel

```kotlin
@Composable
fun CartFilledScreen(
    viewModel: CartViewModel = hiltViewModel(),
    navController: NavController
) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val cartTotal by viewModel.cartTotal.collectAsStateWithLifecycle()
    val orderState by viewModel.orderState.collectAsStateWithLifecycle()

    // Nếu giỏ trống → hiển thị CartScreen (empty state)
    if (cartItems.isEmpty()) { CartScreen(navController); return }

    // Khi đặt hàng thành công → navigate to order tracking
    LaunchedEffect(orderState) {
        if (orderState is OrderUiState.Success)
            navController.navigate("order_detail/${(orderState as OrderUiState.Success).orderId}")
    }

    // UI:
    // - Mỗi item: ảnh, tên, giá, QuantitySelector, TextField notes
    //   → viewModel.updateQuantity(item.productId, newQty)
    //   → viewModel.updateNotes(item.productId, text)
    // - Dropdown payment: Cash / Scan QR-Code / Visa/Master / Apple Pay
    //   (chỉ UI, chưa implement payment gateway)
    // - TextField mã promo (chỉ UI)
    // - Nút "Order": viewModel.placeOrder(deliveryAddress)
    // - Loading overlay khi orderState == Loading
}
```

---

### 5. 🔔 NotificationScreen — gọi NotificationViewModel

```kotlin
// Khi màn hình hiển thị → viewModel refresh() tự động (gọi trong init{})
// Khi nhấn vào notification item → viewModel.markAsRead(id)
// Khi nhấn × → xoá local (optimistic)
// "Clear all" → viewModel.clearAll()
// Badge số trên BottomNav lấy từ viewModel.unreadCount
```

---

### 6. 👤 ProfileScreen — gọi ProfileViewModel

```kotlin
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {
    val userName: StateFlow<String> = userPreferences.getUserName()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun logout() = viewModelScope.launch {
        authRepository.logout()
        // Navigation về màn hình Login xử lý qua isLoggedIn flow trong AppNavigation
    }
}
```

---

## 🧭 ĐIỀU HƯỚNG (`AppNavigation.kt`)

```kotlin
@Composable
fun AppNavigation(
    userPreferences: UserPreferences = /* inject từ Hilt EntryPoint */
) {
    val navController = rememberNavController()
    val isLoggedIn by userPreferences.isLoggedIn().collectAsStateWithLifecycle(false)

    // Tự động redirect dựa theo trạng thái đăng nhập
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) navController.navigate("login") {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("home") { popUpTo("login") { inclusive = true } }
            })
        }
        composable("home") { HomeScreen(navController = navController) }
        composable(
            route = "detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { ProductDetailScreen(navController = navController) }
        composable("cart") { CartFilledScreen(navController = navController) }
        composable("notifications") { NotificationScreen(navController = navController) }
        composable("profile") { ProfileScreen(navController = navController) }
        composable(
            route = "order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { /* OrderDetailScreen */ }
    }
}
```

---

## 🔌 HILT — DI MODULE ĐẦY ĐỦ

```kotlin
// di/AppModule.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideProductDao(db: AppDatabase) = db.productDao()
    @Provides fun provideCartDao(db: AppDatabase) = db.cartDao()
    @Provides fun provideCategoryDao(db: AppDatabase) = db.categoryDao()
    @Provides fun provideNotificationDao(db: AppDatabase) = db.notificationDao()

    @Provides @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context) = UserPreferences(context)
}

// di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindAuthRepo(impl: AuthRepositoryImpl): AuthRepository
    @Binds abstract fun bindProductRepo(impl: ProductRepositoryImpl): ProductRepository
    @Binds abstract fun bindCartRepo(impl: CartRepositoryImpl): CartRepository
    @Binds abstract fun bindNotificationRepo(impl: NotificationRepositoryImpl): NotificationRepository
}
```

---

## 🗺️ MAPPER FUNCTIONS (DTO ↔ Entity ↔ Domain)

```kotlin
// Tạo extension functions trong từng file hoặc file riêng Mappers.kt

// ProductDto → ProductEntity
fun ProductDto.toEntity() = ProductEntity(id, name, description, price, image_url, category_id, is_available)

// ProductEntity → Product (Domain)
fun ProductEntity.toDomain() = Product(id, name, description, price, imageUrl, categoryId, isAvailable)

// ProductDto → Product (Domain) — dùng khi không cần cache
fun ProductDto.toDomain() = Product(id, name, description, price, image_url, category_id, is_available)

// CartItemEntity → CartItem (Domain)
fun CartItemEntity.toDomain() = CartItem(productId, productName, productPrice, productImageUrl, quantity, notes)

// NotificationDto → NotificationEntity
fun NotificationDto.toEntity() = NotificationEntity(id, title, message, is_read, created_at)

// NotificationEntity → Notification (Domain)
fun NotificationEntity.toDomain() = Notification(id, title, message, isRead, createdAt)
```

---

## ✅ YÊU CẦU BẮT BUỘC

1. **Toàn bộ UI dùng Jetpack Compose** — không XML layout
2. **Kiến trúc MVVM + Clean**: Remote → Repository → ViewModel → Screen
3. **Retrofit** gọi đúng endpoint theo API Reference; dùng `Response<ApiResponse<T>>` để xử lý lỗi HTTP chi tiết
4. **Room** là nguồn sự thật duy nhất cho UI (offline-first với Flow); API chỉ dùng để refresh cache
5. **Giỏ hàng lưu Room** — thêm/bớt/sửa số lượng/notes không cần mạng; chỉ gọi API khi đặt hàng
6. **JWT** lưu DataStore, tự động đính vào mọi request qua `AuthInterceptor`
7. **Hilt** inject tất cả dependency — không dùng singleton thủ công hay static
8. **StateFlow + collectAsStateWithLifecycle** trong mọi màn hình — không dùng `LiveData`
9. **Sealed class `Result<T>`** cho mọi kết quả async — không ném Exception thô ra ViewModel
10. **Màu sắc** đúng theo Design System (đỏ chủ đạo, vàng highlight)
11. **Tên package:** `com.example.rtexpress`
12. **minSdk 24**, **targetSdk 34**

---

## 🚀 THỨ TỰ THỰC HIỆN

1. Tạo project (Empty Compose Activity, Kotlin, minSdk 24)
2. Cài đặt tất cả dependencies + plugins Hilt/KSP
3. Tạo `Color.kt`, `Theme.kt`, `Type.kt`
4. Tạo tầng `data/remote`: DTOs → API Services → NetworkModule → AuthInterceptor
5. Tạo tầng `data/local`: Entities → DAOs → AppDatabase → AppModule
6. Tạo `UserPreferences` (DataStore)
7. Tạo tầng `data/repository`: interfaces + implementations + Mappers
8. Tạo `di/RepositoryModule.kt`
9. Tạo Domain Models (`domain/model/`)
10. Tạo `Result<T>` sealed class
11. Tạo từng ViewModel: Auth → Home → ProductDetail → Cart → Notification → Profile
12. Tạo UI Components dùng chung: `UiStateWrapper`, `ProductCard`, `QuantitySelector`, `StarRating`, `BannerSlider`, `BottomNavigationBar`
13. Tạo từng màn hình Composable kết nối ViewModel
14. Tạo `AppNavigation.kt`
15. Cập nhật `MainActivity.kt` với `@AndroidEntryPoint` + gọi `AppNavigation()`
16. Thêm `@HiltAndroidApp` vào `Application` class
