#!/bin/bash
# deploy.sh — Build and deploy web-nursery to Google App Engine
#
# Usage:
#   ./deploy.sh                    # Build and deploy
#   ./deploy.sh --project ID       # Deploy to a specific GCP project
#   ./deploy.sh --skip-build       # Deploy without rebuilding
#
# Prerequisites:
#   - Java JDK 8+ (javac)
#   - Google Cloud SDK (gcloud)
#   - Authenticated: gcloud auth login
#   - Project configured: gcloud config set project YOUR_PROJECT_ID

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

SKIP_BUILD=false
PROJECT_ID=""

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --skip-build)
            SKIP_BUILD=true
            shift
            ;;
        --project)
            PROJECT_ID="$2"
            shift 2
            ;;
        *)
            echo "Unknown option: $1"
            echo "Usage: ./deploy.sh [--project PROJECT_ID] [--skip-build]"
            exit 1
            ;;
    esac
done

echo "=== web-nursery deploy ==="

# Check gcloud
if ! command -v gcloud &> /dev/null; then
    echo "ERROR: gcloud not found. Install Google Cloud SDK:"
    echo "  https://cloud.google.com/sdk/docs/install"
    exit 1
fi

# Set project if specified
if [ -n "$PROJECT_ID" ]; then
    echo "Setting project to: $PROJECT_ID"
    gcloud config set project "$PROJECT_ID"

    # Update appengine-web.xml
    if grep -q "<application>guarderiaweb</application>" WEB-INF/appengine-web.xml; then
        echo "Updating appengine-web.xml with project ID: $PROJECT_ID"
        sed -i "s|<application>guarderiaweb</application>|<application>$PROJECT_ID</application>|" WEB-INF/appengine-web.xml
        sed -i "s|<application>guarderiaweb</application>|<application>$PROJECT_ID</application>|" war/WEB-INF/appengine-web.xml
    fi
fi

# Build
if [ "$SKIP_BUILD" = false ]; then
    echo ""
    bash "$SCRIPT_DIR/build.sh"
    echo ""
fi

# Verify war/ directory
if [ ! -f "war/WEB-INF/appengine-web.xml" ]; then
    echo "ERROR: war/WEB-INF/appengine-web.xml not found. Run build.sh first."
    exit 1
fi

if [ ! -d "war/WEB-INF/classes/com" ]; then
    echo "ERROR: No compiled classes in war/. Run build.sh first."
    exit 1
fi

# Deploy
echo "Deploying to Google App Engine..."
gcloud app deploy war/ --quiet

echo ""
echo "=== Deployment complete ==="
echo "Opening application..."
gcloud app browse
