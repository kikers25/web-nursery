package com.guarderia.servlet.comando;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.AlumnoBean;
import com.guarderia.bean.ClaseBean;
import com.guarderia.bean.MenuBean;
import com.guarderia.negocio.GestionAlumno;
import com.guarderia.negocio.GestionClase;
import com.guarderia.negocio.GestionComedor;
import com.guarderia.negocio.GestionPadre;
import com.guarderia.negocio.GestionProfesor;
import com.guarderia.utils.UtilidadesWeb;

/**
 * Servlet implementation class for Servlet: ServletAdministrador
 *
 * @web.servlet
 *   name="ServletAdministrador"
 *   display-name="ServletAdministrador" 
 *
 * @web.servlet-mapping
 *   url-pattern="/ControladorAdministador"
 *  
 */
 public class ComandoAdministrador extends com.guarderia.servlet.comando.Comando {
	    /**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Atributo utilizada para escribir trazas
		 */
		private Logger log = Logger.getLogger(this.getClass());

		/* (non-Java-doc)
		 * @see javax.servlet.http.HttpServlet#HttpServlet()
		 */
		public ComandoAdministrador() {
		}   	 	
		
		/**
		 * Vincula el alumno seleccionado a la clase
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void vincularAlumnoAClase(HttpServletRequest request, HttpServletResponse response) throws ServletException{
			ArrayList alumnos;
			GestionAlumno gestionAlumno = new GestionAlumno();
			ClaseBean clase = (ClaseBean)request.getSession().getAttribute("clase");
			alumnos = request.getSession().getAttribute("alumnos") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("alumnos");
			String seleccion = request.getParameter("nSeleccion") == null ? "" : request.getParameter("nSeleccion");
			int nSeleccion = 0;
			if (!seleccion.equals("")) nSeleccion = Integer.parseInt(seleccion);
			AlumnoBean alumno = (AlumnoBean) alumnos.get(nSeleccion);
			
			if (gestionAlumno.anadirAlumnoEnLaClase(clase,alumno)){
				anadirMensajeCorrecto(request, response, "Se ha vinculado el alumno a la clase");
				// Actualizo los alumnos de la clase
				if (gestionAlumno.buscarAlumnosDeClase(clase, alumnos)){
					request.getSession().setAttribute("alumnos", alumnos);
				}
			}
			else{
				anadirMensajeError(request, response, "No se ha vinculado el alumno a la clase");
			}
			//anadirOtraAccion(VOLVER);
			//UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Desvincula el alumno seleccionado de la clase
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void desvincularAlumnoDeClase(HttpServletRequest request, HttpServletResponse response) throws ServletException{
			GestionClase gestionClase = new GestionClase();
			ArrayList alumnos;
			ClaseBean clase = (ClaseBean)request.getSession().getAttribute("clase");
			alumnos = request.getSession().getAttribute("alumnos") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("alumnos");
			String seleccion = request.getParameter("numeroAlumno") == null ? "" : request.getParameter("numeroAlumno");
			int nSeleccion = 0;
			if (!seleccion.equals("")) nSeleccion = Integer.parseInt(seleccion);
			AlumnoBean alumno = (AlumnoBean) alumnos.get(nSeleccion);
			
			if (gestionClase.eliminarAlumnoDeClase(clase,alumno)){
				anadirMensajeCorrecto(request, response, "Se ha desvinculado el alumno de la clase");
				// Actualizo los alumnos de la clase
				if (gestionClase.buscarAlumnosDeClase(clase, alumnos)){
					request.getSession().setAttribute("alumnos", alumnos);
				}
			}
			else{
				anadirMensajeError(request, response, "No se ha desvinculado el alumno de la clase");
			}
			//anadirOtraAccion(VOLVER);
			//UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Busca todos los adultos para mostrarlos en una lista
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void vincularAlumnoTutor(HttpServletRequest request, HttpServletResponse response) throws ServletException{
			AlumnoBean alumno = new AlumnoBean();
			ArrayList tutores = new ArrayList();
			GestionPadre gestionPadre = new GestionPadre();
			GestionAlumno gestionAlumno = new GestionAlumno();
			GestionProfesor gestionProfesor = new GestionProfesor();
			alumno = gestionAlumno.recuperaDatosAlumno(request);
			
			gestionPadre.buscarTodosLosPadres(tutores);
			if (gestionProfesor.buscarTodasLasPersonas(tutores)){
				request.getSession().setAttribute("tutores", tutores);
				request.getSession().setAttribute("alumnoSeleccionado", alumno);
				anadirMensajeCorrecto(request, response, "Seleccione el tutor vinculo al alumno");
			}
			else{
				anadirMensajeError(request, response, "Se ha producido un error al crear al alumno");
				//anadirOtraAccion(VOLVER);
				UtilidadesWeb.irMenuPrincipal(request, response);
			}
		}
		
		/**
		 * Anade un alumno
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void anadirAlumno(HttpServletRequest request, HttpServletResponse response) throws ServletException{
			int nSeleccionado = recuperaNumeroSeleccionado(request, response, "numeroPersona");
			GestionAlumno gestionAlumno = new GestionAlumno();
			
			AlumnoBean alumno = new AlumnoBean();
			alumno = (AlumnoBean) request.getSession().getAttribute("alumnoSeleccionado");
			ArrayList tutores = (ArrayList) request.getSession().getAttribute("tutores");
			AdultoBean padre = (AdultoBean) tutores.get(nSeleccionado);
			alumno.setDniTutor1(padre.getDni());
			if ( gestionAlumno.anadirAlumno(alumno) == true){
				anadirMensajeCorrecto(request, response, "Se ha insertado correctamente el alumno, vinculado con su padre y a una clase");
			}
			else{
				anadirMensajeError(request, response, "No se ha anadido el alumno");
			}
			//anadirOtraAccion(VOLVER);
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Muestra los Alumnos pertenecientes a la clase seleccionada
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void ventanaAlumnosClase(HttpServletRequest request, HttpServletResponse response){
			GestionClase gestionClase = new GestionClase();
			String aux = request.getParameter("numeroClase") == null ? "" : request.getParameter("numeroClase");
			ArrayList clases = request.getSession().getAttribute("clases") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("clases"); 
			int nClase = 0;
			if (!aux.equals("")) nClase = Integer.parseInt(aux);
			ClaseBean clase = (ClaseBean) clases.get(nClase);
			ArrayList alumnos = new ArrayList();
			if (gestionClase.buscarAlumnosDeClase(clase, alumnos)){
				request.getSession().setAttribute("clase", clase);
				request.getSession().setAttribute("alumnos", alumnos);
				anadirVolver(request, response, opcion);
			}
		}
		
		/**
		 * Muestra los Profesores que imparten en la clase seleccionada
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void ventanaProfesoresClase(HttpServletRequest request, HttpServletResponse response){
			GestionClase gestionClase = new GestionClase();
			String aux = request.getParameter("numeroClase") == null ? "" : request.getParameter("numeroClase");
			ArrayList clases = request.getSession().getAttribute("clases") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("clases"); 
			int nClase = 0;
			if (!aux.equals("")) nClase = Integer.parseInt(aux);
			ClaseBean clase = (ClaseBean) clases.get(nClase);
			ArrayList profesores = new ArrayList();
			if (gestionClase.buscarProfesoresDeLaClase(clase, profesores)){
				request.getSession().setAttribute("clase", clase);
				request.getSession().setAttribute("profesores", profesores);
				anadirVolver(request, response, opcion);
			}
		}
		
		
		/**
		 * Vincula al profesor seleccionado a la clase
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void vincularProfesorAClase(HttpServletRequest request, HttpServletResponse response) throws ServletException{
			GestionClase gestionClase = new GestionClase();
			ArrayList profesores;
			ClaseBean clase = (ClaseBean)request.getSession().getAttribute("clase");
			profesores = request.getSession().getAttribute("profesores") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("profesores");
			String seleccion = request.getParameter("nSeleccion") == null ? "" : request.getParameter("nSeleccion");
			int nSeleccion = 0;
			if (!seleccion.equals("")) nSeleccion = Integer.parseInt(seleccion);
			AdultoBean profesor = (AdultoBean) profesores.get(nSeleccion);
			
			if (gestionClase.anadirProfesorAClase(clase,profesor)){
				anadirMensajeCorrecto(request, response, "Se ha vinculado el profesor a la clase");
				// Actualizo los profesores de la clase
				if (gestionClase.buscarProfesoresDeLaClase(clase, profesores)){
					request.getSession().setAttribute("profesores", profesores);
				}
			}
			else{
				anadirMensajeError(request, response, "No se ha vinculado el profesor a la clase");
			}
			//anadirOtraAccion(VOLVER);
			//UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Desvincula el profesor seleccionado de la clase
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void desvincularProfesorDeClase(HttpServletRequest request, HttpServletResponse response) throws ServletException{
			GestionClase gestionClase = new GestionClase();
			ArrayList profesores;
			ClaseBean clase = (ClaseBean)request.getSession().getAttribute("clase");
			profesores = request.getSession().getAttribute("profesores") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("profesores");
			String seleccion = request.getParameter("numeroProfesor") == null ? "" : request.getParameter("numeroProfesor");
			int nSeleccion = 0;
			if (!seleccion.equals("")) nSeleccion = Integer.parseInt(seleccion);
			AdultoBean profesor = (AdultoBean) profesores.get(nSeleccion);
			
			if (gestionClase.eliminarProfesorDeClase(clase,profesor)){
				anadirMensajeCorrecto(request, response, "Se ha desvinculado el alumno de la clase");
				// Actualizo los profesores de la clase
				if (gestionClase.buscarProfesoresDeLaClase(clase, profesores)){
					request.getSession().setAttribute("profesores", profesores);
				}
			}
			else{
				anadirMensajeError(request, response, "No se ha desvinculado el alumno de la clase");
			}
			//anadirOtraAccion(VOLVER);
			//UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Muestra todos los alumnos
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void mostrarAlumnos(HttpServletRequest request, HttpServletResponse response) {
			ArrayList padres = new ArrayList();
			ArrayList alumnos = new ArrayList();
			GestionPadre gestionPadre = new GestionPadre();
			GestionAlumno gestionAlumno = new GestionAlumno();
			
			if (gestionAlumno.buscarAlumnos(alumnos)){
				gestionPadre.buscarPadresDeAlumnos(alumnos,padres);
				request.getSession().setAttribute("padres", padres);
				request.getSession().setAttribute("alumnos", alumnos);
				anadirVolver(request, response, opcion);
			}
		}
		
		/**
		 * Muestra una ventana con los datos del alumno seleccionado
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void ventanaModificarAlumno(HttpServletRequest request, HttpServletResponse response) {
			ArrayList alumnos;
			alumnos = request.getSession().getAttribute("alumnos") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("alumnos");
			int nSeleccion = recuperaNumeroSeleccionado(request, response, "numeroAlumno");
			AlumnoBean alumno = (AlumnoBean) alumnos.get(nSeleccion);
			request.getSession().setAttribute("alumno", alumno);
		}
		
		/**
		 * Muestra los datos de todos los padres
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void mostrarPadres(HttpServletRequest request, HttpServletResponse response) {
			ArrayList padres = new ArrayList();
			GestionPadre gestionPadre = new GestionPadre();
			
			if(gestionPadre.buscarTodosLosPadres(padres)){
				request.getSession().setAttribute("padres", padres);
				anadirVolver(request, response, opcion);
			}
			
		}
		
		/**
		 * Modificar los datos de un alumno
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void modificarAlumno(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			GestionAlumno gestionAlumno = new GestionAlumno();
			
			AlumnoBean auxAlumno = (AlumnoBean) request.getSession().getAttribute("alumno");
			AlumnoBean alumno = gestionAlumno.recuperaDatosAlumno(request);
			alumno.setIdentificador(auxAlumno.getIdentificador());
			if(gestionAlumno.actualizarDatosAlumno(alumno)){
				anadirMensajeCorrecto(request, response, "Los datos del alumno se han modificado correctamente");
			}
			else{
				anadirMensajeError(request, response, "Los datos del alumno no se han modificado correctamente");
			}
			request.getSession().setAttribute("alumno", alumno);
			//anadirOtraAccion("27");
			//UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Modifica los datos personales de un padre
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void modificarPadre(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			GestionPadre gestionPadre = new GestionPadre();
			AdultoBean padre = gestionPadre.recuperaDatosPadre(request);
			if(gestionPadre.actualizarDatosPadre(padre)){
				anadirMensajeCorrecto(request, response, "Los datos del padre se han modificado correctamente");
//				Actualizo los datos del padre en session
				request.getSession().setAttribute("padre", padre);
			}
			else{
				anadirMensajeError(request, response, "Los datos del padre no se han modificado correctamente");
			}
			//anadirOtraAccion(VOLVER2);
			//UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Muestra los datos personales de un padre
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void ventanaModificarPadre(HttpServletRequest request, HttpServletResponse response) {
			ArrayList padres;
			padres = request.getSession().getAttribute("padres") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("padres");
			int nSeleccion = recuperaNumeroSeleccionado(request, response, "numeroPadre");
			AdultoBean padre = (AdultoBean) padres.get(nSeleccion);
			request.getSession().setAttribute("padre", padre);
		}
		
		/**
		 * Crea un padre
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void crearPadre(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			GestionPadre gestionPadre = new GestionPadre();
			AdultoBean padre = gestionPadre.recuperaDatosPadre(request);
			if(gestionPadre.anadir(padre)){
				anadirMensajeCorrecto(request, response, "Se ha creado correctamente el padre");
				request.getSession().setAttribute("padre", padre);
			}
			else{
				anadirMensajeError(request, response, "No se ha creado el padre");
			}
			//anadirOtraAccion("16");
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Modifica los datos personales de un profesor
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void modificarProfesor(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			GestionProfesor gestionProfesor = new GestionProfesor();
			AdultoBean profesor = gestionProfesor.recuperaDatosProfesor(request);
			if(gestionProfesor.actualizarDatosProfesor(profesor)){
				anadirMensajeCorrecto(request, response, "Los datos del profesor se han modificado correctamente");
				// Actualizo los datos del profesor en session
				request.getSession().setAttribute("profesor", profesor);
			}
			else{
				anadirMensajeError(request, response, "Los datos del profesor no se han modificado correctamente");
			}
			//anadirOtraAccion(VOLVER2);
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Muestra los datos personales de un profesor
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void ventanaModificarProfesor(HttpServletRequest request, HttpServletResponse response) {
			ArrayList profesores;
			profesores = request.getSession().getAttribute("profesores") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("profesores");
			int nSeleccion = recuperaNumeroSeleccionado(request, response, "numeroProfesor");
			AdultoBean profesor = (AdultoBean) profesores.get(nSeleccion);
			request.getSession().setAttribute("profesor", profesor);
		}
		
		/**
		 * Crea un profesor
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void crearProfesor(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			GestionProfesor gestionProfesor = new GestionProfesor();
			AdultoBean profesor = gestionProfesor.recuperaDatosProfesor(request);
			if(gestionProfesor.anadir(profesor)){
				anadirMensajeCorrecto(request, response, "Se ha creado correctamente el profesor");
				request.getSession().setAttribute("profesor", profesor);
			}
			else{
				anadirMensajeError(request, response, "No se ha creado el profesor");
			}
			//anadirOtraAccion("16");
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Muestra los datos de todos los profesores
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void mostrarProfesores(HttpServletRequest request, HttpServletResponse response) {
			GestionProfesor gestionProfesor = new GestionProfesor();
			ArrayList profesores = new ArrayList();
			if(gestionProfesor.buscarTodosLosProfesores(profesores)){
				request.getSession().setAttribute("profesores", profesores);
				anadirVolver(request, response, opcion);
			}
			
		}
		
		/**
		 * Crea un menu
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void crearMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			GestionComedor gestionComedor = new GestionComedor();
			MenuBean comida = gestionComedor.recuperaDatosComida(request);
			if (gestionComedor.crearMenu(comida) == true){
				anadirMensajeCorrecto(request, response, "Se ha insertado correctamente el menu");
			}
			else{
				anadirMensajeError(request, response, "No se ha insertado el menu ya que existe un menu en esa fecha");
			}
			//anadirOtraAccion("16");
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Muestra el menn que va a ver en un mes
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void seleccionarMesMenu(HttpServletRequest request, HttpServletResponse response) {
			GestionComedor gestionComedor = new GestionComedor();
			int numeroMes = recuperaNumeroSeleccionado(request, response, "numeroMes");
			ArrayList comedor = new ArrayList();
			GregorianCalendar calendario = new GregorianCalendar();
			gestionComedor.consultarMesComedor(comedor,numeroMes,calendario.get(Calendar.YEAR));
			request.getSession().setAttribute("comidas", comedor);
			anadirVolver(request, response, opcion);
		}
		
		/**
		 * Muestra la ventana de modificar Menu
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 */
		private void ventanaModificarMenu(HttpServletRequest request, HttpServletResponse response) {
			int numeroMenu = recuperaNumeroSeleccionado(request, response, "numeroMenu");
			ArrayList comedor = (ArrayList) request.getSession().getAttribute("comidas");
			MenuBean menu = new MenuBean();
			if (comedor != null){
				menu = (MenuBean) comedor.get(numeroMenu);
			}
			request.getSession().setAttribute("menu", menu);
			anadirVolver(request, response, opcion);
		}
		
		/**
		 * Modifica los datos del menu
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void modificarMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			GestionComedor gestionComedor = new GestionComedor();
			MenuBean comida = gestionComedor.recuperaDatosComida(request);
			if (gestionComedor.modificarMenu(comida)){
				anadirMensajeCorrecto(request, response, "Se ha modificado correctamente el menu");
			}
			else{
				anadirMensajeError(request, response, "No se ha podido modificar el menu");
			}
			//anadirOtraAccion(VOLVER2); //Volver2
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Elimina un menu
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void eliminarMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			GestionComedor gestionComedor = new GestionComedor();
			int numeroMenu = recuperaNumeroSeleccionado(request, response, "numeroMenu");
			ArrayList comedor = (ArrayList) request.getSession().getAttribute("comidas");
			MenuBean menu = new MenuBean();
			if (comedor != null){
				menu = (MenuBean) comedor.get(numeroMenu);
			}
			if (gestionComedor.eliminarMenu(menu)){
				anadirMensajeCorrecto(request, response, "Se ha eliminado correctamente el menu");
			}
			else{
				anadirMensajeError(request, response, "No se ha podido eliminar el menu");
			}
			//anadirOtraAccion(VOLVER); //Volver
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		
		/**
		 * Sube un fichero a una ruta
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @throws ServletException 
		 */
		private void subirFichero(HttpServletRequest request, HttpServletResponse response, String directorio) throws ServletException {
			
			// first check if the upload request coming in is a multipart request
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			
			if (isMultipart){
				/*DiskFileItemFactory factory = new DiskFileItemFactory();
	
				factory.setSizeThreshold(200000);
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setFileSizeMax(500000);*/
	
				try {
					//List items = upload.parseRequest(request);
					List items = (List) request.getAttribute(UtilidadesWeb.NOMBRE_LISTA_PARAMETROS_MULTIPART);
					Iterator itr = items.iterator();
	
					while(itr.hasNext()) {
						FileItem item = (FileItem) itr.next();
						// Subimos los fichero de tipo file que estén en el formulario
						if(!item.isFormField()) {
							File fullFile  = new File(FilenameUtils.getName(item.getName()) );  
							String rutaFichero = request.getSession().getServletContext().getRealPath(directorio);
							rutaFichero = FilenameUtils.concat(rutaFichero, 
									FilenameUtils.getName(fullFile.getName()));
							File savedFile = new File (rutaFichero);
							item.write(savedFile);
							anadirMensajeCorrecto(request, response, "Se ha subido correctamente el fichero " + fullFile.getName());
						}
						
					}
				} catch (FileUploadException e) {
					log.error("No me ha dejado guardar el fichero: " + e);
					anadirMensajeError(request, response, "No se ha podido subir el fichero debido a su tamaño excesivo");
				} catch (Exception e){
					log.error("No me ha dejado guardar el fichero: " + e);
					anadirMensajeError(request, response, "No se ha podido subir el fichero");
				}
				//anadirOtraAccion(VOLVER); //Volver
				UtilidadesWeb.irMenuPrincipal(request, response);
			}
			
		}
		
		public String procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			setFicheroRedireccion (UtilidadesWeb.ficheroRedireccionPorDefectoJsp);
			
			log.info("Opcion Menu Administrador " + opcion);
			
			if (opcion.equals("9")){ // Menu Privado Administrador
				anadirVolver(request, response, opcion);
			}
			if (opcion.equals("26a")){ // Ventana vincular Alumno con tutor
				vincularAlumnoTutor(request, response);
			}
			if (opcion.equals("26b")){ // Anadir Alumno con tutor vinculado
				anadirAlumno(request, response);
			}
			if (opcion.equals("27")){ // Mostrar Alumnos
				mostrarAlumnos(request, response);
			}
			if (opcion.equals("27a")){ // Ventana Modificar Alumno
				ventanaModificarAlumno(request, response);
			}
			if (opcion.equals("27b")){ // Modificar Alumnos
				modificarAlumno(request, response);
			}
			if (opcion.equals("28a")){ // Crear Padre
				crearPadre(request, response);
			}
			if (opcion.equals("29")){ // Mostrar Padres
				mostrarPadres(request, response);
			}
			if (opcion.equals("29a")){ // Ventana Modificar Padre
				ventanaModificarPadre(request, response);
			}
			if (opcion.equals("29b")){ // Modificar Padre
				modificarPadre(request, response);
			}
			if (opcion.equals("30a")){ // Crear Profesor
				crearProfesor(request, response);
			}
			if (opcion.equals("31")){ // Mostrar Profesores
				mostrarProfesores(request, response);
			}
			if (opcion.equals("31a")){ // Ventana Modificar Profesor
				ventanaModificarProfesor(request, response);
			}
			if (opcion.equals("31b")){ // Modificar Profesor
				modificarProfesor(request, response);
			}
			if (opcion.equals("34a")){ // Seleccionar Mes Menu
				seleccionarMesMenu(request, response);
			}
			if (opcion.equals("35")){ // Ventana Profesores Clase
				ventanaProfesoresClase(request, response);
			}
			if (opcion.equals("36")){ // Ventana Vincular Profesor a Clase
				GestionProfesor gestionProfesor = new GestionProfesor();
				ArrayList profesores = new ArrayList();
				if (gestionProfesor.buscarTodosLosProfesores(profesores)){
					request.getSession().setAttribute("profesores", profesores);
				}
			}
			if (opcion.equals("36a")){ // Vincular Profesor a Clase
				vincularProfesorAClase(request, response);
			}
			if (opcion.equals("37")){ // Desvincular Profesor a Clase
				desvincularProfesorDeClase(request, response);
			}
			if (opcion.equals("38")){ // Ventana Alumnos Clase
				ventanaAlumnosClase(request, response);
			}
			if (opcion.equals("39")){ // Ventana Vincular Alumno a Clase
				ArrayList alumnos = new ArrayList();
				GestionAlumno gestionAlumno = new GestionAlumno();
				
				if (gestionAlumno.buscarAlumnos(alumnos)){
					request.getSession().setAttribute("alumnos", alumnos);
				}
			}
			if (opcion.equals("39a")){ // Vincular Alumno a Clase
				vincularAlumnoAClase(request, response);
			}
			if (opcion.equals("40")){ // Desvincular Alumno a Clase
				desvincularAlumnoDeClase(request, response);
			}
			if (opcion.equals("41a")){ // Crear Menú
				crearMenu(request, response);
			}
			if (opcion.equals("42")){ // Ventana Modificar Menú
				ventanaModificarMenu(request, response);
			}
			if (opcion.equals("42a")){ // Modificar Menú
				modificarMenu(request, response);
			}
			if (opcion.equals("43")){ // Eliminar Menú
				eliminarMenu(request, response);
			}
			if (opcion.equals("44a")){ // Subir Fichero
				subirFichero(request, response,"/files");
			}
			if (opcion.equals("44b")){ // Subir Fichero Noticias
				subirFichero(request, response,"/files/noticias");
			}
			return ficheroRedireccion;
		}

}