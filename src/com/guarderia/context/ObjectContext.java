package com.guarderia.context;

/**
 * Interfaz principal que proporciona el acceso al contexto.
 * 
 * @author kike
 *
 */
public interface ObjectContext {
	
	/**
     * Devuelve el objeto singleton de la clase dada.
     */
    public Object getSingleObject (Class clase);
    
    /**
     * Establece el objeto singleton de la clase dada.
     */
    public void setSingleObject (Class clase, Object objeto);
    
    /**
     * Elimina el objeto singleton de la clase dada.
     */
    public void unsetSingleObject (Class clase);

    /**
     * Devuelve el objeto de la clase dada registrado con el nombre dado.
     */
    public Object getObject (Class clase, String name);

    /**
     * Registra el objeto de la clase dada con el nombre dado.
     */
    public void setObject (Class clase, String name, Object objeto);

    /**
     * Elimina del registro el objeto de la clase dada con el nombre dado.
     */
    public void unsetObject (Class clase, String name);
    
    /**
     * Elimina todos los objetos del contexto.
     */
    public void clear();
    
    /**
     * Invalida la caché de todos los objetos con interfaz Invalidatable
     * que haya en el contexto.
     */
    public void invalidateAllCache();

}
