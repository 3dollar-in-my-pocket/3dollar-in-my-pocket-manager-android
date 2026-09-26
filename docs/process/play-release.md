# Google Play 업로드

release AAB(`app.threedollars.manager`)를 Google Play에 올린다. 워크플로는 `.github/workflows/play-release.yml`("Play Release").
유저앱 AOS와 달리 **버전은 사람이 올리고, 워크플로는 업로드만** 한다(버전 자동 증가·태그·GitHub Release 없음).

| 언제 | 트랙 | 상태 |
|---|---|---|
| `release/**` 브랜치에 `app/build.gradle.kts`(버전)가 바뀐 커밋 push | 프로덕션 | **초안(draft)** — Play Console에서 사람이 검토 후 출시 |
| 수동 실행 `track=internal` (기본) | 내부 테스트 | 즉시 배포(completed) |
| 수동 실행 `track=production` | 프로덕션 | 초안(draft) |

```bash
gh workflow run "Play Release" --ref release/1.1.12                     # 내부 테스트
gh workflow run "Play Release" --ref release/1.1.12 -f track=production # 프로덕션 초안
```

## 릴리즈 순서

1. `develop`에서 `release/x.y.z` 브랜치를 만든다.
2. `app/build.gradle.kts`의 `versionCode`(+1)·`versionName`을 올려 커밋·push → 프로덕션 초안이 자동으로 올라간다.
3. Play Console → 프로덕션 → 초안 검토 후 출시.
4. 기존처럼 `release/x.y.z` → `master` PR 머지, `develop`에 역머지.

**versionCode는 트랙과 무관하게 한 번만 쓸 수 있다.** 같은 versionCode로 내부 테스트에 올렸다면 프로덕션에는 다시 올릴 수 없으니,
Play Console에서 그 내부 테스트 버전을 프로덕션으로 **승격**하거나 versionCode를 올려 다시 push한다.
release 브랜치에 버전 변경 없이 추가 커밋만 push하면 자동 업로드는 돌지 않는다(`paths` 필터).

"새로운 기능" 문구는 `origin/master..HEAD`(지난 릴리즈 이후) 커밋 제목으로 만들고 Play 제한(500자)에 맞춰 자른다. 스토어에 공개되는 문구이므로 출시 전에 Play Console에서 다듬는다.

## 필요한 레포 시크릿

`firebase-distribution.yml`과 공유: `KEYSTORE_BASE64`, `KAKAO_KEY_DEV`, `KAKAO_KEY_RELEASE`, `BASE_URL_DEV`, `BASE_URL_RELEASE`, `NAVER_MAP_CLIENT_ID`, (선택) `DISCORD_WEBHOOK_URL` — `docs/process/dev-build-distribution.md`.

추가로 필요한 것:

| 시크릿 | 내용 |
|---|---|
| `GOOGLE_SERVICES_JSON_RELEASE` | release용 `google-services.json` 전체(`app/src/release/`에 놓이는 파일). `app.threedollars.manager` 클라이언트가 있어야 한다 |
| `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` | Play Console에 초대된 서비스 계정의 키 JSON (아래) |

```bash
gh secret set GOOGLE_SERVICES_JSON_RELEASE < app/src/release/google-services.json
gh secret set GOOGLE_PLAY_SERVICE_ACCOUNT_JSON < play-service-account.json
```

## `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` 만들기

서비스 계정 키 JSON은 **발급 시점에 한 번만 다운로드**되고 다시 조회할 수 없다. 유저앱에 등록된 값도 꺼낼 수 없으므로, 새로 발급한다.

**권장: 사장님앱 전용 서비스 계정**

1. GCP 콘솔에서 사용할 프로젝트를 고르고 **Google Play Android Developer API**를 사용 설정한다.
   https://console.cloud.google.com/apis/library/androidpublisher.googleapis.com
2. 같은 프로젝트에서 서비스 계정 생성(GCP 역할은 필요 없음) → 키 → 새 키 → JSON 다운로드.
3. Play Console → **사용자 및 권한** → 새 사용자 초대 → 이메일에 서비스 계정 주소(`…@….iam.gserviceaccount.com`) 입력.
   - 앱 권한: **사장님앱만** 추가
   - 권한: 앱 정보 보기, **프로덕션 트랙에 앱 출시**, **테스트 트랙에 앱 출시**(출시 관리)
4. 다운로드한 JSON을 `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON`에 등록한다.

**대안: 유저앱이 쓰는 서비스 계정에 키만 추가**

Play Console → 사용자 및 권한에서 유저앱 업로드용 서비스 계정 이메일을 찾아, 그 계정에 사장님앱 권한을 추가한다.
그다음 GCP 콘솔에서 그 계정에 **새 키를 하나 더** 발급한다(기존 키는 그대로라 유저앱 CI에 영향 없음).
단계는 적지만, 이 키 하나로 유저앱에도 업로드할 수 있게 된다.

## Play Console 쪽 확인

- **업로드 키**: `ThreeDollarsManager.jks`가 Play Console → 앱 무결성 → 업로드 키 인증서와 같아야 한다.
  ```bash
  keytool -list -v -keystore app/ThreeDollarsManager.jks -alias ThreeDollarsManager -storepass ThreeDollarsManager | grep SHA1
  ```
- 서비스 계정을 초대한 직후에는 권한 반영에 시간이 걸릴 수 있다(보통 수 분, 길면 하루). `403`/`The caller does not have permission`이면 먼저 기다렸다가 재실행한다.
