#!/usr/bin/env bash
# 신규 UI는 Jetpack Compose로만 작성한다 — XML 레이아웃·View 기반 화면이 새로 들어오는 것을 막는다.
# 잡는 것:
#   1. src/main/res/layout*/ 아래 XML 레이아웃 파일
#   2. build.gradle.kts / build-logic 의 viewBinding·dataBinding 활성화
#   3. Kotlin 코드의 setContentView(R.layout.…), LayoutInflater.inflate(R.layout.…), *Binding.inflate(…)
# drawable·values·mipmap 등 레이아웃이 아닌 리소스 XML은 대상이 아니다.
# Compose API가 없는 SDK 뷰를 감싸는 AndroidView { } 는 허용한다 (사유를 PR 본문에 남긴다).
# 문서: AGENTS.md "UI", docs/process/pr-process.md
set -uo pipefail

cd "$(dirname "$0")/.."

violations=0

report() {
  echo "::error file=$1::$2"
  violations=$((violations + 1))
}

while IFS= read -r f; do
  report "$f" "XML 레이아웃 금지 — 신규 UI는 Compose(@Composable)로 작성하세요"
done < <(find . -path '*/src/*/res/layout*' -name '*.xml' -not -path '*/build/*' | sed 's|^\./||' | sort)

while IFS= read -r hit; do
  report "${hit%%:*}" "viewBinding/dataBinding 활성화 금지 — Compose를 사용하세요 (${hit#*:})"
done < <(grep -rnE '(viewBinding|dataBinding)[[:space:]]*(=[[:space:]]*true|\{)' --include='*.kts' --include='*.gradle' . 2>/dev/null | grep -v '/build/' | sed 's|^\./||')

while IFS= read -r hit; do
  report "${hit%%:*}" "View 기반 레이아웃 inflate 금지 — Compose를 사용하세요 (${hit#*:})"
done < <(grep -rnE 'setContentView\(R\.layout\.|\.inflate\(R\.layout\.|Binding\.inflate\(' --include='*.kt' --include='*.java' . 2>/dev/null | grep -v '/build/' | sed 's|^\./||')

echo "Compose 전용 검사: 위반 ${violations}건"

if [ "$violations" -gt 0 ]; then
  exit 1
fi
