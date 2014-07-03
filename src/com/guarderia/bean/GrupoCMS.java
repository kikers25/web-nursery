package com.guarderia.bean;

/**
 * Contenedores de los elementos CMS
 * @author  Enrique Martín Martín
 */
public class GrupoCMS implements Bean {
	
	private static final long serialVersionUID = 1L;
	
	protected Integer identificador = null;
	protected String  nombre = null;
	protected String  path_contenido = null;
	
	/**
	 * @return
	 * @uml.property  name="identificador"
	 */
	public Integer getIdentificador() {
		return identificador;
	}
	/**
	 * @param identificador
	 * @uml.property  name="identificador"
	 */
	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}
	/**
	 * @return
	 * @uml.property  name="nombre"
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre
	 * @uml.property  name="nombre"
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return
	 * @uml.property  name="path_contenido"
	 */
	public String getPath_contenido() {
		return path_contenido;
	}
	/**
	 * @param path_contenido
	 * @uml.property  name="path_contenido"
	 */
	public void setPath_contenido(String path_contenido) {
		this.path_contenido = path_contenido;
	}
	
}
