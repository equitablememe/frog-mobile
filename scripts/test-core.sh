#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
BUILD="$ROOT/build/core"
rm -rf "$BUILD"
mkdir -p "$BUILD"
mapfile -t SOURCES < <(find "$ROOT/core/src/main/kotlin" "$ROOT/core/src/test/kotlin" -name '*.kt' | sort)
kotlinc "${SOURCES[@]}" -include-runtime -d "$BUILD/frog-core-demo.jar"
java -jar "$BUILD/frog-core-demo.jar"
