package com.guarderia.servlet.comando;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.guarderia.DAO.OracleElementoCMSDAO;
import com.guarderia.bean.ElementoCMS;
import com.guarderia.bean.UsuarioBean;
import com.guarderia.utils.UtilidadesGenerales;
import com.guarderia.utils.UtilidadesWeb;

public class ComandoCMS extends Comando {

	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	private static final String LISTAR_ELEMENTOS = "cms/listar_elementos";
	private static final String EDITAR_PAGINA = "cms/editar_pagina";
	
	@Override
	public String procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if (StringUtils.equals(opcion, LISTAR_ELEMENTOS)){
 			OracleElementoCMSDAO elementoCMSDAO = new OracleElementoCMSDAO();
 			List lista = elementoCMSDAO.buscarElementosPorDirectorio("MenuPublico");
 			UtilidadesWeb.anadirParametroPeticion(request, response, "elementos_cms", lista);
 		}
		if (StringUtils.equals(opcion, EDITAR_PAGINA)){
 			String accion = UtilidadesWeb.obtenerParametroPeticion(request, "accion");
			if (!StringUtils.equals(accion, "modificar")){
 				String id = UtilidadesWeb.obtenerParametroPeticion(request, "id");
 				OracleElementoCMSDAO elementoCMSDAO = new OracleElementoCMSDAO();
 				ElementoCMS elemento = (ElementoCMS) elementoCMSDAO.buscarPorId(id);
 				File fichero = new File (request.getSession().getServletContext().getRealPath("/MenuPublico/"+elemento.getNombre_fichero()));
 				String textoContenido = null;
 				if (fichero != null){
 					textoContenido = FileUtils.readFileToString (fichero,"ISO-8859-1");
 				}
 				else{
 					throw new ServletException ("No existe el texto contenido de " + elemento.getIdentificador());
 				}
 				UtilidadesWeb.anadirParametroPeticion(request, response, "texto_contenido", textoContenido);
 				UtilidadesWeb.anadirParametroPeticion(request, response, "elemento_cms", elemento);
 			}
 			else{
 				modificar_elemento_cms(request, response);
 			}
 		}
		
		return ficheroRedireccion;
 	}

	private void modificar_elemento_cms(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		ElementoCMS elemento = obtener_beanCMS_de_peticion(request);
		UsuarioBean usuario = UtilidadesWeb.obtenerUsuario(request);
		// 1. Actualizamos en base de datos
		elemento.setUsuario_modificacion(usuario.getUsuario());
		elemento.setFecha_modificacion(UtilidadesGenerales.obtenerGregorianCalendarFechaActual());
		OracleElementoCMSDAO elementoCMSDAO = new OracleElementoCMSDAO();
		elementoCMSDAO.modificiar(elemento);
		// 2. Actualizamos el fichero
		String textoContenido = UtilidadesWeb.obtenerParametroPeticion(request, "elementocms_contenido_fichero");
		File fichero = new File (request.getSession().getServletContext().getRealPath("/MenuPublico/"+elemento.getNombre_fichero()));
		try {
			FileUtils.writeStringToFile(fichero, textoContenido);
		} catch (IOException e) {
			log.error("No se ha podido guarder el fichero " + fichero.getAbsolutePath() + 
					 ": " + e);
			throw new ServletException ("No se ha podido guardar el fichero " + fichero.getAbsolutePath());
		}
		// 3. Añadimos mensaje que todo ha ido correcto y la request para la ventana de mdificar
		UtilidadesWeb.anadirMensajeCorrecto(request, response, "Se ha modificado correctamente el elemento " + elemento.getNombre());
		UtilidadesWeb.anadirParametroPeticion(request, response, "texto_contenido", textoContenido);
		UtilidadesWeb.anadirParametroPeticion(request, response, "elemento_cms", elemento);
	}

	private ElementoCMS obtener_beanCMS_de_peticion(HttpServletRequest request) {
		ElementoCMS elemento = new ElementoCMS();
		String prefijo = "elementocms_";

		elemento.setIdentificador(UtilidadesWeb.obtenerParametroPeticion(
				request, prefijo + "identificador"));
		elemento.setNombre(UtilidadesWeb.obtenerParametroPeticion(request,
				prefijo + "nombre"));
		elemento.setNombre_fichero(UtilidadesWeb.obtenerParametroPeticion(
				request, prefijo + "nombre_fichero"));
		elemento.setDescripcion(UtilidadesWeb.obtenerParametroPeticion(request,
				prefijo + "descripcion"));
		elemento.setTipo_elemento(UtilidadesGenerales
				.convertirStringEnInteger(UtilidadesWeb
						.obtenerParametroPeticion(request, prefijo
								+ "tipo_elemento")));
		elemento.setGrupo(UtilidadesWeb.obtenerParametroPeticion(request,
				prefijo + "grupo"));
		elemento.setUsuario_alta(UtilidadesWeb.obtenerParametroPeticion(
				request, prefijo + "usuario_alta"));
		elemento.setUsuario_modificacion(UtilidadesWeb
				.obtenerParametroPeticion(request, prefijo
						+ "usuario_modificacion"));
		elemento.setFecha_alta(UtilidadesGenerales
				.convertirStringEnGregorianCalendar(UtilidadesWeb
						.obtenerParametroPeticion(request, prefijo
								+ "fecha_alta")));
		elemento.setFecha_modificacion(UtilidadesGenerales
				.convertirStringEnGregorianCalendar(UtilidadesWeb
						.obtenerParametroPeticion(request, prefijo
								+ "fecha_modificacion")));
		return elemento;
	}
	
}
