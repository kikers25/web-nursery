package com.guarderia.negocio;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.guarderia.DAO.DAOFactoria;
import com.guarderia.DAO.NoticiaDAO;
import com.guarderia.bean.NoticiaBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase que realiza las operacion relativas a la noticia
 * @author Enrique Martín Martín
 *
 */
public class GestionNoticia {
	
	/**
	 * Contructor por defecto
	 */
	public GestionNoticia(){
	}
	
	/**
	 * Crea una noticia añadiendole primero el identificador, y la foto y la fecha de creación y de modificacion
	 * @param noticia Bean con los datos de la noticia
	 * @return
	 */
	public boolean crearNoticia(NoticiaBean noticia){
		boolean retorno = false;
		NoticiaDAO noticiaDAO = DAOFactoria.getDAOFactoria().getNoticiaDAO();
		// Añado el identificador
		int id = noticiaDAO.buscarMaximoIdentificador() + 1 ;
		noticia.setIdentificador(new Integer(id));
		// Añado el timestamp a fecha de creación y de modificación
		Timestamp timeFechaActual;
		timeFechaActual = UtilidadesGenerales.crearTimestampFechaActual();
		noticia.setFechaCreacion(timeFechaActual);
		noticia.setFechaModificacion(timeFechaActual);
		// Creo la noticia
		retorno = noticiaDAO.crearNoticia(noticia);
		return retorno;
	}
	
	/**
	 * Busca las ultimas tres noticias publicadas y habilitadas
	 * @param ultimasNoticias Listado con las tres ultimas noticias
	 * @return true si ha ido todo correctamente y false sino
	 */
	public boolean buscarUltimasNoticias(ArrayList ultimasNoticias){
		boolean retorno = false;
		NoticiaDAO noticiaDAO = DAOFactoria.getDAOFactoria().getNoticiaDAO();
		retorno = noticiaDAO.buscarNoticiasHabilitadas(ultimasNoticias);
		if (retorno){
			// Elimino todos las noticias excepto las 3 primeras
			while (ultimasNoticias.size() > 3){
				ultimasNoticias.remove(3);
			}
		}
		return retorno;
	}
	
	/**
	 * Busca todas las noticias
	 * @param noticias Listado con las noticias
	 * @return true si ha ido todo correctamente y false sino
	 */
	public boolean buscarTodasNoticias(ArrayList noticias){
		boolean retorno = false;
		NoticiaDAO noticiaDAO = DAOFactoria.getDAOFactoria().getNoticiaDAO();
		retorno = noticiaDAO.buscarNoticias(noticias);
		return retorno;
	}
	
	
	/**
	 * Busca todas las noticias habilitadas
	 * @param noticias Listado con las noticias
	 * @return true si ha ido todo correctamente y false sino
	 */
	public boolean buscarNoticiasHabilitadas(ArrayList noticias) {
		NoticiaDAO noticiaDAO = DAOFactoria.getDAOFactoria().getNoticiaDAO();
		boolean retorno = false;
		retorno = noticiaDAO.buscarNoticiasHabilitadas(noticias);
		return retorno;
	}
	
	/**
	 * Modifica los datos de una noticia
	 * @param noticia Datos de la noticia a actualizar
	 * @return true si se han efectuado los cambios y false sino
	 */
	public boolean modificarNoticia(NoticiaBean noticia) {
		boolean retorno = false;
		NoticiaDAO noticiaDAO = DAOFactoria.getDAOFactoria().getNoticiaDAO();
		// Añado el timestamp a fecha de modificación
		noticia.setFechaModificacion(UtilidadesGenerales.crearTimestampFechaActual());
		retorno = noticiaDAO.modificarNoticia(noticia);
		return retorno;
	}
	
	/**
	 * 
	 * Convierte los datos de la peticion html en un objeto de tipo bean noticia
	 * @param request Petiticion de usuario
	 * @return Objeto de tipo noticia o el objeto vacio si ha habido algun error
	 */
	public NoticiaBean recuperaDatosNoticia(HttpServletRequest request){
		NoticiaBean noticia = new NoticiaBean();
		String aux;
		
		aux = request.getParameter("titulo") == null ? "" : (String)request.getParameter("titulo");
		noticia.setTitulo(aux);
		
		aux = request.getParameter("subtitulo") == null ? "" : (String)request.getParameter("subtitulo");
		noticia.setSubtitulo(aux);
		
		aux = request.getParameter("resumen") == null ? "" : (String)request.getParameter("resumen");
		noticia.setResumen(aux);
		
		aux = request.getParameter("fecha") == null ? "" : (String)request.getParameter("fecha");
		noticia.setFecha(UtilidadesGenerales.convertirStringEnGregorianCalendar(aux));
		
		aux = request.getParameter("habilitado") == null ? "" : (String)request.getParameter("habilitado");
		noticia.setHabilitado(UtilidadesGenerales.convertirStringEnBoolean(aux));
		
		aux = request.getParameter("descripcion") == null ? "" : (String)request.getParameter("descripcion");
		noticia.setDescripcion(aux);
		
		return noticia;
	}
	
}
