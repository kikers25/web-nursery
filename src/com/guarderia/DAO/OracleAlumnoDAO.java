package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.AlumnoBean;
import com.guarderia.bean.Bean;
import com.guarderia.bean.ClaseBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase para el acceso a la base de datos y mas especificamente a la tabla Alumno
 * @author Enrique Martín Martín
 *
 */
public class OracleAlumnoDAO implements AlumnoDAO {
	
	private final static String CONSULTA_ALUMNO = "SELECT * FROM ALUMNO WHERE IDENTIFICADOR = ?";
	private final static String CONSULTA_HIJOS_PADRE_CURSO_ACTUAL = "SELECT * FROM ALUMNO WHERE (DNI_TUTOR1 LIKE ? OR DNI_TUTOR2 LIKE ?) AND IDENTIFICADOR IN (SELECT IDENTIFICADOR_ALUMNO FROM ALUMNO_PERTENECE_CLASE WHERE CURSO LIKE ?)";
	private final static String ACTUALIZAR_ALUMNO = "UPDATE ALUMNO SET IDENTIFICADOR = ?, DNI = ?, NOMBRE = ?, PRIMER_APELLIDO = ?, SEGUNDO_APELLIDO = ?, ALERGIAS = ?, OBSERVACIONES = ?, DNI_TUTOR1 = ?, DNI_TUTOR2 = ?, FECHA_NACIMIENTO = ? WHERE IDENTIFICADOR = ?";
	private final static String CONSULTA_ALUMNOS_CLASE_CURSO_ACTUAL = "SELECT * FROM ALUMNO WHERE IDENTIFICADOR IN (SELECT IDENTIFICADOR_ALUMNO FROM ALUMNO_PERTENECE_CLASE WHERE IDENTIFICADOR_CLASE = ? AND CURSO LIKE ?) ORDER BY PRIMER_APELLIDO, SEGUNDO_APELLIDO, NOMBRE";
	private final static String CONSULTA_MAXIMO_IDENTIFICADOR_ALUMNO = "SELECT MAX(IDENTIFICADOR) FROM ALUMNO";
	private final static String CREAR_ALUMNO = "INSERT INTO ALUMNO (IDENTIFICADOR,DNI,NOMBRE,PRIMER_APELLIDO,SEGUNDO_APELLIDO,ALERGIAS,OBSERVACIONES,DNI_TUTOR1,DNI_TUTOR2,FECHA_NACIMIENTO) values (?,?,?,?,?,?,?,?,?,?)";
	private final static String CONSULTA_ALUMNOS = "SELECT * FROM ALUMNO";
	private final static String ELIMINAR = "delete from alumno where identificador = ?";
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Constructor por defecto que inicializa los atributos
	 *
	 */
	public OracleAlumnoDAO(){
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AlumnoDAO#consultarHijosPadre(com.guarderia.bean.AdultoBean, java.util.ArrayList)
	 */
	public boolean consultarHijosPadre(AdultoBean padre,ArrayList listaHijos){
		boolean retorno = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(3);
			lista.add(padre.getDni());
			lista.add(padre.getDni());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			log.info(CONSULTA_HIJOS_PADRE_CURSO_ACTUAL);
			if (ad.realizarConsultaParametrizada(CONSULTA_HIJOS_PADRE_CURSO_ACTUAL, lista)){
				ResultSet rs = ad.devolverConsulta();
				retorno = true;
				listaHijos.clear();
				AlumnoBean alumno = null;
				do{
					alumno = convertirResultSetEnBean(rs);
					if (alumno != null){
						log.info("Se ha obtenido el dato de un hijo: " + alumno.getNombre() + " " + alumno.getPrimerApellido() + " " + alumno.getSegundoApellido() + " ");
						listaHijos.add(alumno);
					}
				}while (alumno != null);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				listaHijos.clear();
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AlumnoDAO#buscarTodosAlumnos(java.util.ArrayList)
	 */
	public boolean buscarTodosAlumnos(ArrayList alumnos){
		boolean retorno = false;
		ArrayList lista = new ArrayList(0);
		log.info(CONSULTA_ALUMNOS);
		AccesoDatos ad = new AccesoDatos();
		ad.conexionBDPool();
		if (ad.realizarConsultaParametrizada(CONSULTA_ALUMNOS, lista)){
			ResultSet rs = ad.devolverConsulta();
			retorno = true;
			alumnos.clear();
			AlumnoBean alumno = null;
			do{
				alumno = convertirResultSetEnBean(rs);
				if (alumno != null){
					log.info("Se ha obtenido el dato de un alumno: " + alumno.getNombre() + " " + alumno.getPrimerApellido() + " " + alumno.getSegundoApellido() + " ");
					alumnos.add(alumno);
				}
			}while (alumno != null);
			UtilidadesGenerales.cerrarResulset(rs);
		}
		else{
			alumnos.clear();
		}
		ad.desconexionBD();
		return retorno;
	}

	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AlumnoDAO#consultarAlumnosClaseActual(com.guarderia.bean.ClaseBean, java.util.ArrayList)
	 */
	public boolean consultarAlumnosClaseActual(ClaseBean clase, ArrayList alumnos){
		boolean retorno = false;
		AccesoDatos ad = new AccesoDatos();
		ad.conexionBDPool();
		ArrayList lista = new ArrayList(2);
		lista.add(clase.getIdentificador());
		lista.add(UtilidadesGenerales.obtenerCursoActual());
		log.info(CONSULTA_ALUMNOS_CLASE_CURSO_ACTUAL);
		if (ad.realizarConsultaParametrizada(CONSULTA_ALUMNOS_CLASE_CURSO_ACTUAL, lista)){
			ResultSet rs = ad.devolverConsulta();
			retorno = true;
			alumnos.clear();
			AlumnoBean alumno = null;
			do{
				alumno = convertirResultSetEnBean(rs);
				if (alumno != null){
					log.info("Se ha obtenido el dato de un alumno: " + alumno.getNombre() + " " + alumno.getPrimerApellido() + " " + alumno.getSegundoApellido() + " ");
					alumnos.add(alumno);
				}
			}while (alumno != null);
			UtilidadesGenerales.cerrarResulset(rs);
		}
		else{
			alumnos.clear();
		}
		ad.desconexionBD();
		return retorno;
	}

	/**
	 * Covierte un resulset en un objeto de tipo alumno
	 * @param conjunto
	 * @param alumno
	 */
	public AlumnoBean convertirResultSetEnBean(ResultSet conjunto) {
		AlumnoBean alumno = null;
		int i = 1;
		String aux;
		try {
			if(conjunto.next()){
				alumno = new AlumnoBean();
				aux = conjunto.getString(i++);
				alumno.setIdentificador(Integer.valueOf(UtilidadesGenerales.obtenerString(aux)));
				aux = conjunto.getString(i++);
				alumno.setDni(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				alumno.setNombre(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				alumno.setPrimerApellido(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				alumno.setSegundoApellido(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				alumno.setAlergias(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				alumno.setObservaciones(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				alumno.setDniTutor1(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				alumno.setDniTutor2(UtilidadesGenerales.obtenerString(aux));
				alumno.setFechaNacimiento(UtilidadesGenerales.convertirDateEnGregorianCalendar(conjunto.getDate(i++)));
			}
			else{
				log.info("No hay datos en el ResultSet para convertirlo en Bean de tipo Alumno");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alumno;
	}

	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AlumnoDAO#buscarMaximoIdentificador()
	 */
	public int buscarMaximoIdentificador() {
		int max = -1;
		AccesoDatos ad = new AccesoDatos();
		ad.conexionBDPool();
		ArrayList lista = new ArrayList(0);
		log.info(CONSULTA_MAXIMO_IDENTIFICADOR_ALUMNO);
		if (ad.realizarConsultaParametrizada(CONSULTA_MAXIMO_IDENTIFICADOR_ALUMNO, lista)){
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
		return max;
	}

	@Override
	public Bean borrar(Object identificador_bean) {
		AlumnoBean alumno = null;
		if (identificador_bean instanceof Integer){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			Integer identificadorAlumno = (Integer) identificador_bean;
			alumno =(AlumnoBean) this.buscarPorId(identificadorAlumno);
			if (alumno != null){
				ArrayList lista = new ArrayList(0);
				ad.realizarActualizacionParametrizada(ELIMINAR, lista);
			}
			ad.desconexionBD();
		}
		return alumno;
	}

	@Override
	public Bean buscarPorId(Object identificador_bean) {
		AlumnoBean alumno = null;
		if (identificador_bean instanceof Integer){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add((Integer) identificador_bean);
			log.info( CONSULTA_ALUMNO );
			if (ad.realizarConsultaParametrizada(CONSULTA_ALUMNO, lista) ){
				ResultSet rs = ad.devolverConsulta();
				alumno = convertirResultSetEnBean(rs);
			}
			else{
				log.info("Error al realizar la consulta");
			}
			ad.desconexionBD();
		}
		return alumno;
	}

	@Override
	public void crear(Bean bean) {
		if (bean instanceof AlumnoBean){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			AlumnoBean alumno = (AlumnoBean)bean;
			ArrayList lista = new ArrayList();
			lista.add(alumno.getIdentificador());
			lista.add(alumno.getDni());
			lista.add(alumno.getNombre());
			lista.add(alumno.getPrimerApellido());
			lista.add(alumno.getSegundoApellido());
			lista.add(alumno.getAlergias());
			lista.add(alumno.getObservaciones());
			lista.add(alumno.getDniTutor1());
			lista.add(alumno.getDniTutor2());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(alumno.getFechaNacimiento()));
			log.info(CREAR_ALUMNO);
			if (ad.realizarActualizacionParametrizada(CREAR_ALUMNO, lista)){
				if (ad.devolverNumeroActualizaciones() !=1)
					log.info("Se ha creado el registro");
			}
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if (bean instanceof AlumnoBean){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			AlumnoBean alumno = (AlumnoBean)bean;
			ArrayList lista = new ArrayList();
			lista.add(alumno.getIdentificador());
			lista.add(alumno.getDni());
			lista.add(alumno.getNombre());
			lista.add(alumno.getPrimerApellido());
			lista.add(alumno.getSegundoApellido());
			lista.add(alumno.getAlergias());
			lista.add(alumno.getObservaciones());
			lista.add(alumno.getDniTutor1());
			lista.add(alumno.getDniTutor2());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(alumno.getFechaNacimiento()));
			lista.add(alumno.getIdentificador());
			log.info("Actualizacion: " + ACTUALIZAR_ALUMNO );
			if (ad.realizarActualizacionParametrizada(ACTUALIZAR_ALUMNO, lista) ){
				if (ad.devolverNumeroActualizaciones() == 1){
					log.info("Se ha actualizado el alumno");
				}
				else{
					if (ad.devolverNumeroActualizaciones() == 0)
						log.info("No se ha actualizado el registro");
					else
						log.info("Se han actualizado " + ad.devolverNumeroActualizaciones() + " registros");
				}
			}
			else{
				log.info("Error al actualizacion la tabla Alumno");
			}
			ad.desconexionBD();
		}
		
	}
	
}
