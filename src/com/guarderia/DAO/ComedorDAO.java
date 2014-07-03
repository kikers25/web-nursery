package com.guarderia.DAO;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.guarderia.bean.MenuBean;

public interface ComedorDAO extends DAO{

	/**
	 * Realiza una consulta sobre si hay una comida en cierta fecha
	 * @param comida Datos de la comida
	 * @return true si se ha encontrado la comida en esa fecha y false en culaquier otro caso
	 */
	public abstract boolean consultaEnFecha(MenuBean comida);

	/**
	 * Lista las comidas que hay entre las fecha seleccionadas
	 * @param listaComidas Listado con las
	 * @param calendarioInicio
	 * @param calendarioFin
	 * @return true si se ha realizado correctamente la consulta o false sino
	 */
	public abstract boolean consultaEntreFechas(ArrayList listaComidas,
			GregorianCalendar calendarioInicio, GregorianCalendar calendarioFin);

	/**
	 * Realiza una consulta para obtener el mayor identificador dentro de los registro de la tabla Menu
	 * @return Numero correspondiente con el mayor identificador de los registros
	 */
	public abstract int consultaMayorIdentificador();

	/**
	 * Crea en base de datos un registro con los datos del menú del dia
	 * @param comida
	 * @return true si se ha creado correctamente el registro con los datos del menu 
	 * y false en cualquier otro caso
	 */
	public abstract boolean crearMenu(MenuBean comida);

	/**
	 * Modifica un registro de la tabla Menu
	 * @param menu Datos del menu a modificar
	 * @return true si se ha modificado correctamente y false sino
	 */
	public abstract boolean modificarMenu(MenuBean menu);

	/**
	 * Elimina un registro de la tabla Menu
	 * @param menu Datos del registro a eliminar
	 * @return true si se ha eliminado el registro y false sino
	 */
	public abstract boolean eliminarMenu(MenuBean menu);

}