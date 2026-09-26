# 현재 모듈 의존성

이 문서는 실제 `settings.gradle.kts`와 각 `build.gradle.kts` 기준의 현재 상태를 기록한다.

의존 방향은 `scripts/check-module-deps.sh`가 CI(`lint.yml`)에서 강제한다. 규칙은 두 가지다.

- **랭크**: 자기보다 낮은 계층만 의존한다 (`:app` → `:navigation` → `:feature:*` → `:data` → `:domain` → `:common`). 같은 랭크끼리도 금지라 feature 간 직접 의존이 막힌다.
- **금지쌍**: 랭크로는 표현할 수 없어 명시적으로 막는 간선.
  - `:feature:*` → `:data` — feature는 `:domain`의 Repository 인터페이스·UseCase만 쓴다.

아래 그래프를 바꾸면 그 스크립트의 랭크 표와 금지쌍도 함께 확인한다.

## 모듈 목록

원천: `settings.gradle.kts`

| 모듈 | 플러그인 (`build-logic`) | 역할 |
|---|---|---|
| `:app` | `threedollars.android.application` | `MainActivity`, `LoginActivity`(로그인·회원가입 `sign/**`), `AppSchemeActivity`(딥링크), FCM 서비스, Hilt 진입점 |
| `:navigation` | `threedollars.android.feature` | `MainNavigator`, 하단 탭(`TabType`) |
| `:feature:home` | `threedollars.android.feature` | 지도 기반 영업 시작/종료, 영업 상태 |
| `:feature:storemanagement` | `threedollars.android.feature` | 가게 정보·메뉴·영업 일정·계좌·사장님 한마디·피드백 |
| `:feature:review` | `threedollars.android.feature` | 리뷰 목록(페이징)·사장님 댓글 |
| `:feature:setting` | `threedollars.android.feature` | 설정·FAQ·로그아웃/탈퇴 |
| `:feature:ai` | `threedollars.android.feature` | AI 추천 |
| `:data` | `threedollars.android.library` | Retrofit API, DataSource, Repository 구현, DataStore, Hilt 모듈 |
| `:domain` | `threedollars.android.library` | Repository 인터페이스, UseCase, DTO(`domain/dto`) |
| `:common` | `threedollars.android.feature` | `BaseViewModel`, `EventFlow`, `Resource`, 공통 Compose UI·리소스 |

## 프로젝트 의존성 그래프

```text
:app
  -> :common
  -> :data
  -> :domain
  -> :feature:home
  -> :feature:storemanagement
  -> :feature:setting
  -> :feature:review
  -> :feature:ai
  -> :navigation

:navigation
  -> :feature:setting
  -> :common

:feature:{home,storemanagement,setting,review,ai}
  -> :common
  -> :data      (동결 — 아래 참고)
  -> :domain

:data
  -> :common
  -> :domain

:domain
  -> :common

:common
  -> no project module dependency
```

## 현재 구조 해석

- **feature → `:data` 5건은 죽은 선언이다.** 5개 feature 모두 `:data`의 패키지(`app.threedollars.{data,repository,source,di,network,db}`)를 import 하지 않는다. `scripts/module-deps-baseline.txt`에 동결했고, 제거는 별도 티켓에서 빌드 검증과 함께 한다.
- `:domain`은 순수 Kotlin이 아니라 Android library다.
  - `:common`을 의존하지만 실제로 쓰는 건 `app.threedollars.common.Resource` 하나다. `:common`은 Compose를 쓰는 UI 모듈이라 domain이 UI 모듈을 끌고 오는 셈이다.
  - `okhttp3`에 직접 의존한다. `StoreRepository`와 `ImageUploadUseCase`가 이미지 업로드에 `MultipartBody`/`RequestBody`를 인터페이스로 노출한다.
  - 둘 다 모듈 간선 방향 문제가 아니라 `scripts/check-module-deps.sh` 범위 밖이다.
- `:data`의 패키지 루트는 `app.threedollars.data`가 아니라 `app.threedollars.*`(`repository`, `source`, `di`, `network`, `db`, `data`)로 나뉘어 있다.
- 소스 디렉터리는 `:app`·`:common`·`:data`·`:domain`·`:feature:setting`이 `src/main/java`, 나머지 feature와 `:navigation`이 `src/main/kotlin`이다.
- 공통 Android 설정(compileSdk, minSdk, JDK, desugaring, JUnit)은 `build-logic`의 convention plugin이 주입한다. 모듈 `build.gradle.kts`에는 모듈 고유 의존만 둔다.

## 작업 시 주의

- 모듈 추가, 의존성 방향 변경, `build-logic` 변경은 사용자 승인 없이 진행하지 않는다.
- 의존성 변경이 발생하면 이 문서를 함께 갱신한다.
- 새 의존을 넣기 전에 `scripts/check-module-deps.sh`를 돌린다. 실패하면 **먼저 방향을 뒤집는 설계를 검토하고**, 베이스라인 추가는 마지막 수단이다 (`scripts/module-deps-baseline.txt`는 줄어들기만 해야 한다).
- feature 간에 공유할 코드는 다른 feature를 의존하지 말고 `:common`(UI) 또는 `:domain`(모델·UseCase)으로 내린다. 화면 간 이동은 `:navigation`·`:app`이 조립한다.
