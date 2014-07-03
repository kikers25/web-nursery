package com.guarderia.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Context implements ObjectContext {

	private Map objetos = Collections.synchronizedMap (new HashMap());
	
	@Override
	public void clear() {
		objetos.clear();
	}

	@Override
	public Object getObject(Class clase, String name) {
		return objetos.get (keyName (clase, name));
	}

	@Override
	public Object getSingleObject(Class clase) {
		return objetos.get (keyName (clase, null));
	}

	@Override
	public void invalidateAllCache() {
		Iterator it = this.objetos.values().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Invalidatable) {
                ((Invalidatable) o).invalidateCache();
            }
        }

	}

	@Override
	public void setObject(Class clase, String name, Object objeto) {
		objetos.put (keyName (clase, name), objeto);
	}

	@Override
	public void setSingleObject(Class clase, Object objeto) {
		objetos.put (keyName (clase, null), objeto);
	}

	@Override
	public void unsetObject(Class clase, String name) {
		objetos.remove (keyName (clase, name));
	}

	@Override
	public void unsetSingleObject(Class clase) {
		objetos.remove (keyName (clase, null));
	}
	
	private String keyName (Class clase, String name)
    {
        StringBuffer buf = new StringBuffer();
        if (clase != null)  buf.append (clase.getName());
        buf.append ("::");
        if (name != null)  buf.append (name);
        return buf.toString();
    }	

}
