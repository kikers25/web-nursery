package com.guarderia.error;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.guarderia.servlet.menuConfiguracion.Propiedades;
import com.guarderia.utils.UtilidadesWeb;

public class Error extends HttpServlet {

	/**
	 * @serialField
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response){
		generaPaginaError(request, response);
	}
	
	protected void doPost (HttpServletRequest request, HttpServletResponse response){
		generaPaginaError(request, response);
	}
	
	protected void generaPaginaError(HttpServletRequest request, HttpServletResponse response){
		Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
		String requestURI = (String) request.getAttribute("javax.servlet.error.request_uri");
		try {
			request.setCharacterEncoding("UTF-8");
			UtilidadesWeb.anadirParametroPeticion(request, response, "origenExcepcion", requestURI);
			UtilidadesWeb.anadirParametroPeticion(request, response, "trazaExcepcion", exception);
			request.setAttribute("opcion", "0");
			UtilidadesWeb.anadirParametroPeticion(request, response, Propiedades.CAMPO_IDENTIFICADOR, "0");
			UtilidadesWeb.anadirParametroPeticion(request, response, Propiedades.CAMPO_TITULO, "ERROR");
			UtilidadesWeb.anadirParametroPeticion(request, response, Propiedades.CAMPO_VISTA, "error.jsp");
			
			UtilidadesWeb.redireccionar(request, response, "index.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

}
