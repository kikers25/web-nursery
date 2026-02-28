# Deploying to Google App Engine (Standard Java 8)

## Prerequisites

### Required software

| Tool | Minimum version | Check with |
|---|---|---|
| Java JDK | 8 | `java -version` |
| Maven | 3.x | `mvn -version` |
| Google Cloud SDK | Latest | `gcloud version` |

### Install Google Cloud SDK

Download and install from: https://cloud.google.com/sdk/docs/install

Then log in:

```bash
gcloud auth login
```

---

## 1. Create a Google Cloud project

1. Go to https://console.cloud.google.com
2. Create a new project
3. Note down the **Project ID** (e.g. `guarderiaweb-2025`)

> **Important:** The Project ID must match the value of `<application>` in `WEB-INF/appengine-web.xml`.
> It is currently set to `guarderiaweb`. If your Project ID is different, update that file.

---

## 2. Update the application ID (if needed)

Edit `WEB-INF/appengine-web.xml` and change:

```xml
<application>guarderiaweb</application>
```

to your actual GCP Project ID.

---

## 3. Initialize App Engine in the project

```bash
gcloud config set project YOUR_PROJECT_ID
gcloud app create --region=europe-west
```

Available European regions:
- `europe-west` (Belgium) — recommended for Spain

---

## 4. Build the application

From the project root:

```bash
mvn clean package
```

This generates the expanded WAR in the `war/` folder.

> The `war/` folder is listed in `.gitignore` — it is a build artifact and should not be committed.

---

## 5. Deploy to GAE

```bash
gcloud app deploy war/
```

The command automatically detects `WEB-INF/appengine-web.xml` inside `war/`.

When prompted `Do you want to continue?`, confirm with **Y**.

---

## 6. Open the application

```bash
gcloud app browse
```

Or go directly to: `https://YOUR_PROJECT_ID.appspot.com`

---

## Useful commands

```bash
# Stream logs in real time
gcloud app logs tail -s default

# List deployed versions
gcloud app versions list

# Stop a version (to avoid charges)
gcloud app versions stop live
```

---

## First startup

On first access, Vosao CMS will run its automatic setup process. You will see a setup screen where you can:

1. Create the administrator user
2. Configure the site

---

## Cost (free tier)

For normal use by a small childcare facility, the app should stay within GAE's free tier:

| Resource | Free daily limit |
|---|---|
| F1 instances | 28 instance hours |
| Datastore reads | 50,000 operations |
| Datastore writes | 20,000 operations |
| Outbound traffic | 1 GB |
| Datastore storage | 1 GB total |

If the app exceeds these limits, Google charges for the overage. For small office use this is unlikely.

---

## Known limitations after migration

| Feature | Status | Reason |
|---|---|---|
| Page search (CMS admin) | Not available | Required Channel API, removed by Google in 2021 |
| Rest of the application | Operational | — |

---

## Troubleshooting

### Error: `The requested URL was not found`
- Verify the deployment completed successfully with `gcloud app versions list`
- Check the logs: `gcloud app logs tail -s default`

### Error: `java.lang.ClassNotFoundException`
- Make sure you ran `mvn clean package` before deploying
- Verify that `war/WEB-INF/lib/` contains all JAR files

### Error: `Project not found` when deploying
- Run `gcloud config set project YOUR_PROJECT_ID`
- Verify that the Project ID in `appengine-web.xml` matches exactly

### The app is slow to respond on the first request
- This is normal. GAE Standard cold-starts the instance when there is no traffic. Warmup can take 10–20 seconds.
