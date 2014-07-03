package com.guarderia.DAO;

import com.guarderia.bean.PerfilBean;

public interface PerfilDAO extends DAO {

	/**
	 * Hace una comprobacion de la existencia del perfil seleccionado en base de datos
	 * @param perfil Bean relativo al perfil que se rellenará con los datos obtenidos de la
	 * base de datos si se encuentra en ella.
	 * @return true si se ha encontrado
	 */
	public abstract boolean consultaPerfil(PerfilBean perfil);

}