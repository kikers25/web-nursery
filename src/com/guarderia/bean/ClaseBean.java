package com.guarderia.bean;


/**
 * Clase que implementa un Value Object relativo a una Aula
 * @author  Enrique Martín Martín
 */
public class ClaseBean implements Bean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2220925596658272807L;

	/**
	 * Identificador del aula
	 * @uml.property  name="identificador"
	 */
	protected Integer identificador = null;
	
	/**
	 * Intervalo de edad en la clase
	 * @uml.property  name="intervalo_Edad"
	 * @uml.associationEnd  
	 */
	protected Intervalo_Edad intervalo_Edad = null;
	
	/**
	 * Nombre del aula
	 * @uml.property  name="nombre"
	 */
	protected String nombre = null;
	
	/**
	 * Descripción del aula
	 * @uml.property  name="descripcion"
	 */
	protected String descripcion = null;
	
	/**
	 * Constructor por defecto
	 */
	public ClaseBean(){}
	
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
	
	public Intervalo_Edad getIntervaloEdad (){
		return this.intervalo_Edad;
	}
	
	public void setIntervaloEdad (Intervalo_Edad intervalo){
		this.intervalo_Edad = intervalo;
	}
	
	public String getDescripcionIntervalo(){
		if (intervalo_Edad != null){
			return intervalo_Edad.getDescripcion();
		}
		else{
			return null;
		}
	}
}
