package com.guarderia.DAO;

import com.guarderia.context.ContextProvider;
import com.guarderia.context.ObjectContext;

/**
 * Clase abstracta que define el tipo de Factoria utilizada para el acceso a los datos
 * @author kike
 *
 */
public abstract class DAOFactoria {

	public static final int ORACLE = 1;
	
	/**
	 * Obtiene una factoria a partir de un número
	 * @param nFactoria
	 * @return
	 */
	public static DAOFactoria getDAOFactoria(int nFactoria) {
		ObjectContext ctx = ContextProvider.getCurrentContext ();
		DAOFactoria instance = (DAOFactoria) ctx
				.getObject (DAOFactoria.class,""+nFactoria);
		if (instance == null) {
			switch (nFactoria) {
	    	case ORACLE: 
	    		instance = new OracleFactoria();
	    		ctx.setObject (DAOFactoria.class,""+nFactoria, instance);
	    		break;
	    	default: 
	    		return null;
			}
		}
		return instance;
	}
	
	/**
	 * Obtiene una factoria a partir de un número
	 * @param nFactoria
	 * @return
	 */
	public static DAOFactoria getDAOFactoria() {
		return DAOFactoria.getDAOFactoria(DAOFactoria.ORACLE);
	}
	
	/**
	 * Obtiene el DAO correspondiente a Adultos. Se implementa en las clases
	 * hijas.
	 * @return AdultoDAO
	 */
	public abstract AdultoDAO getAdultoDAO();
	
	/**
	 * Obtiene el DAO correspondiente a Alumnos. Se implementa en las clases
	 * hijas.
	 * @return AlumnoDAO
	 */
	public abstract AlumnoDAO getAlumnoDAO();
	
	/**
	 * Obtiene el DAO correspondiente a Clases. Se implementa en las clases
	 * hijas.
	 * @return ClaseDAO
	 */
	public abstract ClaseDAO getClaseDAO();
	
	/**
	 * Obtiene el DAO correspondiente al Comedor. Se implementa en las clases
	 * hijas.
	 * @return ComedorDAO
	 */
	public abstract ComedorDAO getComedorDAO();
	
	/**
	 * Obtiene el DAO correspondiente a Noticias. Se implementa en las clases
	 * hijas.
	 * @return NoticiaDAO
	 */
	public abstract NoticiaDAO getNoticiaDAO();
	
	/**
	 * Obtiene el DAO correspondiente a Perfiles. Se implementa en las clases
	 * hijas.
	 * @return PerfilDAO
	 */
	public abstract PerfilDAO getPerfilDAO();
	
	/**
	 * Obtiene el DAO correspondiente a Usuarios. Se implementa en las clases
	 * hijas.
	 * @return UsuarioDAO
	 */
	public abstract UsuarioDAO getUsuarioDAO();
	
	/**
	 * Obtiene el DAO correspondiente a ElementoCMS. Se implementa en las clases
	 * hijas.
	 * @return ElementoCMSDAO
	 */
	public abstract ElementoCMSDAO getElementoCMSDAO();
	
	/**
	 * Obtiene el DAO correspondiente a GrupoCMS. Se implementa en las clases
	 * hijas.
	 * @return GrupoCMSDAO
	 */
	public abstract GrupoCMSDAO getGrupoCMSDAO();
	
	/**
	 * Obtiene el DAO correspondiente a CriterioDAO. Se implementa en las clases
	 * hijas.
	 * @return CriterioDAO
	 */
	public abstract CriterioDAO getCriterioDAO();
	
	/**
	 * Obtiene el DAO correspondiente a EvaluacionDAO. Se implementa en las clases
	 * hijas.
	 * @return EvaluacionDAO
	 */
	public abstract EvaluacionDAO getEvaluacionDAO();
	
}
