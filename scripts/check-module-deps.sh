#!/usr/bin/env bash
# 모듈 의존 방향 검사.
# 규칙 1 (랭크): 모듈은 자기보다 낮은 계층만 의존한다 (app → navigation → feature:* → data → domain → common).
#               같은 랭크끼리도 금지하므로 feature 간 직접 의존도 여기서 잡힌다.
# 규칙 2 (금지쌍): 랭크로는 표현할 수 없는 간선을 명시적으로 막는다. is_forbidden 참고.
# 기존 위반은 scripts/module-deps-baseline.txt 에 동결하고, 새 위반만 실패로 처리한다.
# 문서: docs/process/pr-process.md, docs/context/module-dependencies-current.md
set -uo pipefail

cd "$(dirname "$0")/.."

BASELINE="scripts/module-deps-baseline.txt"

# 계층 랭크. 숫자가 클수록 상위. 의존은 "더 낮은 랭크"로만 허용한다.
rank_of() {
  case "$1" in
    :app) echo 50 ;;
    :navigation) echo 45 ;;
    :feature:*) echo 40 ;;
    :data) echo 30 ;;
    :domain) echo 20 ;;
    :common) echo 10 ;;
    *) echo -1 ;;
  esac
}

# 랭크상 하위 계층이어도 막는 간선. 사유를 출력한다.
# 선형 랭크로는 ":app 은 :data 를 써도 되지만 :feature:* 는 안 된다" 같은 규칙을 표현할 수 없다.
is_forbidden() {
  case "$1 -> $2" in
    ":feature:"*" -> :data")
      echo "feature 는 :domain 의 Repository 인터페이스·UseCase 만 쓴다. 구현체 바인딩은 :app 이 :data 를 의존해 Hilt 로 모은다" ;;
    *) return 1 ;;
  esac
}

module_of_file() {
  # app/build.gradle.kts -> :app , feature/home/build.gradle.kts -> :feature:home
  local dir
  dir="$(dirname "$1")"
  [ "$dir" = "." ] && { echo ""; return; }
  echo ":${dir//\//:}"
}

# project(":feature:home") 와 type-safe accessor projects.feature.home 둘 다 읽는다.
deps_of_file() {
  {
    grep -oE 'project\("[^"]+"\)' "$1" | sed -E 's/project\("([^"]+)"\)/\1/'
    grep -oE '\bprojects(\.[A-Za-z0-9_]+)+' "$1" | sed -E 's/^projects//; s/\./:/g'
  } | sort -u
}

is_baselined() {
  [ -f "$BASELINE" ] || return 1
  grep -qxF "$1 -> $2" "$BASELINE"
}

violations=0
baselined=0
checked=0

while IFS= read -r gradle_file; do
  module="$(module_of_file "$gradle_file")"
  [ -z "$module" ] && continue
  from_rank="$(rank_of "$module")"
  [ "$from_rank" -lt 0 ] && continue

  while IFS= read -r dep; do
    [ -z "$dep" ] && continue
    checked=$((checked + 1))
    to_rank="$(rank_of "$dep")"
    if [ "$to_rank" -lt 0 ]; then
      echo "::warning::$gradle_file — 알 수 없는 모듈 '$dep' (scripts/check-module-deps.sh 의 rank_of 에 추가하세요)"
      continue
    fi
    if reason="$(is_forbidden "$module" "$dep")"; then
      if is_baselined "$module" "$dep"; then
        baselined=$((baselined + 1))
        continue
      fi
      echo "::error file=$gradle_file::금지된 의존: $module -> $dep — $reason"
      violations=$((violations + 1))
      continue
    fi
    if [ "$to_rank" -lt "$from_rank" ]; then
      continue
    fi
    if is_baselined "$module" "$dep"; then
      baselined=$((baselined + 1))
      continue
    fi
    echo "::error file=$gradle_file::의존 방향 위반: $module -> $dep (같거나 상위 계층). 방향을 뒤집거나, 정당한 예외면 사유와 함께 $BASELINE 에 '$module -> $dep' 를 추가하세요."
    violations=$((violations + 1))
  done < <(deps_of_file "$gradle_file")
done < <(find . -name 'build.gradle.kts' -not -path './build/*' -not -path '*/build/*' -not -path './build-logic/*' | sed 's|^\./||' | sort)

echo "모듈 의존성 검사: 의존 간선 ${checked}개 확인, 동결 ${baselined}건, 신규 위반 ${violations}건"

if [ "$violations" -gt 0 ]; then
  exit 1
fi
