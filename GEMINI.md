# RandTExpress - Project Overview

RandTExpress is a modern Android mobile application for an e-commerce/food delivery platform. It is built using Kotlin and Jetpack Compose, following Clean Architecture principles to ensure scalability and maintainability.

## Technology Stack

- **Language:** [Kotlin 2.0.21](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Local Storage:** [Room](https://developer.android.com/training/data-storage/room) (Database) & [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (Preferences)
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
- **Async/Concurrency:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Build System:** Gradle (Kotlin DSL) with [Version Catalogs](https://developer.android.com/build/migrate-to-catalogs)

## Architecture

The project follows **Clean Architecture** patterns, organized into the following layers within the `app` module:

- **`domain`**: Contains business logic, domain models (POJOs), and repository interfaces. This layer is independent of any other layer.
- **`data`**: Implements the repository interfaces. Handles data sourcing from the network (Remote) and local database (Local). Includes Mappers to convert data models to domain models.
- **`presentation`**: Contains Hilt-injected ViewModels that manage UI state using `StateFlow`.
- **`ui`**: Contains Compose screens, reusable components, navigation logic, and theme definitions.
- **`di`**: Hilt modules for providing dependencies across the app.
- **`core`**: Common utilities, constants, and base classes.

## Getting Started

### Prerequisites
- Android Studio Ladybug or newer.
- JDK 17+.

### Building and Running
- **Build the project:** `./gradlew assembleDebug`
- **Run Unit Tests:** `./gradlew test`
- **Run Instrumented Tests:** `./gradlew connectedAndroidTest`
- **Linting:** `./gradlew lint`

## Development Conventions

- **Clean Architecture:** Always separate business logic from UI and data implementation.
- **Compose UI:** Use Jetpack Compose for all new UI. Avoid XML layouts.
- **Dependency Injection:** Use Hilt for all dependency injection. Use `@Inject` for constructors and `@Provides` in modules where necessary.
- **State Management:** Use `StateFlow` in ViewModels to expose UI state. Prefer immutable data classes for `UiState`.
- **Naming Conventions:**
    - Screens: `*Screen.kt`
    - ViewModels: `*ViewModel.kt`
    - Repositories: `*Repository.kt` (interface in domain) and `*RepositoryImpl.kt` (implementation in data).
- **Dependency Management:** All dependencies are managed in `gradle/libs.versions.toml`. Do not hardcode versions in `build.gradle.kts` files.

## Backend Integration

The app connects to a Node.js/Express backend. Detailed API documentation can be found in `.github/API_REFERENCE.md`.
- **Base URL:** `http://10.0.2.2:3000/api` (default development)
- **Auth:** Bearer Token (JWT).

## Key Files

- `gradle/libs.versions.toml`: Centralized dependency management.
- `app/src/main/java/com/example/randtexpress/MainActivity.kt`: Main entry point.
- `.github/CLAUDE.md`: Behavioral guidelines for AI coding assistance.
