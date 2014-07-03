package com.guarderia.DAO;

import java.util.ArrayList;

import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.ClaseBean;

public interface AdultoDAO extends DAO{

	/**
	 * Adulto de tipo profesor
	 */
	public final static String PROFESOR = "Profesor";
	/**
	 * Adulto de tipo padre
	 */
	public final static String PADRE = "Padre";
	/**
	 * Adulto de tipo Director
	 */
	public final static String DIRECTOR = "Director";

	/**
	 * Busca en la base de datos la información de un adulto
	 * @param adulto Bean con los datos del Adulto
	 * @return true si todo ha ido correctamente o false sino
	 */
	public abstract boolean buscarAdulto(AdultoBean adulto);

	/**
	 * Busca un tipo especifico de adulto
	 * @param tipo Tipo de Adulto (Profesor, Padre, Director...)
	 * @param listaAdultos Lista de adultos que corresponden con el criterio
	 * @return false si se ha producido un error en la consulta y true sino
	 */
	public abstract boolean buscarTipoAdulto(String tipo, ArrayList listaAdultos);

	/**
	 * Busca todos los adultos de la base de datos
	 * @param personas Lista con los datos de todos los adultos
	 * @return true si se ha realizado correctamente la busqueda y false sino
	 */
	public abstract boolean buscarTodos(ArrayList personas);

	/**
	 * Busca los adultos que imparten en una determinada clase del curso actual
	 * @param clase Datos de la clase
	 * @param profesores Lista con los adultos que dan clase al alumno
	 * @return true si se ha realizado correctamente la consulta y false si ha habido algún error
	 */
	public abstract boolean buscarAdultosDanClaseCursoActual(ClaseBean clase,
			ArrayList profesores);

	/**
	 * Actualiza los datos personales del adulto
	 * @param clase Bean con los datos del adulto
	 * @return true si se ha actualizado los datos y false sino
	 */
	public abstract boolean actualizarAdulto(AdultoBean adulto);

	/**
	 * A
	 * @param adulto Datos del adulto
	 * @param tipoDeAdulto Tipo de adulto a añadir
	 * @return true si se ha añadido correctamente a la base de datos y false sino
	 */
	public abstract boolean anadirAdulto(AdultoBean adulto, String tipoDeAdulto);

}