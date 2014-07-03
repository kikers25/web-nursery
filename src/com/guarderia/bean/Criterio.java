package com.guarderia.bean;

/**
 * Criterios por los que son evaluados los alumnos
 * @author  Enrique Martín Martín
 */
public class Criterio implements Bean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	protected Integer identificador = null;
	protected String  nombre = null;
	/**
	 * @uml.property  name="bloque"
	 * @uml.associationEnd  
	 */
	protected Bloque bloque = null;
	/**
	 * @uml.property  name="area_conocimiento"
	 * @uml.associationEnd  
	 */
	protected Area_Conocimiento area_conocimiento = null;
	/**
	 * @uml.property  name="intervalo_edad"
	 * @uml.associationEnd  
	 */
	protected Intervalo_Edad intervalo_edad = null;
	
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
	 * @uml.property  name="bloque"
	 */
	public Bloque getBloque() {
		return bloque;
	}
	/**
	 * @param bloque
	 * @uml.property  name="bloque"
	 */
	public void setBloque(Bloque bloque) {
		this.bloque = bloque;
	}
	/**
	 * @return
	 * @uml.property  name="area_conocimiento"
	 */
	public Area_Conocimiento getArea_conocimiento() {
		return area_conocimiento;
	}
	/**
	 * @param area_conocimiento
	 * @uml.property  name="area_conocimiento"
	 */
	public void setArea_conocimiento(Area_Conocimiento area_conocimiento) {
		this.area_conocimiento = area_conocimiento;
	}
	/**
	 * @return
	 * @uml.property  name="intervalo_edad"
	 */
	public Intervalo_Edad getIntervalo_edad() {
		return intervalo_edad;
	}
	/**
	 * @param intervalo_edad
	 * @uml.property  name="intervalo_edad"
	 */
	public void setIntervalo_edad(Intervalo_Edad intervalo_edad) {
		this.intervalo_edad = intervalo_edad;
	}
		
}
