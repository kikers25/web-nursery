package com.guarderia.servlet;

import java.util.Stack;

import com.guarderia.context.ContextProvider;
import com.guarderia.context.ObjectContext;
import com.guarderia.servlet.comando.Comando;

public class Navegacion {

	private Stack pila = null;
	
	/**
	 * Contructor privado para que no se pueda instanciar
	 */
	private Navegacion(){
		pila = new Stack<Comando> ();
	}
	
	/**
	 * Recupera la única instancia de Navegacion.
	 * @return Instancia de Navegacion
	 */
	public static synchronized Navegacion getInstance ()
	{
		ObjectContext ctx = ContextProvider.getCurrentContext ();
		Navegacion instance = (Navegacion) ctx
				.getSingleObject (Navegacion.class);
		if (instance == null) {
			instance = new Navegacion ();
			ctx.setSingleObject (Navegacion.class, instance);
		}
		return instance;
	}
	
	public void anadirComando (Comando comando){
		pila.push(comando);
	}
	
	public Comando getComando (){
		if (pila.size() > 1)
			return (Comando) pila.pop();
		else
			if (pila.size() == 1)
				return (Comando) pila.peek();

		return null;
	}
	
}
