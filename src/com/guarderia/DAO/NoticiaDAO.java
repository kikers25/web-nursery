package com.guarderia.DAO;

import java.util.ArrayList;

import com.guarderia.bean.NoticiaBean;

public interface NoticiaDAO extends DAO {

	/**
	 * Busca todas las noticias habilitadas y ordenadas por fecha
	 * @param noticias Lista donde se añadiran las noticias
	 * @return false si se ha producido algún error y true sino
	 */
	public abstract boolean buscarNoticiasHabilitadas(ArrayList noticias);

	/**
	 * Busca todas las noticias ordenadas por fecha
	 * @param noticias Lista donde se añadiran las noticias
	 * @return false si se ha producido algún error y true sino
	 */
	public abstract boolean buscarNoticias(ArrayList noticias);

	/**
	 * Busca cual es el numero de identificador mayor en la tabla noticia
	 * @return el mayor numero de identificador
	 */
	public abstract int buscarMaximoIdentificador();

	/**
	 * Modifica los datos de una Noticia en Base de datos
	 * @param noticia Datos de la noticia
	 * @return true si se ha realizado la modificacion en base de datos y false sino
	 */
	public abstract boolean modificarNoticia(NoticiaBean noticia);

	/**
	 * Crear una noticia
	 * @param noticia Bean con los datos de la noticia
	 * @return false si ha habido algún error y false sino
	 */
	public abstract boolean crearNoticia(NoticiaBean noticia);

}