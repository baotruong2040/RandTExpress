# Copilot Instructions for RandTExpress

This is a Kotlin Android application using Jetpack Compose for UI and Material 3 design. It implements MVVM + Clean Architecture with backend integration via Retrofit.

**Before implementing:**
1. Review `.github/CLAUDE.md` for behavioral guidelines that reduce common mistakes
2. Check `.github/API_REFERENCE.md` for backend API contract and response formats
3. Review `.github/RT_Express_Jetpack_Compose_Prompt_v2.md` for system architecture, design system, and project structure

## Quick Links
- **Behavioral Guidelines**: `.github/CLAUDE.md`
- **Backend API**: `.github/API_REFERENCE.md`
- **Architecture & Design**: `.github/RT_Express_Jetpack_Compose_Prompt_v2.md`

## Build & Test Commands

All commands are run from the project root.

### Building
- **Full build**: `./gradlew build`
- **Build debug APK**: `./gradlew assembleDebug`
- **Build release APK**: `./gradlew assembleRelease`

### Testing
- **Run all unit tests**: `./gradlew test`
- **Run single unit test file**: `./gradlew test --tests com.example.randtexpress.SomeTest`
- **Run single test method**: `./gradlew test --tests com.example.randtexpress.SomeTest.someTestMethod`
- **Run all instrumented tests** (on device/emulator): `./gradlew connectedAndroidTest`
- **Run single instrumented test**: `./gradlew connectedAndroidTest --tests com.example.randtexpress.SomeInstrumentedTest`

### Debugging
- **Clean build** (if encountering build cache issues): `./gradlew clean build`
- **Sync gradle** (if dependencies aren't resolving): `./gradlew --refresh-dependencies`

## Architecture Overview

### MVVM + Clean Architecture
- **Data Layer**: Remote APIs (Retrofit), Local DB (Room), Repositories
- **Domain Layer**: Business logic, use cases, domain models
- **UI Layer**: Composables, ViewModels, state management
- **DI**: Hilt for dependency injection

**Key Technologies:**
- **Retrofit 2**: REST API communication
- **Room Database**: Local caching and offline cart storage
- **DataStore**: JWT token and session persistence
- **Hilt**: Dependency Injection
- **Coroutines**: Async operations

### Dependencies & Versions
- **Kotlin**: 2.0.21 (uses Compose compiler plugin)
- **Compose BOM**: 2024.09.00 (manages all Compose library versions)
- **Target SDK**: 36 (Android 15)
- **Min SDK**: 26 (Android 8)
- **Gradle**: 9.0.1 (AGP)
- **Dependency declarations**: Use version catalog in `gradle/libs.versions.toml` (e.g., `libs.androidx.compose.ui`)

### Testing Structure
- **Unit tests**: `app/src/test/` — JUnit 4, runs on JVM
- **Instrumented tests**: `app/src/androidTest/` — Espresso and Compose UI tests, run on device/emulator
- **Test runner**: `AndroidJUnitRunner`

## Design System

### Brand Colors
```kotlin
val BrandRed = Color(0xFFD32F2F)
val BrandRedDark = Color(0xFFB71C1C)
val BrandYellow = Color(0xFFFFC107)
val BrandOrange = Color(0xFFFF8F00)
val BackgroundLight = Color(0xFFFAFAFA)
val CardBackground = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
val StarYellow = Color(0xFFFFC107)
val GreenCart = Color(0xFF4CAF50)
```

### Spacing & Shape
- **Card corner radius**: `12.dp`
- **Bottom sheet corners**: `RoundedCornerShape(topStart=24.dp, topEnd=24.dp)`
- **CTA button shape**: `RoundedCornerShape(50.dp)` (pill shape)
- **Screen padding**: `16.dp`

## Backend API Integration

**Base URL**: `http://localhost:3000/api` (development)

**Authentication**: Bearer token in `Authorization` header
```
Authorization: Bearer <JWT_TOKEN>
```

**API Response Format** (standard envelope):
```json
{
  "status": "success | error",
  "message": "Human-readable description",
  "data": {}
}
```

**Key Endpoints** (see `.github/API_REFERENCE.md` for full spec):
- `POST /auth/register` — User registration
- `POST /auth/login` — Login and get JWT token
- `GET /products` — Fetch all products
- `GET /categories` — Fetch categories
- `POST /orders` — Create new order
- `GET /orders/:id` — Get order details

**Error Handling:**
- `400`: Bad request (validation errors)
- `401`: Unauthorized (invalid/expired token)
- `404`: Not found
- `409`: Conflict (duplicate username/email)
- `500`: Server error

## Key Conventions

### Kotlin & Compose Patterns
1. **Composables**: Mark all UI functions with `@Composable`; use `modifier: Modifier = Modifier` as the last parameter for composability
2. **Preview**: Use `@Preview` to make composables previewable in Android Studio; include a preview function for each screen-level composable
3. **State**: Prefer state hoisting; pass state as parameters rather than managing it deep in the compose tree
4. **Immutability**: Data classes and sealed classes for model types; avoid mutable state where possible

### Project Structure

```
app/src/main/java/com/example/rtexpress/
├── di/                          ← Dependency injection (Hilt)
│   ├── AppModule.kt
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
├── data/                        ← Data layer
│   ├── remote/
│   │   ├── api/                 ← Retrofit API services
│   │   └── dto/                 ← Data Transfer Objects
│   ├── local/
│   │   ├── AppDatabase.kt       ← Room Database
│   │   ├── dao/                 ← Data Access Objects
│   │   └── entity/              ← Room Entities
│   ├── repository/              ← Repository implementations
│   └── preferences/             ← DataStore (JWT tokens, user prefs)
├── domain/
│   ├── model/                   ← Domain models
│   ├── repository/              ← Repository interfaces
│   └── usecase/                 ← Business logic
├── presentation/
│   ├── ui/
│   │   ├── theme/               ← Material 3 theme & colors
│   │   ├── screens/             ← Screen composables
│   │   ├── components/          ← Reusable UI components
│   │   └── navigation/          ← Navigation graph
│   └── viewmodel/               ← ViewModels (state management)
└── MainActivity.kt
```

### Naming Conventions
- **Composable functions**: PascalCase (e.g., `MainScreen`, `UserCard`)
- **Compose preview functions**: `[FunctionName]Preview` (e.g., `MainScreenPreview`)
- **Theme components**: Descriptive names (e.g., `DarkColorScheme`, `LightColorScheme`)

### Resource Organization
- **Strings**: `app/src/main/res/values/strings.xml`
- **Colors**: `app/src/main/res/values/colors.xml` (also defined in `ui/theme/Color.kt`)
- **Drawables**: `app/src/main/res/drawable/`
- **Themes**: `app/src/main/res/values/styles.xml`

### Manifest
- **Location**: `app/src/main/AndroidManifest.xml`
- **App name**: Defined as string resource `@string/app_name`
- **Main activity**: `com.example.randtexpress.MainActivity` with `android:exported="true"` and MAIN/LAUNCHER intent filter

## State Management & ViewModel Patterns

### UiState Pattern
Each screen has an associated UiState data class that encapsulates all UI state:
```kotlin
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
```

ViewModels expose this state as a **read-only StateFlow**:
```kotlin
private val _uiState = MutableStateFlow(LoginUiState())
val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
```

Screens collect state with `collectAsStateWithLifecycle()` to ensure lifecycle-aware updates.

### Token & Session Management
- JWT tokens are stored in **DataStore** (via `UserPreferences.kt`) for secure, persistent storage
- `SessionViewModel` monitors token state and drives navigation between Auth and Home flows
- Authentication interceptor (in `NetworkModule`) automatically adds Bearer token to all API requests
- On 401 Unauthorized responses, implement token refresh or redirect to login

### Coroutines & Error Handling
All async operations run in `viewModelScope`:
```kotlin
fun login() {
    viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.login(...)
            // Handle success
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }
}
```

## Room Database Patterns

When caching data locally:
1. Create entity, DAO, and repository implementation
2. Use `@Transaction` for multi-entity operations
3. Emit updates via Flow in repository: `fun getItems(): Flow<List<Item>> = itemDao.observeAll()`
4. ViewModel collects Flow and updates UiState

**Cart Example**: CartItemEntity is stored locally, synced to server via OrderRepository.

## Custom JSON Deserializers

For complex or non-standard API responses (e.g., paginated lists), custom deserializers are defined in `data/remote/dto/response/`:
- `ProductListResponseDeserializer` — Extracts data from wrapper envelope
- `UserListResponseDeserializer` — Handles user list responses

**When adding new API response types**: Create a deserializer if the response structure is non-trivial.

## Development Tips

### Android Studio Integration
- Uses IDE-friendly Gradle configuration with parallel builds disabled (can be enabled in `gradle.properties`)
- Compose Preview works in-editor for `@Composable` functions
- R8 ProGuard rules in `app/proguard-rules.pro` (currently disabled for release builds)

### Version Catalog Usage
All dependency versions are centralized in `gradle/libs.versions.toml`. When adding dependencies:
1. Define version in `[versions]` section
2. Define library reference in `[libraries]` section
3. Use `libs.libraryName` in `build.gradle.kts`

Example: `implementation(libs.androidx.compose.ui)`

### Kotlin Code Style
- **Style**: "official" (set in `gradle.properties`)
- **JVM target**: Java 11
- **File encoding**: UTF-8
- **No linters configured** — Follow Kotlin's official style guidelines by convention

## Before You Start

1. **Behavioral Guidelines**: Read `.github/CLAUDE.md` for guidelines that reduce common mistakes
2. **API Contract**: Check `.github/API_REFERENCE.md` for all endpoints and response formats
3. **Architecture**: Review `.github/RT_Express_Jetpack_Compose_Prompt_v2.md` for system design and project structure
4. **Setup**: Verify Android SDK is installed and `local.properties` exists with `sdk.dir` pointing to it
5. **Verify Gradle**: Run `./gradlew --version` to confirm Gradle is working
