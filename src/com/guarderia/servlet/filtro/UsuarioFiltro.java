package com.guarderia.servlet.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.guarderia.bean.PerfilBean;
import com.guarderia.bean.UsuarioBean;
import com.guarderia.negocio.GestionUsuario;
import com.guarderia.servlet.comando.ComandoUsuario;
import com.guarderia.servlet.menuConfiguracion.MenuConfiguracion;
import com.guarderia.servlet.menuConfiguracion.Propiedades;
import com.guarderia.utils.UtilidadesWeb;

/**
 * Filtro para logearnos, ir al menú principal y gestionar la seguridad
 * @author Enrique Martín Martín
 */
public final class UsuarioFiltro implements Filter {

	private final static String DIRECTORIO_PRIVADO = "MenuPrivado";
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain filterChain) throws IOException, ServletException {
		String opcion = (String) servletRequest.getParameter("opcion");
		
		// Si queremos logearnos
		if ( StringUtils.equals(opcion,ComandoUsuario.COMANDO_LOGEO) ){
			login(servletRequest, opcion);
		}
		
		// Si queremos ir al menu privado
		if ( StringUtils.equals(opcion,ComandoUsuario.COMANDO_MENU_PRIVADO) ||
				StringUtils.equals(opcion,"menu_privado")){
			opcion = ComandoUsuario.COMANDO_MENU_PRIVADO;
			redireccionar_a_menu_privado(servletRequest, opcion);
		}
		
		// Vamos a comprobar si se quiere acceder a una opción privada
		if (StringUtils.isNotEmpty(opcion)){
			Propiedades propiedades = MenuConfiguracion.getFicheroPropiedades();
			String vista = propiedades.get(opcion, Propiedades.CAMPO_VISTA);
			HttpSession session = ((HttpServletRequest) servletRequest).getSession();
			// Se comprueba que quiere acceder a una opción privada
			if ((StringUtils.contains(vista, DIRECTORIO_PRIVADO) && session.getAttribute("usuario") == null)
					|| StringUtils.isEmpty(vista) && session.getAttribute("usuario") == null){
				//Si es así se añade un mensaje y vamos a la página de inicio
				UtilidadesWeb.anadirMensajeError((HttpServletRequest) servletRequest, null, "No tiene permisos para acceder a esa página");
				servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, UtilidadesWeb.IDENTIFICADOR_PAGINA_INICIO);
			}
			
		}
		
		// Si la opción está vacia se añade la página de inicio
		if (StringUtils.isEmpty(opcion)){
			servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, UtilidadesWeb.IDENTIFICADOR_PAGINA_INICIO);
		}
		filterChain.doFilter(servletRequest, servletResponse);
		
	}

	/**
	 * Nos logeamos
	 * @param servletRequest ServletRequest
	 * @param opcion Opción de la petición
	 */
	private void login(ServletRequest servletRequest, String opcion) {
		/*
		 * Obtenemos usuario y contraseña de la petición
		 */
		//log.info("Logeo");
		GestionUsuario usuarioBO = new GestionUsuario();
		String sUsuario=(servletRequest.getParameter("usuario")== null ? "" : servletRequest.getParameter("usuario"));
		String sContrasena=(servletRequest.getParameter("contrasena")== null ? "" : servletRequest.getParameter("contrasena"));
		
		UsuarioBean usuario = new UsuarioBean();
		PerfilBean perfil = new PerfilBean();
		usuario.setUsuario(sUsuario);
		usuario.setContrasena(sContrasena);
		/*
		 * validamos el usuario y la contraseña
		 */
		if (usuarioBO.validarUsuario(usuario,perfil)){ // Si se ha validado el usuario
			usuario.setContrasena("");
			HttpSession session = ((HttpServletRequest) servletRequest).getSession();
			// Añadimos a la sesión el usuario y el perfil
			session.setAttribute("usuario", usuario);
			session.setAttribute("perfil", perfil);
			
			servletRequest.removeAttribute(Propiedades.CAMPO_IDENTIFICADOR);
			if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.ADMINISTRADOR.intValue()){
				servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, UtilidadesWeb.IDENTIFICADOR_MENU_PRINCIPAL_ADMINISTRADOR);
 			}else{
	 			if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.PADRE.intValue()){
	 				servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, UtilidadesWeb.IDENTIFICADOR_MENU_PRINCIPAL_PADRE);
	 			}
	 			else{
	 				if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.PROFESOR.intValue()){
	 					servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, UtilidadesWeb.IDENTIFICADOR_MENU_PRINCIPAL_PROFESOR);
		 			}
	 			}
 			}
		}
		else{
			//anadirMensajeError(request, response, MENSAJE_ERROR);
			//log.info("NO Encontrado usuario");
		}
		//UtilidadesWeb.irMenuPrincipal(request, response);
	}

	/**
	 * Si queremos ir al menu privado, lo que hacemos es añadir la opción correcta según 
	 * el tipo de usuario.
	 * @param servletRequest ServletRequest
	 * @param opcion Opción de la petición
	 */
	private void redireccionar_a_menu_privado(ServletRequest servletRequest, String opcion) {
		HttpSession session = ((HttpServletRequest) servletRequest).getSession();
		UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
		servletRequest.removeAttribute(Propiedades.CAMPO_IDENTIFICADOR);
		if (usuario != null){
			if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.ADMINISTRADOR.intValue()){
				servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, UtilidadesWeb.IDENTIFICADOR_MENU_PRINCIPAL_ADMINISTRADOR);
 			}else{
	 			if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.PADRE.intValue()){
	 				servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, UtilidadesWeb.IDENTIFICADOR_MENU_PRINCIPAL_PADRE);
	 			}
	 			else{
	 				if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.PROFESOR.intValue()){
	 					servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, UtilidadesWeb.IDENTIFICADOR_MENU_PRINCIPAL_PROFESOR);
		 			}
	 			}
 			}
		}
		else{
			UtilidadesWeb.anadirMensajeError((HttpServletRequest) servletRequest, null, "No tiene permisos para acceder al menú privado");
			servletRequest.setAttribute(Propiedades.CAMPO_IDENTIFICADOR, UtilidadesWeb.IDENTIFICADOR_PAGINA_INICIO);
		}
	}

}
