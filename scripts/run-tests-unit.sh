#!/bin/bash

# avoid virtual desktop jumps at mac terminal on test run
export JAVA_TOOL_OPTIONS='-Djava.awt.headless=true'

./gradlew :app:assembleProductionDebug
./gradlew :app:testProductionDebugUnitTest

echo "test reports: $(pwd)/app/build/reports/tests/productionDebug/index.html"
