package com.guarderia.bean;

import java.util.GregorianCalendar;

/**
 * Clase que implementa un Value Object relativo a un Adulto
 * @author  Enrique Martín Martín
 */
public class AdultoBean implements Bean {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1982015416059163770L;

	/**
	 * dni del adulto
	 * @uml.property  name="dni"
	 */
	protected String dni;
	
	/**
	 * Nombre del adulto
	 * @uml.property  name="nombre"
	 */
	protected String nombre;
	
	/**
	 * Primer Apellido del adulto
	 * @uml.property  name="primerApellido"
	 */
	protected String primerApellido;
	
	/**
	 * Segundo Apellido del adulto
	 * @uml.property  name="segundoApellido"
	 */
	protected String segundoApellido;
	
	/**
	 * Direccion en la que vive el adulto
	 * @uml.property  name="direccion"
	 */
	protected String direccion;
	
	/**
	 * Codigo postal de la dirección del adulto
	 * @uml.property  name="codigoPostal"
	 */
	protected Integer codigoPostal;
	
	/**
	 * Localidad en la que vive el adulto
	 * @uml.property  name="localidad"
	 */
	protected String localidad;
	
	/**
	 * Provincia en la que vive el adulto
	 * @uml.property  name="provincia"
	 */
	protected String provincia;
	
	/**
	 * Nación en la que ha nacido el adulto
	 * @uml.property  name="nacionalidad"
	 */
	protected String nacionalidad;
	
	/**
	 * Correo Electrónico del adulto
	 * @uml.property  name="correoElectronico"
	 */
	protected String correoElectronico;
	
	/**
	 * Fecha de nacimiento del adulto
	 * @uml.property  name="fechaNacimiento"
	 */
	protected GregorianCalendar fechaNacimiento;
	
	/**
	 * Nivel de estudios del adulto
	 * @uml.property  name="nivelEstudios"
	 */
	protected String nivelEstudios;
	
	/**
	 * Puesto de Trabajo en la guarderia si trabaja en ella
	 * @uml.property  name="tipoAdulto"
	 */
	protected String tipoAdulto;
	
	/**
	 * Teléfono de contacto del adulto
	 * @uml.property  name="telefono"
	 */
	protected Integer telefono;
	
	/**
	 * @return  the telefono
	 * @uml.property  name="telefono"
	 */
	public Integer getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono  the telefono to set
	 * @uml.property  name="telefono"
	 */
	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}

	/**
	 * Constructor por defecto
	 */
	public AdultoBean(){}
	
	/**
	 * @return  the codigoPostal
	 * @uml.property  name="codigoPostal"
	 */
	public Integer getCodigoPostal() {
		return codigoPostal;
	}

	/**
	 * @param codigoPostal  the codigoPostal to set
	 * @uml.property  name="codigoPostal"
	 */
	public void setCodigoPostal(Integer codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	/**
	 * @return  the correoElectronico
	 * @uml.property  name="correoElectronico"
	 */
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	/**
	 * @param correoElectronico  the correoElectronico to set
	 * @uml.property  name="correoElectronico"
	 */
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	/**
	 * @return  the direccion
	 * @uml.property  name="direccion"
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion  the direccion to set
	 * @uml.property  name="direccion"
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
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
	 * @return  the localidad
	 * @uml.property  name="localidad"
	 */
	public String getLocalidad() {
		return localidad;
	}

	/**
	 * @param localidad  the localidad to set
	 * @uml.property  name="localidad"
	 */
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	/**
	 * @return  the nacionalidad
	 * @uml.property  name="nacionalidad"
	 */
	public String getNacionalidad() {
		return nacionalidad;
	}

	/**
	 * @param nacionalidad  the nacionalidad to set
	 * @uml.property  name="nacionalidad"
	 */
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	/**
	 * @return  the nivelEstudios
	 * @uml.property  name="nivelEstudios"
	 */
	public String getNivelEstudios() {
		return nivelEstudios;
	}

	/**
	 * @param nivelEstudios  the nivelEstudios to set
	 * @uml.property  name="nivelEstudios"
	 */
	public void setNivelEstudios(String nivelEstudios) {
		this.nivelEstudios = nivelEstudios;
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
	 * @return  the provincia
	 * @uml.property  name="provincia"
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @param provincia  the provincia to set
	 * @uml.property  name="provincia"
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
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
	 * @return  the tipoAdulto
	 * @uml.property  name="tipoAdulto"
	 */
	public String getTipoAdulto() {
		return tipoAdulto;
	}

	/**
	 * @param tipoAdulto  the tipoAdulto to set
	 * @uml.property  name="tipoAdulto"
	 */
	public void setTipoAdulto(String tipoAdulto) {
		this.tipoAdulto = tipoAdulto;
	}
}
