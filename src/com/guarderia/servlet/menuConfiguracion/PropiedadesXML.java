package com.guarderia.servlet.menuConfiguracion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.guarderia.error.XMLException;


public class PropiedadesXML implements Propiedades {

	/**
	 * Trazas
	 */
	private Logger log = Logger.getLogger(PropiedadesXML.class);
	
	/**
	 * Fichero xml de tipo InputSource
	 */
	private InputSource inputSource = null;
	
	/**
	 * HasMap de acciones
	 */
	private HashMap acciones = null;
	
	/**
	 * HasMap de comandos
	 */
	private HashMap comandos = null;
	
	/**
	 * Nombre del identificador de cada accion
	 */
	private static final String IDENTIFICADOR_ACCION = "id";
	
	/**
	 * Si se está buscando este campo se busca su correspondencia en comandos
	 */
	private static final String CAMPO_UNION = "fichero_redireccion";
	
	/**
	 * Nombre del identificador de cada accion
	 */
	private static final String IDENTIFICADOR_COMANDO = "id";
	
	/**
	 * Patrones para buscar los elementos acciones dentro del xml
	 */
	private static final String PATRON_INICIO_ACCION = "/menuPrincipal/acciones/accion[@id='";
	private static final String PATRON_FIN_ACCION = "']/";
	private static final String PATRON_ACCIONES = "/menuPrincipal/acciones//accion";
	
	/**
	 * Patrones para buscar los elementos acciones dentro del xml
	 */
	private static final String PATRON_INICIO_COMANDO = "/menuPrincipal/comandos/comando[@id='";
	private static final String PATRON_FIN_COMANDO = "']/";
	private static final String PATRON_COMANDOS = "/menuPrincipal/comandos//comando";
	
	
	public PropiedadesXML(){
	}
	
	/**
	 * Lee todo el contenido de un fichero
	 * @param file Dirección al fichero
	 */
	public void leerXML (String file){
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	    documentBuilderFactory.setNamespaceAware(true);
	    DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(new File(file));
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    transformer.transform(new DOMSource(document), new StreamResult(System.out));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Convierte un XML en un HashMap con otro HashMap dentro
	 * @param file Dirección al fichero
	 */
	public void convertir_XML_a_HashMap (){
		if (inputSource == null){
			throw new XMLException("No se ha especificado fichero");
		}
		XPathFactory  factory=XPathFactory.newInstance();
		XPath xPath=factory.newXPath();
		try {
			// 1. Recogemos las acciones
			NodeList nodesAcciones = (NodeList) xPath.evaluate(PATRON_ACCIONES,
        			inputSource, XPathConstants.NODESET);
			NodeList nodesComandos = (NodeList) xPath.evaluate(PATRON_COMANDOS,
					inputSource, XPathConstants.NODESET);
			cargarAcciones(nodesAcciones);
			// 2. Recogemos los comandos
			cargarComandos(nodesComandos);
		} catch (XPathExpressionException e) {
			log.error(e);
			throw new XMLException(e);
		}
	}

	/**
	 * Obtiene las acciones y las convierte en un HashMap
	 * @param xPath
	 * @throws XPathExpressionException
	 */
	private void cargarAcciones(NodeList nodes) throws XPathExpressionException {
		acciones = new HashMap<String, HashMap>();
		for (int i = 0; i < nodes.getLength(); i++) { 
			String identificador = obtenerAtributo(nodes.item(i),IDENTIFICADOR_ACCION);
			NodeList nodes2 = nodes.item(i).getChildNodes();
			HashMap accion = new HashMap<String, String>();
			for (int j = 0; j < nodes2.getLength(); j++) {
				Node node = nodes2.item(j);
				if (node.getNodeType() == Node.ELEMENT_NODE){
					accion.put(node.getNodeName(), node.getTextContent());
					log.debug("nodo(" + i + "," + j + "): " + node.getNodeName() + "=" + node.getTextContent());
				}
			}
			if (StringUtils.isNotEmpty(identificador) && accion.size() != 0){
				log.debug("Se añade accion al HashMap " + IDENTIFICADOR_ACCION + "=" + identificador);
				acciones.put(identificador, accion);
			}
		}
	}
	
	/**
	 * Obtiene los comandos y las convierte en un HashMap
	 * @param xPath
	 * @throws XPathExpressionException
	 */
	private void cargarComandos(NodeList nodes) throws XPathExpressionException {
		comandos = new HashMap<String, HashMap>();
		for (int i = 0; i < nodes.getLength(); i++) { 
			String identificador = obtenerAtributo(nodes.item(i),IDENTIFICADOR_COMANDO);
			NodeList nodes2 = nodes.item(i).getChildNodes();
			HashMap comando = new HashMap<String, String>();
			for (int j = 0; j < nodes2.getLength(); j++) {
				Node node = nodes2.item(j);
				if (node.getNodeType() == Node.ELEMENT_NODE){
					comando.put(node.getNodeName(), node.getTextContent());
					log.debug("nodo(" + i + "," + j + "): " + node.getNodeName() + "=" + node.getTextContent());
				}
			}
			if (StringUtils.isNotEmpty(identificador) && comando.size() != 0){
				log.debug("Se añade comando al HashMap " + IDENTIFICADOR_COMANDO + "=" + identificador);
				comandos.put(identificador, comando);
			}
		}
	}

	private String obtenerAtributo(Node node, String campo) {
		NamedNodeMap atributos = node.getAttributes();
		if (atributos != null){
			for (int i = 0; i < atributos.getLength(); i++) {
				Node auxNode = atributos.item(i);
				if (auxNode.getNodeType() == Node.ATTRIBUTE_NODE){
					if (StringUtils.equals(auxNode.getNodeName(), campo)){
						return auxNode.getNodeValue();
					}
				}
				else{
					log.debug("!!!!!!No se que es");
				}
			}
		}
		else{
			log.debug("No tiene atributos");
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.servlet.menuConfiguracion.Propiedades#setFile(java.lang.String)
	 */
	public void setFile(String file) throws FileNotFoundException{
		this.inputSource=new InputSource(file);
		convertir_XML_a_HashMap ();
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.servlet.menuConfiguracion.Propiedades#setFile(java.io.InputStream)
	 */
	public void setFile(InputStream file) throws FileNotFoundException{
		this.inputSource=new InputSource(file);
		convertir_XML_a_HashMap ();
	}
	
	/**
	 * Lee un valor de una acción si hay caché por la caché sino directamente del fichero
	 * @param identificador
	 * @param campo
	 */
	public String leerParametroAccion(String identificador, String parametro)throws XMLException{
		// Si está en caché
		if (acciones != null){
			//Lo obtenemos de la caché
			HashMap accion = (HashMap) acciones.get(identificador);
			if (accion != null){
				return (String) accion.get(parametro);
			}
			else{
				return null;
			}
		}
		// Si no directamente del fichero xml
		else{
			if (inputSource == null){
				throw new XMLException("No se ha especificado fichero");
			}
			XPathFactory  factory=XPathFactory.newInstance();
			XPath xPath=factory.newXPath();
			String expresion = PATRON_INICIO_ACCION + identificador + PATRON_FIN_ACCION + parametro;
			try {
				return xPath.evaluate(expresion , inputSource);
			} catch (XPathExpressionException e) {
				log.error(e);
				throw new XMLException(e);
			}
		}
	}
	
	/**
	 * Lee un valor de una acción si hay caché por la caché sino directamente del fichero
	 * @param identificador
	 * @param campo
	 */
	public String leerParametroComando(String identificador, String parametro)throws XMLException{
		// Si está en caché
		if (comandos != null){
			//Lo obtenemos de la caché
			HashMap comando = (HashMap) comandos.get(identificador);
			if (comando != null){
				return (String) comando.get(parametro);
			}
			else{
				return null;
			}
		}
		// Si no directamente del fichero xml
		else{
			if (inputSource == null){
				throw new XMLException("No se ha especificado fichero");
			}
			XPathFactory  factory=XPathFactory.newInstance();
			XPath xPath=factory.newXPath();
			String expresion = PATRON_INICIO_COMANDO + identificador + PATRON_FIN_COMANDO + parametro;
			try {
				return xPath.evaluate(expresion , inputSource);
			} catch (XPathExpressionException e) {
				log.error(e);
				throw new XMLException(e);
			}
		}
	}
	
	@Override
	public String get(String identificador, String campo) {
		String retorno = leerParametroAccion(identificador, campo);
		if (StringUtils.equals(campo, CAMPO_UNION) 
				&& retorno != null
				&& leerParametroComando(retorno, "class") != null){
			return leerParametroComando(retorno, "class");
		}
		return retorno;
	}
	
	public static void main (String[] args){
		BasicConfigurator.configure();
		Logger log = Logger.getLogger(PropiedadesXML.class);
		PropiedadesXML xml = new PropiedadesXML();
		try {
			xml.setFile("WebContent/WEB-INF/menu.xml");
			log.info("De forma local");
			log.info(xml.get("10", "titulo"));
			log.info(xml.get("9", "vista"));
			log.info(xml.get("10", "fichero_redireccion"));
			log.info(xml.get("500", "fichero_redireccion"));
		} catch (FileNotFoundException e) {
			log.error("no encuentra el fichero " + e);
			e.printStackTrace();
		} catch (XMLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}

}
