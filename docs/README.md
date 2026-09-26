# Project Documentation

이 디렉터리는 세션이 끊겨도 프로젝트 상태와 작업 맥락을 복구할 수 있게 유지하는 canonical 문서 위치다.

## 새 세션 시작 순서

1. `AGENTS.md`(Claude는 `CLAUDE.md`)를 읽어 응답 언어, 안전 규칙, 검증 원칙을 확인한다.
2. 변경 범위에 따라 아래 문서를 읽는다.
   - 모듈/의존성: `docs/context/module-dependencies-current.md`
   - 검증: `docs/context/verification-matrix.md`
   - PR/테스트 프로세스: `docs/process/pr-process.md`, `docs/process/testing.md`
3. 사용할 도구(스킬)는 `docs/agents/skill-index.md`에서 찾는다.

## Directory Map

- `context/`: 현재 프로젝트 사실 — 모듈 의존성, 검증 매트릭스
- `process/`: AI 개발 프로세스 — 테크스펙, 테스트 세 계층, 자동화·수동 체크리스트, 검증 장치 변경, PR 프로세스, 개발 빌드 배포
- `agents/`: 에이전트용 skill 인덱스

## Documentation Rules

- 현재 사실과 목표 계획을 같은 문서에서 섞지 않는다.
- 코드베이스에서 확인한 내용만 현재 상태로 기록한다.
- 유저앱 레포(`3dollar-in-my-pocket-android`, `3dollars-in-my-pocket-ios`)의 `docs/process/`와 같은 형태·용어를 유지한다. 프로세스를 바꿀 때는 유저앱 쪽도 함께 맞출지 확인한다.
