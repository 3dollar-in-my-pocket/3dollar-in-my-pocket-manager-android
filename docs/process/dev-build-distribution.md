# 개발 빌드 배포 (Firebase App Distribution)

PR 검증·QA용 debug 빌드(`app.threedollars.manager.debug`)를 Firebase App Distribution으로 배포한다.
워크플로는 `.github/workflows/firebase-distribution.yml`("Firebase App Distribution"), 유저앱 AOS와 같은 이름이라 `3dollars:deploy-dev-build` 스킬이 그대로 호출한다.

```bash
gh workflow run "Firebase App Distribution" --ref <브랜치>
gh workflow run "Firebase App Distribution" --ref <브랜치> -f groups=android -f release_notes="TH-1234 검증용"
```

지금은 **수동 실행만** 열려 있다. 시크릿이 모두 준비되고 한 번 성공하면 유저앱처럼 `release/**` PR 머지 트리거를 추가한다.

## 필요한 레포 시크릿

워크플로 첫 스텝이 필수 시크릿이 비어 있는지 검사하고, 없으면 이름을 찍고 실패한다.

| 시크릿 | 필수 | 내용 | 현재 상태 (TH-1385 확인) |
|---|---|---|---|
| `KEYSTORE_BASE64` | ✅ | `app/ThreeDollarsManager.jks` 를 base64로 인코딩한 값. debug 빌드도 이 키로 서명한다 | **없음** — 신규 등록 |
| `LOCAL_PROPERTIES` | ✅ | `local.properties` 전체 내용. `kakao_key_dev`, `kakao_key_release`, `base_url_dev`, `base_url_release`, `naver_map_client_id` 필수(`sdk.dir`은 워크플로가 채움) | **없음** — 신규 등록. 기존 `LOCAL_PROPERTIRES`(오타, 2022-03)는 `base_url_*`(2023-02)·`naver_map_client_id`(2022-05) 도입 전 값이라 쓸 수 없다 |
| `GOOGLE_SERVICES_JSON` | ✅ | debug용 `google-services.json` 전체 내용(`app/src/debug/`에 놓이는 파일). `app.threedollars.manager.debug` 클라이언트가 있어야 한다 | 있음(2022-03) — **`.debug` suffix(2022-04) 도입 전 값이라 갱신 필요** |
| `FIREBASE_APP_ID` | ✅ | Firebase 콘솔의 `app.threedollars.manager.debug` 앱 ID (`1:…:android:…`) | 있음(2022-03) — `.debug` 앱 ID가 맞는지 확인 후 갱신 |
| `FIREBASE_SERVICE_ACCOUNT` | ✅ | App Distribution 권한(`Firebase App Distribution Admin`)이 있는 서비스 계정 JSON | **없음** — 신규 등록. 기존 `FIREBASE_TOKEN`(CLI 토큰 방식)은 Firebase가 폐기 예정이라 쓰지 않는다 |
| `SLACK_WEBHOOK_URL` | 선택 | 배포 결과 알림 | 있음(2022-03) — 살아 있는지 모름. 실패해도 배포는 성공 처리 |
| `DISCORD_WEBHOOK_URL` | 선택 | 배포 결과 알림 | 없음 — 비어 있으면 생략 |

더 이상 쓰지 않는 시크릿: `LOCAL_PROPERTIRES`, `FIREBASE_TOKEN`, `SLACK_TOKEN` — 배포가 한 번 성공한 뒤 지운다.

### 등록 명령 (레포 관리자, 로컬에 실제 파일이 있을 때)

```bash
base64 -i app/ThreeDollarsManager.jks | gh secret set KEYSTORE_BASE64
gh secret set LOCAL_PROPERTIES < local.properties
gh secret set GOOGLE_SERVICES_JSON < app/src/debug/google-services.json
gh secret set FIREBASE_APP_ID --body "1:…:android:…"
gh secret set FIREBASE_SERVICE_ACCOUNT < service-account.json
```

## Firebase 쪽 확인

- `app.threedollars.manager.debug` 패키지 앱이 Firebase 프로젝트에 등록돼 있어야 한다. APK 패키지와 `FIREBASE_APP_ID`의 앱이 다르면 업로드가 거부된다.
- 테스터 그룹 기본값은 `android`(2022년 워크플로 값). 콘솔에 같은 이름의 그룹이 없으면 실행 시 `groups` 입력으로 바꾸거나 기본값을 고친다.

## 참고

- 키스토어 비밀번호·alias는 `app/build.gradle.kts`에 평문으로 들어 있고 레포는 public이다. `.jks` 파일 자체는 gitignore라 유출되지 않았지만, 시크릿으로 옮기는 것은 별도 과제로 검토한다.
- 2022년에 같은 목적의 `github-ci.yml`(firebase CLI + 토큰 방식)이 있었으나 삭제됐다(`521d992`).
