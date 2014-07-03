package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.Bean;
import com.guarderia.bean.Evaluacion;
import com.guarderia.utils.UtilidadesGenerales;

public class OracleEvaluacionDAO implements EvaluacionDAO {

	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	private final static String CONSULTA_ALUMNO_TIENE_EVALUACION = "select count(*) as "+ UtilidadesGenerales.CAMPO_COUNT_RESULSET + " from evaluacion where ALUMNO = ? and ADULTO = ? and CURSO = ? and TRIMESTRE = ?";
	private final static String CONSULTA_EVALUACIONES_ALUMNO = "select * from evaluacion where ALUMNO = ? and ADULTO = ? and CURSO = ? and TRIMESTRE = ? order by criterio";
	private final static String CONSULTA = "select * from evaluacion where identificador = ?";
	private final static String CREAR = "insert into evaluacion (identificador, curso, trimestre, alumno, " +
			"adulto, criterio, valor, descripcion) VALUES (?,?,?,?,?,?,?,?)";
	private final static String MODIFICAR = "update evaluacion set identificador = ?, curso = ?, trimestre = ?, alumno = ?, " +
			"adulto = ?, criterio = ?, valor = ?, descripcion = ? where identificador = ?";
	private final static String MODIFICAR2 = "update evaluacion set valor = ?, descripcion = ? " +
	"  where curso = ? and trimestre = ? and alumno = ? and adulto = ? and criterio = ?";
	private final static String ELIMINAR = "delete from evaluacion where identificador = ?";
	
	@Override
	public Bean borrar(Object identificador_bean) {
		Evaluacion evaluacion = null;
		if ( identificador_bean instanceof Integer ){
			evaluacion = (Evaluacion) buscarPorId(identificador_bean);
			if (evaluacion != null){
				AccesoDatos ad = new AccesoDatos();
				ad.conexionBDPool();
				ArrayList lista = new ArrayList(1);
				lista.add(identificador_bean);
				if (ad.realizarActualizacionParametrizada(ELIMINAR, lista) ){
					if (ad.devolverNumeroActualizaciones() != 1)
						log.error("No se ha eliminado la evaluacion con id: " + identificador_bean);
				}
				ad.desconexionBD();
			}
			else{
				log.error("No existe el criterio a borrar: " + identificador_bean);
			}
		}
		return evaluacion;
	}

	@Override
	public Bean buscarPorId(Object identificador_bean) {
		Evaluacion evaluacion = null;
		if ( identificador_bean instanceof Integer ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add(identificador_bean);
			if (ad.realizarConsultaParametrizada(CONSULTA, lista) ){
				ResultSet rs = ad.devolverConsulta();
				evaluacion = convertirResultSetEnBean (rs);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return evaluacion;
	}

	private Evaluacion convertirResultSetEnBean(ResultSet rs) {
		Evaluacion evaluacion = null;
		try {
			if (rs.next() && rs != null){
				evaluacion = new Evaluacion ();
				evaluacion.setIdentificador(rs.getInt("identificador"));
				evaluacion.setCurso(rs.getString("curso"));
				evaluacion.setTrimestre(rs.getInt("trimestre"));
				evaluacion.setAlumno(rs.getInt("alumno"));
				evaluacion.setAdulto(rs.getString("adulto"));
				evaluacion.setCriterio(rs.getInt("criterio"));
				evaluacion.setValor(rs.getString("valor"));
				evaluacion.setDescripcion(rs.getString("descripcion"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return evaluacion;
	}

	@Override
	public void crear(Bean bean) {
		if ( bean instanceof Evaluacion ){
			Evaluacion evaluacion = (Evaluacion) bean;
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(8);
			lista.add(evaluacion.getIdentificador());
			lista.add(evaluacion.getCurso());
			lista.add(evaluacion.getTrimestre());
			lista.add(evaluacion.getAlumno());
			lista.add(evaluacion.getAdulto());
			lista.add(evaluacion.getCriterio());
			lista.add(evaluacion.getValor());
			lista.add(evaluacion.getDescripcion());
			if (ad.realizarActualizacionParametrizada(CREAR, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1){
					log.error("No se ha creado la evaluacion del alumno: " + evaluacion.getAlumno());
					log.error("Para el criterio con id " + evaluacion.getCriterio());
				}
			}
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if ( bean instanceof Evaluacion ){
			Evaluacion evaluacion = (Evaluacion) bean;
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(9);
			lista.add(evaluacion.getIdentificador());
			lista.add(evaluacion.getCurso());
			lista.add(evaluacion.getTrimestre());
			lista.add(evaluacion.getAlumno());
			lista.add(evaluacion.getAdulto());
			lista.add(evaluacion.getCriterio());
			lista.add(evaluacion.getValor());
			lista.add(evaluacion.getDescripcion());
			lista.add(evaluacion.getIdentificador());
			if (ad.realizarActualizacionParametrizada(MODIFICAR, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1){
					log.error("No se ha creado la evaluacion del alumno: " + evaluacion.getAlumno());
					log.error("Para el criterio con id " + evaluacion.getCriterio());
				}
			}
			ad.desconexionBD();
		}
	}
	
	/**
	 * Busca si el alumno está evaluado en el trimestre correspondiente. 
	 * @param idAlumno Identifidcador del alumno
	 * @param idProfesor Identificador del profesor
	 * @param Curso Curso (año-año)
	 * @param Trimestre Número del trimestre entre 1 y 3
	 * @return true si existe la evaluación o false sino
	 */
	public boolean isEvaluacionAlumnoDeProfesor (Integer idAlumno, String idProfesor, String Curso, Integer Trimestre){
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		ad.conexionBDPool();
		ArrayList lista = new ArrayList(4);
		lista.add(idAlumno);
		lista.add(idProfesor);
		lista.add(Curso);
		lista.add(Trimestre);
		if (ad.realizarConsultaParametrizada(CONSULTA_ALUMNO_TIENE_EVALUACION, lista) ){
			ResultSet rs = ad.devolverConsulta();
			resultado = UtilidadesGenerales.isResulSetConResultado(rs);
			UtilidadesGenerales.cerrarResulset(rs);
		}
		ad.desconexionBD();
		return resultado;
	}
	
	/**
	 * Obtiene las evaluaciones del alumno en el trimestre correspondiente. Si el trimestre vale null en 
	 * cualquier trimestre del curso
	 * @param idAlumno Identifidcador del alumno
	 * @param idProfesor Identificador del profesor
	 * @param Curso Curso (año-año)
	 * @param Trimestre Número del trimestre entre 1 y 3 o null
	 * @return true si existe la evaluación o false sino
	 */
	public List obtenerEvaluacionesAlumnoDeProfesor (Integer idAlumno, String idProfesor, String Curso, Integer Trimestre){
		List resultado = new ArrayList();
		AccesoDatos ad = new AccesoDatos();
		ad.conexionBDPool();
		ArrayList lista = new ArrayList(4);
		lista.add(idAlumno);
		lista.add(idProfesor);
		lista.add(Curso);
		lista.add(Trimestre);
		if (ad.realizarConsultaParametrizada(CONSULTA_EVALUACIONES_ALUMNO, lista) ){
			ResultSet rs = ad.devolverConsulta();
			Evaluacion evaluacion = null;
			do{
				evaluacion = this.convertirResultSetEnBean(rs); 
				if (evaluacion != null)
					resultado.add( evaluacion );
			}while (evaluacion != null);
			UtilidadesGenerales.cerrarResulset(rs);
		}
		ad.desconexionBD();
		return resultado;
	}

	public void modificiar2(Evaluacion evaluacion) {
		AccesoDatos ad = new AccesoDatos();
		ad.conexionBDPool();
		ArrayList lista = new ArrayList(7);
		lista.add(evaluacion.getValor());
		lista.add(evaluacion.getDescripcion());
		lista.add(evaluacion.getCurso());
		lista.add(evaluacion.getTrimestre());
		lista.add(evaluacion.getAlumno());
		lista.add(evaluacion.getAdulto());
		lista.add(evaluacion.getCriterio());
		if (ad.realizarActualizacionParametrizada(MODIFICAR2, lista) ){
			if (ad.devolverNumeroActualizaciones() != 1){
				log.error("No se ha creado la evaluacion del alumno: " + evaluacion.getAlumno());
				log.error("Para el criterio con id " + evaluacion.getCriterio());
			}
		}
		ad.desconexionBD();
	}

}
