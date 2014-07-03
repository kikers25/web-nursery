package com.guarderia.servlet.menuConfiguracion;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.guarderia.error.XMLException;

public interface Propiedades {

	public static final String CAMPO_TITULO = "titulo";
	public static final String CAMPO_VISTA = "vista";
	public static final String CAMPO_FICHERO_REDIRECCION = "fichero_redireccion";
	public static final String CAMPO_IDENTIFICADOR = "opcion";
	public static final String IDENTIFICADOR_ERROR_POR_DEFECTO = "error_por_defecto";
	public static final String IDENTIFICADOR_DEFECTO = "defecto";
	public static final String IDENTIFICADOR_PAGINA_INICIO = "0";
	
	/**
	 * Selecciona el fichero xml donse se buscará
	 * @param file Ruta al fichero
	 * @throws FileNotFoundException
	 * @throws XMLException
	 */
	public abstract void setFile(String file) throws FileNotFoundException;

	/**
	 * Selecciona el fichero xml donse se buscará
	 * @param file InputStream
	 * @throws FileNotFoundException
	 * @throws XMLException
	 */
	public abstract void setFile(InputStream file) throws FileNotFoundException;
	
	/**
	 * Obtiene el valor de una accion
	 * @param identificador identificador de la accion
	 * @param campo campo de la accion
	 * @return Valor
	 */
	public abstract String get (String identificador, String campo);

}