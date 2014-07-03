package com.guarderia.bean;

/**
 * Intervalo de edades de los alumnos
 * @author Enrique Martín Martín
 */
public class Intervalo_Edad implements Bean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2965276389625883938L;
	
	public static final Integer DE_CERO_A_UNO 	= 4;
	public static final Integer DE_UNO_A_DOS 	= 5;
	public static final Integer DE_DOS_A_TRES 	= 7;
	public static final Integer DE_TRES_A_SEIS	= 6;
	
	/**
	 * Idenficador del Intervalo de edad
	 * @uml.property  name="identificador"
	 */
	protected Integer identificador = null;
	
	/**
	 * Años expresado en número con los que se comienza el intervalo. No está incluido
	 * @uml.property  name="inicio"
	 */
	protected Integer inicio = null;
	
	/**
	 * Años expresado en número con los que se finaliza el intervalo. Está incluido
	 * @uml.property  name="fin"
	 */
	protected Integer fin = null;
	
	/**
	 * Descripción
	 * @uml.property  name="descripcion"
	 */
	protected String descripcion = null;
	
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
	 * @uml.property  name="inicio"
	 */
	public Integer getInicio() {
		return inicio;
	}

	/**
	 * @param inicio
	 * @uml.property  name="inicio"
	 */
	public void setInicio(Integer inicio) {
		this.inicio = inicio;
	}

	/**
	 * @return
	 * @uml.property  name="fin"
	 */
	public Integer getFin() {
		return fin;
	}

	/**
	 * @param fin
	 * @uml.property  name="fin"
	 */
	public void setFin(Integer fin) {
		this.fin = fin;
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
