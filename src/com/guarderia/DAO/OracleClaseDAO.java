package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.AlumnoBean;
import com.guarderia.bean.Bean;
import com.guarderia.bean.ClaseBean;
import com.guarderia.bean.Intervalo_Edad;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase para el acceso a la base de datos y mas especificamente a la tabla Clase
 * @author Enrique Martín Martín
 */
public class OracleClaseDAO implements ClaseDAO {

	private final static String CONSULTA_CLASES_PROFESOR_CURSO_ACTUAL = "SELECT * FROM CLASE WHERE IDENTIFICADOR IN (SELECT IDENTIFICADOR_CLASE FROM ADULTO_ENSENA_CLASE WHERE DNI_ADULTO LIKE ? AND CURSO LIKE ?)";
	private final static String CONSULTA_CLASE_ALUMNO_CURSO_ACTUAL = "SELECT * FROM CLASE WHERE IDENTIFICADOR = (SELECT IDENTIFICADOR_CLASE FROM ALUMNO_PERTENECE_CLASE WHERE IDENTIFICADOR_ALUMNO = ? AND CURSO LIKE ?)";
	private final static String CONSULTA_CLASES = "SELECT * FROM CLASE ORDER BY NOMBRE";
	private final static String CONSULTA_ALUMNO_PERTENECE_CLASE_ACTUAL = "SELECT * FROM ALUMNO_PERTENECE_CLASE WHERE IDENTIFICADOR_ALUMNO = ? AND IDENTIFICADOR_CLASE = ? AND CURSO = ?";
	private final static String CONSULTA_PROFESOR_PERTENECE_CLASE_ACTUAL = "SELECT * FROM ADULTO_ENSENA_CLASE WHERE DNI_ADULTO = ? AND IDENTIFICADOR_CLASE = ? AND CURSO = ?";
	private final static String ELIMINAR_ALUMNO_PERTENECE_CLASE_ACTUAL = "DELETE FROM ALUMNO_PERTENECE_CLASE WHERE IDENTIFICADOR_ALUMNO = ? AND IDENTIFICADOR_CLASE = ? AND CURSO = ?";
	private final static String ELIMINAR_PROFESOR_PERTENECE_CLASE_ACTUAL = "DELETE FROM ADULTO_ENSENA_CLASE WHERE DNI_ADULTO = ? AND IDENTIFICADOR_CLASE = ? AND CURSO = ?";
	private final static String CREAR_ALUMNO_PERTENECE_CLASE_ACTUAL = "INSERT INTO ALUMNO_PERTENECE_CLASE (IDENTIFICADOR_ALUMNO, IDENTIFICADOR_CLASE, CURSO ) VALUES (?,?,?)";
	private final static String CREAR_PROFESOR_PERTENECE_CLASE_ACTUAL = "INSERT INTO ADULTO_ENSENA_CLASE (DNI_ADULTO, IDENTIFICADOR_CLASE, CURSO) VALUES (?,?,?)";
	private final static String CONSULTA_MAXIMO_IDENTIFICADOR_CLASE = "SELECT MAX(IDENTIFICADOR) FROM CLASE";
	private final static String CONSULTA_CLASE = "SELECT * FROM CLASE WHERE IDENTIFICADOR = ?";
	private final static String INSERTAR_CLASE = "INSERT INTO CLASE (IDENTIFICADOR,NOMBRE,DESCRIPCION,IDENTIFICADOR_INTERVALO) values (?,?,?,?)";
	private final static String ELIMINAR_CLASE = "DELETE FROM CLASE WHERE IDENTIFICADOR = ?";
	private final static String MODIFICAR_CLASE = "UPDATE FROM CLASE SET IDENTIFICADOR = ?,NOMBRE = ?,DESCRIPCION = ?, IDENTIFICADOR_INTERVALO = ? WHERE IDENTIFICADOR = ?";
	private final static String CONSULTA_INTERVALO_EDAD = "SELECT * FROM INTERVALO_EDAD WHERE IDENTIFICADOR = ?";
	private final static String CONSULTA_TODOS_LOS_INTERVALO_EDAD = "SELECT * FROM INTERVALO_EDAD";
	private final static String CONSULTA_CLASES_POSIBLES_PARA_ALUMNO = "select c.* from clase c, intervalo_edad e where c.identificador_intervalo = e.identificador and e.inicio <= ? and e.fin > ?";
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Constructor por defecto que inicializa los atributos
	 */
	public OracleClaseDAO(){
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#consultaClases(java.util.ArrayList)
	 */
	public boolean consultaClases(ArrayList clases){
		boolean retorno = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(0);
			if (ad.realizarConsultaParametrizada(CONSULTA_CLASES, lista)){
				retorno = true;
				ResultSet rs = ad.devolverConsulta();
				clases.clear();
				boolean bAux = true;
				while (bAux == true){
					ClaseBean clase = new ClaseBean();
					bAux = convertirResultSetEnBean(rs, clase);
					if (bAux){
						log.info("Se ha obtenido el dato de una clase " + clase.getNombre());
						clases.add(clase);
					}
				}
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				clases.clear();
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#consultaClase(com.guarderia.bean.ClaseBean)
	 */
	public boolean consultaClase(ClaseBean clase){
		boolean retorno = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(clase.getIdentificador());
			if (ad.realizarConsultaParametrizada(CONSULTA_CLASE, lista)){
				ResultSet rs = ad.devolverConsulta();
				retorno = convertirResultSetEnBean(rs, clase);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				clase = null;
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#consultarClaseAlumnoActual(com.guarderia.bean.AlumnoBean, com.guarderia.bean.ClaseBean)
	 */
	public boolean consultarClaseAlumnoActual(AlumnoBean alumno,ClaseBean clase){
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(2);
			lista.add(alumno.getIdentificador());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			log.info(CONSULTA_CLASE_ALUMNO_CURSO_ACTUAL);
			if (ad.realizarConsultaParametrizada(CONSULTA_CLASE_ALUMNO_CURSO_ACTUAL, lista)){
				ResultSet rs = ad.devolverConsulta();
				resultado = convertirResultSetEnBean(rs, clase);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				clase = null;
			}
			ad.desconexionBD();
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#consultaClasesProfesor(com.guarderia.bean.AdultoBean, java.util.ArrayList)
	 */
	public boolean consultaClasesProfesor(AdultoBean profesor,ArrayList clases){
		boolean retorno = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(2);
			lista.add(profesor.getDni());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			if (ad.realizarConsultaParametrizada(CONSULTA_CLASES_PROFESOR_CURSO_ACTUAL, lista)){
				log.info("Realiza consulta");
				ResultSet rs = ad.devolverConsulta();
				retorno = true;
				clases.clear();
				boolean bAux = true;
				while (bAux == true){
					ClaseBean clase = new ClaseBean();
					bAux = convertirResultSetEnBean(rs, clase);
					if (bAux){
						log.info("Se ha obtenido el dato de una clase");
						clases.add(clase);
					}
				}
				UtilidadesGenerales.cerrarResulset(rs);
				ad.desconexionBD();
			}
			else{
				clases = new ArrayList();
			}
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#consultaMayorIdentificador()
	 */
	public int consultaMayorIdentificador(){
		int max = -1;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(0);
			log.info(CONSULTA_MAXIMO_IDENTIFICADOR_CLASE);
			if (ad.realizarConsultaParametrizada(CONSULTA_MAXIMO_IDENTIFICADOR_CLASE, lista)){
				ResultSet rs = ad.devolverConsulta();
				try {
					if (rs.next()){
						max = rs.getInt(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ad.desconexionBD();
		}
		return max;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#anadirClase(com.guarderia.bean.ClaseBean)
	 */
	public boolean anadirClase(ClaseBean clase){
		boolean retorno =  false;
		int iRegistros;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(4);
			lista.add(clase.getIdentificador());
			lista.add(clase.getNombre());
			lista.add(clase.getDescripcion());
			lista.add(clase.getIntervaloEdad().getIdentificador());
			if (ad.realizarActualizacionParametrizada(INSERTAR_CLASE, lista) ){
				log.info("Actualizacion: " + INSERTAR_CLASE );
				iRegistros = ad.devolverNumeroActualizaciones();
				log.info("Numero de registro Actualizacos: " + iRegistros);
				if (iRegistros == 1) retorno = true;
			}
			else{
				log.info("Error al actualizacion la tabla Clase");
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#perteneceAlumnoaClaseActual(com.guarderia.bean.AlumnoBean, com.guarderia.bean.ClaseBean)
	 */
	public boolean perteneceAlumnoaClaseActual(AlumnoBean alumno,ClaseBean clase){
		boolean retorno =  false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(3);
			lista.add(alumno.getIdentificador());
			lista.add(clase.getIdentificador());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			log.info(CONSULTA_ALUMNO_PERTENECE_CLASE_ACTUAL);
			if (ad.realizarConsultaParametrizada(CONSULTA_ALUMNO_PERTENECE_CLASE_ACTUAL, lista)){
				ResultSet rs = ad.devolverConsulta();
				if (hayDatosResultSet(rs) == false)retorno = true;
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#AnadirAlumnoaClaseActual(com.guarderia.bean.AlumnoBean, com.guarderia.bean.ClaseBean)
	 */
	public boolean AnadirAlumnoaClaseActual(AlumnoBean alumno,ClaseBean clase){
		boolean retorno =  false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(3);
			lista.add(alumno.getIdentificador());
			lista.add(clase.getIdentificador());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			log.info(CREAR_ALUMNO_PERTENECE_CLASE_ACTUAL);
			if (ad.realizarActualizacionParametrizada(CREAR_ALUMNO_PERTENECE_CLASE_ACTUAL, lista)){
				int n = ad.devolverNumeroActualizaciones();
				if (n == 1){
					retorno = true;
					log.info("Se ha añadido el alumno a la clase actual ");
				}
				else log.info("NO Se ha añadido el alumno a la clase actual ");
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#EliminarAlumnoDeClaseActual(com.guarderia.bean.AlumnoBean, com.guarderia.bean.ClaseBean)
	 */
	public boolean EliminarAlumnoDeClaseActual(AlumnoBean alumno,ClaseBean clase){
		boolean retorno =  false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(3);
			lista.add(alumno.getIdentificador());
			lista.add(clase.getIdentificador());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			log.info(ELIMINAR_ALUMNO_PERTENECE_CLASE_ACTUAL);
			if (ad.realizarActualizacionParametrizada(ELIMINAR_ALUMNO_PERTENECE_CLASE_ACTUAL, lista)){
				int n = ad.devolverNumeroActualizaciones();
				if (n == 1){
					retorno = true;
					log.info("Se ha eliminado el alumno de la clase actual ");
				}
				else log.info("NO Se ha eliminado el alumno de la clase actual ");
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#perteneceProfesoraClaseActual(com.guarderia.bean.AdultoBean, com.guarderia.bean.ClaseBean)
	 */
	public boolean perteneceProfesoraClaseActual(AdultoBean profesor,ClaseBean clase){
		boolean retorno =  false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(3);
			lista.add(profesor.getDni());
			lista.add(clase.getIdentificador());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			log.info(CONSULTA_PROFESOR_PERTENECE_CLASE_ACTUAL);
			if (ad.realizarConsultaParametrizada(CONSULTA_PROFESOR_PERTENECE_CLASE_ACTUAL, lista)){
				ResultSet rs = ad.devolverConsulta();
				if (hayDatosResultSet(rs) == false)retorno = true;
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#anadirProfesoraClaseActual(com.guarderia.bean.AdultoBean, com.guarderia.bean.ClaseBean)
	 */
	public boolean anadirProfesoraClaseActual(AdultoBean profesor,ClaseBean clase){
		boolean retorno =  false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(3);
			lista.add(profesor.getDni());
			lista.add(clase.getIdentificador());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			log.info(CREAR_PROFESOR_PERTENECE_CLASE_ACTUAL);
			if (ad.realizarActualizacionParametrizada(CREAR_PROFESOR_PERTENECE_CLASE_ACTUAL, lista)){
				int n = ad.devolverNumeroActualizaciones();
				if (n == 1){
					retorno = true;
					log.info("Se ha añadido el profesor a la clase actual ");
				}
				else log.info("NO Se ha añadido el profesor a la clase actual ");
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#eliminarProfesorDeClaseActual(com.guarderia.bean.AdultoBean, com.guarderia.bean.ClaseBean)
	 */
	public boolean eliminarProfesorDeClaseActual(AdultoBean profesor,ClaseBean clase){
		boolean retorno =  false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(3);
			lista.add(profesor.getDni());
			lista.add(clase.getIdentificador());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			log.info(ELIMINAR_PROFESOR_PERTENECE_CLASE_ACTUAL);
			if (ad.realizarActualizacionParametrizada(ELIMINAR_PROFESOR_PERTENECE_CLASE_ACTUAL, lista)){
				int n = ad.devolverNumeroActualizaciones();
				if (n == 1){
					retorno = true;
					log.info("Se ha eliminado el profesor de la clase actual ");
				}
				else log.info("NO Se ha eliminado el profesor de la clase actual ");
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#hayDatosResultSet(java.sql.ResultSet)
	 */
	public boolean hayDatosResultSet(ResultSet conjunto){
		boolean retorno = false;
		try {
			if (conjunto.next() == false){
				retorno = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ClaseDAO#convertirResultSetEnBean(java.sql.ResultSet, com.guarderia.bean.ClaseBean)
	 */
	public boolean convertirResultSetEnBean(ResultSet conjunto,ClaseBean clase) {
		boolean retorno = false;
		String aux;
		int i=1;
		try {
			if(conjunto.next()){
				aux = conjunto.getString(i++);
				clase.setIdentificador(UtilidadesGenerales.convertirStringEnInteger(aux));
				aux = conjunto.getString(i++);
				clase.setNombre(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				clase.setDescripcion(UtilidadesGenerales.obtenerString(aux));
				Integer entero = conjunto.getInt("IDENTIFICADOR_INTERVALO");
				if (entero != null){
					Intervalo_Edad intervalo = buscarIntervaloEdad (entero);
					clase.setIntervaloEdad(intervalo);
				}
				retorno = true;
			}
			else{
				log.info("No hay datos en el ResultSet para convertirlo en Bean de tipo Clase");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorno;
	}

	@Override
	public Bean borrar(Object identificador_bean) {
		ClaseBean clase = null;
		if ( identificador_bean instanceof String ){
			clase = (ClaseBean) buscarPorId (identificador_bean);
			if (clase != null){
				AccesoDatos ad = new AccesoDatos();
				ArrayList lista = new ArrayList(3);
				lista.add(clase.getIdentificador());
				lista.add(clase.getNombre());
				lista.add(clase.getDescripcion());
				if (ad.realizarActualizacionParametrizada(ELIMINAR_CLASE, lista) ){
					if (ad.devolverNumeroActualizaciones() == 1)
						log.info("Registro de clase borrado");
				}
				ad.desconexionBD();
			}
		}
		return clase;
	}

	@Override
	public Bean buscarPorId(Object identificador_bean) {
		ClaseBean clase = null;
		if ( identificador_bean instanceof String ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add((String) identificador_bean);
			if (ad.realizarConsultaParametrizada(CONSULTA_CLASE, lista)){
				ResultSet rs = ad.devolverConsulta();
				convertirResultSetEnBean(rs, clase);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				clase = null;
			}
			ad.desconexionBD();
		}
		return clase;
	}

	@Override
	public void crear(Bean bean) {
		if ( bean instanceof ClaseBean ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ClaseBean clase = new ClaseBean ();
			ArrayList lista = new ArrayList(3);
			lista.add(clase.getIdentificador());
			lista.add(clase.getNombre());
			lista.add(clase.getDescripcion());
			if (ad.realizarActualizacionParametrizada(INSERTAR_CLASE, lista) ){
				log.info("Actualizacion: " + INSERTAR_CLASE );
				if (ad.devolverNumeroActualizaciones() == 1)
					log.info("Registro insertado");
			}
			else{
				log.info("Error al actualizacion la tabla Clase");
			}
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if ( bean instanceof ClaseBean ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ClaseBean clase = new ClaseBean ();
			ArrayList lista = new ArrayList(4);
			lista.add(clase.getIdentificador());
			lista.add(clase.getNombre());
			lista.add(clase.getDescripcion());
			lista.add(clase.getIntervaloEdad().getIdentificador());
			lista.add(clase.getIdentificador());
			if (ad.realizarActualizacionParametrizada(MODIFICAR_CLASE, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1)
					log.error("No se ha podido modificar la clase "+ clase.getIdentificador());
			}
			ad.desconexionBD();
		}
	}
	
	@Override
	public Intervalo_Edad buscarIntervaloEdad(Object identificador_bean){
		Intervalo_Edad intervalo = null;
		if ( identificador_bean instanceof Integer ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add((Integer) identificador_bean);
			intervalo = (Intervalo_Edad) ad.realizarConsultaParametrizada(CONSULTA_INTERVALO_EDAD, lista,Intervalo_Edad.class);
			ad.desconexionBD();
		}
		return intervalo;
	}
	
	@Override
	public List buscarTodosLosIntervalosEdad(){
		List listaResultado = null;
		AccesoDatos ad = new AccesoDatos();
		ad.conexionBDPool();
		ArrayList lista = new ArrayList(0);
		listaResultado = ad.realizarConsultaListaParametrizada(CONSULTA_TODOS_LOS_INTERVALO_EDAD, lista,Intervalo_Edad.class);
		ad.desconexionBD();
		return listaResultado;
	}

	@Override
	public List buscarPorEdad(Integer edad) {
		List listaResultado = null;
		AccesoDatos ad = new AccesoDatos();
		ad.conexionBDPool();
		ArrayList lista = new ArrayList(2);
		lista.add((Integer)edad);
		lista.add((Integer)edad);
		ad.realizarConsultaParametrizada(CONSULTA_CLASES_POSIBLES_PARA_ALUMNO, lista);
		ResultSet rs = ad.devolverConsulta();
		listaResultado = new ArrayList<ClaseBean>();
		boolean bAux = true;
		while (bAux == true){
			ClaseBean clase = new ClaseBean();
			bAux = convertirResultSetEnBean(rs, clase);
			if (bAux){
				log.debug("Se ha obtenido el dato de una clase " + clase.getNombre());
				listaResultado.add(clase);
			}
		}
		UtilidadesGenerales.cerrarResulset(rs);
		ad.desconexionBD();
		return listaResultado;
	}
	
}
