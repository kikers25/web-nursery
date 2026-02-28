# Guía de despliegue en Google App Engine (Standard Java 8)

## Prerrequisitos

### Software necesario

| Herramienta | Versión mínima | Verificar con |
|---|---|---|
| Java JDK | 8 | `java -version` |
| Maven | 3.x | `mvn -version` |
| Google Cloud SDK | Última | `gcloud version` |

### Instalar Google Cloud SDK

Descarga e instala desde: https://cloud.google.com/sdk/docs/install

Tras instalar, inicia sesión:

```bash
gcloud auth login
```

---

## 1. Crear el proyecto en Google Cloud

1. Ve a https://console.cloud.google.com
2. Crea un nuevo proyecto
3. Anota el **Project ID** (ej. `guarderiaweb-2025`)

> **Importante:** El Project ID debe coincidir con el valor de `<application>` en `WEB-INF/appengine-web.xml`.
> Actualmente está configurado como `guarderiaweb`. Si tu Project ID es diferente, edita ese fichero.

---

## 2. Ajustar el ID de aplicación (si es necesario)

Edita `WEB-INF/appengine-web.xml` y cambia:

```xml
<application>guarderiaweb</application>
```

por el Project ID real que hayas creado en GCP.

---

## 3. Inicializar App Engine en el proyecto

```bash
gcloud config set project TU_PROJECT_ID
gcloud app create --region=europe-west
```

Para la región, las opciones europeas disponibles son:
- `europe-west` (Bélgica) — recomendada para España

---

## 4. Compilar la aplicación

Desde la raíz del proyecto:

```bash
mvn clean package
```

Esto genera el WAR expandido en la carpeta `war/`.

> La carpeta `war/` está en `.gitignore` — es el artefacto de build, no se sube al repositorio.

---

## 5. Desplegar en GAE

```bash
gcloud app deploy war/
```

El comando detecta automáticamente el `WEB-INF/appengine-web.xml` dentro de `war/`.

Cuando pregunte `Do you want to continue?`, confirma con **Y**.

---

## 6. Abrir la aplicación

```bash
gcloud app browse
```

O accede directamente a: `https://TU_PROJECT_ID.appspot.com`

---

## Comandos útiles

```bash
# Ver logs en tiempo real
gcloud app logs tail -s default

# Ver versiones desplegadas
gcloud app versions list

# Detener una versión (para no incurrir en costes)
gcloud app versions stop live
```

---

## Primer arranque

La primera vez que accedas, Vosao CMS iniciará su proceso de configuración automática. Verás la pantalla de setup donde podrás:

1. Crear el usuario administrador
2. Configurar el sitio

---

## Costes (tier gratuito)

Con uso normal de una guardería pequeña, la app debería mantenerse dentro del tier gratuito de GAE:

| Recurso | Límite gratuito diario |
|---|---|
| Instancias F1 | 28 horas de instancia |
| Datastore (lecturas) | 50.000 operaciones |
| Datastore (escrituras) | 20.000 operaciones |
| Tráfico de salida | 1 GB |
| Almacenamiento Datastore | 1 GB total |

Si la app supera estos límites, Google cobra por el exceso. Para un uso de oficina pequeña es improbable.

---

## Limitaciones conocidas tras la migración

| Funcionalidad | Estado | Motivo |
|---|---|---|
| Buscador de páginas (CMS admin) | No disponible | Requería Channel API, eliminada por Google en 2021 |
| Resto de la aplicación | Operativo | — |

---

## Solución de problemas frecuentes

### Error: `The requested URL was not found`
- Verifica que el despliegue terminó correctamente con `gcloud app versions list`
- Comprueba los logs: `gcloud app logs tail -s default`

### Error: `java.lang.ClassNotFoundException`
- Asegúrate de haber compilado con `mvn clean package` antes de desplegar
- Verifica que la carpeta `war/WEB-INF/lib/` contiene todos los JARs

### Error: `Project not found` al desplegar
- Ejecuta `gcloud config set project TU_PROJECT_ID`
- Verifica que el Project ID en `appengine-web.xml` coincide exactamente

### La app tarda mucho en responder la primera vez
- Normal. GAE Standard arranca la instancia en frío cuando no hay tráfico. El warmup puede tardar 10-20 segundos.
