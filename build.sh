#!/usr/bin/env bash
set -e

echo "=== Building MyLog Plugin ==="
./gradlew clean buildPlugin

ZIP_FILE=$(find build/distributions -name "MyLog-*.zip" | head -1)
echo ""
echo "=== Build Complete ==="
echo "Output: $ZIP_FILE"
echo ""
echo "To install: IDE -> Settings -> Plugins -> ⚙️ -> Install from Disk -> select $ZIP_FILE"