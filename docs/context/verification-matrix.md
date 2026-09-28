# 검증 매트릭스

변경 범위에 맞는 최소 검증을 선택한다.

PR을 여는 작업이라면 이 표만으로는 부족하다. 테크스펙 TC에서 도출한 테스트 계층과 증거 요구는 `docs/process/testing.md`,
PR 위험도별 요구 수준은 `docs/process/pr-process.md`를 따른다.

## 기본

| 변경 유형 | 권장 검증 |
| --- | --- |
| 문서만 변경 | 링크·경로가 실제로 있는지 확인, 관련 Markdown 검토 |
| Gradle/의존성/`build-logic` 변경 | `./gradlew dependencies`, `./gradlew testDebugUnitTest`, `./gradlew assembleDebug` |
| 모듈 의존성 변경 | `scripts/check-module-deps.sh`, `docs/context/module-dependencies-current.md` 갱신 |
| DTO/mapper/UseCase 변경 | 해당 모듈 `testDebugUnitTest --tests ...` |
| ViewModel/Contract 변경 | 해당 feature 모듈 `testDebugUnitTest`, `./gradlew assembleDebug` |
| Compose UI 변경 | 관련 unit test, `./gradlew assembleDebug`, 에뮬레이터 자동화 TC |
| 지도/위치/영업 상태 변경 | `./gradlew assembleDebug`, 에뮬레이터 자동화 TC(`adb emu geo fix`) |
| 로그인/딥링크/푸시 변경 | `./gradlew assembleDebug`, 자동화 TC + 실기기 수동 TC |
| release/CI 설정 변경 | 관련 workflow dry review, release build task는 필요 시 별도 승인 |

## 자주 쓰는 명령

```bash
./gradlew testDebugUnitTest                        # CI(test.yml)와 같은 범위
./gradlew :feature:home:testDebugUnitTest          # 모듈만
./gradlew assembleDebug
scripts/check-module-deps.sh                       # CI(lint.yml)와 같은 범위
scripts/check-compose-only.sh                      # CI(lint.yml)와 같은 범위
scripts/test-summary.sh                            # 로컬 테스트 결과를 PR 코멘트 형식으로
```

## 보고 규칙

- 실행한 검증, 실패한 검증, 실행하지 못한 검증을 완료 보고에 구분해서 남긴다.
- 네트워크, 기기, 권한, 로그인 상태에 의존한 검증은 제약을 함께 적는다.
- 실서버 응답 기반 QA는 데이터가 실행 시점에 달라질 수 있음을 기록한다.
