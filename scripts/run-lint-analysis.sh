#!/bin/bash

# avoid virtual desktop jumps at mac terminal on test run
export JAVA_TOOL_OPTIONS='-Djava.awt.headless=true'

./gradlew :app:lintProductionRelease

echo "lint reports: $(pwd)/app/build/outputs/lint-results-productionRelease.html"
