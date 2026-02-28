#!/bin/bash
# setup-secrets.sh — Create and configure Google Cloud Secret Manager secrets
#                    for the guarderiaweb project.
#
# Run this script ONCE before the first deployment, or whenever you need to
# rotate credentials.
#
# Prerequisites:
#   - Google Cloud SDK installed and authenticated:
#       gcloud auth login
#       gcloud config set project guarderiaweb
#   - The Secret Manager API enabled on the project:
#       gcloud services enable secretmanager.googleapis.com
#   - The App Engine default service account must have the
#     "Secret Manager Secret Accessor" (roles/secretmanager.secretAccessor) role.
#
# Usage:
#   ./setup-secrets.sh                     # Interactive — prompts for each value
#   ./setup-secrets.sh --rotate            # Re-add a new version to existing secrets
#
# Secrets created:
#   db-url          Oracle JDBC URL  (e.g. jdbc:oracle:thin:@host:1521/XE)
#   db-usuario      Oracle DB username
#   db-contrasena   Oracle DB password
#   mail-usuario    Outbound mail account (e.g. GuarderiaWeb@ono.com)
#   mail-contrasena Outbound mail password

set -e

PROJECT_ID="guarderiaweb"
ROTATE=false

# ---------------------------------------------------------------------------
# Parse arguments
# ---------------------------------------------------------------------------
while [[ $# -gt 0 ]]; do
    case $1 in
        --rotate)
            ROTATE=true
            shift
            ;;
        --project)
            PROJECT_ID="$2"
            shift 2
            ;;
        *)
            echo "Unknown option: $1"
            echo "Usage: ./setup-secrets.sh [--project PROJECT_ID] [--rotate]"
            exit 1
            ;;
    esac
done

echo "=== guarderiaweb — Google Cloud Secret Manager setup ==="
echo "Project: $PROJECT_ID"
echo ""

# ---------------------------------------------------------------------------
# Check prerequisites
# ---------------------------------------------------------------------------
if ! command -v gcloud &>/dev/null; then
    echo "ERROR: gcloud not found. Install the Google Cloud SDK:"
    echo "  https://cloud.google.com/sdk/docs/install"
    exit 1
fi

# ---------------------------------------------------------------------------
# Enable the Secret Manager API (idempotent)
# ---------------------------------------------------------------------------
echo "Enabling Secret Manager API..."
gcloud services enable secretmanager.googleapis.com --project="$PROJECT_ID"

# ---------------------------------------------------------------------------
# Grant the App Engine service account access to Secret Manager
# ---------------------------------------------------------------------------
PROJECT_NUMBER=$(gcloud projects describe "$PROJECT_ID" --format="value(projectNumber)")
AE_SERVICE_ACCOUNT="${PROJECT_NUMBER}@appspot.gserviceaccount.com"

echo ""
echo "Granting Secret Manager Secret Accessor role to: $AE_SERVICE_ACCOUNT"
gcloud projects add-iam-policy-binding "$PROJECT_ID" \
    --member="serviceAccount:${AE_SERVICE_ACCOUNT}" \
    --role="roles/secretmanager.secretAccessor" \
    --quiet

# ---------------------------------------------------------------------------
# Helper: create or rotate a secret
# ---------------------------------------------------------------------------
create_or_rotate_secret() {
    local SECRET_ID="$1"
    local PROMPT="$2"

    echo ""
    read -rsp "  Enter value for '${SECRET_ID}' (${PROMPT}): " SECRET_VALUE
    echo ""

    if $ROTATE; then
        # Add a new version to an existing secret
        echo "  Adding new version to secret '${SECRET_ID}'..."
        printf '%s' "$SECRET_VALUE" | \
            gcloud secrets versions add "$SECRET_ID" \
                --project="$PROJECT_ID" \
                --data-file=-
    else
        # Create the secret if it does not exist, then add the first version
        if gcloud secrets describe "$SECRET_ID" --project="$PROJECT_ID" &>/dev/null; then
            echo "  Secret '${SECRET_ID}' already exists — adding a new version."
            printf '%s' "$SECRET_VALUE" | \
                gcloud secrets versions add "$SECRET_ID" \
                    --project="$PROJECT_ID" \
                    --data-file=-
        else
            echo "  Creating secret '${SECRET_ID}'..."
            printf '%s' "$SECRET_VALUE" | \
                gcloud secrets create "$SECRET_ID" \
                    --project="$PROJECT_ID" \
                    --replication-policy="automatic" \
                    --data-file=-
        fi
    fi

    echo "  Done: ${SECRET_ID}"
}

# ---------------------------------------------------------------------------
# Create / rotate each secret
# ---------------------------------------------------------------------------
echo ""
echo "You will now be prompted for each secret value."
echo "Values are sent directly to Secret Manager — they are never stored locally."
echo ""

create_or_rotate_secret "db-url"          "Oracle JDBC URL, e.g. jdbc:oracle:thin:@localhost:1521/XE"
create_or_rotate_secret "db-usuario"      "Oracle DB username"
create_or_rotate_secret "db-contrasena"   "Oracle DB password"
create_or_rotate_secret "mail-usuario"    "Outbound mail username, e.g. GuarderiaWeb@ono.com"
create_or_rotate_secret "mail-contrasena" "Outbound mail password"

# ---------------------------------------------------------------------------
# Summary
# ---------------------------------------------------------------------------
echo ""
echo "=== Secret Manager setup complete ==="
echo ""
echo "Secrets configured in project '${PROJECT_ID}':"
gcloud secrets list --project="$PROJECT_ID" \
    --filter="name:(db-url OR db-usuario OR db-contrasena OR mail-usuario OR mail-contrasena)" \
    --format="table(name, createTime)"
echo ""
echo "Next steps:"
echo "  1. Add the Secret Manager client library JAR to WEB-INF/lib/:"
echo "     See docs/setup-google-cloud-secrets.md for download instructions."
echo "  2. Build and deploy:  ./deploy.sh --project $PROJECT_ID"
