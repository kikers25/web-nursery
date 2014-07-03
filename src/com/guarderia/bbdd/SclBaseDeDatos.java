package com.guarderia.bbdd;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.guarderia.context.ContextProvider;
import com.guarderia.context.ObjectContext;
import com.guarderia.error.DataException;

public class SclBaseDeDatos implements ServletContextListener {

	private static final String FICHERO_TRAZAS = "/WEB-INF/log4j.xml";
	private Logger log = null;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		ServletContext context = arg0.getServletContext();
		AccesoDatos adDAO = (AccesoDatos) context.getAttribute("accesodatos");
		if (adDAO != null){
			if (adDAO.desconexionBD() == true){
				System.out.println("Desconexion correcto de base de datos");
			}
			else{
				System.out.println("Desconexion incorrecto de base de datos");
			}
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Inicializando Listener Context");
		ServletContext context = arg0.getServletContext();
		try {
			DOMConfigurator.configure(context.getResource(FICHERO_TRAZAS));
		} catch (MalformedURLException e) {
			throw new DataException(e);
		} catch (FactoryConfigurationError e) {
			throw new DataException(e);
		}
		log = Logger.getLogger(this.getClass());
		GregorianCalendar calendario = new GregorianCalendar();
		if (log==null){
			System.out.println(calendario.getTime() + " :: Error al configurar log4j");
		}
		else{
			log.info(calendario.getTime() + " :: No ha habido error al configurar log4j");
		}
		DataSource ds = obtenerDataSource();
		context.setAttribute("datasource", ds);
		ObjectContext ctx = ContextProvider.getCurrentContext ();
		ctx.setObject(DataSource.class, "datasource", ds);
	}
	
	/**
	 * Obtiene el datasource para acceder a la base de datos
	 * @return Datasource
	 */
	private DataSource obtenerDataSource(){
		Context initCtx;
		DataSource ds = null;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

//			 Y aquí pedimos nuestra conexión, por su nombre
//			OracleDataSource ds = (OracleDataSource) envCtx.lookup("jdbc/guarderia");
			ds = (DataSource) envCtx.lookup("jdbc/guarderia");
			if (envCtx == null) log.fatal("Error: No Context");
			if (ds == null) log.fatal("Error: No DataSource");
			Connection conexion = null;
			if (ds != null){
				conexion = ds.getConnection();
//				 Se cierra la conexión, ya que estamos en un pool de conexiones
//				 para dejarla libre a otros procesos.
				conexion.close();
			}
			else{
				log.info("El datasource vale null");
				System.out.println("El datasource vale null");
			}


			log.info("Creada la conexion con el pool");
		} catch (NamingException e) {
			e.printStackTrace();
			log.fatal("Error NamingException: " + e);
		} catch (SQLException e) {
			e.printStackTrace();
			log.fatal("Error SQLException ErrorCode=" + e.getErrorCode() + " SQLState=" + e.getSQLState());
		}
		return ds;
	}

}
