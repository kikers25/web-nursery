package com.guarderia.DAO;

import java.util.List;

public interface ElementoCMSDAO extends DAO {

	/**
	 * Lista los elementos de un directorio CMS
	 * @param identificador_directorio Id del directorio CMS
	 * @return Lista de elementos CMS o null si no hay 
	 */
	public List buscarElementosPorDirectorio(String identificador_directorio);
	
	/**
	 * Lista los elementos de un directorio CMS que son de un tipo de elemento en especial
	 * @param identificador_directorio Id del directorio CMS
	 * @param tipo_elemento el tipo de elemento a buscar
	 * @return Lista de elementos CMS o null si no hay 
	 */
	public List buscarElementosPorDirectorioyTipo(String identificador_directorio, Integer tipo_elemento);
}
