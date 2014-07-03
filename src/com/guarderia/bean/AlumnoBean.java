package com.guarderia.bean;

import java.util.GregorianCalendar;

/**
 * Clase que implementa un Value Object relativo a un Alumno
 * @author  Enrique Martín Martín
 */
public class AlumnoBean implements Bean {
		
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7343385557473706835L;

	/**
	 * Identificador del alumno
	 * @uml.property  name="identificador"
	 */
	protected Integer identificador;
	
	/**
	 * dni del alumno
	 * @uml.property  name="dni"
	 */
	protected String dni;
	
	/**
	 * Nombre del alumno
	 * @uml.property  name="nombre"
	 */
	protected String nombre;
	
	/**
	 * Primer Apellido del alumno
	 * @uml.property  name="primerApellido"
	 */
	protected String primerApellido;
	
	/**
	 * Segundo Apellido del alumno
	 * @uml.property  name="segundoApellido"
	 */
	protected String segundoApellido;
	
	/**
	 * Alegias que tiene el alumno
	 * @uml.property  name="alergias"
	 */
	protected String alergias;
	
	/**
	 * Observaciones relativas al alumno
	 * @uml.property  name="observaciones"
	 */
	protected String observaciones;
	
	/**
	 * dni del primero de sus tutores
	 * @uml.property  name="dniTutor1"
	 */
	protected String dniTutor1;
	
	/**
	 * dni del segundo de sus tutores
	 * @uml.property  name="dniTutor2"
	 */
	protected String dniTutor2;
	
	/**
	 * Fecha de nacimiento del alumno
	 * @uml.property  name="fechaNacimiento"
	 */
	protected GregorianCalendar fechaNacimiento;

	/**
	 * @return  the fechaNacimiento
	 * @uml.property  name="fechaNacimiento"
	 */
	public GregorianCalendar getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento  the fechaNacimiento to set
	 * @uml.property  name="fechaNacimiento"
	 */
	public void setFechaNacimiento(GregorianCalendar fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	/**
	 * @return  the alergias
	 * @uml.property  name="alergias"
	 */
	public String getAlergias() {
		return alergias;
	}

	/**
	 * @param alergias  the alergias to set
	 * @uml.property  name="alergias"
	 */
	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}

	/**
	 * @return  the dni
	 * @uml.property  name="dni"
	 */
	public String getDni() {
		return dni;
	}

	/**
	 * @param dni  the dni to set
	 * @uml.property  name="dni"
	 */
	public void setDni(String dni) {
		this.dni = dni;
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

	/**
	 * @return  the observaciones
	 * @uml.property  name="observaciones"
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones  the observaciones to set
	 * @uml.property  name="observaciones"
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return  the primerApellido
	 * @uml.property  name="primerApellido"
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}

	/**
	 * @param primerApellido  the primerApellido to set
	 * @uml.property  name="primerApellido"
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * @return  the segundoApellido
	 * @uml.property  name="segundoApellido"
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}

	/**
	 * @param segundoApellido  the segundoApellido to set
	 * @uml.property  name="segundoApellido"
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * @return  the dniTutor1
	 * @uml.property  name="dniTutor1"
	 */
	public String getDniTutor1() {
		return dniTutor1;
	}

	/**
	 * @param dniTutor1  the dniTutor1 to set
	 * @uml.property  name="dniTutor1"
	 */
	public void setDniTutor1(String dniTutor1) {
		this.dniTutor1 = dniTutor1;
	}

	/**
	 * @return  the dniTutor2
	 * @uml.property  name="dniTutor2"
	 */
	public String getDniTutor2() {
		return dniTutor2;
	}

	/**
	 * @param dniTutor2  the dniTutor2 to set
	 * @uml.property  name="dniTutor2"
	 */
	public void setDniTutor2(String dniTutor2) {
		this.dniTutor2 = dniTutor2;
	}
	
}
