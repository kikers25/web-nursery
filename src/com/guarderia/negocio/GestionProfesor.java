package com.guarderia.negocio;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.guarderia.DAO.AdultoDAO;
import com.guarderia.DAO.ClaseDAO;
import com.guarderia.DAO.DAOFactoria;
import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.ClaseBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase que realiza las operaciones relativas al Adulto
 * @author Enrique Martín Martín
 *
 */
public class GestionProfesor {
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Contructor por defecto
	 */
	public GestionProfesor(){
	}
	
	
	/**
	 * Busca los datos personales del profesor
	 * @param adulto Bean de tipo Adulto
	 * @return true si todo ha ido correctamente y false sino
	 */
	public boolean seleccionarProfesor(AdultoBean profesor){
		boolean resultado = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		resultado = adultoDAO.buscarAdulto(profesor);
		return resultado;
	}
	
	/**
	 * Busca todos los Profesores
	 * @param profesores Lista con todos los profesores
	 * @return true si se ha realizado la consulta correctamente y false sino
	 */	
	public boolean buscarTodosLosProfesores(ArrayList profesores){
		boolean resultado = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		resultado = adultoDAO.buscarTipoAdulto(AdultoDAO.PROFESOR, profesores);
		return resultado;
	}
	
	
	/**
	 * Busca los hijos del padre y los guarda en una lista
	 * @param adulto Bean con los datos del padre
	 * @param hijos Lista donde cada bean de tipo alumno correponden a los hijos del padre
	 * @return true si no ha habido ningún error en la busqueda y false sino
	 */
	public boolean seleccionarClasesDelProfesor(AdultoBean profesor,ArrayList clases){
		boolean resultado = false;
		ClaseDAO claseDAO = DAOFactoria.getDAOFactoria().getClaseDAO();
		resultado = claseDAO.consultaClasesProfesor(profesor, clases);
		return resultado;
	}
	
	/**
	 * Datos necesarios para el Menu del profesor
	 * @param adulto Bean con los datos del profesor
	 * @param clase Lista que corresponde cada bean con los datos de cada clase a la que tiene que ir el profesor
	 * este curso
	 * @return true si se ha encontrado los datos del profesor y de las clases y false sino
	 */
	public boolean bienvenida(AdultoBean profesor,ArrayList clases){
		boolean resultado = false;
		resultado = seleccionarProfesor(profesor);
		if (resultado) log.info("Obtenido datos profesor: " + profesor.getNombre() + "- dni-" + profesor.getDni());
		if (resultado ) resultado = seleccionarClasesDelProfesor(profesor,clases);
		return resultado;
	}
	
	/**
	 * Busca los profesores que imparten en la clase del curso actual
	 * @param clase Datos de la clase
	 * @param profesores Lista con los profesores del alumno
	 * @return true si no ha habido ningún error en la busqueda y false sino 
	 */
	public boolean buscarProfesoresDeLaClase(ClaseBean clase,ArrayList profesores){
		boolean resultado = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		resultado = adultoDAO.buscarAdultosDanClaseCursoActual(clase,profesores);
		return resultado;
	}
	
	/**
	 * Convierte los datos de la peticion html en un objeto de tipo bean adulto
	 * @param request Petiticion de usuario
	 * @return Objeto de tipo adulto o el objeto vacio si ha habido algun error
	 */
	public AdultoBean recuperaDatosProfesor(HttpServletRequest request){
		AdultoBean profesor = new AdultoBean();
		String aux;
		
		aux = request.getParameter("dni");
		if (aux == null){
			AdultoBean auxProfesor = (AdultoBean) request.getSession().getAttribute("profesor");
			aux = auxProfesor.getDni();
		}
		profesor.setDni(aux);
		
		aux = request.getParameter("nombre") == null ? "" : (String)request.getParameter("nombre");
		profesor.setNombre(aux);
		
		aux = request.getParameter("primerApellido") == null ? "" : (String)request.getParameter("primerApellido");
		profesor.setPrimerApellido(aux);
		
		aux = request.getParameter("segundoApellido") == null ? "" : (String)request.getParameter("segundoApellido");
		profesor.setSegundoApellido(aux);
		
		aux = request.getParameter("direccion") == null ? "" : (String)request.getParameter("direccion");
		profesor.setDireccion(aux);
		
		aux = request.getParameter("cp") == null ? "" : (String)request.getParameter("cp");
		profesor.setCodigoPostal(UtilidadesGenerales.convertirStringEnInteger(aux));
		
		aux = request.getParameter("localidad") == null ? "" : (String)request.getParameter("localidad");
		profesor.setLocalidad(aux);
		
		aux = request.getParameter("provincia") == null ? "" : (String)request.getParameter("provincia");
		profesor.setProvincia(aux);
		
		aux = request.getParameter("nacionalidad") == null ? "" : (String)request.getParameter("nacionalidad");
		profesor.setNacionalidad(aux);
		
		aux = request.getParameter("email") == null ? "" : (String)request.getParameter("email");
		profesor.setCorreoElectronico(aux);
		
		aux = request.getParameter("telefono") == null ? "" : (String)request.getParameter("telefono");
		profesor.setTelefono(UtilidadesGenerales.convertirStringEnInteger(aux));
		
		aux = request.getParameter("fechaNacimiento") == null ? "" : (String)request.getParameter("fechaNacimiento");
		profesor.setFechaNacimiento(UtilidadesGenerales.convertirStringEnGregorianCalendar(aux));
		
		aux = request.getParameter("nivelEstudios") == null ? "" : (String)request.getParameter("nivelEstudios");	
		profesor.setNivelEstudios(aux);
		
		aux = request.getParameter("tipoPuesto") == null ? "" : (String)request.getParameter("tipoPuesto");	
		profesor.setNivelEstudios(aux);
		
		return profesor;
	}
	
	/**
	 * Actualiza los datos personales del profesor
	 * @param profesor Datos personales del profesor
	 * @return true si se ha actualizado correctamente y false sino
	 */
	public boolean actualizarDatosProfesor(AdultoBean profesor){
		boolean retorno = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		profesor.setTipoAdulto(AdultoDAO.PROFESOR);
		retorno = adultoDAO.actualizarAdulto(profesor);
		return retorno;
	}

	/**
	 * Añade un profesor a la clase
	 * @param clase Datos de la clase
	 * @param profesor Datos del profesor
	 * @return true si se ha añadido correctamente el profesor a la clase y false sino
	 */
	public boolean anadirProfesorAClase(ClaseBean clase, AdultoBean profesor) {
		boolean retorno = false;
		ClaseDAO claseDAO = DAOFactoria.getDAOFactoria().getClaseDAO();
		retorno = claseDAO.anadirProfesoraClaseActual(profesor, clase);
		return retorno;
	}

	/**
	 * Desvincula un profesor de la clase seleccionada
	 * @param clase Datos de la clase
	 * @param profesor Datos del profesor
	 * @return true si se ha eliminado correctamente el profesor de la clase y false sino
	 */
	public boolean eliminarProfesorDeClase(ClaseBean clase, AdultoBean profesor) {
		boolean retorno = false;
		ClaseDAO claseDAO = DAOFactoria.getDAOFactoria().getClaseDAO();
		retorno = claseDAO.eliminarProfesorDeClaseActual(profesor, clase);
		return retorno;
	}


	/**
	 * Añade los datos de un profesor
	 * @param profesor Datos del profesor
	 * @return true si se ha creado el profesor y false sino
	 */
	public boolean anadir(AdultoBean profesor) {
		boolean retorno = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		profesor.setTipoAdulto(AdultoDAO.PROFESOR);
		retorno = adultoDAO.anadirAdulto(profesor,AdultoDAO.PROFESOR);
		return retorno;
	}

	/**
	 * Busca todas las personas y las inserta en la lista
	 * @param personas Lista con los datos de todas las personas
	 */
	public boolean buscarTodasLasPersonas(ArrayList personas) {
		boolean resultado = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		resultado = adultoDAO.buscarTodos(personas);
		return resultado;
		
	}
	
}
