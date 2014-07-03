package com.guarderia.negocio;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.guarderia.DAO.AdultoDAO;
import com.guarderia.DAO.AlumnoDAO;
import com.guarderia.DAO.ClaseDAO;
import com.guarderia.DAO.DAOFactoria;
import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.AlumnoBean;
import com.guarderia.bean.ClaseBean;
import com.guarderia.bean.Intervalo_Edad;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase que realiza las operacion relativas a la clase
 * @author Enrique Martín Martín
 *
 */
public class GestionClase {
	
	/**
	 * Contructor por defecto
	 */
	public GestionClase(){
	}
	
	/**
	 * Busca los alumnos que pertenecen a una clase
	 * @param clase Datos de la clase
	 * @param alumnos Lista de alumnos que pertenece a la clase
	 * @return true si no ha habido ningún error y false sino
	 */
	public boolean buscarAlumnosDeClase(ClaseBean clase,ArrayList alumnos){
		boolean resultado = false;
		AlumnoDAO alumnoDAO = DAOFactoria.getDAOFactoria().getAlumnoDAO();
		resultado = alumnoDAO.consultarAlumnosClaseActual(clase, alumnos);
		return resultado;
	}
	
	/**
	 * Busca todas las clases de la guarderia
	 * @param clases Lista con las clases
	 * @return false si ha producido algún error y true sino
	 */
	public boolean buscarClases(ArrayList clases) {
		boolean retorno = false;
		ClaseDAO claseDAO = DAOFactoria.getDAOFactoria().getClaseDAO();
		retorno = claseDAO.consultaClases(clases);
		return retorno;
	}
	
	/**
	 * Añade el alumno seleccionada a la clase en el curso actual
	 * @param clase Datos de la clase
	 * @param alumno Datos del Alumno
	 * @return true si se ha añadido correctamente el alumno a la clase y false sino
	 */
	public boolean anadirAlumnoEnLaClase(ClaseBean clase, AlumnoBean alumno) {
		boolean retorno = false;
		ClaseDAO claseDAO = DAOFactoria.getDAOFactoria().getClaseDAO();
		retorno = claseDAO.AnadirAlumnoaClaseActual(alumno, clase);
		return retorno;
	}
	
	/**
	 * Elimina el alumno seleccionada de la clase en el curso actual
	 * @param clase Datos de la clase
	 * @param alumno Datos del Alumno
	 * @return true si se ha eliminado correctamente el alumno de la clase y false sino
	 */
	public boolean eliminarAlumnoDeClase(ClaseBean clase, AlumnoBean alumno) {
		boolean retorno = false;
		ClaseDAO claseDAO = DAOFactoria.getDAOFactoria().getClaseDAO();
		retorno = claseDAO.EliminarAlumnoDeClaseActual(alumno, clase);
		return retorno;
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
	 * Añade un clase
	 * @param clase Datos de la clase
	 * @return true si se ha creado correctamente y false sino
	 */
	public boolean anadirClase(ClaseBean clase) {
		boolean retorno = false;
		ClaseDAO claseDAO = DAOFactoria.getDAOFactoria().getClaseDAO();
		int identificador = claseDAO.consultaMayorIdentificador();
		identificador++;
		clase.setIdentificador(new Integer(identificador));
		retorno = claseDAO.anadirClase(clase);
		return retorno;
	}

	/**
	 * Recupera los datos de la clase de la peticion http
	 * @param request HttpServletRequest
	 * @return Bean con los datos de la clase
	 */
	public ClaseBean recuperarDatosClase(HttpServletRequest request) {
		ClaseBean clase = new ClaseBean();
		Intervalo_Edad intervalo_Edad = new Intervalo_Edad();
		String aux;
		
		aux = request.getParameter("identificador");
		clase.setIdentificador(UtilidadesGenerales.convertirStringEnInteger(aux));
		
		aux = request.getParameter("nombre") == null ? "" : (String)request.getParameter("nombre");
		clase.setNombre(aux);
		
		aux = request.getParameter("descripcion") == null ? "" : (String)request.getParameter("descripcion");
		clase.setDescripcion(aux);
		
		aux = request.getParameter("intervalo_Edad") == null ? "" : (String)request.getParameter("intervalo_Edad");
		intervalo_Edad.setIdentificador(UtilidadesGenerales.convertirStringEnInteger(aux));
		clase.setIntervaloEdad(intervalo_Edad);
		
		return clase;
	}
	
	/**
	 * Busca todos los intervalos de edad en que se dividen las clases
	 * @return Lista con los intervalores de edades
	 */
	public List buscarTodosLosIntervalosEdad(){
		ClaseDAO claseDAO = DAOFactoria.getDAOFactoria().getClaseDAO();
		return claseDAO.buscarTodosLosIntervalosEdad();
	}
	
}
