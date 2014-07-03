package com.guarderia.bean;

import java.util.GregorianCalendar;

/**
 * Clase que implementa un Value Object relativo a una Comida
 * @author  Enrique Martín Martín
 */
public class MenuBean implements Bean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7257561727535910160L;

	/**
	 * Identificador de la comida
	 * @uml.property  name="identificador"
	 */
	protected Integer identificador = null;
	
	/**
	 * Primer plato de la comida
	 * @uml.property  name="primerPlato"
	 */
	protected String primerPlato = null;
	
	/**
	 * Segundo plato de la comida
	 * @uml.property  name="segundoPlato"
	 */
	protected String segundoPlato = null;
	
	/**
	 * Postre de la comida
	 * @uml.property  name="postre"
	 */
	protected String postre = null;
	
	/**
	 * Fecha en la que se realizará la comida
	 * @uml.property  name="fecha"
	 */
	protected GregorianCalendar fecha = null;

	/**
	 * Constructor por defecto
	 */
	public MenuBean(){}
	
	/**
	 * @return  the fecha
	 * @uml.property  name="fecha"
	 */
	public GregorianCalendar getFecha() {
		return fecha;
	}
	

	/**
	 * @param fecha  the fecha to set
	 * @uml.property  name="fecha"
	 */
	public void setFecha(GregorianCalendar fecha) {
		this.fecha = fecha;
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
	 * @return  the postre
	 * @uml.property  name="postre"
	 */
	public String getPostre() {
		return postre;
	}

	/**
	 * @param postre  the postre to set
	 * @uml.property  name="postre"
	 */
	public void setPostre(String postre) {
		this.postre = postre;
	}

	/**
	 * @return  the primerPlato
	 * @uml.property  name="primerPlato"
	 */
	public String getPrimerPlato() {
		return primerPlato;
	}

	/**
	 * @param primerPlato  the primerPlato to set
	 * @uml.property  name="primerPlato"
	 */
	public void setPrimerPlato(String primerPlato) {
		this.primerPlato = primerPlato;
	}

	/**
	 * @return  the segundoPlato
	 * @uml.property  name="segundoPlato"
	 */
	public String getSegundoPlato() {
		return segundoPlato;
	}

	/**
	 * @param segundoPlato  the segundoPlato to set
	 * @uml.property  name="segundoPlato"
	 */
	public void setSegundoPlato(String segundoPlato) {
		this.segundoPlato = segundoPlato;
	}

}
