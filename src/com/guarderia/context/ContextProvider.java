package com.guarderia.context;

/**
 * Punto de acceso al contexto actual. El tipo de contexto que se haya establecido dependerá de la arquitectura sobre la que se ejecute la aplicación.
 * @author  kike
 */
public class ContextProvider {
	
	/**
	 * @uml.property  name="s_currentContext"
	 * @uml.associationEnd  
	 */
	private static ObjectContext s_currentContext = new Context();
    
    /**
     * Devuelve el contexto actual en el que se ejecuta la aplicación.
     */
    public static ObjectContext getCurrentContext()
    {
        return s_currentContext;
    }

    /**
     * Establece el tipo de contexto en el que se ejecuta la aplicación.
     */
    public static void setCurrentContext (ObjectContext context)
    {
        s_currentContext = context;
    }
}
