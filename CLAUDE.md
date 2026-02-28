# CLAUDE.md — AI Assistant Guide for web-nursery

## Project Overview

**web-nursery** (internally: *guarderiaweb*) is a Java-based web application for managing a childcare/kindergarten facility. It combines a custom business logic layer with the **Vosao CMS** (v0.9) and is deployed on **Google App Engine (GAE)**.

The application is written primarily in Spanish (variable names, comments, class names, documentation) — this is intentional and consistent with the project's origin.

---

## Architecture

### Layered Architecture (MVC + DAO)

```
Browser / CMS UI (Backbone.js)
        ↓
Servlet Layer (com.guarderia.servlet)
        ↓
Business Logic (com.guarderia.negocio)
        ↓
DAO Layer (com.guarderia.DAO)
        ↓
Database (Oracle via com.guarderia.bbdd)
```

### Key Design Patterns

- **Command Pattern** — Servlet actions are dispatched through command classes in `servlet/comando/`
- **DAO Pattern** — All data access is abstracted behind interfaces; Oracle implementations live in `DAO/`
- **Factory Pattern** — `DAOFactoria` and `OracleFactoria` manage DAO instantiation
- **MVC** — Backbone.js on the frontend with Underscore.js templates; Java Servlets as controllers on the backend

---

## Directory Structure

```
web-nursery/
├── src/com/guarderia/        # Java source code
│   ├── bbdd/                 # DB connectivity (SclBaseDeDatos, AccesoDatos)
│   ├── bean/                 # Entity/data model POJOs (14 files)
│   ├── context/              # Application context management
│   ├── DAO/                  # Data access — interfaces + Oracle implementations
│   ├── error/                # Custom exceptions (DataException, XMLException)
│   ├── mail/                 # Email sending utilities
│   ├── negocio/              # Business logic layer (GestionAlumno, GestionClase, etc.)
│   ├── servlet/              # Main servlet controller + Navegacion
│   │   ├── comando/          # Command pattern servlet actions
│   │   ├── filtro/           # Servlet filters
│   │   └── menuConfiguracion/# Configuration menu handlers
│   └── utils/                # General/web utilities
├── cms/                      # Vosao CMS frontend (Backbone.js SPA)
│   ├── app.js                # Backbone Router — all routes and views
│   ├── main.js               # App initialization (IE guard, RequireJS bootstrap)
│   ├── main.min.js           # Production minified bundle
│   ├── libs/                 # jQuery, Backbone, Underscore, RequireJS
│   ├── template/             # Underscore.js HTML templates
│   └── view/                 # Backbone Views
├── static/                   # Static assets (images, CKEditor)
├── WEB-INF/                  # Java EE & GAE configuration
│   ├── web.xml               # Servlet/filter mappings
│   ├── appengine-web.xml     # Google App Engine settings
│   ├── cron.xml              # Scheduled jobs
│   ├── queue.xml             # Task queues
│   ├── datastore-indexes.xml # Manual datastore indexes
│   ├── velocity.properties   # Velocity template engine config
│   └── logging.properties    # Java logging
├── META-INF/maven/           # Maven POM (WAR packaging)
├── favicon.ico
├── README.md
└── CLAUDE.md                 # This file
```

---

## Java Source Details

### Bean Layer (`com.guarderia.bean`)

Data models / POJOs representing domain entities:

| Class | Description |
|---|---|
| `UsuarioBean` | User accounts |
| `AlumnoBean` | Student (child) entity |
| `AdultoBean` | Adult (parent/guardian) entity |
| `ClaseBean` | Classroom / group |
| `PerfilBean` | User role/profile |
| `NoticiaBean` | News and announcements |
| `Evaluacion` | Student assessment/evaluation |
| `GrupoCMS`, `ElementoCMS` | CMS grouping entities |
| `MenuBean` | Navigation/menu configuration |
| `Area_Conocimiento` | Knowledge area (curriculum) |
| `Criterio`, `Bloque`, `Intervalo_Edad` | Assessment criteria structures |

### DAO Layer (`com.guarderia.DAO`)

- Each entity has an interface (e.g., `UsuarioDAO`) and an Oracle implementation (e.g., `OracleUsuarioDAO`)
- `DAOFactoria` is the abstract factory; `OracleFactoria` provides Oracle-specific implementations
- `DAO.java` is the base interface

### Business Logic (`com.guarderia.negocio`)

| Class | Responsibility |
|---|---|
| `GestionAlumno` | Student management |
| `GestionClase` | Class/group management |
| `GestionComedor` | Dining hall management |
| `GestionNoticia` | News/announcements |
| `GestionPadre` | Parent management |
| `GestionProfesor` | Teacher management |
| `GestionUsuario` | User account management |

### Servlet Layer (`com.guarderia.servlet`)

- `ServletControlador` — main dispatcher servlet
- `Navegacion` — handles navigation/routing logic
- `servlet/comando/` — one class per command/action (Command pattern)
- `servlet/filtro/` — authentication and utility filters
- `servlet/menuConfiguracion/` — configuration menu request handlers

### Configuration (`menuConfiguracion`)

Configuration is loaded via the init parameter `fichero_propiedades`. It supports:
- XML format (`PropiedadesXML`)
- `.properties` file format (`Propiedadesprops`)

The `Propiedades` interface abstracts both formats.

---

## Frontend (CMS)

The CMS is a **Backbone.js Single-Page Application** using:

- **RequireJS** (AMD module loading)
- **Underscore.js** (templating and utilities)
- **jQuery** (DOM manipulation)
- **CKEditor** (rich text editing)

### Key Frontend Files

| File | Purpose |
|---|---|
| `cms/main.js` | Entry point; RequireJS config, IE check |
| `cms/app.js` | Backbone Router with all route definitions |
| `cms/template/*.html` | Underscore.js HTML templates |
| `cms/view/` | Backbone Views |
| `cms/libs/` | Third-party libraries (do not modify) |

**Note:** `main.min.js` is the minified production bundle. Edit `main.js` (source) and re-minify for production.

---

## Web Configuration (`WEB-INF/web.xml`)

### Filter Chain (applied in order to all requests)

1. `ContextFilter` — Sets up application context
2. `InitFilter` — Application initialization
3. `UpdateFilter` — Handles updates
4. `LanguageFilter` — i18n/locale detection
5. `PluginCronFilter` — Plugin cron support
6. `AuthenticationFilter` — Enforces authentication
7. `RestFilter` (`/rest/*`) — REST API handling
8. `RewriteFilter` — URL rewriting
9. `SiteFilter` — Site-level processing

### Servlet Mappings

| Servlet | URL Pattern |
|---|---|
| `ServiceServlet` | `/json-rpc/*` |
| `FileUploadServlet` | `/cms/upload` |
| `FileDownloadServlet` | `/file/*` |
| `FormSendServlet` | `/_ah/plugin/form/send` |
| `ForgotPasswordServlet` | `/_ah/changePassword` |
| `MessageQueueServlet` | `/_ah/queue/mq` |
| `CacheResetServlet` | `/_ah/reset_cache` |
| `LogoutServlet` | `/_ah/logout` |
| `ChannelCommandServlet` | `/_ah/channelCommand` |
| `JSBundleServlet` | `/i18n.js` |

---

## Google App Engine Configuration

**App ID:** `guarderiaweb`
**Version:** `live`
**Threading:** Threadsafe enabled
**Warmup:** Enabled (`/_ah/warmup`)

### Static File Expiration

- `/cms/**` — 1 day cache
- `/static/**` — 1 day cache
- `/favicon.ico` — 1 day cache

### Scheduled Jobs (`cron.xml`)

| Job | Schedule | URL |
|---|---|---|
| Plugin cron scheduler | Every 1 minute | `/_ah/cron/plugin` |
| Page cache reset (published pages) | Every 1 hour | `/_ah/cron/page_publish` |

### Task Queues (`queue.xml`)

| Queue | Rate |
|---|---|
| `mq-high` | 10 requests/second (burst: 3) |
| `mq-medium` | 1 request/second |
| `mq-low` | 6 requests/minute |

---

## Build System

**Build Tool:** Maven
**Packaging:** WAR (`vosaocms.war`)
**Parent POM:** `org.vosao:vosaocms:0.9`
**Key dependency:** `vosao-kernel:0.9`
**Character encoding:** UTF-8

Build artifact is output to `war/` which is **gitignored**.

To build:
```bash
mvn clean package
```

To deploy to Google App Engine (requires GAE SDK):
```bash
appcfg.sh update war/
```

---

## Dependencies (`WEB-INF/lib`)

| Library | Version | Purpose |
|---|---|---|
| `appengine-api-1.0-sdk` | 1.6.2.1 | GAE APIs |
| `velocity` | 1.6.3 | Server-side templating |
| `velocity-tools` | 2.0 | Velocity helper tools |
| `vosao-kernel` | 0.9 | CMS core |
| `vosao-api` | 0.9 | CMS API |
| `jabsorb` | 1.3.1 | JSON-RPC |
| `commons-lang` | 2.4 | String/object utilities |
| `commons-fileupload` | 1.2.1 | File upload handling |
| `commons-io` | 1.4 | I/O utilities |
| `dom4j` | 1.6.1 | XML processing |
| `mail` | 1.4.1 | JavaMail API |
| `recaptcha4j` | 0.0.7 | CAPTCHA support |
| `slf4j` | 1.5.6 | Logging facade |
| `bliki-core` | 3.0.15 | Wiki markup parsing |

---

## Code Conventions

### Java

- **Language:** Spanish naming for all domain concepts (variables, methods, classes, comments)
- **Class naming:** PascalCase (`AlumnoBean`, `GestionClase`)
- **Method/variable naming:** camelCase (`getNombre()`, `listaAlumnos`)
- **Comments:** Javadoc in Spanish with `@uml.property` UML annotations
- **Serialization:** All serializable beans include `serialVersionUID`
- **Package root:** `com.guarderia`

### Do Not

- Do not rename Spanish identifiers to English — the Spanish naming is intentional and consistent
- Do not modify minified files directly (`main.min.js`, `backbone-min.js`) — edit source files
- Do not commit files in `war/` — it is build output and gitignored
- Do not add `target/` or Maven build artifacts — gitignored

### Language

- All documentation files (`.md`, guides, comments in new files) must be written in **English**
- This applies even when the user communicates in Spanish
- The Spanish naming of Java identifiers, variables, and existing comments is unaffected by this rule

---

## Testing

There is currently **no automated test suite** in this project. No `src/test/` directory exists and no testing framework (JUnit, TestNG) is configured. If adding tests, create a `src/test/java/com/guarderia/` directory and configure Maven Surefire plugin.

---

## CI/CD

There is currently **no CI/CD pipeline** configured. Deployment is manual via the Google App Engine SDK (`appcfg.sh`).

---

## Domain Model Summary

The application manages a childcare facility with these core entities:

- **Alumno** (Student/Child) — the primary subject being cared for
- **Adulto** (Adult) — parent, guardian, or staff member
- **Usuario** (User) — system accounts with roles (Perfil)
- **Clase** (Classroom) — groups of students
- **Comedor** (Dining Hall) — meal/lunch management
- **Evaluacion** (Evaluation) — student assessments with Criterio/Bloque/Area_Conocimiento
- **Noticia** (News) — announcements to parents/staff
- **MenuBean** — navigation menu configuration
