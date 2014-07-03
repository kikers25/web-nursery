package com.guarderia.bean;

/**
 * División de los criterios
 * @author  Enrique Martín Martín
 */
public class Bloque implements Bean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	protected Integer identificador = null;
	protected String  nombre = null;
	protected String  descripcion = null;
	
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
	 * @uml.property  name="descripcion"
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion
	 * @uml.property  name="descripcion"
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
