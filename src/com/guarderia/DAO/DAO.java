package com.guarderia.DAO;

import com.guarderia.bean.Bean;

/**
 * Interfaz común que deben implementar todo objetos de tipo DAO
 * @author kike
 *
 */
public interface DAO {

	/**
	 * Crea una objeto 
	 * @param bean Datos del objeto a crear
	 */
	public abstract void crear (Bean bean);
	
	/**
	 * Modifica un objeto
	 * @param bean Datos del objetos
	 */
	public abstract void modificiar (Bean bean);
	
	/**
	 * Borrar un objeto a partir de su identificador
	 * @param identificador_bean Indentificador del objeto a eliminar
	 * @return El objeto borrado
	 */
	public abstract Bean borrar (Object identificador_bean);
	
	/**
	 * Buscar un objeto por su identificador
	 * @param identificador_bean Indentificador del objeto a buscar
	 * @return El bean con el identificador buscador o null
	 */
	public abstract Bean buscarPorId (Object identificador_bean);
	
}
