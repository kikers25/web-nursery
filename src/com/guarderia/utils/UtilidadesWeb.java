package com.guarderia.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.PerfilBean;
import com.guarderia.bean.UsuarioBean;
import com.guarderia.negocio.GestionPadre;
import com.guarderia.negocio.GestionProfesor;
import com.guarderia.servlet.menuConfiguracion.MenuConfiguracion;
import com.guarderia.servlet.menuConfiguracion.Propiedades;

public class UtilidadesWeb {

	public static final String ficheroRedireccionPorDefectoJsp = "index.jsp";
	public static final String PAGINA_MENU_PRINCIPAL_PROFESOR = "/MenuPrivado/ProfesorVentanaPrincipal.jsp";
	public static final String PAGINA_MENU_PRINCIPAL_PADRE = "/MenuPrivado/TutorVentanaPrincipal.jsp";
	public static final String PAGINA_MENU_PRINCIPAL_ADMINISTRADOR = "/MenuPrivado/AdministradorVentanaPrincipal.jsp";
	public static final String IDENTIFICADOR_MENU_PRINCIPAL_PROFESOR = "10";
	public static final String IDENTIFICADOR_MENU_PRINCIPAL_PADRE = "11";
	public static final String IDENTIFICADOR_MENU_PRINCIPAL_ADMINISTRADOR = "9";
	public static final String IDENTIFICADOR_PAGINA_INICIO = "0";
	public static final String PAGINA_INICIO = "/MenuPublico/bienvenida.jsp";
	public static final String NOMBRE_LISTA_PARAMETROS_MULTIPART = "listaParametrosMultiPart";
	private static Logger log = Logger.getLogger(UtilidadesGenerales.class);
	
	/**
 	 * Redirecciona la petición al fichero indicado como parámetro
 	 * @param request HttpServletRequest
 	 * @param response HttpServletRequest
 	 * @param fichero Ruta completa al fichero al que hay que direccionar la peticion
 	 * @throws Exception 
 	 */
 	public static void redireccionar(HttpServletRequest request, HttpServletResponse response, String fichero) throws ServletException{
 		RequestDispatcher dispatcher = request.getRequestDispatcher(fichero);
	      if (dispatcher != null)
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				log.fatal("ServletException al direccion a " + fichero + ": " + e );
				e.printStackTrace();
			} catch (IOException e) {
				log.fatal("IOException al direccion a " + fichero + ": " + e );
				e.printStackTrace();
			}
		else{
	    	  log.fatal("dispatcher=null");
	    	  throw new ServletException ("Se ha producido un error interno");
		}
 	}
 	
 	/**
 	 * Añade un mensaje de error a la session para que luego aparezca en la página
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param mensajeError Mensaje de Error para añadir en la session
 	 */
 	public static void anadirMensajeError(HttpServletRequest request, HttpServletResponse response,String mensajeError){
 		request.getSession().setAttribute("mensajeError", mensajeError);
 	}
 	
 	/**
 	 * Añade un mensaje de confirmacion de que todo ha ido correctamente
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param mensajeCorrecto Mensaje para informar al usuario
 	 */
 	public static void anadirMensajeCorrecto(HttpServletRequest request, HttpServletResponse response,String mensajeCorrecto){
 		request.getSession().setAttribute("mensajeCorrecto", mensajeCorrecto);
 	}
 	
 	/**
 	 * Añade la opcion volver a la session
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param opcion Opcion a la que se volverá
 	 */
 	public static void anadirVolver(HttpServletRequest request, HttpServletResponse response,String opcion){
 		String opcionVolver2 = request.getSession().getAttribute("opcionVolver") == null ? "" : (String) request.getSession().getAttribute("opcionVolver");
 		request.getSession().setAttribute("opcionVolver2", opcionVolver2);
 		request.getSession().setAttribute("opcionVolver", opcion);
 	}
 	
 	/**
 	 * Añade un parámetro en la sesion
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param nombreParametro Nombre del Parámetro
 	 * @param valor Valor del parámetro
 	 */
 	public static void anadirParametroSession(HttpServletRequest request, HttpServletResponse response,String nombreParametro,Object valor){
 		request.getSession().setAttribute(nombreParametro, valor);
 	}
 	
 	/**
 	 * Añade un parámetro en la petición
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param nombreParametro Nombre del Parámetro
 	 * @param valor Valor del parámetro
 	 */
 	public static void anadirParametroPeticion(HttpServletRequest request, HttpServletResponse response,String nombreParametro,Object valor){
 		request.setAttribute(nombreParametro, valor);
 	}
 	
 	/**
 	 * Recupera un parametro de tipo int ya sea por peticion o por session
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param nombreDelParametro Nombre Del Parametro que queremos recuperar
 	 * @return Valor del parametro
 	 */
 	public static int recuperaNumeroSeleccionado(HttpServletRequest request, HttpServletResponse response,String nombreDelParametro){
 		int retorno = 0;
		String sRetorno = request.getParameter(nombreDelParametro);
		if (sRetorno == null){
			sRetorno = (String) request.getSession().getAttribute(nombreDelParametro);
			if (sRetorno == null){
				log.info("sRetorno = null");
				sRetorno = "0";
			}
		}
		if (!sRetorno.equals("")) retorno = Integer.parseInt(sRetorno);
		request.getSession().setAttribute(nombreDelParametro, sRetorno);
 		return retorno;
 	}
 	
 	/**
 	 * Obtiene un parametro de la petición o null si no existe
 	 * @param request HttpServletRequest
 	 * @param parametro Parámetro a buscar su valor
 	 * @return Valor del parámetro o null
 	 */
 	public static String obtenerParametroPeticion(HttpServletRequest request, String parametro){
 		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
 		if (isMultipart){
 			DiskFileItemFactory factory = new DiskFileItemFactory();
 			
			factory.setSizeThreshold(200000);
 			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List items = upload.parseRequest(request);
				Iterator itr = items.iterator();
				// Se envia la lista de items para que el Comando pueda procesarlos
				UtilidadesWeb.anadirParametroPeticion(request, null, NOMBRE_LISTA_PARAMETROS_MULTIPART, items);
				
				while(itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (StringUtils.equals(item.getFieldName(), parametro)){
						return item.getString();
					}
				}
			} catch (FileUploadException e) {
				log.error(e);
			}
		}
		else{
			if (request.getAttribute(parametro)!= null)
				return (String) request.getAttribute(parametro);
			else
				return (String) request.getParameter(parametro);
		}
		return null;
 	}
 	
 	/**
 	 * Va al menú principal del usuario que esté logeado o a la página de inicio
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @throws ServletException 
 	 */
 	public static void irMenuPrincipal (HttpServletRequest request, HttpServletResponse response) throws ServletException{
 		Object objeto = request.getSession().getAttribute("usuario");
 		if (objeto != null && objeto instanceof UsuarioBean){
 			UsuarioBean usuario = (UsuarioBean) objeto;
 			if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.ADMINISTRADOR.intValue()){
 				request.setAttribute(Propiedades.CAMPO_VISTA, PAGINA_MENU_PRINCIPAL_ADMINISTRADOR);
 			}else{
	 			if (usuario.getIdentificadorPerfil().intValue() == PerfilBean.PADRE.intValue()){
	 				GestionPadre gestionPadre = new GestionPadre();
	 				AdultoBean adulto = new AdultoBean();
	 				adulto.setDni(usuario.getDniUsuario());
	 				ArrayList hijos = new ArrayList();
	 				gestionPadre.bienvenida(adulto,hijos); // Busco los datos personales del padre y los hijos que van a la guarderia
	 				request.getSession().setAttribute("padre", adulto);
	 				request.getSession().setAttribute("hijos", hijos);
	 				request.setAttribute(Propiedades.CAMPO_VISTA, PAGINA_MENU_PRINCIPAL_PADRE);
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
	 					request.setAttribute(Propiedades.CAMPO_VISTA, PAGINA_MENU_PRINCIPAL_PROFESOR);
		 			}
	 			}
 			}
 		}
 		else{
 			request.setAttribute(Propiedades.CAMPO_VISTA, PAGINA_INICIO);
 		}
 		request.setAttribute(Propiedades.CAMPO_TITULO, "Bienvenido a Guarderia Web");
 		//redireccionar(request, response, ficheroRedireccionPorDefectoJsp);
 	}

 	/**
 	 * Obtiene los datos de una opción en el menú de configuración y posteriormente
 	 * los añade a la sesion
 	 * @param request HttpServletRequest
 	 * @param identificador Identificador
 	 */
 	public static String obtenerDatosOpcion(HttpServletRequest request, String identificador){
 		Propiedades propiedades = MenuConfiguracion.getFicheroPropiedades(MenuConfiguracion.FICHERO_XML);
 		request.setAttribute("vista", propiedades.get(identificador, Propiedades.CAMPO_VISTA));
		request.setAttribute("titulo", propiedades.get(identificador, Propiedades.CAMPO_TITULO));
		return propiedades.get(identificador, Propiedades.CAMPO_FICHERO_REDIRECCION);
 	}

 	/**
 	 * Obtenemos el usuario que está logeado o null
 	 * @param request HttpServletRequest
 	 * @return UsuarioBean
 	 */
	public static UsuarioBean obtenerUsuario(HttpServletRequest request) {
		UsuarioBean usuario = (UsuarioBean) request.getSession(true).getAttribute("usuario");
		return usuario;
	}
 	
}
