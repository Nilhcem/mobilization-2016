#!/bin/bash

# avoid virtual desktop jumps at mac terminal on test run
export JAVA_TOOL_OPTIONS='-Djava.awt.headless=true'

./gradlew :app:clean :app:assembleProductionRelease

echo "Signed apk: $(pwd)/app/build/outputs/apk/app-production-release.apk"
