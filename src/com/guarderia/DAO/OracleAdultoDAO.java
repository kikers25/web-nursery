package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.Bean;
import com.guarderia.bean.ClaseBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase para el acceso a la base de datos y mas especificamente a la tabla Adulto
 * @author Enrique Martín Martín
 */
public class OracleAdultoDAO implements AdultoDAO {
	
	private final static String CONSULTA_ADULTO = "SELECT * FROM ADULTO WHERE DNI LIKE ?";
	private final static String ACTUALIZAR_ADULTO = "UPDATE ADULTO SET DNI = ?, NOMBRE = ?, PRIMER_APELLIDO = ?, SEGUNDO_APELLIDO = ?, DIRECCION = ?, CODIGO_POSTAL = ?, LOCALIDAD = ?, PROVINCIA = ?, NACIONALIDAD = ?, CORREO_ELECTRONICO = ?, FECHA_NACIMIENTO = ?, NIVEL_ESTUDIOS = ?, TIPO_ADULTO = ?, TELEFONO = ? WHERE DNI LIKE ?";
	private final static String CONSULTA_ADULTOS_CLASE_CURSO_ACTUAL = "SELECT * FROM ADULTO WHERE DNI IN (SELECT DNI_ADULTO FROM ADULTO_ENSENA_CLASE WHERE IDENTIFICADOR_CLASE = ? AND CURSO LIKE ?)";
	private final static String CONSULTA_ADULTOS_TIPO = "SELECT * FROM ADULTO WHERE TIPO_ADULTO = ?";
	private final static String CREAR_ADULTO = "INSERT INTO ADULTO (DNI, NOMBRE, PRIMER_APELLIDO, SEGUNDO_APELLIDO, DIRECCION, CODIGO_POSTAL, LOCALIDAD, PROVINCIA, NACIONALIDAD, CORREO_ELECTRONICO, FECHA_NACIMIENTO, NIVEL_ESTUDIOS, TIPO_ADULTO, TELEFONO) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final static String BORRAR_ADULTO = "DELETE * FROM ADULTO WHERE DNI =  ?";
	private final static String CONSULTA_ADULTOS = "SELECT * FROM ADULTO ORDER BY NOMBRE, PRIMER_APELLIDO, SEGUNDO_APELLIDO";
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Constructor por defecto
	 */
	public OracleAdultoDAO(){
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AdultoDAO#buscarAdulto(com.guarderia.bean.AdultoBean)
	 */
	public boolean buscarAdulto(AdultoBean adulto){
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(adulto.getDni());
			log.info(CONSULTA_ADULTO);
			if (ad.realizarConsultaParametrizada(CONSULTA_ADULTO, lista)){
				ResultSet rs = ad.devolverConsulta();
				resultado = convertirResultSetEnBean(rs, adulto);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				adulto = null;
			}
			ad.desconexionBD();
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AdultoDAO#buscarTipoAdulto(java.lang.String, java.util.ArrayList)
	 */
	public boolean buscarTipoAdulto(String tipo, ArrayList listaAdultos){
		boolean retorno = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(tipo);
			log.info(CONSULTA_ADULTOS_TIPO);
			if (ad.realizarConsultaParametrizada(CONSULTA_ADULTOS_TIPO, lista)){
				ResultSet rs = ad.devolverConsulta();
				retorno = true;
				listaAdultos.clear();
				boolean bAux = true;
				while (bAux == true){
					AdultoBean adultoTipo = new AdultoBean();
					bAux = convertirResultSetEnBean(rs, adultoTipo);
					if (bAux){
						log.info("Se ha obtenido el dato de un adulto: " + adultoTipo.getNombre() + " " + adultoTipo.getPrimerApellido() + " " + adultoTipo.getSegundoApellido() + " ");
						listaAdultos.add(adultoTipo);
					}
				}
				UtilidadesGenerales.cerrarResulset(rs);
				ad.desconexionBD();
			}
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AdultoDAO#buscarTodos(java.util.ArrayList)
	 */
	public boolean buscarTodos(ArrayList personas) {
		boolean retorno = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(0);
			log.info(CONSULTA_ADULTOS);
			if (ad.realizarConsultaParametrizada(CONSULTA_ADULTOS, lista)){
				ResultSet rs = ad.devolverConsulta();
				retorno = true;
				personas.clear();
				boolean bAux = true;
				while (bAux == true){
					AdultoBean adultoTipo = new AdultoBean();
					bAux = convertirResultSetEnBean(rs, adultoTipo);
					if (bAux){
						log.info("Se ha obtenido el dato de un adulto: " + adultoTipo.getNombre() + " " + adultoTipo.getPrimerApellido() + " " + adultoTipo.getSegundoApellido() + " ");
						personas.add(adultoTipo);
					}
				}
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AdultoDAO#buscarAdultosDanClaseCursoActual(com.guarderia.bean.ClaseBean, java.util.ArrayList)
	 */
	public boolean buscarAdultosDanClaseCursoActual(ClaseBean clase,ArrayList profesores){
		boolean retorno =  false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(2);
			lista.add(clase.getIdentificador());
			lista.add(UtilidadesGenerales.obtenerCursoActual());
			log.info(CONSULTA_ADULTOS_CLASE_CURSO_ACTUAL);
			if (ad.realizarConsultaParametrizada(CONSULTA_ADULTOS_CLASE_CURSO_ACTUAL, lista)){
				ResultSet rs = ad.devolverConsulta();
				retorno = true;
				profesores.clear();
				boolean bAux = true;
				while (bAux == true){
					AdultoBean profesor = new AdultoBean();
					bAux = convertirResultSetEnBean(rs, profesor);
					if (bAux){
						log.info("Se ha obtenido el dato de un profesor: " + profesor.getNombre() + " " + profesor.getPrimerApellido() + " " + profesor.getSegundoApellido() + " ");
						profesores.add(profesor);
					}
				}
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				profesores.clear();
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AdultoDAO#actualizarAdulto(com.guarderia.bean.AdultoBean)
	 */
	public boolean actualizarAdulto(AdultoBean adulto){
		boolean retorno =  false;
		int iRegistros;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList();
			lista.add(adulto.getDni());
			lista.add(adulto.getNombre());
			lista.add(adulto.getPrimerApellido());
			lista.add(adulto.getSegundoApellido());
			lista.add(adulto.getDireccion());
			lista.add(adulto.getCodigoPostal());
			lista.add(adulto.getLocalidad());
			lista.add(adulto.getProvincia());
			lista.add(adulto.getNacionalidad());
			lista.add(adulto.getCorreoElectronico());
			//lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(adulto.getFechaNacimiento()));
			lista.add(adulto.getFechaNacimiento());
			lista.add(adulto.getNivelEstudios());
			lista.add(adulto.getTipoAdulto());
			lista.add(adulto.getTelefono());
			lista.add(adulto.getDni());
			log.info("Actualizacion: " + ACTUALIZAR_ADULTO );
			if (ad.realizarActualizacionParametrizada(ACTUALIZAR_ADULTO, lista) ){
				iRegistros = ad.devolverNumeroActualizaciones();
				log.info("Numero de registro Actualizacos: " + iRegistros);
				if (iRegistros == 1)retorno = true;
			}
			else{
				log.info("Error al actualizacion la tabla Adulto");
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AdultoDAO#anadirAdulto(com.guarderia.bean.AdultoBean, java.lang.String)
	 */
	public boolean anadirAdulto(AdultoBean adulto, String tipoDeAdulto) {
		boolean retorno =  false;
		int iRegistros;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList();
			lista.add(adulto.getDni());
			lista.add(adulto.getNombre());
			lista.add(adulto.getPrimerApellido());
			lista.add(adulto.getSegundoApellido());
			lista.add(adulto.getDireccion());
			lista.add(adulto.getCodigoPostal());
			lista.add(adulto.getLocalidad());
			lista.add(adulto.getProvincia());
			lista.add(adulto.getNacionalidad());
			lista.add(adulto.getCorreoElectronico());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(adulto.getFechaNacimiento()));
			lista.add(adulto.getNivelEstudios());
			lista.add(tipoDeAdulto);
			lista.add(adulto.getTelefono());
			log.info("Actualizacion: " + CREAR_ADULTO );
			if (ad.realizarActualizacionParametrizada(CREAR_ADULTO, lista) ){
				iRegistros = ad.devolverNumeroActualizaciones();
				log.info("Numero de registro Actualizacos: " + iRegistros);
				if (iRegistros == 1)retorno = true;
			}
			else{
				log.info("Error al actualizacion la tabla Adulto");
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.AdultoDAO#convertirResultSetEnBean(java.sql.ResultSet, com.guarderia.bean.AdultoBean)
	 */
	public boolean convertirResultSetEnBean(ResultSet conjunto, AdultoBean adulto){
		boolean resultado = false;
		String aux;
		int i=1;
		try {
			if(conjunto.next()){
				aux = conjunto.getString(i++);
				adulto.setDni(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setNombre(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setPrimerApellido(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setSegundoApellido(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setDireccion(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setCodigoPostal(UtilidadesGenerales.convertirStringEnInteger(aux));
				aux = conjunto.getString(i++);
				adulto.setLocalidad(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setProvincia(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setNacionalidad(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setCorreoElectronico(UtilidadesGenerales.obtenerString(aux));
				adulto.setFechaNacimiento(UtilidadesGenerales.convertirStringEnGregorianCalendar2(conjunto.getString(i++)));
				//adulto.setFechaNacimiento(UtilidadesGenerales.convertirDateEnGregorianCalendar(conjunto.getDate("FECHA_NACIMIENTO")));
				aux = conjunto.getString(i++);
				adulto.setNivelEstudios(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setTipoAdulto(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				adulto.setTelefono(UtilidadesGenerales.convertirStringEnInteger(aux));
				resultado = true;
			}
			else{
				log.info("No hay datos en el ResultSet para convertirlo en Bean de tipo Adulto");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
			log.error ("Error " + e);
		}
		return resultado;
	}

	@Override
	public Bean borrar(Object identificador_bean) {
		AdultoBean adulto = (AdultoBean) this.buscarPorId(identificador_bean); 
		if ( adulto != null ){
			AccesoDatos ad = new AccesoDatos();
			ArrayList lista = new ArrayList(1);
			lista.add(identificador_bean);
			log.info("borrar: " + BORRAR_ADULTO);
			if (ad.realizarActualizacionParametrizada(CREAR_ADULTO, lista) ){
				int iRegistros = ad.devolverNumeroActualizaciones();
				if (iRegistros == 0)return null;
			}
			ad.desconexionBD();
		}
		return adulto;
	}

	@Override
	public Bean buscarPorId(Object identificador_bean) {
		AdultoBean adulto = null;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(identificador_bean);
			log.info(CONSULTA_ADULTO);
			if (ad.realizarConsultaParametrizada(CONSULTA_ADULTO, lista)){
				ResultSet rs = ad.devolverConsulta();
				convertirResultSetEnBean(rs, adulto);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return adulto;
	}

	@Override
	public void crear(Bean bean) {
		if (bean instanceof AdultoBean){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			AdultoBean adulto = (AdultoBean)bean;
			ArrayList lista = new ArrayList();
			lista.add(adulto.getDni());
			lista.add(adulto.getNombre());
			lista.add(adulto.getPrimerApellido());
			lista.add(adulto.getSegundoApellido());
			lista.add(adulto.getDireccion());
			lista.add(adulto.getCodigoPostal());
			lista.add(adulto.getLocalidad());
			lista.add(adulto.getProvincia());
			lista.add(adulto.getNacionalidad());
			lista.add(adulto.getCorreoElectronico());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(adulto.getFechaNacimiento()));
			lista.add(adulto.getNivelEstudios());
			lista.add(adulto.getTipoAdulto());
			lista.add(adulto.getTelefono());
			ad.realizarActualizacionParametrizada(CREAR_ADULTO, lista);
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if (bean instanceof AdultoBean){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			AdultoBean adulto = (AdultoBean)bean;
			ArrayList lista = new ArrayList();
			lista.add(adulto.getDni());
			lista.add(adulto.getNombre());
			lista.add(adulto.getPrimerApellido());
			lista.add(adulto.getSegundoApellido());
			lista.add(adulto.getDireccion());
			lista.add(adulto.getCodigoPostal());
			lista.add(adulto.getLocalidad());
			lista.add(adulto.getProvincia());
			lista.add(adulto.getNacionalidad());
			lista.add(adulto.getCorreoElectronico());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(adulto.getFechaNacimiento()));
			lista.add(adulto.getNivelEstudios());
			lista.add(adulto.getTipoAdulto());
			lista.add(adulto.getTelefono());
			lista.add(adulto.getDni());
			log.info("Actualizacion: " + ACTUALIZAR_ADULTO );
			ad.realizarActualizacionParametrizada(ACTUALIZAR_ADULTO, lista);
			ad.desconexionBD();
		}
	}

}
