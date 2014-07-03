/**
 * 
 */
package com.guarderia.negocio;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.guarderia.DAO.AdultoDAO;
import com.guarderia.DAO.AlumnoDAO;
import com.guarderia.DAO.ClaseDAO;
import com.guarderia.DAO.DAOFactoria;
import com.guarderia.bean.AlumnoBean;
import com.guarderia.bean.ClaseBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase que realiza las operacion relativas al alumno
 * @author Enrique Martín Martín
 *
 */
public class GestionAlumno {
	
	/**
	 * Contructor por defecto
	 */
	public GestionAlumno(){
	}
	
	/**
	 * Busca los profesores del alumno y los guarda en una lista
	 * @param alumno Datos del alumno 
	 * @param profesores Lista con los profesores del alumno
	 * @return true si no ha habido ningún error en la busqueda y false sino 
	 */
	public boolean buscarProfesoresDelAlumno(AlumnoBean alumno,ArrayList profesores){
		DAOFactoria factoria = DAOFactoria.getDAOFactoria();
		ClaseDAO claseDAO = factoria.getClaseDAO();
		AdultoDAO adultoDAO = factoria.getAdultoDAO();
		ClaseBean clase = new ClaseBean();
		boolean resultado = false;
		resultado = claseDAO.consultarClaseAlumnoActual(alumno, clase);
		if (resultado) resultado = adultoDAO.buscarAdultosDanClaseCursoActual(clase,profesores);
		return resultado;
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
	 * Busca todos los alumnos
	 * @param alumnos Lista con todos los alumnos
	 * @return true si no ha habido ningún error y false sino
	 */
	public boolean buscarAlumnos(ArrayList alumnos){
		boolean resultado = false;
		AlumnoDAO alumnoDAO = DAOFactoria.getDAOFactoria().getAlumnoDAO();
		resultado = alumnoDAO.buscarTodosAlumnos(alumnos);
		return resultado;
	}
	
	/**
	 * Convierte los datos de la peticion html en un objeto de tipo bean alumno
	 * @param request Petiticion de usuario
	 * @return Objeto de tipo alumno o el objeto vacio si ha habido algun error
	 */
	public AlumnoBean recuperaDatosAlumno(HttpServletRequest request){
		AlumnoBean alumno = new AlumnoBean();
		String aux;
		
		aux = request.getParameter("identificador") == null ? "" : (String)request.getParameter("identificador");
		alumno.setIdentificador(UtilidadesGenerales.convertirStringEnInteger(aux));
		
		aux = request.getParameter("dni") == null ? "" : (String)request.getParameter("dni");
		alumno.setDni(aux);
		
		aux = request.getParameter("nombre") == null ? "" : (String)request.getParameter("nombre");
		alumno.setNombre(aux);
		
		aux = request.getParameter("primerApellido") == null ? "" : (String)request.getParameter("primerApellido");
		alumno.setPrimerApellido(aux);
		
		aux = request.getParameter("segundoApellido") == null ? "" : (String)request.getParameter("segundoApellido");
		alumno.setSegundoApellido(aux);
		
		aux = request.getParameter("alergias") == null ? "" : (String)request.getParameter("alergias");
		alumno.setAlergias(aux);
		
		aux = request.getParameter("observaciones") == null ? "" : (String)request.getParameter("observaciones");
		alumno.setObservaciones(aux);
		
		//aux = request.getParameter("dniTutor1") == null ? "" : (String)request.getParameter("dniTutor1");
		aux = request.getParameter("dniTutor1");
		alumno.setDniTutor1(aux);
		
		//aux = request.getParameter("dniTutor2") == null ? "" : (String)request.getParameter("dniTutor2");
		aux = request.getParameter("dniTutor2");
		alumno.setDniTutor2(aux);
		
		aux = request.getParameter("fechaNacimiento") == null ? "" : (String)request.getParameter("fechaNacimiento");
		alumno.setFechaNacimiento(UtilidadesGenerales.convertirStringEnGregorianCalendar(aux));
		
		return alumno;
	}
	
	/**
	 * Actualiza los datos personales del alumno
	 * @param padre Datos personales del alumno ha actualizar
	 * @return true si se ha actualizado correctamente y false sino
	 */
	public boolean actualizarDatosAlumno(AlumnoBean alumno){
		boolean retorno = true;
		AlumnoDAO alumnoDAO = DAOFactoria.getDAOFactoria().getAlumnoDAO();
		alumnoDAO.modificiar(alumno);
		return retorno;
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
	 * Añade un alumno vinculándolo con su tutor y con una clase de forma automática
	 * @param alumno Datos del alumno
	 * @return true si se ha podido añadir el alumno y false sino 
	 */
	public boolean anadirAlumno(AlumnoBean alumno) {
		boolean retorno = true;
		AlumnoDAO alumnoDAO = DAOFactoria.getDAOFactoria().getAlumnoDAO();
		ClaseDAO claseDAO = DAOFactoria.getDAOFactoria().getClaseDAO();
		/* Añadir de forma automática alumno a clase*/
		/* 1. Comprobamos los años que tiene el alumno*/
		Integer edad = UtilidadesGenerales.calcularEdad(alumno.getFechaNacimiento());
		/* 2. Comprobamos en las clases en las que puede asistir */
		List clasesPosibles = claseDAO.buscarPorEdad(edad);
		/* 3. Si hay más de una se recupera la que tenga menos alumnos*/
		ClaseBean clase = null;
		if (clasesPosibles.size() > 1){
			int numAlumnos = 100000;
			for (int i=0; i<clasesPosibles.size(); i++){
				ClaseBean auxClase = (ClaseBean) clasesPosibles.get(i);
				ArrayList auxAlumnos = new ArrayList<AlumnoBean>();
				alumnoDAO.consultarAlumnosClaseActual(auxClase, auxAlumnos);
				if (auxAlumnos.size() < numAlumnos){
					clase  = auxClase;
					numAlumnos = auxAlumnos.size(); 
				}
			}
		}
		else{
			clase = (ClaseBean) clasesPosibles.get(0);
		}
		/* 4. Se añade el alumno */
		int id = alumnoDAO.buscarMaximoIdentificador() + 1 ;
		alumno.setIdentificador(new Integer(id));
		alumnoDAO.crear(alumno);
		/* 5. Se le vincula a la clase */
		claseDAO.AnadirAlumnoaClaseActual(alumno, clase);
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
}
