# 개발 빌드 배포 (Firebase App Distribution)

PR 검증·QA용 debug 빌드(`app.threedollars.manager.debug`)를 Firebase App Distribution으로 배포한다.
워크플로는 `.github/workflows/firebase-distribution.yml`("Firebase App Distribution"), 유저앱 AOS와 같은 이름이라 `3dollars:deploy-dev-build` 스킬이 그대로 호출한다.

```bash
gh workflow run "Firebase App Distribution" --ref <브랜치>
gh workflow run "Firebase App Distribution" --ref <브랜치> -f groups=android -f release_notes="TH-1234 검증용"
```

지금은 **수동 실행만** 열려 있다. 2026-09-26 첫 배포 성공(1.1.11 (21), Firebase 프로젝트 `dollars-manager-dev`, 디스코드 알림 확인). 필요하면 유저앱처럼 `release/**` PR 머지 트리거를 추가한다.

## 필요한 레포 시크릿

워크플로 첫 스텝이 필수 시크릿이 비어 있는지 검사하고, 없으면 이름을 찍고 실패한다.
`local.properties`는 유저앱 AOS처럼 **키별 시크릿**으로 만든다. 값에 따옴표를 넣든 안 넣든 워크플로가 맞춘다(`kakao_*`·`base_url_*`는 따옴표로 감싸고, `naver_map_client_id`는 벗긴다).

| 시크릿 | 필수 | 내용 |
|---|---|---|
| `KEYSTORE_BASE64` | ✅ | 기존 `app/ThreeDollarsManager.jks`(release 서명 키)를 base64로 인코딩한 값. **새로 만들지 않는다.** debug 빌드도 이 키로 서명한다 |
| `KAKAO_KEY_DEV` | ✅ | `local.properties`의 `kakao_key_dev` 값 |
| `KAKAO_KEY_RELEASE` | ✅ | `kakao_key_release` 값. debug 빌드여도 Gradle 설정 단계에서 읽으므로 필요 |
| `BASE_URL_DEV` | ✅ | `base_url_dev` 값. debug 빌드의 서버 주소 |
| `BASE_URL_RELEASE` | ✅ | `base_url_release` 값 |
| `NAVER_MAP_CLIENT_ID` | ✅ | `naver_map_client_id` 값 |
| `GOOGLE_SERVICES_JSON` | ✅ | debug용 `google-services.json` 전체 내용(`app/src/debug/`에 놓이는 파일). `app.threedollars.manager.debug` 클라이언트가 있어야 한다 |
| `FIREBASE_APP_ID` | ✅ | Firebase 프로젝트 `dollars-manager-dev`(3dollars-manager-dev)의 `app.threedollars.manager.debug` 앱 ID (`1:…:android:…`) |
| `FIREBASE_SERVICE_ACCOUNT` | ✅ | `dollars-manager-dev` 프로젝트의 전용 서비스 계정 `github-app-distribution@…` 키 JSON. 역할은 `Firebase App Distribution Admin`(Firebase 앱 배포 관리자) 하나만. 자동 생성된 `firebase-adminsdk` 계정은 권한이 넓어 쓰지 않는다 |
| `SLACK_WEBHOOK_URL` | 선택 | 배포 결과 알림 |
| `DISCORD_WEBHOOK_URL` | 선택 | 배포 결과 알림 |

더 이상 쓰지 않아 삭제한 시크릿: `LOCAL_PROPERTIRES`, `FIREBASE_TOKEN`, `SLACK_TOKEN`, `SLACK_WEBHOOK_URL`(슬랙 알림은 시크릿이 없으면 생략).

### 등록 명령 (레포 관리자, 로컬에 실제 파일이 있을 때)

```bash
base64 -i app/ThreeDollarsManager.jks | gh secret set KEYSTORE_BASE64
gh secret set KAKAO_KEY_DEV --body "…"
gh secret set KAKAO_KEY_RELEASE --body "…"
gh secret set BASE_URL_DEV --body "https://…"
gh secret set BASE_URL_RELEASE --body "https://…"
gh secret set NAVER_MAP_CLIENT_ID --body "…"
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
