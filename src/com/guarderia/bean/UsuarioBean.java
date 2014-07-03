package com.guarderia.bean;


/**
 * Clase que implementa un Value Object relativo al UsuarioBean
 * @author  Enrique Martín Martín
 */
public class UsuarioBean implements Bean {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4238723441527011700L;

	/**
	 * UsuarioBean
	 * @uml.property  name="usuario"
	 */
	protected String usuario;
	
	/**
	 * Contraseña del usuario
	 * @uml.property  name="contrasena"
	 */
	protected String contrasena;
	
	/**
	 * descripcion del usuario
	 * @uml.property  name="descripcion"
	 */
	protected String descripcion;

	/**
	 * dni del usuario
	 * @uml.property  name="dniUsuario"
	 */
	protected String dniUsuario;
	
	/**
	 * Identificador del Perfil al que corresponde el usuario
	 * @uml.property  name="identificadorPerfil"
	 */
	protected Integer identificadorPerfil;

	/**
	 * @return  the identificadorPerfil
	 * @uml.property  name="identificadorPerfil"
	 */
	public Integer getIdentificadorPerfil() {
		return identificadorPerfil;
	}

	/**
	 * @param identificadorPerfil  the identificadorPerfil to set
	 * @uml.property  name="identificadorPerfil"
	 */
	public void setIdentificadorPerfil(Integer identificadorPerfil) {
		this.identificadorPerfil = identificadorPerfil;
	}

	/**
	 * @return  the contraseña
	 * @uml.property  name="contrasena"
	 */
	public String getContrasena() {
		return contrasena;
	}

	/**
	 * @param contraseña  the contraseña to set
	 * @uml.property  name="contrasena"
	 */
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	/**
	 * @return  the descripcion
	 * @uml.property  name="descripcion"
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion  the descripcion to set
	 * @uml.property  name="descripcion"
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return  the usuario
	 * @uml.property  name="usuario"
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario  the usuario to set
	 * @uml.property  name="usuario"
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return  the dniUsuario
	 * @uml.property  name="dniUsuario"
	 */
	public String getDniUsuario() {
		return dniUsuario;
	}

	/**
	 * @param dniUsuario  the dniUsuario to set
	 * @uml.property  name="dniUsuario"
	 */
	public void setDniUsuario(String dniUsuario) {
		this.dniUsuario = dniUsuario;
	}


}
