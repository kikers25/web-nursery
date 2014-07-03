package com.guarderia.bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.sql.Types;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.guarderia.bean.Bean;
import com.guarderia.context.ContextProvider;
import com.guarderia.context.ObjectContext;
import com.guarderia.error.DataException;

import org.apache.commons.dbutils.BeanProcessor;

import oracle.jdbc.pool.OracleDataSource;

/**
 * Clase que se relaciona con la base de datos, así es la encargada de conectarse,
 *  realizar consultar y actualizaciones
 * @author Enrique Martín Martín
 *
 */
public class AccesoDatos {
	
	/**
	 * Conexión con la base de datos
	 */
	private Connection conexion = null;
	
	/**
	 * Resultado de las consultas a la base de datos
	 */
	private ResultSet rs = null;
	
	/**
	 * Nos dice el numero de registro que se han modificado en la actualización en la base de datos
	 */
	private int iNumeroRegistrosActualizados = 0;
	
	/**
	 * Url para acceder a la base de datos
	 */
	private String sUrl = null;
	
	/**
	 * Usuario para acceder a la base de datos
	 */
	private String sUsuario = null;
	
	/**
	 * Contraseña para acceder a la base de datos
	 */
	private String sContrasena = null;
	
	/**
	 * Url por defecto para acceder a la base de datos
	 */
	static private String URL_POR_DEFECTO = "jdbc:oracle:thin";
	
	/**
	 * Usuario por defecto para acceder a la base de datos
	 */
	static private String USUARIO_POR_DEFECTO = "guarderiaweb";
	
	/**
	 * Contraseña por defecto para acceder a la base de datos
	 */
	static private String CONTRASENA_POR_DEFECTO = "guarderia";
	
	/**
	 * Atributo utilizada para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Constructor por defecto no se puede acceder desde fuera
	 *
	 */
	public AccesoDatos(){
		sUrl = URL_POR_DEFECTO;
		sUsuario = USUARIO_POR_DEFECTO;
		sContrasena = CONTRASENA_POR_DEFECTO;
	}
	
	/**
	 * Si no se ha establecido ninguna conexión anteriormente con la base de datos se conecta
	 * @return true si hay establecida una conexión con la base datos y false sino
	 */
	public boolean conexionBD(){
		boolean bResultado = false;
		rs = null;
		iNumeroRegistrosActualizados = 0;
		if (conexion == null){
			try{
				OracleDataSource ods = new OracleDataSource();
				ods.setURL(sUrl + ":" + sUsuario + "/" + sContrasena + "@localhost:1521/XE");
				conexion = ods.getConnection();
				log.info("Creada la conexión a BBDD");
				bResultado = true;
			}catch (SQLException sql){
				log.info("Error al crear la conexion con la BBDD");
			}
		}
		else{
			bResultado = true;
		}
		return bResultado;
	}
	
	/**
	 * Si no se ha establecido ninguna conexión anteriormente con la base de datos se conecta
	 * @return true si hay establecida una conexión con la base datos y false sino
	 */
	public boolean conexionBDPool(){
		boolean bResultado = false;
		rs = null;
		iNumeroRegistrosActualizados = 0;
		if (conexion == null){
			ObjectContext ctx = ContextProvider.getCurrentContext ();
			DataSource ds = (DataSource) ctx.getObject(DataSource.class, "datasource");
			// Esta parte es fija
			Context initCtx;
			try {
				if (ds == null){
					initCtx = new InitialContext();
					Context envCtx = (Context) initCtx.lookup("java:comp/env");
	
	//				 Y aquí pedimos nuestra conexión, por su nombre
					ds = (DataSource) envCtx.lookup("jdbc/guarderia");
					if (envCtx == null) log.fatal("Error: No Context");
					if (ds == null) log.fatal("Error: No DataSource");
					//if (ds != null) conexion = ds.getConnection("guarderiaweb","guarderia");
				}
				if (ds != null) conexion = ds.getConnection();
			     
//				 Se cierra la conexión, ya que estamos en un pool de conexiones
//				 para dejarla libre a otros procesos.
				//conexion.close();
				bResultado = true;
				log.info("Creada la conexion con el pool");
			} catch (NamingException e) {
				e.printStackTrace();
				log.fatal("Error NamingException: " + e);
			} catch (SQLException e) {
				e.printStackTrace();
				log.fatal("Error SQLException ErrorCode=" + e.getErrorCode() + " SQLState=" + e.getSQLState());
			}
			
		}
		else{
			bResultado = true;
		}
		return bResultado;
	}
	
	/**
	 * Se desconecta de la base de datos
	 * @return true si se ha desconectado de la base de datos y false sino
	 */
	public boolean desconexionBD(){
		boolean bResultado = false;
		iNumeroRegistrosActualizados = 0;
		if(conexion != null){
			try {
				if (rs != null)
					rs.close();
				if (!conexion.isClosed())
					conexion.close();
				bResultado = true;
				log.debug("La conexión con la BBDD se cerró correctamente");
			} catch (SQLException e) {
				log.debug("Error al cerrar la conexión con la BBDD : " + e);
				e.printStackTrace();
			}
		}
		else{
			bResultado = true;
		}
		return bResultado;
	}
	
	/**
	 * Realiza una consulta a la base de datos
	 * @param consulta Consulta ha realizar en la base de datos
	 * @return true si la consuta se ha realizado correctamente y false sino
	 */
	public boolean realizarConsulta(String consulta){
		boolean bResultado = false;
		if (conexionBDPool() == true){
			try{
				Statement stmt = conexion.createStatement();
				rs = stmt.executeQuery(consulta);
				System.out.println("No ha habido problemas en realizar la consulta");
				bResultado = true;
			}catch (SQLException sql){
				System.out.println(sql);
			}	
		}
		return bResultado;
	}
	
	/**
	 * Realiza una consulta a la base de datos a traves de parametros
	 * @param consulta Consulta que hay que realizar con los parámetros con ?
	 * @param lista Lista con los valores parametrizados
	 * @return true si la consuta se ha realizado correctamente y false sino
	 */
	public boolean realizarConsultaParametrizada(String consulta,ArrayList lista){
		boolean bResultado = false;
		if (conexionBDPool() == true){
			try{
				PreparedStatement pstmt = conexion.prepareStatement(consulta);
				for(int i=0;i<lista.size();i++){
					setParametro(pstmt, i+1, lista.get(i));
				}
				rs = pstmt.executeQuery();
				bResultado = true;
			}catch (SQLException sql){
				log.fatal(sql);
				log.fatal("Codigo de error: " + sql.getErrorCode());
				sql.printStackTrace();
			}	
		}
		return bResultado;
	}
	
	/**
	 * Realiza una consulta a la base de datos a traves de parametros
	 * @param consulta Consulta que hay que realizar con los parámetros con ?
	 * @param lista Lista con los parámetros
	 * @param clase Clase en la que se convertirá el resulset
	 * @return 
	 */
	public Bean realizarConsultaParametrizada(String consulta,ArrayList lista, Class clase)
			throws DataException{
		Bean resultado = null;
		if (conexionBDPool()){
			try{
				PreparedStatement pstmt = conexion.prepareStatement(consulta);
				for(int i=0;i<lista.size();i++){
					pstmt.setObject(i+1,(Object) lista.get(i));
				}
				rs = pstmt.executeQuery();
				if (clase != null){
					if (rs.next()){
						BeanProcessor procesadorBean = new BeanProcessor();
						resultado =(Bean) procesadorBean.toBean(rs, clase);
					}
				}
				rs.close();
			}catch (SQLException sql){
				log.fatal(sql.getMessage());
				throw new DataException (consulta,sql);
			}	
		}
		return resultado;
	}
	
	/**
	 * Realiza una consulta a la base de datos a traves de parametros obteniendo una lista de clases
	 * @param consulta Consulta que hay que realizar con los parámetros con ?
	 * @param lista Lista con los parámetros
	 * @param clase Clase en la que se convertirá el resulset
	 * @return Lista de tipo Bean con el resultado
	 */
	public List realizarConsultaListaParametrizada(String consulta,ArrayList lista, Class clase)
			throws DataException{
		List listaResultado = null;
		if (conexionBDPool()){
			try{
				PreparedStatement pstmt = conexion.prepareStatement(consulta);
				for(int i=0;i<lista.size();i++){
					setParametro(pstmt, i+1, lista.get(i));
				}
				rs = pstmt.executeQuery();
				if (clase != null){
					BeanProcessor procesadorBean = new BeanProcessor();
					listaResultado = new ArrayList();
					while(rs.next()){
						Bean resultado = (Bean) procesadorBean.toBean(rs, clase);
						if (resultado != null)
							listaResultado.add(resultado);
					}
				}
				rs.close();
			}catch (SQLException sql){
				log.fatal(sql.getMessage());
				throw new DataException (consulta,sql);
			}	
		}
		return listaResultado;
	}
	
	/**
	 * Realiza una actualización a la base de datos a traves de parametros
	 * @param consulta Actualización a realizar
	 * @param lista Lista con los valores parametrizados
	 * @return true si la actualización se ha realizado correctamente y false sino
	 */
	public boolean realizarActualizacionParametrizada(String consulta,ArrayList lista){
		boolean bResultado = false;
		if (conexionBDPool() == true){
			try{
				PreparedStatement pstmt = conexion.prepareStatement(consulta);
				for(int i=0;i<lista.size();i++){
					setParametro(pstmt, i+1, lista.get(i));
				}
				iNumeroRegistrosActualizados = pstmt.executeUpdate();
				bResultado = true;
			}catch (SQLException sql){
				System.out.println(sql);
				log.fatal(sql);
			}	
		}
		return bResultado;
	}
	
	/**
	 * Devuelve El resultado obtenido de la consulta a la base de datos
	 * @return Resulset con los resultados
	 */
	public ResultSet devolverConsulta(){
		return rs;
	}
	
	/**
	 * Realiza una actualización de los datos de la base de datos
	 * @param consulta Actualización a realizar
	 * @return true si la actualización se ha realizado correctamente o false sino
	 */
	public boolean realizarActualizacion(String consulta){
		boolean bResultado = false;
		if (conexionBDPool() == true){
			try{
				Statement stmt = conexion.createStatement();
				iNumeroRegistrosActualizados = stmt.executeUpdate(consulta);
				bResultado = true;
			}catch (SQLException sql){
				
			}	
		}
		return bResultado;
	}
	
	/**
	 * Devuelve El resultado obtenido de la actualizacion a la base de datos
	 * @return Número de regitros que se han modificado de la base de datos
	 */
	public int devolverNumeroActualizaciones(){
		return iNumeroRegistrosActualizados;
	}
	
	/**
	 * Cierra el resultset
	 * @param rs Resultset a cerrar
	 */
 	public void cerrarResulset(){
		try {
			if (rs != null)
				this.rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Añade a un PreparedStatement el objeto
	 * @param pstmt PreparedStatement
	 * @param nObjeto Número de objeto para el PreparedStatement 
	 * @param objeto Objeto a añadir
	 * @throws SQLException 
	 */
	private void setParametro(PreparedStatement pstmt, int nObjeto, Object objeto) throws SQLException{
		if (objeto instanceof Boolean){
			Boolean objetoBoolean = (Boolean) objeto;
			if (objetoBoolean.equals(Boolean.TRUE)){
				pstmt.setString(nObjeto, "Y");
			}
			else{
				pstmt.setString(nObjeto, "N");
			}
			return;
		}
		if (objeto instanceof String){
			pstmt.setString(nObjeto, (String)objeto);
			return;
		}
		if (objeto instanceof Integer){
			pstmt.setInt(nObjeto, (Integer)objeto);
			return;
		}
		if (objeto instanceof Long){
			pstmt.setLong(nObjeto, (Long)objeto);
			return;
		}
		if (objeto instanceof java.util.Date){
			Date objetoFecha = (Date) objeto;
			pstmt.setDate( nObjeto,new java.sql.Date( objetoFecha.getTime() ) );
			return;
		}
		if (objeto instanceof GregorianCalendar){
			Date objetoFecha = ((GregorianCalendar)objeto).getTime();
			pstmt.setDate( nObjeto,new java.sql.Date( objetoFecha.getTime() ) );
			return;
		}
		if (objeto instanceof Timestamp){
			pstmt.setTimestamp(nObjeto,(Timestamp) objeto);
			return;
		}
		pstmt.setNull(nObjeto, Types.NULL);
	}
	
}