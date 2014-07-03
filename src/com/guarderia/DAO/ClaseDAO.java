package com.guarderia.DAO;

import java.util.ArrayList;
import java.util.List;

import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.AlumnoBean;
import com.guarderia.bean.ClaseBean;
import com.guarderia.bean.Intervalo_Edad;

public interface ClaseDAO extends DAO {

	/**
	 * Busca los datos de todas las clases
	 * @param clases Lista con todos los datos de las clases
	 * @return false si se ha producido algún error en la busqueda y true sino
	 */
	public abstract boolean consultaClases(ArrayList clases);

	/**
	 * Busca los datos de una clase a traves de su indentificador
	 * @param clase Bean de tipo clase
	 * @return true si ha encontrado la clase o false sino
	 */
	public abstract boolean consultaClase(ClaseBean clase);

	/**
	 * Busca la clase del alumno en el curso actual
	 * @param alumno Datos del alumno
	 * @param clase datos de la clase
	 * @return true si se ha encontrado la clase y false en cualquier otro caso
	 */
	public abstract boolean consultarClaseAlumnoActual(AlumnoBean alumno,
			ClaseBean clase);

	/**
	 * Consulta un registro de la tabla Clase de base de datos y si no lo encuentra convierte los datos en un bean
	 * @param clase Bean de tipo adulto con los datos del profesor
	 * @param clases Lista con las clases a las que tiene que ir el profesor en el curso actual
	 * @return false si ha habido un error en la obtención de los datos o true sino
	 */
	public abstract boolean consultaClasesProfesor(AdultoBean profesor,
			ArrayList clases);

	/**
	 * Realiza una consulta para obtener el mayor identificador dentro de los registro de la tabla Clase
	 * @return Numero correspondiente con el mayor identificador de los registros
	 */
	public abstract int consultaMayorIdentificador();

	/**
	 * Añade un registro a la tabla Clase
	 * @param clase Bean con los datos de la tabla a crear
	 * @return true si se ha añadido el registro y false sino
	 */
	public abstract boolean anadirClase(ClaseBean clase);

	/**
	 * Comprueba si el alumno pertenece a la clase en curso actual
	 * @param alumno Datos del alumno
	 * @param clase Datos de la clase
	 * @return true si el alumno pertenece a la clase y false sino
	 */
	public abstract boolean perteneceAlumnoaClaseActual(AlumnoBean alumno,
			ClaseBean clase);

	/**
	 * Añade el alumno a la clase en curso actual
	 * @param alumno Datos del alumno
	 * @param clase Datos de la clase
	 * @return true si se ha realizado la insercion correctamente y false sino
	 */
	public abstract boolean AnadirAlumnoaClaseActual(AlumnoBean alumno,
			ClaseBean clase);

	/**
	 * Eliminar el alumno a la clase en curso actual
	 * @param alumno Datos del alumno
	 * @param clase Datos de la clase
	 * @return true si se ha realizado la eliminacion correctamente y false sino
	 */
	public abstract boolean EliminarAlumnoDeClaseActual(AlumnoBean alumno,
			ClaseBean clase);

	/**
	 * Comprueba si el profesor pertenece a la clase en curso actual
	 * @param profesor Datos del profesor
	 * @param clase Datos de la clase
	 * @return true si el profesor pertenece a la clase y false sino
	 */
	public abstract boolean perteneceProfesoraClaseActual(AdultoBean profesor,
			ClaseBean clase);

	/**
	 * Añade el profesor a la clase en curso actual
	 * @param profesor Datos del profesor
	 * @param clase Datos de la clase
	 * @return true si se ha realizado la insercion correctamente y false sino
	 */
	public abstract boolean anadirProfesoraClaseActual(AdultoBean profesor,
			ClaseBean clase);

	/**
	 * Eliminar el profesor de la clase en curso actual
	 * @param profesor Datos del profesor
	 * @param clase Datos de la clase
	 * @return true si se ha realizado la eliminacion correctamente y false sino
	 */
	public abstract boolean eliminarProfesorDeClaseActual(AdultoBean profesor,
			ClaseBean clase);
	
	/**
	 * Obtiene los datos de un intervalo de edad
	 * @param identificador_bean
	 * @return intervalo de edad
	 */
	public abstract Intervalo_Edad buscarIntervaloEdad(Object identificador_bean);
	
	/**
	 * Obtiene todos lo intervalo de edad
	 * @return List de tipo intervalo de edad
	 */
	public abstract List buscarTodosLosIntervalosEdad();
	
	/**
	 * Busca las clases que hay para esa edad
	 * @param edad Edad expresada en un número entero
	 * @return Lista de clases por edad
	 */
	public abstract List buscarPorEdad(Integer edad);

}