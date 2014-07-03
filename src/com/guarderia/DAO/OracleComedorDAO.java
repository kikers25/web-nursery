package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.Bean;
import com.guarderia.bean.MenuBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase para el acceso a la base de datos y mas especificamente a la tabla Menu
 * @author Enrique Martín Martín
 *
 */
public class OracleComedorDAO implements ComedorDAO {

	private final static String CONSULTA_MENU_POR_FECHA = "SELECT * FROM MENU WHERE FECHA = ?";
	private final static String CONSULTA_MENUS_ENTRE_FECHAS = "SELECT * FROM MENU WHERE FECHA BETWEEN ? AND ? ORDER BY FECHA";
	private final static String INSERTAR_MENU = "INSERT INTO MENU (IDENTIFICADOR, PRIMER_PLATO, SEGUNDO_PLATO, POSTRE, FECHA) values (?,?,?,?,?)";
	private final static String CONSULTA_MAXIMO_IDENTIFICADOR_MENU = "SELECT MAX(IDENTIFICADOR) FROM MENU";
	private final static String ACTUALIZAR_MENU = "UPDATE MENU SET IDENTIFICADOR = ?, PRIMER_PLATO = ?, SEGUNDO_PLATO = ?, POSTRE = ?, FECHA = ? WHERE IDENTIFICADOR = ?";
	private final static String ELIMINAR_MENU = "DELETE FROM MENU WHERE IDENTIFICADOR = ?";
	private final static String CONSULTA_MENU_POR_ID = "SELECT * FROM MENU WHERE IDENTIFICADOR = ?";
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Constructor por defecto
	 */
	public OracleComedorDAO() {
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ComedorDAO#consultaEnFecha(com.guarderia.bean.MenuBean)
	 */
	public boolean consultaEnFecha(MenuBean comida) {
		boolean retorno =  false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(comida.getFecha()));
			log.info(CONSULTA_MENU_POR_FECHA);
			if (ad.realizarConsultaParametrizada(CONSULTA_MENU_POR_FECHA, lista)){
				ResultSet rs = ad.devolverConsulta();
				retorno = convertirResultSetEnBean(rs, comida);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ComedorDAO#consultaEntreFechas(java.util.ArrayList, java.util.GregorianCalendar, java.util.GregorianCalendar)
	 */
	public boolean consultaEntreFechas(ArrayList listaComidas, GregorianCalendar calendarioInicio, GregorianCalendar calendarioFin) {
		boolean retorno =  false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(2);
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(calendarioInicio));
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(calendarioFin));
			log.info(CONSULTA_MENUS_ENTRE_FECHAS);
			if (ad.realizarConsultaParametrizada(CONSULTA_MENUS_ENTRE_FECHAS, lista)){
				ResultSet rs = ad.devolverConsulta();
				retorno = true;
				listaComidas.clear();
				boolean bAux = true;
				while (bAux == true){
					MenuBean comida = new MenuBean();
					bAux = convertirResultSetEnBean(rs, comida);
					if (bAux){
						log.info("Se ha obtenido el dato de una comida: " + UtilidadesGenerales.convertirGregorianCalendarEnString(comida.getFecha())  + " ");
						listaComidas.add(comida);
					}
				}
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				listaComidas.clear();
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ComedorDAO#consultaMayorIdentificador()
	 */
	public int consultaMayorIdentificador(){
		int max = -1;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(0);
			log.info(CONSULTA_MAXIMO_IDENTIFICADOR_MENU);
			if (ad.realizarConsultaParametrizada(CONSULTA_MAXIMO_IDENTIFICADOR_MENU, lista)){
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
	 * @see com.guarderia.DAO.ComedorDAO#crearMenu(com.guarderia.bean.MenuBean)
	 */
	public boolean crearMenu(MenuBean comida) {
		boolean retorno =  false;
		int iRegistros;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(5);
			lista.add(comida.getIdentificador());
			lista.add(comida.getPrimerPlato());
			lista.add(comida.getSegundoPlato());
			lista.add(comida.getPostre());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(comida.getFecha()));
			if (ad.realizarActualizacionParametrizada(INSERTAR_MENU, lista) ){
				log.info("Actualizacion: " + INSERTAR_MENU );
				iRegistros = ad.devolverNumeroActualizaciones();
				log.info("Numero de registro Actualizacos: " + iRegistros);
				if (iRegistros == 1) retorno = true;
			}
			ad.desconexionBD();
		}
		return retorno;
		
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ComedorDAO#modificarMenu(com.guarderia.bean.MenuBean)
	 */
	public boolean modificarMenu(MenuBean menu) {
		boolean retorno =  false;
		int iRegistros;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(6);
			lista.add(menu.getIdentificador());
			lista.add(menu.getPrimerPlato());
			lista.add(menu.getSegundoPlato());
			lista.add(menu.getPostre());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(menu.getFecha()));
			lista.add(menu.getIdentificador());
			if (ad.realizarActualizacionParametrizada(ACTUALIZAR_MENU, lista) ){
				log.info("Actualizacion: " + ACTUALIZAR_MENU );
				iRegistros = ad.devolverNumeroActualizaciones();
				log.info("Numero de registro Actualizacos: " + iRegistros);
				if (iRegistros == 1) retorno = true;
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ComedorDAO#eliminarMenu(com.guarderia.bean.MenuBean)
	 */
	public boolean eliminarMenu(MenuBean menu) {
		boolean retorno =  false;
		int iRegistros;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(menu.getIdentificador());
			if (ad.realizarActualizacionParametrizada(ELIMINAR_MENU, lista) ){
				log.info("Actualizacion: " + ELIMINAR_MENU );
				iRegistros = ad.devolverNumeroActualizaciones();
				log.info("Numero de registros Eliminados: " + iRegistros);
				if (iRegistros == 1) retorno = true;
			}
			ad.desconexionBD();
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.ComedorDAO#convertirResultSetEnBean(java.sql.ResultSet, com.guarderia.bean.MenuBean)
	 */
	public boolean convertirResultSetEnBean(ResultSet conjunto,MenuBean comida) {
		boolean retorno = false;
		String aux;
		int i=1;
		try {
			if (conjunto.next()){
				aux = conjunto.getString(i++);
				comida.setIdentificador(UtilidadesGenerales.convertirStringEnInteger(aux));		
				aux = conjunto.getString(i++);
				comida.setPrimerPlato(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				comida.setSegundoPlato(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				comida.setPostre(UtilidadesGenerales.obtenerString(aux));
				comida.setFecha(UtilidadesGenerales.convertirDateEnGregorianCalendar(conjunto.getDate(i++)));
				retorno = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorno;
	}

	@Override
	public Bean borrar(Object identificadorBean) {
		MenuBean menu = null;
		if ( identificadorBean instanceof Integer ){
			menu = (MenuBean) buscarPorId(identificadorBean);
			if (menu != null){
				AccesoDatos ad = new AccesoDatos();
				ad.conexionBDPool();
				ArrayList lista = new ArrayList(1);
				lista.add((Integer)identificadorBean);
				if (ad.realizarActualizacionParametrizada(ELIMINAR_MENU, lista) ){
					if (ad.devolverNumeroActualizaciones() != 1)
						log.error("No se ha eliminado el mnú del comedor de forma correcta");
				}
				ad.desconexionBD();
			}
			else{
				log.error("No existe el menu a borrar: " + identificadorBean);
			}
		}
		return menu;
	}

	@Override
	public Bean buscarPorId(Object identificadorBean) {
		MenuBean menu = null;
		if ( identificadorBean instanceof Number){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add((Number)identificadorBean);
			if (ad.realizarConsultaParametrizada(CONSULTA_MENU_POR_ID, lista) ){
				ResultSet rs = ad.devolverConsulta();
				convertirResultSetEnBean(rs, menu);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return menu;
	}

	@Override
	public void crear(Bean bean) {
		if ( bean instanceof MenuBean ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			MenuBean comida = new MenuBean();
			ArrayList lista = new ArrayList(5);
			lista.add(comida.getIdentificador());
			lista.add(comida.getPrimerPlato());
			lista.add(comida.getSegundoPlato());
			lista.add(comida.getPostre());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(comida.getFecha()));
			if (ad.realizarActualizacionParametrizada(INSERTAR_MENU, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1) {
					log.error("No se ha actualizado");
				}
			}
			ad.desconexionBD();
		}
		
	}

	@Override
	public void modificiar(Bean bean) {
		if ( bean instanceof MenuBean ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			MenuBean menu = (MenuBean) bean;
			ArrayList lista = new ArrayList(6);
			lista.add(menu.getIdentificador());
			lista.add(menu.getPrimerPlato());
			lista.add(menu.getSegundoPlato());
			lista.add(menu.getPostre());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(menu.getFecha()));
			lista.add(menu.getIdentificador());
			if (ad.realizarActualizacionParametrizada(ACTUALIZAR_MENU, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1) 
					log.error("No se pudo modificar el menu con identificador " + menu.getIdentificador());
			}
			ad.desconexionBD();
		}
	}
	
}
