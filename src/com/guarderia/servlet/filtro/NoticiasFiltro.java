package com.guarderia.servlet.filtro;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.guarderia.negocio.GestionNoticia;

/**
 * Añade las últimas noticias a la sesión
 * @author Enrique Martín Martín
 *
 */
public class NoticiasFiltro implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) servletRequest).getSession();
		if (session.getAttribute("ultimasNoticias")==null){
			GestionNoticia gestionNoticia = new GestionNoticia();
			ArrayList noticias = new ArrayList();
			if (gestionNoticia.buscarNoticiasHabilitadas(noticias)){
				session.setAttribute("ultimasNoticias", noticias);
			}
		}
		if (servletRequest.getCharacterEncoding() == null)
			servletRequest.setCharacterEncoding("UTF-8");
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
