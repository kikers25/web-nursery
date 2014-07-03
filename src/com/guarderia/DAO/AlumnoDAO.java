package com.guarderia.DAO;

import java.util.ArrayList;

import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.ClaseBean;

public interface AlumnoDAO extends DAO {

	/**
	 * Consulta los Hijos que tiene un padre en la guarderia en el curso actual
	 * @param adulto Bean con los datos del padre
	 * @param listaAlumnos Lista con los alumnos que van a la guarderia y cuyo tutor es el adulto
	 * @return false si ha habido algún error y true sino
	 */
	public abstract boolean consultarHijosPadre(AdultoBean padre,
			ArrayList listaHijos);

	/**
	 * Busca todos los alumnos existentes en base de datos
	 * @param alumnos Lista con todos los alumnos hallados
	 * @return true si se ha realizado la consulta correctamente y false sino
	 */
	public abstract boolean buscarTodosAlumnos(ArrayList alumnos);

	/**
	 * Consulta todos los alumnos que hay en una clase
	 * @param clase Datos de la clase
	 * @param alumnos Lista de alumnos de la clase
	 * @return true si se ha realizado correctamente la consulta y false si ha 
	 * habido algún error
	 */
	public abstract boolean consultarAlumnosClaseActual(ClaseBean clase,
			ArrayList alumnos);

	/**
	 * Busca en los registros de la tabla alumno el identificador mayor
	 * @return el mayor numero de identificador o -1 si no hay registros
	 */
	public abstract int buscarMaximoIdentificador();

}