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
./gradlew testDebugUnitTest      # Unit tests (CI 기준, test.yml)
./gradlew test                    # All variants
./gradlew connectedAndroidTest   # Instrumentation tests (requires device, CI 미실행)
```

### Module Boundary Check
```bash
scripts/check-module-deps.sh     # 모듈 의존 방향 검사 (CI 기준, lint.yml)
scripts/check-compose-only.sh    # XML 레이아웃·ViewBinding 금지 검사 (CI 기준, lint.yml)
scripts/test-summary.sh          # 테스트 결과를 PR 코멘트 형식으로 요약
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
    ├── setting/      # User settings and account management
    └── ai/           # AI recommendation
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

## AI Development Process

유저앱 iOS/AOS 레포와 같은 프로세스(테크스펙 → 테스트 → 증거 → PR)를 쓴다. 세션 시작 시 `AGENTS.md`와 `docs/README.md`를 먼저 읽는다.

- 전체 흐름과 PR 위험도(경량/풀코스) 기준: `docs/process/pr-process.md`
- 의도 문서(테크스펙): `docs/process/tech-spec-process.md` — 지라 `테크스펙` 필드가 단일 진실 소스, iOS/AOS 공유
- 테스트 세 계층과 `` `TH{티켓}_TC{n}_…` `` 네이밍: `docs/process/testing.md`
- 자동화·수동 체크리스트: `docs/process/e2e-and-manual-tests.md`
- 검증 장치 변경 라벨: `docs/process/verification-change.md`
- 모듈 의존성 현황·규칙: `docs/context/module-dependencies-current.md`
- PR 생성은 `/3dollars:pr-body`, 본문 형식은 `.github/PULL_REQUEST_TEMPLATE.md`
- 개발 빌드 배포: `firebase-distribution.yml` (`docs/process/dev-build-distribution.md`)
- 기본 응답 언어는 한국어. 버전 변경·의존성 추가·`build-logic` 변경은 사용자 승인 없이 하지 않는다.
- **신규 UI는 Jetpack Compose로만 작성한다.** XML 레이아웃·Fragment·ViewBinding을 새로 만들지 않는다(`scripts/check-compose-only.sh`). 세부 규칙은 `AGENTS.md` "UI".

## Module Dependencies

- `app` depends on all feature modules, navigation, common
- Feature modules depend on domain, common (feature 간 직접 의존 금지, `:data` 의존 금지 — `scripts/check-module-deps.sh`)
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

Unit tests use JUnit4 (`build-logic`의 `configureKotlinAndroid`가 모든 Android 모듈에 주입). 테스트 작성 규칙은 `docs/process/testing.md`. Instrumentation tests use Espresso. Repository tests mock data sources. ViewModel tests verify state changes and use case interactions.