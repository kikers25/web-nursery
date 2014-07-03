package com.guarderia.mail;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Clase encarga de enviar mail
 * @author Enrique Martín Martín
 *
 */
public class EnvioMailConAutentificacion {
	
	/**
	 * Dirección de correo desde donde se envia el e-mail
	 */
	String from;
	
	/**
	 * Dirección de correo a donde se envia el e-mail
	 */
	String to;
	
	/**
	 * Dirección del servidor de correo
	 */
	String servidorCorreo;
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Contructor por defecto
	 */
	public EnvioMailConAutentificacion(){
		from = "GuarderiaWeb@ono.com";
		servidorCorreo = "smtp.ono.com";
	}
	
	/**
	 * Envio de un mail
	 * @param to Correo al que vamos a enviar el mail
	 * @param asunto Asunto del mail
	 * @param mensaje Contenido del mail
	 * @return true si se ha enviado el mail y false sino se ha enviado
	 */
	public boolean Send(String to, String asunto, String mensaje){
		boolean retorno = false;
		
		log.info("Enviando un mail..." + new java.util.Date());

        Properties prop = new Properties();

        prop.put("mail.smtp.host", servidorCorreo);
        /*Esta línea es la que indica al API que debe autenticarse*/
        prop.put("mail.smtp.auth", "true");

        /*Añadir esta linea si queremos ver una salida detallada del programa*/
        //prop.put("mail.debug", "true");

        try{

            AutentificacionServidorCorreo auth = new AutentificacionServidorCorreo();
            Session session = Session.getInstance(prop , auth );
            //Session session = Session.getInstance(prop );
            Message msg = getMensaje(session, from, to, mensaje,asunto);
            log.info("Enviando ..." );
            Transport.send(msg);
            log.info("Mensaje enviado!");
            retorno = true;

        }

        catch (Exception e)
        {
        	retorno = false;
            ExcepcionEnvioMail.ManageException(e);

        }
        return retorno;

	}
	
	/**
	 * Enviamos un mail a varias personas
	 * @param from Persona que envia el email
	 * @param to Lista de Correos a los que vamos a enviar el mail
	 * @param asunto Asunto del mail
	 * @param mensaje Contenido del mail
	 * @return true si se ha enviado el mail y false sino se ha enviado
	 */
	public boolean Send(String from, ArrayList to, String asunto, String mensaje){
		boolean retorno = false;

        //String host ="127.0.0.1";//Suponiendo que el servidor SMTPsea la propia máquina

		log.info("Enviando un mail..." + new java.util.Date());

        Properties prop = new Properties();

        prop.put("mail.smtp.host", servidorCorreo);
        /*Esta línea es la que indica al API que debe autenticarse*/
        prop.put("mail.smtp.auth", "true");

        /*Añadir esta linea si queremos ver una salida detallada del programa*/
        //prop.put("mail.debug", "true");

        try{

            AutentificacionServidorCorreo auth = new AutentificacionServidorCorreo();
            Session session = Session.getInstance(prop , auth );
            //Session session = Session.getInstance(prop ); Sin autentificacion
            Message msg = getMensajeVariosReceptores(session, from, to, mensaje,asunto);
            log.info("Enviando ..." );
            Transport.send(msg);
            log.info("Mensaje enviado!");
            retorno = true;

        }

        catch (Exception e)
        {
        	retorno = false;
            ExcepcionEnvioMail.ManageException(e);

        }
        return retorno;

	}

	/**
	 * Obtiene el mensaje a enviar al destinatario
	 * @param session Session
	 * @param fromTexto Nombre del persona que envia el mail
	 * @param to Direccion de correo a la que se envia el mail
	 * @param mensaje Mensaje del mail
	 * @param asunto Asunto del mail
	 * @return El mensaje a enviar si se ha formado correctamente o null sino 
	 */
    private MimeMessage getMensaje(Session session, String fromTexto, String to, String mensaje, String asunto)
    {

        try{

            MimeMessage msg = new MimeMessage(session);
            msg.setText(mensaje, "ISO-8859-1", "html");
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setFrom(new InternetAddress(from,fromTexto));
            msg.setSubject(asunto);
            return msg;

        }

        catch (java.io.UnsupportedEncodingException ex)
        {

            ExcepcionEnvioMail.ManageException(ex);
            return null;

        }

        catch (MessagingException ex)
        {

            ExcepcionEnvioMail.ManageException(ex);
            return null;

        } 

    }
    
    /**
     * Obtiene el mensaje a Enviar a varios destinatarios
     * @param session Session
     * @param fromTexto Persona que envia el mail
     * @param to Destinatarios del mail
     * @param mensaje Mensaje del mail
     * @param asunto Asunto del mail
     * @return El mansaje de enviar si todo ha ido correctamente o null sino
     */
    private MimeMessage getMensajeVariosReceptores(Session session, String fromTexto, ArrayList to, String mensaje, String asunto)
    {

        try{

            MimeMessage msg = new MimeMessage(session);
            msg.setText(mensaje, "ISO-8859-1", "html");
            InternetAddress[] direcciones = new InternetAddress[to.size()];
           for (int i=0;i<to.size();i++){
            	String direccion = (String)to.get(i);
            	direcciones[i] = new InternetAddress(direccion);
            }
            msg.addRecipients(Message.RecipientType.TO, direcciones);
            msg.setFrom(new InternetAddress(from,fromTexto+"(Guaderia Web)"));
            msg.setSubject(asunto);
            return msg;

        }

        catch (java.io.UnsupportedEncodingException ex)
        {

            ExcepcionEnvioMail.ManageException(ex);
            return null;

        }

        catch (MessagingException ex)
        {

            ExcepcionEnvioMail.ManageException(ex);
            return null;

        } 

    }
	
}
