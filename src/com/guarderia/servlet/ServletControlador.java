package com.guarderia.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.guarderia.error.DataException;
import com.guarderia.servlet.comando.Comando;
import com.guarderia.servlet.comando.ComandoFactoria;
import com.guarderia.servlet.menuConfiguracion.MenuConfiguracion;
import com.guarderia.servlet.menuConfiguracion.Propiedades;
import com.guarderia.utils.UtilidadesWeb;

/**
 * Implementación de la clase Servlet para el Servlet: Controlador
 * @web.servlet  name="Controlador"  display-name="Controlador" 
 * @web.servlet-mapping  url-pattern="/Controlador"
 */
public class ServletControlador extends javax.servlet.http.HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo utilizada para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Atributo para abrir el fichero de configuracion del menu
	 * @uml.property  name="propiedades"
	 * @uml.associationEnd  
	 */
	private Propiedades propiedades;
	
	/**
	 * Inicializa los atributos
	 */
	public void init(ServletConfig config) throws ServletException {
		String ficheroPropriedades, tipoFicheroPropiedades;
		ficheroPropriedades=config.getInitParameter("fichero_propiedades");
		log.info( "ficheroPropriedades = " + ficheroPropriedades );
		tipoFicheroPropiedades = config.getInitParameter("tipo_fichero_propiedades");
		if (StringUtils.equals(tipoFicheroPropiedades, "props") ){
			propiedades = MenuConfiguracion.getFicheroPropiedades(MenuConfiguracion.FICHERO_PROPIEDADES);
		}
		else{
			propiedades = MenuConfiguracion.getFicheroPropiedades(MenuConfiguracion.FICHERO_XML);
		}
		
		try {
			propiedades.setFile(config.getServletContext().getRealPath(ficheroPropriedades));
		} catch (FileNotFoundException e) {
			log.error("Error al cargar el fichero de propiedades: " + ficheroPropriedades);
			throw new DataException(e);
		}
		log.info("Todo correcto al cargar el fichero de propiedades: " + ficheroPropriedades);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		procesar(request, response);
	}
	
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesar(request, response);
	}

	private void procesar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ComandoFactoria helper = new ComandoFactoria(request);
		Comando servlet = helper.getComando();
		String pagina = servlet.procesarPeticion(request, response);
		UtilidadesWeb.redireccionar(request, response, pagina);
	}
	
}
