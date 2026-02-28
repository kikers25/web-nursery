# Casos de Uso — Guardería Web

Documento de casos de uso de la aplicación **guarderiaweb**, un sistema de gestión para una guardería/centro de educación infantil. Se detallan los actores del sistema y los casos de uso por módulo funcional.

---

## Actores

| Actor | Descripción |
|---|---|
| **Administrador** | Gestiona todos los datos del sistema: alumnos, adultos, clases, menús, usuarios. |
| **Padre/Tutor** | Padre o tutor legal de un alumno. Consulta información de sus hijos y se comunica con profesores. |
| **Profesor** | Docente asignado a una o varias clases. Gestiona evaluaciones, noticias y comunicación con familias. |
| **Sistema** | Procesos automáticos: caché de páginas, cron jobs, colas de tareas. |

---

## Módulo 1: Autenticación y Sesión

### UC-01 — Iniciar sesión

- **Actor:** Administrador, Padre, Profesor
- **Precondición:** El usuario tiene una cuenta activa en el sistema.
- **Flujo principal:**
  1. El usuario accede al formulario de login.
  2. Introduce usuario y contraseña.
  3. El sistema valida las credenciales (`GestionUsuario.validarUsuario`).
  4. El sistema recupera el perfil del usuario (administrador / padre / profesor).
  5. El sistema redirige al menú privado correspondiente según el rol.
- **Flujo alternativo:** Si las credenciales son incorrectas, se muestra mensaje de error.
- **Clases involucradas:** `ComandoUsuario` (opción 7), `GestionUsuario.validarUsuario`, `UsuarioBean`, `PerfilBean`

---

### UC-02 — Cerrar sesión

- **Actor:** Administrador, Padre, Profesor
- **Flujo principal:**
  1. El usuario pulsa "Cerrar sesión".
  2. El sistema invalida la sesión HTTP.
  3. El sistema redirige a la página pública.
- **Clases involucradas:** `ComandoUsuario` (opción 8), `LogoutServlet`

---

### UC-03 — Acceder al menú privado según rol

- **Actor:** Administrador, Padre, Profesor
- **Precondición:** Usuario autenticado.
- **Flujo principal:**
  1. El sistema detecta el perfil del usuario en sesión.
  2. Redirige al menú de administrador, padre o profesor según corresponda.
- **Clases involucradas:** `ComandoFactoria.irMenuPrincipal`, `ComandoUsuario` (opción 16)

---

## Módulo 2: Gestión de Alumnos

### UC-04 — Listar todos los alumnos

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador accede a la sección de alumnos.
  2. El sistema recupera todos los registros de alumnos.
  3. Se muestra el listado con nombre, apellidos e identificador.
- **Clases involucradas:** `ComandoAdministrador`, `GestionAlumno.buscarAlumnos`

---

### UC-05 — Añadir alumno

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador rellena el formulario con los datos del alumno (nombre, apellidos, DNI, fecha de nacimiento, alergias, observaciones, DNI tutores).
  2. El sistema valida los datos.
  3. El sistema crea el registro del alumno (`GestionAlumno.anadirAlumno`).
  4. El sistema asigna automáticamente al alumno a la clase correspondiente según su edad.
- **Clases involucradas:** `ComandoAdministrador.anadirAlumno`, `GestionAlumno.anadirAlumno`, `AlumnoBean`

---

### UC-06 — Modificar datos de un alumno

- **Actor:** Administrador, Padre (sus propios hijos)
- **Flujo principal:**
  1. El actor selecciona el alumno a editar.
  2. El sistema muestra el formulario con los datos actuales.
  3. El actor modifica los campos deseados y confirma.
  4. El sistema actualiza el registro.
- **Clases involucradas:** `ComandoAdministrador.ventanaModificarAlumno / modificarAlumno`, `GestionAlumno.actualizarDatosAlumno`, `ComandoPadre` (opción 15)

---

### UC-07 — Ver detalle de un alumno

- **Actor:** Administrador, Padre, Profesor
- **Flujo principal:**
  1. El actor selecciona un alumno del listado.
  2. El sistema muestra nombre, apellidos, DNI, fecha de nacimiento, alergias, observaciones y tutores.
- **Clases involucradas:** `ComandoPadre` (opción 13), `GestionAlumno`

---

## Módulo 3: Gestión de Clases

### UC-08 — Listar clases

- **Actor:** Administrador, Profesor
- **Flujo principal:**
  1. El actor accede a la sección de clases.
  2. El sistema recupera todas las clases con su nombre e intervalo de edad.
- **Clases involucradas:** `GestionClase.buscarClases`, `ClaseBean`

---

### UC-09 — Crear clase

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador rellena el nombre, descripción e intervalo de edad de la nueva clase.
  2. El sistema registra la clase en base de datos.
- **Clases involucradas:** `ComandoClase.anadirClase`, `GestionClase.anadirClase`, `ClaseBean`

---

### UC-10 — Vincular alumno a clase

- **Actor:** Administrador
- **Precondición:** El alumno y la clase existen en el sistema.
- **Flujo principal:**
  1. El administrador selecciona un alumno y una clase.
  2. El sistema crea la relación alumno–clase.
- **Clases involucradas:** `ComandoAdministrador.vincularAlumnoAClase`, `GestionClase.anadirAlumnoEnLaClase`

---

### UC-11 — Desvincular alumno de clase

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona el alumno y la clase de la que se quiere desvincular.
  2. El sistema elimina la relación.
- **Clases involucradas:** `ComandoAdministrador.desvincularAlumnoDeClase`, `GestionClase.eliminarAlumnoDeClase`

---

### UC-12 — Asignar profesor a clase

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona un profesor y una clase.
  2. El sistema registra la asignación.
- **Clases involucradas:** `ComandoAdministrador.vincularProfesorAClase`, `GestionClase.anadirProfesorAClase`

---

### UC-13 — Desasignar profesor de clase

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona el profesor y la clase.
  2. El sistema elimina la asignación.
- **Clases involucradas:** `ComandoAdministrador.desvincularProfesorDeClase`, `GestionClase.eliminarProfesorDeClase`

---

### UC-14 — Ver alumnos de una clase

- **Actor:** Administrador, Profesor
- **Flujo principal:**
  1. El actor selecciona una clase.
  2. El sistema muestra el listado de alumnos asignados a esa clase.
- **Clases involucradas:** `ComandoAdministrador.ventanaAlumnosClase`, `ComandoProfesor` (opción 20), `GestionClase.buscarAlumnosDeClase`

---

### UC-15 — Ver profesores de una clase

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona una clase.
  2. El sistema muestra los profesores asignados.
- **Clases involucradas:** `ComandoAdministrador.ventanaProfesoresClase`, `GestionClase.buscarProfesoresDeLaClase`

---

## Módulo 4: Gestión de Padres y Tutores

### UC-16 — Listar padres/tutores

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador accede a la sección de padres.
  2. El sistema muestra todos los adultos con rol de padre/tutor.
- **Clases involucradas:** `ComandoAdministrador.mostrarPadres`, `GestionPadre.buscarTodosLosPadres`

---

### UC-17 — Añadir padre/tutor

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador rellena el formulario con nombre, apellidos, DNI, dirección, contacto, nivel de estudios, etc.
  2. El sistema crea el registro del adulto con tipo `PADRE`.
- **Clases involucradas:** `ComandoAdministrador.crearPadre`, `GestionPadre.anadir`, `AdultoBean`

---

### UC-18 — Modificar datos de padre/tutor

- **Actor:** Administrador, Padre (sus propios datos)
- **Flujo principal:**
  1. El actor accede al formulario de edición del padre.
  2. Modifica los campos y confirma.
  3. El sistema actualiza los datos.
- **Clases involucradas:** `ComandoAdministrador.modificarPadre`, `GestionPadre.actualizarDatosPadre`, `ComandoPadre` (opción 14)

---

### UC-19 — Ver hijos de un padre

- **Actor:** Padre
- **Precondición:** El padre está autenticado.
- **Flujo principal:**
  1. El sistema ejecuta la secuencia de bienvenida del padre.
  2. Recupera los datos del padre y el listado de sus hijos matriculados.
- **Clases involucradas:** `ComandoPadre` (opción 11), `GestionPadre.bienvenida`, `GestionPadre.buscarHijosDelPadre`

---

### UC-20 — Vincular alumno con tutor

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona un alumno y un adulto (tutor).
  2. El sistema almacena el DNI del tutor en el registro del alumno.
- **Clases involucradas:** `ComandoAdministrador.vincularAlumnoTutor`

---

## Módulo 5: Gestión de Profesores

### UC-21 — Listar profesores

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador accede a la sección de profesores.
  2. El sistema muestra todos los adultos con rol de profesor.
- **Clases involucradas:** `ComandoAdministrador.mostrarProfesores`, `GestionProfesor.buscarTodosLosProfesores`

---

### UC-22 — Añadir profesor

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador rellena el formulario con los datos del profesor.
  2. El sistema crea el registro con tipo `PROFESOR`.
- **Clases involucradas:** `ComandoAdministrador.crearProfesor`, `GestionProfesor.anadir`, `AdultoBean`

---

### UC-23 — Modificar datos de profesor

- **Actor:** Administrador, Profesor (sus propios datos)
- **Flujo principal:**
  1. El actor accede al formulario de edición.
  2. Modifica los datos y confirma.
  3. El sistema actualiza el registro.
- **Clases involucradas:** `ComandoAdministrador.modificarProfesor`, `GestionProfesor.actualizarDatosProfesor`, `ComandoProfesor` (opción 19)

---

### UC-24 — Ver clases asignadas al profesor

- **Actor:** Profesor
- **Precondición:** El profesor está autenticado.
- **Flujo principal:**
  1. El sistema ejecuta la bienvenida del profesor.
  2. Recupera las clases asignadas al profesor en sesión.
- **Clases involucradas:** `ComandoProfesor` (opción 10), `GestionProfesor.bienvenida`, `GestionClase.seleccionarClasesDelProfesor`

---

## Módulo 6: Evaluaciones de Alumnos

### UC-25 — Ver criterios de evaluación de un alumno

- **Actor:** Profesor, Padre
- **Flujo principal:**
  1. El actor selecciona un alumno.
  2. El sistema muestra los criterios de evaluación organizados por trimestre, bloque y área de conocimiento.
- **Clases involucradas:** `ComandoProfesor.mostrar_criterios_evaluacion`, `Evaluacion`, `Criterio`, `Bloque`, `Area_Conocimiento`

---

### UC-26 — Crear/Modificar evaluación trimestral

- **Actor:** Profesor
- **Precondición:** El alumno está en una clase asignada al profesor.
- **Flujo principal:**
  1. El profesor selecciona el alumno y el trimestre.
  2. Completa o edita los criterios de evaluación por área de conocimiento y bloque.
  3. El sistema guarda la evaluación.
- **Clases involucradas:** `ComandoProfesor.anadir_modificar_criterios_evaluacion`, `Evaluacion`, `Criterio`

---

## Módulo 7: Menú del Comedor

### UC-27 — Crear menú diario

- **Actor:** Administrador
- **Precondición:** No existe ya un menú para esa fecha.
- **Flujo principal:**
  1. El administrador accede al formulario de nuevo menú.
  2. Selecciona la fecha e introduce primer plato, segundo plato y postre.
  3. El sistema verifica que no existe menú para esa fecha y lo crea.
- **Clases involucradas:** `ComandoAdministrador.crearMenu`, `GestionComedor.crearMenu`, `MenuBean`

---

### UC-28 — Consultar menú mensual

- **Actor:** Administrador, Profesor, Padre
- **Flujo principal:**
  1. El actor selecciona el mes y año a consultar.
  2. El sistema recupera todos los menús del mes.
  3. Se muestra un calendario con los platos de cada día.
- **Clases involucradas:** `ComandoAdministrador.seleccionarMesMenu`, `GestionComedor.consultarMesComedor`

---

### UC-29 — Modificar menú

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona un menú existente.
  2. Modifica los platos y confirma.
  3. El sistema actualiza el registro.
- **Clases involucradas:** `ComandoAdministrador.modificarMenu`, `GestionComedor.modificarMenu`

---

### UC-30 — Eliminar menú

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona un menú y confirma la eliminación.
  2. El sistema borra el registro.
- **Clases involucradas:** `ComandoAdministrador.eliminarMenu`, `GestionComedor.eliminarMenu`

---

## Módulo 8: Noticias y Anuncios

### UC-31 — Crear noticia

- **Actor:** Profesor, Administrador
- **Flujo principal:**
  1. El actor rellena el formulario de nueva noticia (título, contenido, imagen).
  2. El sistema asigna automáticamente ID y fecha de creación.
  3. La noticia queda registrada en el sistema.
- **Clases involucradas:** `ComandoProfesor` (opción 22a), `GestionNoticia.crearNoticia`, `NoticiaBean`

---

### UC-32 — Listar noticias publicadas

- **Actor:** Administrador, Profesor, Padre
- **Flujo principal:**
  1. El actor accede a la sección de noticias.
  2. El sistema recupera las noticias habilitadas/publicadas.
  3. Se muestran título, fecha y contenido resumido.
- **Clases involucradas:** `ComandoProfesor` (opción 3 / 23), `GestionNoticia.buscarNoticiasHabilitadas`, `GestionNoticia.buscarUltimasNoticias`

---

### UC-33 — Modificar noticia

- **Actor:** Profesor, Administrador
- **Flujo principal:**
  1. El actor selecciona una noticia existente.
  2. Edita título y/o contenido.
  3. El sistema actualiza el registro.
- **Clases involucradas:** `ComandoProfesor` (opciones 23a / 23b), `GestionNoticia.modificarNoticia`

---

## Módulo 9: Comunicación por Correo Electrónico

### UC-34 — Padre envía email al profesor

- **Actor:** Padre
- **Precondición:** El padre está autenticado.
- **Flujo principal:**
  1. El padre accede al formulario de contacto con el tutor.
  2. Redacta el mensaje.
  3. El sistema envía el email al profesor asignado al hijo.
- **Clases involucradas:** `ComandoPadre` (opción 17), `ComandoMail` (opción 18), `GestionProfesor`

---

### UC-35 — Profesor envía email a un padre

- **Actor:** Profesor
- **Precondición:** El profesor está autenticado.
- **Flujo principal:**
  1. El profesor selecciona el padre/tutor destinatario.
  2. Redacta el mensaje.
  3. El sistema envía el email al padre.
- **Clases involucradas:** `ComandoProfesor` (opción 21), `ComandoMail` (opción 18)

---

### UC-36 — Profesor envía email a toda la clase

- **Actor:** Profesor
- **Flujo principal:**
  1. El profesor selecciona "Enviar a toda la clase".
  2. Redacta el mensaje.
  3. El sistema recupera todos los padres de los alumnos de la clase y envía el email a todos.
- **Clases involucradas:** `ComandoProfesor` (opción 21), `ComandoMail`, `GestionPadre.buscarPadresDeAlumnos`

---

### UC-37 — Visitante envía formulario de contacto

- **Actor:** Visitante (usuario no autenticado)
- **Flujo principal:**
  1. El visitante rellena el formulario de contacto público del sitio web.
  2. El sistema envía el mensaje al correo de la guardería.
- **Clases involucradas:** `ComandoMail` (opción 12)

---

## Módulo 10: Gestión de Usuarios del Sistema

### UC-38 — Listar usuarios

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador accede a la sección de usuarios.
  2. El sistema recupera todos los usuarios con su persona asociada.
- **Clases involucradas:** `ComandoUsuario.mostrarUsuarios` (opción 33), `GestionUsuario.obtenerUsuarios`

---

### UC-39 — Crear cuenta de usuario

- **Actor:** Administrador
- **Precondición:** La persona (adulto) ya existe en el sistema.
- **Flujo principal:**
  1. El administrador verifica que el nombre de usuario no existe (`GestionUsuario.verificarExistenciaUsuario`).
  2. Introduce usuario, contraseña y perfil (administrador/padre/profesor).
  3. El sistema vincula la cuenta al DNI de la persona y la registra.
- **Clases involucradas:** `ComandoUsuario` (opciones 32a / 32b), `GestionUsuario.crearUsuario`, `UsuarioBean`

---

### UC-40 — Modificar cuenta de usuario

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona el usuario a editar.
  2. Modifica usuario, contraseña o perfil.
  3. El sistema actualiza el registro.
- **Clases involucradas:** `ComandoUsuario` (opciones 45 / 45a), `GestionUsuario.actualizarUsuario`

---

### UC-41 — Eliminar cuenta de usuario

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona el usuario y confirma la eliminación.
  2. El sistema borra la cuenta.
- **Clases involucradas:** `ComandoUsuario` (opción 46), `GestionUsuario.eliminarUsuario`

---

## Módulo 11: Gestión de Contenido (CMS)

### UC-42 — Editar contenido de una página

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador accede a la interfaz CMS (Backbone.js SPA).
  2. Selecciona el elemento de contenido a modificar.
  3. Edita el texto o imagen con el editor CKEditor.
  4. El sistema guarda el elemento actualizado en base de datos y en el sistema de archivos.
- **Clases involucradas:** `ComandoCMS.modificar_elemento_cms`, `ElementoCMS`, `GrupoCMS`

---

### UC-43 — Subir fichero

- **Actor:** Administrador
- **Flujo principal:**
  1. El administrador selecciona un fichero (documento, imagen) desde su equipo.
  2. El sistema lo recibe y lo almacena en el servidor.
- **Clases involucradas:** `ComandoAdministrador.subirFichero`, `FileUploadServlet`

---

## Módulo 12: Procesos Automáticos del Sistema

### UC-44 — Resetear caché de páginas publicadas

- **Actor:** Sistema (cron)
- **Frecuencia:** Cada hora.
- **Flujo principal:**
  1. El planificador ejecuta el job `page_publish`.
  2. El sistema invalida y regenera la caché de las páginas publicadas.
- **Configuración:** `cron.xml`, `CacheResetServlet`

---

### UC-45 — Ejecutar cron de plugins

- **Actor:** Sistema (cron)
- **Frecuencia:** Cada minuto.
- **Flujo principal:**
  1. El planificador lanza el endpoint `/_ah/cron/plugin`.
  2. Se ejecutan los cron jobs de los plugins activos de Vosao CMS.
- **Configuración:** `cron.xml`, `PluginCronFilter`

---

## Resumen de Casos de Uso por Actor

| Módulo | Administrador | Profesor | Padre | Visitante | Sistema |
|---|---|---|---|---|---|
| Autenticación y sesión | UC-01..03 | UC-01..03 | UC-01..03 | — | — |
| Alumnos | UC-04..07 | UC-07 | UC-06, 07 | — | — |
| Clases | UC-08..15 | UC-08, 14 | — | — | — |
| Padres/Tutores | UC-16..20 | — | UC-18, 19 | — | — |
| Profesores | UC-21..24 | UC-23, 24 | — | — | — |
| Evaluaciones | UC-25, 26 | UC-25, 26 | UC-25 | — | — |
| Comedor | UC-27..30 | UC-28 | UC-28 | — | — |
| Noticias | UC-31..33 | UC-31..33 | UC-32 | — | — |
| Comunicación | UC-34..37 | UC-35, 36 | UC-34 | UC-37 | — |
| Usuarios | UC-38..41 | — | — | — | — |
| CMS | UC-42, 43 | — | — | — | — |
| Procesos automáticos | — | — | — | — | UC-44, 45 |

---

*Generado el 2026-02-28 a partir del análisis del código fuente de `com.guarderia`.*
