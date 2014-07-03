package com.guarderia.servlet.menuConfiguracion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Clase utilizada para abrir un fichero de propiedades y poder obtener los valores de los campos
 */
public class Propiedadesprops implements Propiedades {

	/**
	 * Atributo donde se va a guardar el fichero de propiedades que se va abrir
	 */
	private Properties prop;
	
	/**
	 * Nombre del Fichero que se ha cargado
	 */
	private String fichero;
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Atributo para saber si se ha cargado el fichero de configuracion
	 */
	private boolean abierto;
	
	/**
	 * Contructor por defecto: Inicializa los atributos
	 */
	public Propiedadesprops(){
		super();
		prop = new Properties();
		fichero = "";
		abierto=false;
	}
	
	/**
	 * Abre el Fichero de configuracion
	 * @param fichero  Ruta del fichero que se va a cargar
	 * @return  true si se ha abierto el fichero y false sino se ha abierto
	 * @uml.property  name="fichero"
	 */
	public boolean setFichero(String fichero){
		this.fichero = fichero;
		log.info(Charset.defaultCharset());
		abierto=false;
		try {
			FileInputStream in = new FileInputStream(this.fichero);
			prop.load(in);
			in.close();
			abierto = true;
		} catch (FileNotFoundException e) {
			log.fatal("Porperties no encontrado: " + e);
		} catch (IOException e) {
			log.fatal("Error: " + e);
		}
		return abierto;
	}
	
	public boolean setFichero(InputStream fichero){
		abierto=false;
		log.info(Charset.defaultCharset());
		try {
			//prop.load(new BufferedReader(new InputStreamReader(fichero,CODIFICACION_POR_DEFECTO)));
			prop.load(fichero);
			abierto = true;
		} catch (FileNotFoundException e) {
			log.fatal("Porperties no encontrado: " + e);
		} catch (IOException e) {
			log.fatal("Error: " + e);
		}
		return abierto;
	}
	
	/**
	 * Devuelve el valor del campo del fichero de configuracion
	 * @param campo Nombre del campo que se quiere obtener el valor
	 * @return Valor del campo
	 */
	public String get(String campo){
		String retorno = null;
		if (abierto == true){
			if ( prop == null){
				abierto= false;
			}
			else{
				retorno = prop.getProperty(campo);
			}
		}
		return retorno;
	}

	@Override
	public String get(String identificador, String campo) {
		return get(campo + "_" + identificador );
	}

	@Override
	public void setFile(String file) throws FileNotFoundException {
		setFichero(file);
		
	}

	@Override
	public void setFile(InputStream file) throws FileNotFoundException {
		setFichero(file);
	}
}
