package com.guarderia.servlet.comando;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guarderia.DAO.DAOFactoria;
import com.guarderia.DAO.OracleEvaluacionDAO;
import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.AlumnoBean;
import com.guarderia.bean.UsuarioBean;
import com.guarderia.negocio.GestionAlumno;
import com.guarderia.negocio.GestionPadre;
import com.guarderia.utils.UtilidadesGenerales;
import com.guarderia.utils.UtilidadesWeb;

/**
 * Servlet implementation class for Servlet: ControladorPadre
 * @web.servlet  name="ControladorPadre"  display-name="ControladorPadre" 
 * @web.servlet-mapping  url-pattern="/ControladorPadre"
 */
 public class ComandoPadre extends com.guarderia.servlet.comando.Comando {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Objeto de Negocio para las acciones con el padre
	 * @uml.property  name="padreBO"
	 * @uml.associationEnd  
	 */
	private GestionPadre padreBO;
	
	/**
	 * Objeto de Negocio para las acciones con el padre
	 * @uml.property  name="alumnoBO"
	 * @uml.associationEnd  
	 */
	private GestionAlumno alumnoBO;

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ComandoPadre() {
		padreBO = new GestionPadre();
		alumnoBO = new GestionAlumno();
	}   	 	
	
	public String procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (opcion.equals("11")){ // Menu Privado Padre
			UsuarioBean usuario = (UsuarioBean) request.getSession().getAttribute("usuario");
			AdultoBean adulto = new AdultoBean();
			adulto.setDni(usuario.getDniUsuario());
			ArrayList hijos = new ArrayList();
			padreBO.bienvenida(adulto,hijos); // Busco los datos personales del padre y los hijos que van a la guarderia
			request.getSession().setAttribute("padre", adulto);
			request.getSession().setAttribute("hijos", hijos);
			anadirVolver(request, response, opcion);
		}
		if(opcion.equals("13")){ // Ver Datos hijo
			OracleEvaluacionDAO evaluacionDAO = (OracleEvaluacionDAO) DAOFactoria.getDAOFactoria().getEvaluacionDAO();
			String aux = request.getParameter("numeroHijo") == null ? "" : request.getParameter("numeroHijo");
			ArrayList hijos = request.getSession().getAttribute("hijos") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("hijos"); 
			int nHijo = 0;
			if (!aux.equals("")) nHijo = Integer.parseInt(aux);
			AlumnoBean hijo = (AlumnoBean) hijos.get(nHijo);
			ArrayList profesores = new ArrayList();
			int nEvaluacion = 0;
			if (alumnoBO.buscarProfesoresDelAlumno(hijo, profesores)){
				request.getSession().setAttribute("hijo", hijo);
				request.getSession().setAttribute("profesores", profesores);
				request.getSession().setAttribute("opcionVolver", opcion);
				AdultoBean profesor = (AdultoBean) profesores.get(0);
				for (int j=0; j<UtilidadesGenerales.obtenerNumeroTrimestre();j++){
					if (evaluacionDAO.isEvaluacionAlumnoDeProfesor(new Integer(hijo.getIdentificador()),
							profesor.getDni(), UtilidadesGenerales.obtenerCursoActual(),
							new Integer(j+1))) {
						nEvaluacion ++;
					}
					else{
						break;
					}
				}
				UtilidadesWeb.anadirParametroPeticion(request, response, "nEvaluacion", "" + nEvaluacion);
				UtilidadesWeb.anadirParametroSession(request, response, "nEvaluacion", "" + nEvaluacion);
			}
		}
		if (opcion.equals("14")){ // Actualizar Datos Padre
			AdultoBean padre = padreBO.recuperaDatosPadre(request);
			if(padreBO.actualizarDatosPadre(padre)){
				anadirMensajeCorrecto(request, response, "Sus datos personales se han actualizado correctamente");
			}
			else{
				anadirMensajeError(request, response, "Sus datos personales no se han podido actualizado");
			}
			//Actualizo los datos del padre en session
			request.getSession().setAttribute("padre", padre);
		}
		if (opcion.equals("15")){ // Actualizar Datos Hijo
			AlumnoBean hijo = alumnoBO.recuperaDatosAlumno(request);
			AlumnoBean auxAlumno = (AlumnoBean) request.getSession().getAttribute("hijo");
			hijo.setIdentificador(auxAlumno.getIdentificador());
			if (alumnoBO.actualizarDatosAlumno(hijo)){
				anadirMensajeCorrecto(request, response, "Los datos de su hijo se han actualizado correctamente");
			}
			else{
				anadirMensajeError(request, response, "Se ha producido un error al actualizar los datos de su hijo");
			}
			int nEvaluacion = 0;
			if (request.getSession().getAttribute("nEvaluacion") != null){
				nEvaluacion = ((Integer)request.getSession().getAttribute("nEvaluacion")).intValue();
			}
			else{
				nEvaluacion = 1;
			}
			UtilidadesWeb.anadirParametroPeticion(request, response, "nEvaluacion", "" + nEvaluacion);
			//Actualizo los datos del hijo en session
			request.getSession().setAttribute("hijo", hijo);
		}
		if (opcion.equals("17")){ // Pantalla Principal Envio mail a profesor
			String aux = request.getParameter("numeroProfesor") == null ? "" : request.getParameter("numeroProfesor");
			ArrayList profesores = request.getSession().getAttribute("profesores") == null ? new ArrayList() : (ArrayList) request.getSession().getAttribute("profesores"); 
			int nProfesor = 0;
			if (!aux.equals("")) nProfesor = Integer.parseInt(aux);
			AdultoBean profesor = (AdultoBean) profesores.get(nProfesor);
			// Añadimos a la session las personas a las que se va a enviar el e-mail
			// y la persona que lo envia
			ArrayList to = new ArrayList();
			if (profesor.getCorreoElectronico() != null)
				if (!profesor.getCorreoElectronico().equals(""))
					to.add(profesor.getCorreoElectronico());
			request.getSession().setAttribute("to", to);
			AdultoBean padre = request.getSession().getAttribute("padre") == null ? new AdultoBean() : (AdultoBean) request.getSession().getAttribute("padre");
			request.getSession().setAttribute("from", padre);
		}
		return ficheroRedireccion;
		
	} 
  
}