package com.guarderia.bean;

/**
 * Resultado de la evaluacion de los criterios en cada alumno en un curso y en un trimestre
 * @author  Enrique Martín Martín
 */
public class Evaluacion implements Bean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public static final Integer SATISFACTORIA = 0;
	public static final Integer PROGRESA = 1;
	public static final Integer NECESITA_PRACTICA = 2;
	public static final Integer NO_EVALUADO = 3;
	
	public static final String [] VALORES = {"S","P","NP","NE"};
	public static final String [] TEXTOS = {"Satisfactorio","Progresa","Necesita más práctica","No evaluado"};
	
	protected Integer identificador = null;
	protected String  curso = null;
	protected Integer trimestre = null;
	protected Integer alumno = null;
	protected String  adulto = null;
	protected Integer criterio = null;
	protected String  valor = null;
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
	 * @uml.property  name="curso"
	 */
	public String getCurso() {
		return curso;
	}
	/**
	 * @param curso
	 * @uml.property  name="curso"
	 */
	public void setCurso(String curso) {
		this.curso = curso;
	}
	/**
	 * @return
	 * @uml.property  name="trimestre"
	 */
	public Integer getTrimestre() {
		return trimestre;
	}
	/**
	 * @param trimestre
	 * @uml.property  name="trimestre"
	 */
	public void setTrimestre(Integer trimestre) {
		this.trimestre = trimestre;
	}
	/**
	 * @return
	 * @uml.property  name="alumno"
	 */
	public Integer getAlumno() {
		return alumno;
	}
	/**
	 * @param alumno
	 * @uml.property  name="alumno"
	 */
	public void setAlumno(Integer alumno) {
		this.alumno = alumno;
	}
	/**
	 * @return
	 * @uml.property  name="adulto"
	 */
	public String getAdulto() {
		return adulto;
	}
	/**
	 * @param adulto
	 * @uml.property  name="adulto"
	 */
	public void setAdulto(String adulto) {
		this.adulto = adulto;
	}
	/**
	 * @return
	 * @uml.property  name="criterio"
	 */
	public Integer getCriterio() {
		return criterio;
	}
	/**
	 * @param criterio
	 * @uml.property  name="criterio"
	 */
	public void setCriterio(Integer criterio) {
		this.criterio = criterio;
	}
	/**
	 * @return
	 * @uml.property  name="valor"
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor
	 * @uml.property  name="valor"
	 */
	public void setValor(String valor) {
		this.valor = valor;
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
