package com.guarderia.servlet.filtro;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.guarderia.error.DataException;
import com.guarderia.servlet.comando.ComandoUsuario;
import com.guarderia.servlet.menuConfiguracion.MenuConfiguracion;
import com.guarderia.servlet.menuConfiguracion.Propiedades;
import com.guarderia.utils.UtilidadesWeb;

/**
 * 
 * @author Enrique Martín Martín
 */
public class VistaFiltro implements Filter{

	private static final String RUTA_MENU_XML = "/WEB-INF/menu.xml";
	private Logger log = Logger.getLogger(VistaFiltro.class); // Trazas
	
	/**
	 * @uml.property  name="menuPropiedades"
	 * @uml.associationEnd  
	 */
	private Propiedades menuPropiedades = null;
	
	@Override
	public void destroy() {
		menuPropiedades = null;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		String vista = null; // vista que se va a abrir en la parte inferior izquierda
		String opcion; // opcion seleccionada
		String titulo = null; // Titulo de la opcion seleccionada
		
		// Si se ha producido un error
		if (servletRequest.getAttribute("javax.servlet.error.exception") != null){
			log.error("Se ha producido un error");
			opcion = Propiedades.IDENTIFICADOR_ERROR_POR_DEFECTO;
			titulo = menuPropiedades.get(Propiedades.IDENTIFICADOR_ERROR_POR_DEFECTO, Propiedades.CAMPO_TITULO);
			vista = menuPropiedades.get(Propiedades.IDENTIFICADOR_ERROR_POR_DEFECTO, Propiedades.CAMPO_VISTA);
			servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, opcion);
			servletRequest.setAttribute(Propiedades.CAMPO_TITULO, titulo);
			servletRequest.setAttribute(Propiedades.CAMPO_VISTA, vista);
		}
		// Si NO se ha producido un error
		else{
			opcion = servletRequest.getParameter(Propiedades.CAMPO_IDENTIFICADOR) != null ? servletRequest.getParameter(Propiedades.CAMPO_IDENTIFICADOR) : null;
			/**
			 * Si no se ha añadido las opción se añade la de por defecto
			 */
			if (opcion == null && servletRequest.getAttribute(Propiedades.CAMPO_IDENTIFICADOR) == null){
				opcion = menuPropiedades.get(Propiedades.IDENTIFICADOR_DEFECTO, Propiedades.CAMPO_IDENTIFICADOR);
				titulo = menuPropiedades.get(Propiedades.IDENTIFICADOR_DEFECTO, Propiedades.CAMPO_TITULO);
				vista = menuPropiedades.get(Propiedades.IDENTIFICADOR_DEFECTO, Propiedades.CAMPO_VISTA);
				servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, opcion);
				servletRequest.setAttribute(Propiedades.CAMPO_TITULO, titulo);
				servletRequest.setAttribute(Propiedades.CAMPO_VISTA, vista);
			}
			/**
			 * Si no hay opcion pero no hay vista que por defecto vaya al menú principal o la página de inicio
			 */
			if (StringUtils.isNotEmpty(opcion) && StringUtils.isEmpty(vista)){
				UtilidadesWeb.anadirParametroPeticion( (HttpServletRequest) servletRequest, null, Propiedades.CAMPO_IDENTIFICADOR, ComandoUsuario.COMANDO_MENU_PRIVADO);
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String ficheroPropriedades = RUTA_MENU_XML;
		log.info( "ficheroPropriedades = " + ficheroPropriedades );
		menuPropiedades = MenuConfiguracion.getFicheroPropiedades(MenuConfiguracion.FICHERO_XML);
		
		try {
			menuPropiedades.setFile(config.getServletContext().getRealPath(ficheroPropriedades));
		} catch (FileNotFoundException e) {
			log.error("Error al cargar el fichero de propiedades: " + ficheroPropriedades);
			throw new DataException(e);
		}
		log.info("Todo correcto al cargar el fichero de propiedades: " + ficheroPropriedades);
		menuPropiedades = MenuConfiguracion.getFicheroPropiedades();
	}

}
