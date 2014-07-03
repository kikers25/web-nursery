package com.guarderia.servlet.comando;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.guarderia.DAO.DAOFactoria;
import com.guarderia.DAO.OracleAlumnoDAO;
import com.guarderia.DAO.OracleCriterioDAO;
import com.guarderia.DAO.OracleEvaluacionDAO;
import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.AlumnoBean;
import com.guarderia.bean.ClaseBean;
import com.guarderia.bean.Evaluacion;
import com.guarderia.bean.Intervalo_Edad;
import com.guarderia.bean.NoticiaBean;
import com.guarderia.bean.UsuarioBean;
import com.guarderia.negocio.GestionAlumno;
import com.guarderia.negocio.GestionNoticia;
import com.guarderia.negocio.GestionPadre;
import com.guarderia.negocio.GestionProfesor;
import com.guarderia.utils.UtilidadesGenerales;
import com.guarderia.utils.UtilidadesWeb;

/**
 * Implementación de la clase Servlet para el Servlet: ServletProfesor
 *
 * @web.servlet
 *   name="ServletProfesor"
 *   display-name="ControladorProfesor" 
 *
 * @web.servlet-mapping
 *   url-pattern="/ControladorProfesor"
 *  
 */
 public class ComandoProfesor extends com.guarderia.servlet.comando.Comando {
	 
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MOSTRAR_CRITERIOS_EVALUACION_ALUMNO = "evaluacion/mostrar_criterios";
	private static final String MOSTRAR_CRITERIOS_EVALUACION_HIJO = "evaluacion/mostrar_criterios_hijo";
	private static final String MODIFICAR_CRITERIO_EVALUACION_ALUMNO = "evaluacion/modificar";
	private static final String NUEVO_CRITERIO_EVALUACION_ALUMNO = "evaluacion/nuevo";
	
	/**
	 * Atributo utilizada para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ComandoProfesor() {
	}   	 	 	  	  
	
	public String procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (opcion.equals("0")){ // Bienvenida
			log.info("Bienvenida");
			GestionNoticia gestionNoticia = new GestionNoticia();
			ArrayList ultimasNoticias = new ArrayList();
			if (gestionNoticia.buscarUltimasNoticias(ultimasNoticias)){
				log.info("Anado ultimasNoticias a la session");
				request.getSession().setAttribute("ultimasNoticias", ultimasNoticias);
			}
			else{
				log.info("Error al obtener las ultimas noticias");
			}
		}
		if (opcion.equals("3")){ // Pestana pnblica Noticias
			GestionNoticia gestionNoticia = new GestionNoticia();
			ArrayList noticias = new ArrayList();
			if (gestionNoticia.buscarNoticiasHabilitadas(noticias)){
				log.info("Anado Noticias a la session");
				request.getSession().setAttribute("noticias", noticias);
			}
			else{
				log.fatal("Error al obtener las noticias");
			}
			anadirVolver(request, response, opcion);
		}
		if (opcion.equals("10")){ // Menu Privado Profesor
			log.info("Menu privado Profesor");
			GestionProfesor gestionProfesor = new GestionProfesor();
			UsuarioBean usuario = (UsuarioBean) request.getSession().getAttribute("usuario");
			AdultoBean adulto = new AdultoBean();
			adulto.setDni(usuario.getDniUsuario());
			ArrayList clases = new ArrayList();
			gestionProfesor.bienvenida(adulto,clases); // Busco los datos personales del profesor y las clases que imparte
			request.getSession().setAttribute("profesor", adulto);
			request.getSession().setAttribute("clases", clases);
			//request.getSession().setAttribute("opcionVolver", opcion);
			anadirVolver(request, response, opcion);
		}
		if (opcion.equals("19")){ // Actualizar Datos Profesor
			GestionProfesor gestionProfesor = new GestionProfesor();
			AdultoBean profesor = gestionProfesor.recuperaDatosProfesor(request);
			gestionProfesor.actualizarDatosProfesor(profesor);
			//Actualizo los datos del padre en session
			request.getSession().setAttribute("profesor", profesor);
		}
		if (opcion.equals("20")){ // Mostrar Alumnos Clase
			String aux = request.getParameter("numeroClase") == null ? "" : request.getParameter("numeroClase");
			ArrayList clases = request.getSession().getAttribute("clases") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("clases");
			GestionAlumno gestionAlumno = new GestionAlumno();
			int nClase = 0;
			if (!aux.equals("")) nClase = Integer.parseInt(aux);
			ClaseBean clase = (ClaseBean) clases.get(nClase);
			ArrayList alumnos = new ArrayList();
			if (gestionAlumno.buscarAlumnosDeClase(clase, alumnos)){
				GestionPadre gestionPadre = new GestionPadre();
				ArrayList padres = new ArrayList();
				gestionPadre.buscarPadresDeAlumnos(alumnos,padres);
				request.getSession().setAttribute("padres", padres);
				request.getSession().setAttribute("clase", clase);
				request.getSession().setAttribute("alumnos", alumnos);
				//request.getSession().setAttribute("opcionVolver", opcion);
				anadirVolver(request, response, opcion);
			}
		}
		if (opcion.equals("21")){ // Pantalla Principal Envio mail a padre o clase
			String aux = request.getParameter("numeroPadre") == null ? "" : request.getParameter("numeroPadre");
			ArrayList padres = request.getSession().getAttribute("padres") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("padres"); 
			int nPadre = 0;
			if (!aux.equals("")) nPadre = Integer.parseInt(aux);
			ArrayList to = new ArrayList();
			AdultoBean padre;
			if ( nPadre == -1){
				for (int i=0; i<padres.size();i++){
					padre = (AdultoBean)padres.get(i);
					if (padre.getCorreoElectronico() != null)
						if (!padre.getCorreoElectronico().equals(""))
							to.add(padre.getCorreoElectronico());
				}
			}
			else{
				padre = (AdultoBean) padres.get(nPadre);
				if (padre.getCorreoElectronico() != null)
					if (!padre.getCorreoElectronico().equals(""))
						to.add(padre.getCorreoElectronico());
			}
			request.getSession().setAttribute("to", to);
			AdultoBean profesor = request.getSession().getAttribute("profesor") == null ? new AdultoBean() : (AdultoBean) request.getSession().getAttribute("profesor");
			request.getSession().setAttribute("from", profesor);
		}
		if (opcion.equals("22a")){ // Crear Noticia
			GestionNoticia gestionNoticia = new GestionNoticia();
			NoticiaBean noticia= gestionNoticia.recuperaDatosNoticia(request);
			if (gestionNoticia.crearNoticia(noticia) == true){
				anadirMensajeCorrecto(request, response, "La noticia se ha insertado correctamente");
				ArrayList ultimasNoticias = new ArrayList();
				gestionNoticia.buscarUltimasNoticias(ultimasNoticias);
				log.info("Anado ultimasNoticias a la session");
				request.getSession().setAttribute("ultimasNoticias", ultimasNoticias);
			}
			else{
				anadirMensajeError(request, response, "La noticia no se ha insertado");
			}
			//masOpcion = "16";
			//ficheroRedireccion = UtilidadesWeb.ficheroRedireccionPorDefectoServlet;
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		if (opcion.equals("23")){ // Mostrar Noticias
			GestionNoticia gestionNoticia = new GestionNoticia();
			ArrayList noticias = new ArrayList();
			if (gestionNoticia.buscarTodasNoticias(noticias)){
				log.info("Anado Noticias a la session");
				request.getSession().setAttribute("noticias", noticias);
			}
			else{
				log.fatal("Error al obtener las noticias");
			}
			anadirVolver(request, response, opcion);
		}
		if (opcion.equals("23a")){ // Ventana Modificar Noticia
			ArrayList noticias = new ArrayList();
			noticias = (ArrayList)request.getSession().getAttribute("noticias");
			String sNoticia = request.getParameter("numeroNoticia");
			int nNoticia = 0;
			if (!sNoticia.equals("")) nNoticia = Integer.parseInt(sNoticia);
			NoticiaBean noticia = (NoticiaBean) noticias.get(nNoticia);
			request.getSession().setAttribute("noticia", noticia);
		}
		if (opcion.equals("23b")){ // Modificar Noticia
			GestionNoticia gestionNoticia = new GestionNoticia();
			NoticiaBean auxNoticia = (NoticiaBean) request.getSession().getAttribute("noticia");
			NoticiaBean noticia = gestionNoticia.recuperaDatosNoticia(request);
			noticia.setIdentificador(auxNoticia.getIdentificador());
			noticia.setFoto("");
			noticia.setFechaCreacion(auxNoticia.getFechaCreacion());
			noticia.setFechaModificacion(auxNoticia.getFechaModificacion());
			if(gestionNoticia.modificarNoticia(noticia) == true){
				anadirMensajeCorrecto(request, response, "La noticia se ha modificado correctamente");
				ArrayList ultimasNoticias = new ArrayList();
				gestionNoticia.buscarUltimasNoticias(ultimasNoticias);
				request.getSession().setAttribute("ultimasNoticias", ultimasNoticias);
				UtilidadesWeb.anadirParametroSession(request, response, "noticia", noticia);
			}
			else{
				anadirMensajeError(request, response, "La noticia no se ha actualizado");
			}
			//UtilidadesWeb.irMenuPrincipal(request, response);
		}
		if (StringUtils.equals(opcion, MOSTRAR_CRITERIOS_EVALUACION_ALUMNO)){
			mostrar_criterios_evaluacion(request, response,true);
		}
		if (StringUtils.equals(opcion, MOSTRAR_CRITERIOS_EVALUACION_HIJO)){
			mostrar_criterios_evaluacion(request, response,false);
		}
		if (StringUtils.equals(opcion, NUEVO_CRITERIO_EVALUACION_ALUMNO)){
			anadir_modificar_criterios_evaluacion(request, response, true);
		}
		if (StringUtils.equals(opcion, MODIFICAR_CRITERIO_EVALUACION_ALUMNO)){
			anadir_modificar_criterios_evaluacion(request, response, false);
		}
		
		return ficheroRedireccion;
	}

	private void anadir_modificar_criterios_evaluacion(HttpServletRequest request,
			HttpServletResponse response,
			boolean isNuevo) {
		OracleEvaluacionDAO evaluacionDAO = (OracleEvaluacionDAO) DAOFactoria.getDAOFactoria().getEvaluacionDAO();
		OracleAlumnoDAO alumnoDAO = (OracleAlumnoDAO) DAOFactoria.getDAOFactoria().getAlumnoDAO();
		
		// Se añaden los parámetros a la salida: idAlumno, idProfesor, curso y trimestre
		Integer idAlumno = new Integer( UtilidadesWeb.obtenerParametroPeticion(request, "idAlumno") );
		String idProfesor = UtilidadesWeb.obtenerParametroPeticion(request, "idProfesor");
		String curso = UtilidadesWeb.obtenerParametroPeticion(request, "curso");
		Integer trimestre = new Integer( UtilidadesWeb.obtenerParametroPeticion(request, "trimestre") );
		Integer nCriterios = new Integer( UtilidadesWeb.obtenerParametroPeticion(request, "nCriterios") );
		// Obtenemos los datos del alumno
		AlumnoBean alumno = (AlumnoBean) alumnoDAO.buscarPorId(idAlumno);
		
		// Recuperamos los criterios de evaluación del alumno y los añadimos en base de datos
		for (int i = 0; i < nCriterios; i++) {
			Evaluacion evaluacion= new Evaluacion();
			evaluacion.setAdulto(idProfesor);
			evaluacion.setAlumno(idAlumno);
			evaluacion.setCurso(curso);
			evaluacion.setTrimestre(trimestre);
			evaluacion.setCriterio(new Integer(
				UtilidadesWeb.obtenerParametroPeticion(request,"identificador_" + i)
			));
			evaluacion.setValor(
				UtilidadesWeb.obtenerParametroPeticion(request,"valor_" + i)
			);
			evaluacion.setDescripcion(
				UtilidadesWeb.obtenerParametroPeticion(request,"descripcion_" + i)
			);
			if (isNuevo){
				evaluacionDAO.crear(evaluacion);
			}
			else{
				evaluacionDAO.modificiar2(evaluacion);
			}
		}
		
		if (isNuevo){
			UtilidadesWeb.anadirMensajeCorrecto(request, response, "Añadida Evaluación de " 
					+ alumno.getNombre() + " " + alumno.getPrimerApellido() + alumno.getSegundoApellido());
		}
		else{
			UtilidadesWeb.anadirMensajeCorrecto(request, response, "Modificada Evaluación de " 
					+ alumno.getNombre() + " " + alumno.getPrimerApellido() + alumno.getSegundoApellido());
		}
	}

	private void mostrar_criterios_evaluacion(HttpServletRequest request,
			HttpServletResponse response, boolean isModificable) {
		OracleCriterioDAO criterioDAO = (OracleCriterioDAO) DAOFactoria.getDAOFactoria().getCriterioDAO();
		OracleEvaluacionDAO evaluacionDAO = (OracleEvaluacionDAO) DAOFactoria.getDAOFactoria().getEvaluacionDAO();
		List evaluaciones = null;
		
		// Se añaden los parámetros a la salida: idAlumno, idProfesor, curso y trimestre
		String idAlumno = UtilidadesWeb.obtenerParametroPeticion(request, "idAlumno");
		UtilidadesWeb.anadirParametroPeticion(request, response, "idAlumno", idAlumno);
		String idProfesor = UtilidadesWeb.obtenerParametroPeticion(request, "idProfesor");
		UtilidadesWeb.anadirParametroPeticion(request, response, "idProfesor", idProfesor);
		
		String curso = null;
		if (UtilidadesWeb.obtenerParametroPeticion(request, "curso") == null ){
			curso = UtilidadesGenerales.obtenerCursoActual(); 
		}
		else{
			curso = UtilidadesWeb.obtenerParametroPeticion(request, "curso");
		}
		UtilidadesWeb.anadirParametroPeticion(request, response, "curso", curso);
		
		Integer trimestre = null;
		if (UtilidadesWeb.obtenerParametroPeticion(request, "trimestre") == null ){
			trimestre = UtilidadesGenerales.obtenerNumeroTrimestre();
		}
		else{
			trimestre = new Integer(UtilidadesWeb.obtenerParametroPeticion(request, "trimestre"));
		}
		UtilidadesWeb.anadirParametroPeticion(request, response, "trimestre", "" + trimestre);
		
		// Se busca si ya existe la evaluación del alumno
		if (evaluacionDAO.isEvaluacionAlumnoDeProfesor(new Integer(idAlumno),
				idProfesor, UtilidadesGenerales.obtenerCursoActual(),
				new Integer(UtilidadesGenerales.obtenerNumeroTrimestre()))) {
			// Si existe
			UtilidadesWeb.anadirParametroPeticion(request, response, "comando", ComandoProfesor.MODIFICAR_CRITERIO_EVALUACION_ALUMNO);
			evaluaciones = evaluacionDAO.obtenerEvaluacionesAlumnoDeProfesor(new Integer(idAlumno), idProfesor, curso, trimestre);
			if (isModificable) UtilidadesWeb.anadirMensajeCorrecto(request, response, "Modificar evaluación del alumno");
		} else {
			// Si No existe
			UtilidadesWeb.anadirParametroPeticion(request, response, "comando", ComandoProfesor.NUEVO_CRITERIO_EVALUACION_ALUMNO);
			if (isModificable) UtilidadesWeb.anadirMensajeCorrecto(request, response, "Nueva evaluación del alumno");
		}
		
		Intervalo_Edad intervalo_Edad = new Intervalo_Edad();
		intervalo_Edad.setIdentificador(Intervalo_Edad.DE_DOS_A_TRES);
		List listaCriterios = criterioDAO.buscarCriterioCompletoPorEdad(intervalo_Edad);
		UtilidadesWeb.anadirParametroPeticion(request, response, "listaCriterios", listaCriterios);
		UtilidadesWeb.anadirParametroPeticion(request, response, "listaEvaluaciones", evaluaciones);
	}
	
}