# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

3달러 매니저 안드로이드 앱은 Clean Architecture 기반의 멀티 모듈 구조를 가진 푸드트럭/길거리 음식점 사장용 관리 앱입니다.

## Build Commands

### Development Build
```bash
./gradlew assembleDebug
```

### Release Build  
```bash
./gradlew assembleRelease
```

### Run Tests
```bash
./gradlew test                    # All unit tests
./gradlew testDebugUnitTest      # Debug unit tests only
./gradlew connectedAndroidTest   # Instrumentation tests (requires device)
```

### Code Quality
```bash
./gradlew lint                   # Run lint checks
./gradlew lintFix               # Auto-fix lint issues
./gradlew check                 # Run all verification tasks
```

### Clean Build
```bash
./gradlew clean build
```

## Architecture Overview

### Multi-Module Structure
```
app/                    # Main application module (Activities, Application class)
├── common/            # Shared utilities, base classes, UI components
├── navigation/        # Navigation logic and routing
├── data/             # Data layer (Repository implementations, API, DataStore)
├── domain/           # Business logic (UseCase, Repository interfaces, DTOs)
└── feature/          # Feature modules
    ├── home/         # Map-based store operations and status management
    ├── storemanagement/ # Store info, menu, schedule, account management
    ├── review/       # Review management with paging and comments
    └── setting/      # User settings and account management
```

### Key Architectural Patterns

**Clean Architecture**: Data → Domain → Feature modules with clear separation of concerns

**MVVM with Compose**: 
- `BaseViewModel` provides common functionality (loading states, exception handling)
- `EventFlow` for one-time UI events to prevent recomposition issues
- `Resource<T>` wrapper for API response states (Success/Error)

**Type-Safe Navigation**: 
- `RouteModel` with Kotlinx Serialization for compile-time route safety
- `MainNavigator` manages navigation state and bottom bar visibility
- Each feature module defines its own navigation graph

**Dependency Injection**: 
- Hilt with constructor injection throughout
- Modular DI structure (NetworkModule, DataSourceModule, RepositoryModule)

### Data Flow Pattern
```
UI (Compose) → ViewModel → UseCase → Repository → DataSource → API/DataStore
```

### Key Components

**BaseViewModel**: Common ViewModel functionality including loading states and exception handling using `EventFlow<Boolean>` for loading indicators.

**EventFlow**: Custom Flow wrapper that ensures single consumption for UI events, preventing duplicate handling during recomposition.

**Resource<T>**: Sealed class wrapping API responses with Success/Error states and response codes.

**DataStore Integration**: Local data persistence using AndroidX DataStore with JSON serialization via Moshi.

## Module Dependencies

- `app` depends on all feature modules, navigation, common
- Feature modules depend on domain, common  
- Domain defines repository interfaces implemented in data
- Data layer handles API calls and local storage
- Common provides shared utilities and base classes

## Key Configuration

**Build Logic**: Custom Gradle convention plugins in `build-logic/` module standardize configuration across modules.

**Version Catalog**: `gradle/libs.versions.toml` centrally manages all dependency versions.

**Firebase Integration**: Crashlytics, Analytics, and Cloud Messaging configured.

**External APIs**: Kakao Login SDK, Naver Maps, Google Location Services.

## Local Development Setup

1. Add `local.properties` with required API keys:
   ```
   kakao_key_dev="YOUR_KAKAO_KEY"
   kakao_key_release="YOUR_KAKAO_KEY" 
   base_url_dev="YOUR_DEV_URL"
   base_url_release="YOUR_RELEASE_URL"
   naver_map_client_id=YOUR_NAVER_MAP_ID
   ```

2. Place Firebase config files:
   - `app/src/debug/google-services.json` 
   - `app/src/release/google-services.json`

## Testing Strategy

Unit tests use JUnit4. Instrumentation tests use Espresso. Repository tests mock data sources. ViewModel tests verify state changes and use case interactions.