package com.guarderia.servlet.comando;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guarderia.bean.AdultoBean;
import com.guarderia.mail.Mail;
import com.guarderia.utils.UtilidadesWeb;


/**
 * Implementación de la clase Servlet para el Servlet: ServletMail
 * @web.servlet  name="ServletMail"  display-name="ServletMail" 
 * @web.servlet-mapping  url-pattern="/ServletMail"
 */
 public class ComandoMail extends com.guarderia.servlet.comando.Comando {
	 
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo para enviar el correo al email de Guarderia Web
	 * @uml.property  name="mail"
	 * @uml.associationEnd  
	 */
	private Mail mail;

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ComandoMail() {
		mail = new Mail();
	}
	
	public String procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean resultado = true;
		if (opcion.equals("12")){ // Envio Contactar
			String nombre = request.getParameter("nombre") != null ? request.getParameter("nombre") : "";
			String primerApellido = request.getParameter("primerApellido") != null ? request.getParameter("primerApellido") : "";
			String segundoApellido = request.getParameter("segundoApellido") != null ? request.getParameter("segundoApellido") : "";
			String localidad = request.getParameter("localidad") != null ? request.getParameter("localidad") : "";
			String provincia = request.getParameter("provincia") != null ? request.getParameter("provincia") : "";
			String telefono = request.getParameter("telefono") != null ? request.getParameter("telefono") : "";
			String email = request.getParameter("email") != null ? request.getParameter("email") : "";
			String asunto = request.getParameter("asunto") != null ? request.getParameter("asunto") : "";
			String comentario = request.getParameter("comentario") != null ? request.getParameter("comentario") : "";
			resultado = mail.envioMailContactar(nombre, primerApellido, segundoApellido, localidad, provincia, telefono, email, asunto, comentario);
			if (resultado == true){
				anadirMensajeCorrecto(request, response, "Se ha enviado correctamente el email");
			}
			else{
				anadirMensajeError(request, response, "No se ha podido enviar el email. Disculpe las molestias");
			}
		}
		if (opcion.equals("18")){ // Envio mail
			ArrayList to = request.getSession().getAttribute("to") == null ? new ArrayList() : (ArrayList)request.getSession().getAttribute("to");
			AdultoBean adulto = request.getSession().getAttribute("from") == null ? new AdultoBean() : (AdultoBean)request.getSession().getAttribute("from");
			String from = "";
			from += adulto.getNombre() == null ? "" : adulto.getNombre()+" ";
			from += adulto.getPrimerApellido() == null ? "" : adulto.getPrimerApellido()+" ";
			from += adulto.getSegundoApellido() == null ? "" : adulto.getSegundoApellido();
			String asunto = request.getParameter("asunto") == null ? "" : request.getParameter("asunto");
			String contenido = request.getParameter("contenido") == null ? "" : request.getParameter("contenido");
			resultado = mail.envioGenerico(from, to, asunto, contenido);
			//masOpcion = request.getSession().getAttribute("opcionVolver") == null ? "0" : (String) request.getSession().getAttribute("opcionVolver");
			//ficheroRedireccion = UtilidadesWeb.ficheroRedireccionPorDefectoServlet;
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		if (resultado == true) anadirMensajeCorrecto(request, response, "Se ha enviado correctamente el email");
		else anadirMensajeError(request, response, "No se ha podido enviar el email. Disculpe las molestias");
		return ficheroRedireccion;
	}
	
}