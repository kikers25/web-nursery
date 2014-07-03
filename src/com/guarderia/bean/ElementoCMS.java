package com.guarderia.bean;

import java.util.GregorianCalendar;

/**
 * Representa un elemento de tipo CMS
 * @author  Enrique Martín Martín
 */
public class ElementoCMS implements Bean {
	
	private static final long serialVersionUID = 1L;
	
	/** Tipo de elemento que define elementos de tipo Texto */
    public static final Integer ELEMENTO_PAGINA = 1;

    /** Tipo de elemento que define elementos de tipo Imagen */
    public static final Integer ELEMENTO_IMAGEN = 2;
    
    /** Tipo de elemento que define cualquier tipo de Fichero */
    public static final Integer ELEMENTO_FICHERO = 3;
	
	protected String  identificador = null;
	protected String  nombre = null;
	protected String  nombre_fichero = null;
	protected String 	descripcion = null;
	protected Integer tipo_elemento = null;
	protected String grupo = null;
	protected String 	usuario_alta = null;
	protected GregorianCalendar 	fecha_alta = null;
	protected String 	usuario_modificacion = null;
	protected GregorianCalendar 	fecha_modificacion = null;
	

	/**
	 * @return
	 * @uml.property  name="identificador"
	 */
	public String getIdentificador() {
		return identificador;
	}
	/**
	 * @param identificador
	 * @uml.property  name="identificador"
	 */
	public void setIdentificador(String identificador) {
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
	/**
	 * @return
	 * @uml.property  name="tipo_elemento"
	 */
	public Integer getTipo_elemento() {
		return tipo_elemento;
	}
	/**
	 * @param tipo_elemento
	 * @uml.property  name="tipo_elemento"
	 */
	public void setTipo_elemento(Integer tipo_elemento) {
		this.tipo_elemento = tipo_elemento;
	}
	/**
	 * @return
	 * @uml.property  name="grupo"
	 */
	public String getGrupo() {
		return grupo;
	}
	/**
	 * @param grupo
	 * @uml.property  name="grupo"
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	/**
	 * @return
	 * @uml.property  name="usuario_alta"
	 */
	public String getUsuario_alta() {
		return usuario_alta;
	}
	/**
	 * @param usuario_alta
	 * @uml.property  name="usuario_alta"
	 */
	public void setUsuario_alta(String usuario_alta) {
		this.usuario_alta = usuario_alta;
	}
	/**
	 * @return
	 * @uml.property  name="fecha_alta"
	 */
	public GregorianCalendar getFecha_alta() {
		return fecha_alta;
	}
	/**
	 * @param fecha_alta
	 * @uml.property  name="fecha_alta"
	 */
	public void setFecha_alta(GregorianCalendar fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	/**
	 * @return
	 * @uml.property  name="usuario_modificacion"
	 */
	public String getUsuario_modificacion() {
		return usuario_modificacion;
	}
	/**
	 * @param usuario_modificacion
	 * @uml.property  name="usuario_modificacion"
	 */
	public void setUsuario_modificacion(String usuario_modificacion) {
		this.usuario_modificacion = usuario_modificacion;
	}
	/**
	 * @return
	 * @uml.property  name="fecha_modificacion"
	 */
	public GregorianCalendar getFecha_modificacion() {
		return fecha_modificacion;
	}
	/**
	 * @param fecha_modificacion
	 * @uml.property  name="fecha_modificacion"
	 */
	public void setFecha_modificacion(GregorianCalendar fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}
	/**
	 * @return
	 * @uml.property  name="nombre_fichero"
	 */
	public String getNombre_fichero() {
		return nombre_fichero;
	}
	/**
	 * @param nombre_fichero
	 * @uml.property  name="nombre_fichero"
	 */
	public void setNombre_fichero(String nombre_fichero) {
		this.nombre_fichero = nombre_fichero;
	}
	
}
