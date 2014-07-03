package com.guarderia.bean;



/**
 * Clase que implementa un Value Object relativo a un Perfil
 * @author  Enrique Martín Martín
 */
public class PerfilBean implements Bean {
	
	public static final Integer ADMINISTRADOR = 1;
	public static final Integer PROFESOR = 2;
	public static final Integer PADRE = 3;
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4034293625940964996L;

	/**
	 * Identificador del perfil
	 * @uml.property  name="identificador"
	 */
	protected Integer identificador;
	
	/**
	 * Nombre del perfil
	 * @uml.property  name="nombre"
	 */
	protected String nombre;
	
	/**
	 * Descripción del perfil
	 * @uml.property  name="descripcion"
	 */
	protected String descripcion;
	
	/**
	 * Nivel de prioridad del perfil
	 * @uml.property  name="nivelPrioridad"
	 */
	protected Integer nivelPrioridad;

	/**
	 * Constrcutor Por defecto
	 */
	public PerfilBean(){}
	
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
	 * @return  the identificador
	 * @uml.property  name="identificador"
	 */
	public Integer getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador  the identificador to set
	 * @uml.property  name="identificador"
	 */
	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return  the nivelPrioridad
	 * @uml.property  name="nivelPrioridad"
	 */
	public Integer getNivelPrioridad() {
		return nivelPrioridad;
	}

	/**
	 * @param nivelPrioridad  the nivelPrioridad to set
	 * @uml.property  name="nivelPrioridad"
	 */
	public void setNivelPrioridad(Integer nivelPrioridad) {
		this.nivelPrioridad = nivelPrioridad;
	}

	/**
	 * @return  the nombre
	 * @uml.property  name="nombre"
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre  the nombre to set
	 * @uml.property  name="nombre"
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
