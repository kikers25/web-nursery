package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.Bean;
import com.guarderia.bean.PerfilBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * @author Enrique Martín Martín
 * Clase para el acceso a la base de datos y mas especificamente a la tabla Perfil
 */
public class OraclePerfilDAO implements PerfilDAO {

	private final static String CONSULTA_PERFIL = "SELECT * FROM PERFIL WHERE IDENTIFICADOR = ?";
	private final static String BORRAR = "DELETE FROM PERFIL WHERE IDENTIFICADOR = ?";
	private final static String CREAR = "INSERT INTO PERFIL (identificador,nombre,descripcion,nivelPrioridad) VALUE (?,?,?,?)";
	private final static String MODIFICAR = "UPDATE PERFIL SET identificador = ?, nombre = ?,descripcion = ?,nivelPrioridad = ? where identificador = ?)";
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Constructor por defecto
	 */
	public OraclePerfilDAO(){
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.PerfilDAO#consultaPerfil(com.guarderia.bean.PerfilBean)
	 */
	public boolean consultaPerfil(PerfilBean perfil){
		boolean bResultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(perfil.getIdentificador());
			if (ad.realizarConsultaParametrizada(CONSULTA_PERFIL, lista)){
				ResultSet rs = ad.devolverConsulta();
				bResultado = convertirResultSetEnBean(rs, perfil);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				perfil = null;
			}
			ad.desconexionBD();
		}
		return bResultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.PerfilDAO#convertirResultSetEnBean(java.sql.ResultSet, com.guarderia.bean.PerfilBean)
	 */
	public boolean convertirResultSetEnBean(ResultSet conjunto, PerfilBean perfil){
		boolean resultado = false;
		String aux;
		int i=1;
		try {
			if(conjunto.next()){
				aux = conjunto.getString(i++);
				perfil.setIdentificador(UtilidadesGenerales.convertirStringEnInteger(aux));	
				aux = conjunto.getString(i++);
				perfil.setNombre(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				perfil.setDescripcion(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				perfil.setNivelPrioridad(UtilidadesGenerales.convertirStringEnInteger(aux));
				resultado = true;
			}
			else{
				log.info("No hay datos en el ResultSet para convertirlo en Bean de tipo Perfil");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultado;
	}

	@Override
	public Bean borrar(Object identificadorBean) {
		PerfilBean perfil = null;
		if (identificadorBean instanceof Integer){
			perfil = (PerfilBean) this.buscarPorId(identificadorBean);
			if (perfil != null){
				AccesoDatos ad = new AccesoDatos();
				ArrayList lista = new ArrayList(1);
				lista.add(identificadorBean);
				ad.realizarActualizacionParametrizada(BORRAR, lista);
				if (ad.devolverNumeroActualizaciones() != 1){
					log.error("No se ha borrado el perfil con identificador " + identificadorBean);
				}
				ad.desconexionBD();
			}
		}
		return perfil;
	}

	@Override
	public Bean buscarPorId(Object identificadorBean) {
		PerfilBean perfil = null;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(identificadorBean);
			if (ad.realizarConsultaParametrizada(CONSULTA_PERFIL, lista)){
				ResultSet rs = ad.devolverConsulta();
				perfil = new PerfilBean();
				convertirResultSetEnBean(rs, perfil);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				perfil = null;
			}
			ad.desconexionBD();
		}
		return perfil;
	}

	@Override
	public void crear(Bean bean) {
		if (bean instanceof PerfilBean){
			AccesoDatos ad = new AccesoDatos();
			PerfilBean perfil = (PerfilBean) bean;
			ArrayList lista = new ArrayList(4);
			lista.add(perfil.getIdentificador());
			lista.add(perfil.getNombre());
			lista.add(perfil.getDescripcion());
			lista.add(perfil.getNivelPrioridad());
			ad.realizarActualizacionParametrizada(CREAR, lista);
			if (ad.devolverNumeroActualizaciones() != 1){
				log.error("No se ha creado el perfil");
			}
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if (bean instanceof PerfilBean){
			AccesoDatos ad = new AccesoDatos();
			PerfilBean perfil = (PerfilBean) bean;
			ArrayList lista = new ArrayList(5);
			lista.add(perfil.getIdentificador());
			lista.add(perfil.getNombre());
			lista.add(perfil.getDescripcion());
			lista.add(perfil.getNivelPrioridad());
			lista.add(perfil.getIdentificador());
			ad.realizarActualizacionParametrizada(MODIFICAR, lista);
			if (ad.devolverNumeroActualizaciones() != 1){
				log.error("No se ha podido modificar el perfil con identificador " + perfil.getIdentificador());
			}
			ad.desconexionBD();
		}
	}
	
}
