package com.guarderia.bean;

import java.util.GregorianCalendar;
import java.sql.Timestamp;

/**
 * Clase que implementa un Value Object relativo a una Noticia
 * @author  Enrique Martín Martín
 */
public class NoticiaBean implements Bean {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8371891873327293289L;

	/**
	 * Identificador de la noticia
	 * @uml.property  name="identificador"
	 */
	protected Integer identificador;
	
	/**
	 * La noticia se muestra en la web si el valor de este atributo es true
	 * @uml.property  name="habilitado"
	 */
	protected Boolean habilitado;
	
	/**
	 * Descripcion de la noticia
	 * @uml.property  name="descripcion"
	 */
	protected String descripcion;
	
	/**
	 * Ruta hacia la imagen de la noticia
	 * @uml.property  name="foto"
	 */
	protected String foto;
	
	/**
	 * Titulo de la noticia
	 * @uml.property  name="titulo"
	 */
	protected String titulo;
	
	/**
	 * Subtitulo de la noticia
	 * @uml.property  name="subtitulo"
	 */
	protected String subtitulo;
	
	/**
	 * Resumen de la noticia
	 * @uml.property  name="resumen"
	 */
	protected String resumen;
	
	/**
	 * Fecha que debe mostrar la noticia
	 * @uml.property  name="fecha"
	 */
	protected GregorianCalendar fecha;
	
	/**
	 * Fecha en la que se creó la noticia
	 * @uml.property  name="fechaCreacion"
	 */
	protected Timestamp fechaCreacion;
	
	/**
	 * Fecha de la última modificacion de la noticia
	 * @uml.property  name="fechaModificacion"
	 */
	protected Timestamp fechaModificacion;

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
	 * @return  the fechaCreacion
	 * @uml.property  name="fechaCreacion"
	 */
	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion  the fechaCreacion to set
	 * @uml.property  name="fechaCreacion"
	 */
	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return  the fechaModificacion
	 * @uml.property  name="fechaModificacion"
	 */
	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}

	/**
	 * @param fechaModificacion  the fechaModificacion to set
	 * @uml.property  name="fechaModificacion"
	 */
	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	/**
	 * @return  the foto
	 * @uml.property  name="foto"
	 */
	public String getFoto() {
		return foto;
	}

	/**
	 * @param foto  the foto to set
	 * @uml.property  name="foto"
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	 * @return  the habilitado
	 * @uml.property  name="habilitado"
	 */
	public Boolean getHabilitado() {
		return habilitado;
	}

	/**
	 * @param habilitado  the habilitado to set
	 * @uml.property  name="habilitado"
	 */
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
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
	 * @return  the resumen
	 * @uml.property  name="resumen"
	 */
	public String getResumen() {
		return resumen;
	}

	/**
	 * @param resumen  the resumen to set
	 * @uml.property  name="resumen"
	 */
	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	/**
	 * @return  the subtitulo
	 * @uml.property  name="subtitulo"
	 */
	public String getSubtitulo() {
		return subtitulo;
	}

	/**
	 * @param subtitulo  the subtitulo to set
	 * @uml.property  name="subtitulo"
	 */
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	/**
	 * @return  the titulo
	 * @uml.property  name="titulo"
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo  the titulo to set
	 * @uml.property  name="titulo"
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

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

}

