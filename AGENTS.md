# Project Agent Instructions

## Language

- 기본 사용자 응답은 한국어로 작성한다.
- 코드, 경로, 명령어, API 이름은 프로젝트의 기존 표기를 유지한다.

## Repository Purpose

이 저장소는 "가슴속 3천원" **사장님 앱** Android다. 푸드트럭·노점 사장님이 영업 시작/종료, 가게 정보·메뉴·영업 일정 관리, 리뷰 응대, 설정을 하는 앱이다.

## Project Shape

- Gradle Kotlin DSL 기반 Android multi-module 프로젝트다. 공통 설정은 `build-logic/` convention plugin이 주입한다.
- 모듈 목록은 `settings.gradle.kts`를 원천으로 확인한다.
- 현재 모듈: `:app`, `:navigation`, `:feature:home`, `:feature:storemanagement`, `:feature:review`, `:feature:setting`, `:feature:ai`, `:data`, `:domain`, `:common`.
- 세션을 새로 시작할 때는 먼저 `docs/README.md`를 읽고 필요한 context 문서를 따라간다.

## Current Toolchain

- 버전 원천은 `gradle/libs.versions.toml`이다.
- 현재 확인된 값: AGP `8.7.2`, Kotlin `2.0.0`, Gradle `8.9`, compileSdk `35`, targetSdk `35`, minSdk `29`.
- Java/Kotlin toolchain은 JDK 17 기준이다.
- 버전 변경이나 의존성 업그레이드는 사용자 승인 없이 하지 않는다.

## Canonical Commands

- Debug APK: `./gradlew assembleDebug`
- Release APK: `./gradlew assembleRelease`
- 단위 테스트: `./gradlew testDebugUnitTest` (CI 기준. `./gradlew test`는 전체 variant)
- 모듈 의존 방향 검사: `scripts/check-module-deps.sh`
- Compose 전용 UI 검사: `scripts/check-compose-only.sh`
- 테스트 결과 요약(PR 코멘트 형식): `scripts/test-summary.sh`
- 의존성 확인: `./gradlew dependencies`
- 로컬 빌드에는 `local.properties`와 `google-services.json`이 필요하다(`CLAUDE.md` "Local Development Setup").
- 필요한 범위의 최소 Gradle task를 우선 사용하고, full clean build는 필요한 경우에만 실행한다.

## Documentation Map

- 모듈 의존성·구조: `docs/context/module-dependencies-current.md`
- 변경 유형별 검증: `docs/context/verification-matrix.md`
- 에이전트 skill 인덱스: `docs/agents/skill-index.md`
- AI 개발 프로세스(테크스펙 → 테스트 → 증거 → PR): `docs/process/`

## AI Development Process

- 전체 흐름과 PR 위험도(경량/풀코스) 기준은 `docs/process/pr-process.md` 한 장에 있다. 유저앱 iOS/AOS 레포와 같은 형태를 쓴다.
- 의도 문서(테크스펙)는 `docs/process/tech-spec-process.md`. 지라 `테크스펙` 필드가 단일 진실 소스이고 iOS/AOS가 스펙을 공유한다.
- 테스트는 diff가 아니라 테크스펙 TC에서 도출한다. 세 계층(유닛/자동화/수동)과 네이밍은 `docs/process/testing.md`.
- 유닛으로 못 덮는 TC의 자동화·수동 분류와 화면 변경 공통 체크는 `docs/process/e2e-and-manual-tests.md`.
- 검증 장치(CI·스크립트·규칙 문서)를 바꾸는 PR은 `docs/process/verification-change.md`의 라벨 규칙을 따른다.
- PR은 `/3dollars:pr-body`로 만들고, 본문 형식은 `.github/PULL_REQUEST_TEMPLATE.md`를 따른다.

## Agent Workflow

- 작업 전 짧은 구현 계획을 먼저 보고한다.
- 변경은 작고 원자적인 단위로 수행한다.
- 관련 없는 파일은 수정하지 않는다. 특히 git이 추적 중인 `*/build/**` 산출물이 빌드로 바뀌면 커밋하지 말고 되돌린다.
- 기존 사용자 변경사항을 되돌리지 않는다.
- 대규모 리팩터링, 의존성 추가/업그레이드, 포맷 전용 변경은 명시적 승인 없이 하지 않는다.
- 완료 전 변경 범위에 맞는 최소 검증을 실행하고, 실행하지 못한 검증은 보고한다.

## Architecture

- Clean Architecture + MVVM(Compose) 흐름: UI → ViewModel → UseCase → Repository(`:domain` 인터페이스) → DataSource(`:data`) → API/DataStore.
- feature는 `:domain`·`:common`만 쓴다. feature 간 직접 의존은 금지이고, 화면 간 이동은 `:navigation`·`:app`이 조립한다.
- 공통 UI·`BaseViewModel`·`EventFlow`·`Resource`는 `:common`에 있다. 새 추상화보다 기존 유틸리티와 패턴을 우선한다.

## UI

- **신규 UI는 Jetpack Compose로만 작성한다.** XML 레이아웃(`res/layout*/`), Fragment 기반 화면, ViewBinding/DataBinding을 새로 만들지 않는다.
- 현재 앱에는 XML 레이아웃이 하나도 없다. `scripts/check-compose-only.sh`가 CI(`lint.yml`)에서 이를 강제한다.
- 화면은 기존 feature 패턴을 따른다: `XxxRoute`(ViewModel 연결) → `XxxScreen`(상태를 받는 순수 Composable) + `XxxContract`(State/Effect) + `XxxViewModel`. 하위 UI는 `components/`에 둔다.
- 다이얼로그는 `:common`의 `BaseDialog` Composable을 쓴다. `:common`의 `BaseFragment`는 쓰이지 않는 레거시이므로 새 코드에서 쓰지 않는다.
- Compose API가 없는 SDK 뷰만 `AndroidView { }`로 감싼다. 이때 사유를 PR 본문 "설명이 필요한 결정"에 남긴다. 네이버 지도는 `naver-map-compose`를 쓴다.
- 리소스 XML(drawable, values, mipmap)은 대상이 아니다. 색·문자열은 기존 `res/values`와 `:common` 리소스를 먼저 확인한다.

## Safety

- `local.properties`, 키스토어, `google-services.json`, 카카오/네이버맵 키, 서버 URL 등 민감정보를 노출하지 않는다.
- destructive git 명령은 사용자가 명시적으로 요청한 경우에만 수행한다.
- 문서 변경은 사실을 코드베이스에서 확인한 뒤 반영한다.
