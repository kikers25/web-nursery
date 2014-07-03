package com.guarderia.servlet.comando;

import com.guarderia.bean.ClaseBean;
import com.guarderia.negocio.GestionClase;
import com.guarderia.utils.UtilidadesWeb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class ServletClase
 */
public class ComandoClase extends Comando {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo utilizada para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
       
    /**
     * @see Comando#ServletGenerico()
     */
    public ComandoClase() {
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarPeticion(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarPeticion(request, response);
	}
	
	/**
	 * Crea una clase
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException 
	 */
	private void anadirClase(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		GestionClase gestionclase = new GestionClase();
		ClaseBean clase = gestionclase.recuperarDatosClase(request);
		gestionclase.anadirClase(clase);
		anadirMensajeCorrecto(request, response, "Se ha creado la nueva clase");
		//anadirOtraAccion("16");
		UtilidadesWeb.irMenuPrincipal(request, response);
	}
	
	public String procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("Opcion Menu Clase " + opcion);
		if (StringUtils.equals(opcion, "24")){
			GestionClase gestionClase = new GestionClase();
			List listaIntervaloEdades = gestionClase.buscarTodosLosIntervalosEdad();
			anadirParametroPeticion(request, response, "listaEdades", listaIntervaloEdades);
		}
		if (opcion.equals("24a")){ // Crear Clase
			anadirClase(request, response);
		}
		if (opcion.equals("25")){ // Mostrar Clases
			ArrayList clases = new ArrayList();
			GestionClase gestionclase = new GestionClase();
			if (gestionclase.buscarClases(clases) == true){
				request.getSession().setAttribute("clases", clases);
				anadirVolver(request, response, opcion);
			}
		}
		return ficheroRedireccion;
	}

}
