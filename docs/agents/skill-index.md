# Skill Index

## 3dollars 플러그인 스킬 (AI 개발 프로세스)

`3dollars` Claude 플러그인은 **개인 환경 플러그인**이다. 저장소에 포함되지 않으므로 팀원 환경에 없을 수 있다.
호출 시 `3dollars:` 접두어가 필요하다. 프로세스 정의는 `docs/process/` 에 있고, 스킬은 그 문서를 실행하는 도구다.
유저앱 iOS/AOS 레포와 같은 스킬을 쓴다.

| 스킬 | 하는 일 | 관련 문서 |
|---|---|---|
| `3dollars:test-cases` | 테크스펙 TC → 계층 배정 → 유닛 테스트 코드 → 커버리지 표 | `docs/process/testing.md` |
| `3dollars:simulator-test` | 에뮬레이터를 조작해 자동화 TC 재현, 스크린샷·영상 증거 | `docs/process/e2e-and-manual-tests.md` |
| `3dollars:drift` | 테크스펙 요구사항 ↔ diff 대조, 스펙 밖 변경 목록 | `docs/process/pr-process.md` |
| `3dollars:ask-author` | diff에서 설명이 필요한 결정 3개를 작성자에게 질문 | `docs/process/pr-process.md` |
| `3dollars:pr-body` | 위 결과를 템플릿에 채워 PR 생성/갱신 | `.github/PULL_REQUEST_TEMPLATE.md` |
| `3dollars:pr-code-review` | PR 코드 리뷰(테크스펙·피그마 참고, 유저 플로우 중심) | — |
| `3dollars:review-digest` | 반복 리뷰 지적을 스크립트·문서 규칙으로 승격 제안 | `docs/process/pr-process.md` |
| `3dollars:code-cleanup` | 변경된 파일의 정리·코드 스멜·성능·메모리 점검 | — |
| `3dollars:bug-fix` / `3dollars:feature-implementer` | 지라 티켓 → 구현 → 검증 → PR 파이프라인 | `docs/process/pr-process.md` |

- `3dollars:deploy-dev-build`는 이 레포에서 쓸 수 없다. 사장님앱 AOS에는 개발 빌드 배포 워크플로가 아직 없다.
- 플랫폼별 명령·경로 차이는 플러그인의 `docs/platform-profiles.md` 에 정의되어 있다. 사장님앱 AOS는 유저앱 AOS 프로필을 따르되, 패키지(`app.threedollars.manager.debug`)·딥링크 스킴(`dollars-manager-dev://`)·모듈 구성(`:feature:*`)이 다르다.

## 레포 내 에이전트

- `.claude/agents/android-developer.md`: Kotlin/Compose/Clean Architecture 구현용 서브에이전트 정의.
