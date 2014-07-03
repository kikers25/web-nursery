package com.guarderia.servlet.comando;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.PerfilBean;
import com.guarderia.bean.UsuarioBean;
import com.guarderia.negocio.GestionPadre;
import com.guarderia.negocio.GestionProfesor;
import com.guarderia.servlet.menuConfiguracion.MenuConfiguracion;
import com.guarderia.servlet.menuConfiguracion.Propiedades;
import com.guarderia.utils.UtilidadesWeb;

public class ComandoFactoria {
	
	private String vista = null;
	private String opcion = null;
	private String ficheroRedireccion = null;
	private String titulo = null;

	private Logger log = Logger.getLogger(ComandoFactoria.class);
	
	public ComandoFactoria (HttpServletRequest _request){
		Propiedades propiedades = MenuConfiguracion.getFicheroPropiedades(MenuConfiguracion.FICHERO_XML);
		opcion = UtilidadesWeb.obtenerParametroPeticion(_request, "opcion");
		if (opcion == null)
			opcion = UtilidadesWeb.IDENTIFICADOR_PAGINA_INICIO;
		
		UtilidadesWeb.anadirParametroPeticion(_request, null, "opcion", opcion);

		if (opcion.equals("99")){ // volver a la opcion anterior
			try {
				this.irMenuPrincipal(_request, null);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		else{
			if (opcion.equals("98")){ // volver a la segunda opcion anterior
				try {
					this.irMenuPrincipal(_request, null);
				} catch (ServletException e) {
					e.printStackTrace();
				}
			}else{
				vista = propiedades.get(opcion,Propiedades.CAMPO_VISTA);
				if (vista != null)vista = vista.trim();
				titulo = propiedades.get(opcion,Propiedades.CAMPO_TITULO);
				ficheroRedireccion = propiedades.get(opcion,Propiedades.CAMPO_FICHERO_REDIRECCION);
				if (ficheroRedireccion != null)ficheroRedireccion = ficheroRedireccion.trim();
			}
		}
		log.info("Numero Opcion = " + opcion);
		log.info("Opcion Seleccionada = " + titulo);
		_request.setAttribute("vista", vista);
		_request.setAttribute("titulo", titulo);
	}
	
	/**
	 * Obtiene el Comando
	 * @return
	 * @throws ServletException
	 */
	public Comando getComando() throws ServletException{
		if (StringUtils.isBlank(opcion)){
			throw new ServletException ("No hay opcion");
		}
		if (StringUtils.isNotBlank(opcion) && StringUtils.isNotBlank(ficheroRedireccion)){
			try {
				Class clase = Class.forName(ficheroRedireccion);
				// Generamos el objeto
				Comando comando = (Comando)clase.newInstance();
				// Añadimos las propiedades
				comando.setFicheroRedireccion(UtilidadesWeb.ficheroRedireccionPorDefectoJsp);
				comando.setTitulo(titulo);
				comando.setOpcion(opcion);
				return comando;
			} catch (InstantiationException e) {
				log.error("Clase no concreta("+ ficheroRedireccion+"): " + e);
				throw new ServletException ("Clase no concreta("+ ficheroRedireccion+"): " + e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				log.error("No se encuentra la clase: " + e);
				throw new ServletException ("No se encuentra la petición");
			}
		}
		else{
			// Si no existe el fichero
			if (StringUtils.isNotBlank(opcion)
					&& StringUtils.isNotBlank(vista) 
					&& StringUtils.isBlank(ficheroRedireccion)){
				// Generamos un objeto comando
				Comando comando = new Comando();
				// Añadimos las propiedades
				comando.setFicheroRedireccion(UtilidadesWeb.ficheroRedireccionPorDefectoJsp);
				comando.setTitulo(titulo);
				comando.setOpcion(opcion);
				return comando;
			}
			else{
				log.error(("No se encuentra la petición"));
				throw new ServletException ("No se encuentra la petición");
			}
		}
		return null;
	}
	
	/**
	 * Version del existente en UtilidadesWeb @see com.guarderia.utils.UtilidadesWeb 
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	private void irMenuPrincipal(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		Object objeto = request.getSession().getAttribute("usuario");
 		if (objeto != null && objeto instanceof UsuarioBean){
 			UsuarioBean usuario = (UsuarioBean) objeto;
 			if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.ADMINISTRADOR.intValue()){
 				this.vista = UtilidadesWeb.PAGINA_MENU_PRINCIPAL_ADMINISTRADOR;
 			}else{
	 			if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.PADRE.intValue()){
	 				GestionPadre gestionPadre = new GestionPadre();
	 				AdultoBean adulto = new AdultoBean();
	 				adulto.setDni(usuario.getDniUsuario());
	 				ArrayList hijos = new ArrayList();
	 				gestionPadre.bienvenida(adulto,hijos); // Busco los datos personales del padre y los hijos que van a la guarderia
	 				request.getSession().setAttribute("padre", adulto);
	 				request.getSession().setAttribute("hijos", hijos);
	 				this.vista = UtilidadesWeb.PAGINA_MENU_PRINCIPAL_PADRE;
	 			}
	 			else{
	 				if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.PROFESOR.intValue()){
	 					GestionProfesor gestionProfesor = new GestionProfesor();
	 					//UsuarioBean usuario = (UsuarioBean) request.getSession().getAttribute("usuario");
	 					AdultoBean adulto = new AdultoBean();
	 					adulto.setDni(usuario.getDniUsuario());
	 					ArrayList clases = new ArrayList();
	 					gestionProfesor.bienvenida(adulto,clases); // Busco los datos personales del profesor y las clases que imparte
	 					request.getSession().setAttribute("profesor", adulto);
	 					request.getSession().setAttribute("clases", clases);
	 					this.vista = UtilidadesWeb.PAGINA_MENU_PRINCIPAL_PROFESOR;
		 			}
	 			}
 			}
 		}
 		else{
 			this.vista = UtilidadesWeb.PAGINA_INICIO;
 		}
 		this.titulo =  "Bienvenido a Guarderia Web";
	}
	
}
