package com.guarderia.DAO;

import java.util.ArrayList;

import com.guarderia.bean.UsuarioBean;

public interface UsuarioDAO extends DAO {

	/**
	 * Accede a base de datos para comprobar que el usuario y la contraseña que aparecen en el bean
	 * son correctas
	 * @param usuario Bean relativo a la tabla Usuario
	 * @return true si el usuario correcponde con el de base datos y false sino correcponde
	 */
	public abstract boolean validarUsuario(UsuarioBean usuario);

	/**
	 * Recupera los datos de un usuario
	 * @param usuario Datos del usuario
	 * @return true si se ha recuperado los datos del usuario y false sino
	 */
	public abstract boolean consultaUsuario(UsuarioBean usuario);

	/**
	 * Recupera los datos de todos los usuarios
	 * @param usuarios Datos de los usuarios
	 * @return true si la consulta se ha realizado correctamente y false sino
	 */
	public abstract boolean consultaUsuarios(ArrayList usuarios);

	/**
	 * Crea un usuario en la tabla usuario
	 * @param usuario Datos del usuario
	 * @return true si se ha creado el registro correctamente y false sino
	 */
	public abstract boolean crearUsuario(UsuarioBean usuario);

	/**
	 * Actualiza los datos del usuario en base de datos
	 * @param usuario Datos del usuario
	 * @return true si se ha actualizado el usuario y false sino
	 */
	public abstract boolean actualizarUsuario(UsuarioBean usuario);

	/**
	 * Elimina el usuario de la base de datos
	 * @param usuario Datos del usuario
	 * @return true si se ha eliminado correctamente y false sino
	 */
	public abstract boolean eliminarUsuario(UsuarioBean usuario);

}