package com.guarderia.servlet.comando;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Clase que aglutina todos las funcionalidades genericas de los servlets
 */
 public class Comando {
	 
	/**
	 * opcion elegida en la web
	 * @uml.property  name="opcion"
	 */
	protected String opcion = null;
	
	/**
	 * @return
	 * @uml.property  name="opcion"
	 */
	public String getOpcion() {
		return opcion;
	}

	/**
	 * @param opcion
	 * @uml.property  name="opcion"
	 */
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

	/**
	 * Otra accion a realizar
	 * @uml.property  name="masOpcion"
	 */
	protected String masOpcion;
	
	/**
	 * Fichero a donde se va a redirigir la salida
	 * @uml.property  name="ficheroRedireccion"
	 */
	protected String ficheroRedireccion;
	
	 /**
	  * Jsp a redireccionar por defecto 
	  */
	 protected String ficheroRedireccionPorDefectoJsp;
	 
	 /**
	  * Atributo para poder escribir trazas
	  */
	 private Logger log;
		
	/**
	 * Titulo que aparecerá en el fichero dependiendo la opción que se ha elegido del menú
	 * @uml.property  name="titulo"
	 */
	protected String titulo;

	/**
	 * Número de opción para volver atrás
	 */
	protected static final String VOLVER = "99";
	
	/**
	 * Número de opción para volver atrás dos veces
	 */
	protected static final String VOLVER2 = "98";
    
	 /**
	 * SerialVersoin UID
	 */
	 private static final long serialVersionUID = 1L;
	 
	 /**
	 * @return
	 * @uml.property  name="masOpcion"
	 */
	public String getMasOpcion() {
		return masOpcion;
	}

	/**
	 * @param masOpcion
	 * @uml.property  name="masOpcion"
	 */
	public void setMasOpcion(String masOpcion) {
		this.masOpcion = masOpcion;
	}

	/**
	 * @return
	 * @uml.property  name="ficheroRedireccion"
	 */
	public String getFicheroRedireccion() {
		return ficheroRedireccion;
	}

	/**
	 * @param ficheroRedireccion
	 * @uml.property  name="ficheroRedireccion"
	 */
	public void setFicheroRedireccion(String ficheroRedireccion) {
		this.ficheroRedireccion = ficheroRedireccion;
	}

	/**
	 * @return
	 * @uml.property  name="titulo"
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo
	 * @uml.property  name="titulo"
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	
	/**
	 * Constructor por defecto que inicializa los atributos
	 */
	public Comando() {
		super();
		log = Logger.getLogger(this.getClass());
	}   	
 	
 	/**
 	 * Redirecciona la petición al fichero indicado como parámetro
 	 * @param request HttpServletRequest
 	 * @param response HttpServletRequest
 	 * @param fichero Ruta completa al fichero al que hay que direccionar la peticion
 	 * @throws Exception 
 	 */
 	protected void redireccionar(HttpServletRequest request, HttpServletResponse response, String pagina) throws ServletException{
 		RequestDispatcher dispatcher = request.getRequestDispatcher(pagina);
	      if (dispatcher != null)
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				log.fatal("ServletException al direccion a " + pagina + ": " + e );
				e.printStackTrace();
			} catch (IOException e) {
				log.fatal("IOException al direccion a " + pagina + ": " + e );
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
 	public void anadirMensajeError(HttpServletRequest request, HttpServletResponse response,String mensajeError){
 		request.getSession().setAttribute("mensajeError", mensajeError);
 	}
 	
 	/**
 	 * Añade un mensaje de confirmacion de que todo ha ido correctamente
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param mensajeCorrecto Mensaje para informar al usuario
 	 */
 	public void anadirMensajeCorrecto(HttpServletRequest request, HttpServletResponse response,String mensajeCorrecto){
 		request.getSession().setAttribute("mensajeCorrecto", mensajeCorrecto);
 	}
 	
 	/**
 	 * Añade la opcion volver a la session
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param opcion Opcion a la que se volverá
 	 */
 	public void anadirVolver(HttpServletRequest request, HttpServletResponse response,String opcion){
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
 	public void anadirParametroSession(HttpServletRequest request, HttpServletResponse response,String nombreParametro,Object valor){
 		request.getSession().setAttribute(nombreParametro, valor);
 	}
 	
 	/**
 	 * Añade un parámetro en la sesion
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param nombreParametro Nombre del Parámetro
 	 * @param valor Valor del parámetro
 	 */
 	public void anadirParametroPeticion(HttpServletRequest request, HttpServletResponse response,String nombreParametro,Object valor){
 		request.setAttribute(nombreParametro, valor);
 	}
 	
 	/**
 	 * Recupera un parametro de tipo int ya sea por peticion o por session
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @param nombreDelParametro Nombre Del Parametro que queremos recuperar
 	 * @return Valor del parametro
 	 */
 	public int recuperaNumeroSeleccionado(HttpServletRequest request, HttpServletResponse response,String nombreDelParametro){
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
 	 * Procesa la petición y devuelve la página a la que se redireccionará
 	 * @param request HttpServletRequest
 	 * @param response HttpServletResponse
 	 * @return
 	 * @throws ServletException
 	 * @throws IOException
 	 */
 	public String procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
 		return ficheroRedireccion;
 	}
 	
}