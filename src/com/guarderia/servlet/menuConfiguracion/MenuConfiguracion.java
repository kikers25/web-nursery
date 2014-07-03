package com.guarderia.servlet.menuConfiguracion;

import com.guarderia.context.ContextProvider;
import com.guarderia.context.ObjectContext;

public class MenuConfiguracion {
	
	public static final int FICHERO_PROPIEDADES = 1;
	public static final int FICHERO_XML = 2;
	
	/**
	 * Constructor por defecto privado
	 */
	private MenuConfiguracion(){}
	
	/**
	 * Obtiene el fichero de propiedades
	 * @param tipo_fichero Tipo de fichero de propiedades
	 * @return Propiedades
	 */
	public static synchronized Propiedades getFicheroPropiedades( int tipo_fichero ){
		ObjectContext ctx = ContextProvider.getCurrentContext ();
		Propiedades instance = (Propiedades) ctx
				.getObject (Propiedades.class,""+tipo_fichero);
		if (instance == null) {
			switch (tipo_fichero) {
	    	case FICHERO_PROPIEDADES: 
	    		instance = new Propiedadesprops();
	    		ctx.setObject (Propiedades.class,""+tipo_fichero, instance);
	    		break;
	    	case FICHERO_XML:
	    		instance = new PropiedadesXML();
	    		ctx.setObject (Propiedades.class,""+tipo_fichero, instance);
	    		break;
	    	default: 
	    		return null;
			}
		}
		return instance;
	}
	
	/**
	 * Obtiene el fichero de propiedades por defecto
	 * @param tipo_fichero Tipo de fichero de propiedades
	 * @return Propiedades
	 */
	public static synchronized Propiedades getFicheroPropiedades( ){
		ObjectContext ctx = ContextProvider.getCurrentContext ();
		Propiedades instance = (Propiedades) ctx
				.getObject (Propiedades.class,""+FICHERO_XML);
		if (instance == null) {
			instance = new PropiedadesXML();
	    	ctx.setObject (Propiedades.class,""+FICHERO_XML, instance);
		}
		return instance;
	}
}
