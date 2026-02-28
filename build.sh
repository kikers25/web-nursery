#!/bin/bash
# build.sh — Compile Java source and prepare the WAR for deployment
#
# Usage: ./build.sh
#
# Prerequisites:
#   - Java JDK 8+ (java, javac)
#
# Output:
#   - Compiled classes in WEB-INF/classes/
#   - war/ directory ready for deployment

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "=== web-nursery build ==="

# Check Java
if ! command -v javac &> /dev/null; then
    echo "ERROR: javac not found. Install Java JDK 8+."
    exit 1
fi

echo "Using: $(javac -version 2>&1)"

# Find all Java source files
JAVA_FILES=$(find src -name "*.java")
FILE_COUNT=$(echo "$JAVA_FILES" | wc -l)
echo "Found $FILE_COUNT Java source files"

# Compile
echo "Compiling..."
mkdir -p WEB-INF/classes
javac -source 8 -target 8 \
    -cp "WEB-INF/lib/*" \
    -d WEB-INF/classes \
    -encoding UTF-8 \
    -Xlint:-options \
    -proc:none \
    $JAVA_FILES

CLASS_COUNT=$(find WEB-INF/classes -name "*.class" | wc -l)
echo "Compiled $CLASS_COUNT class files"

# Copy to war/ directory
echo "Preparing war/ directory..."
mkdir -p war/WEB-INF/classes
cp -r WEB-INF/classes/com war/WEB-INF/classes/

# Ensure runtime JARs are in war/WEB-INF/lib/
for jar in log4j-1.2.17.jar commons-dbutils-1.6.jar ojdbc8-19.3.0.0.jar; do
    if [ -f "WEB-INF/lib/$jar" ] && [ ! -f "war/WEB-INF/lib/$jar" ]; then
        cp "WEB-INF/lib/$jar" war/WEB-INF/lib/
        echo "  Copied $jar to war/WEB-INF/lib/"
    fi
done

echo ""
echo "=== Build complete ==="
echo "war/ directory is ready for deployment."
echo "Run: gcloud app deploy war/"
