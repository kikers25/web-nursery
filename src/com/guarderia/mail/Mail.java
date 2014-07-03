package com.guarderia.mail;

import java.util.ArrayList;

/**
 * Clase encargada de formar el mensaje a enviar por mail y enviarlo
 * @author  Enrique Martín Martín
 */
public class Mail {
	
	/**
	 * Atributo para enviar el correo al email de Guarderia Web
	 * @uml.property  name="envioMail"
	 * @uml.associationEnd  
	 */
	private EnvioMailConAutentificacion envioMail;
	
	/**
	 * Constructor por defecto
	 */
	public Mail(){
		envioMail = new EnvioMailConAutentificacion();
	}
	
	/**
	 * Envia un mail al correo de la Guarderia de tipo Contacta
	 * @param nombre Nombre de la persona que quiere contactar con la Guarderia
	 * @param primerApellido Primer apellido de la persona que quiere contactar con la Guarderia
	 * @param segundoApellido Segundo apellido de la persona que quiere contactar con la Guarderia
	 * @param localidad Localidad de la persona que quiere contactar con la Guarderia
	 * @param provincia Provincia de la persona que quiere contactar con la Guarderia
	 * @param telefono Teléfono para contactar con la persona
	 * @param eMail Email de la persona
	 * @param asunto Asunto del envio del mail
	 * @param comentario Contenido del mail
	 * @return true si se ha enviado el mail correctamente y false sino
	 */
	public boolean envioMailContactar(String nombre, String primerApellido,String segundoApellido, String localidad,
									String provincia, String telefono, String eMail, String asunto, String comentario){
		boolean retorno = false;
		String mensaje = new String();
		mensaje = "<b><i><u>Formulario Contactar</u></i></b><br><br><br>";
		mensaje += "<b>Nombre: </b>" + nombre + "<br>";
		mensaje += "<b>Primer Apellido: </b>" + primerApellido + "<br>";
		mensaje += "<b>Segundo Apellido: </b>" + segundoApellido + "<br>";
		mensaje += "<b>Localidad: </b>" + localidad + "<br>";
		mensaje += "<b>Provincia: </b>" + comentario + "<br>";
		mensaje += "<b>Teléfono: </b>" + telefono + "<br>";
		mensaje += "<b>E-Mail: </b>" + eMail + "<br>";
		mensaje += "<b>Asunto del Correo: </b>" + asunto + "<br>";
		mensaje += "<b>Comentario: </b>" + comentario + "<br>";
		retorno = envioMail.Send("guarderiaweb@ono.com", "CONTACTAR", mensaje);
		return retorno;
	}
	
	/**
	 * Envia un mail de forma Generica a través del correo de la Guarderia
	 * @param from Persona que envia el mail
	 * @param to Destinatarios del mail
	 * @param asunto Asunto del mail
	 * @param contenido Contenido del mail
	 * @return true si se ha enviado el mail correctamente y false sino
	 */
	public boolean envioGenerico(String from, ArrayList to, String asunto, String contenido){
		boolean retorno = false;
		retorno = envioMail.Send(from,to, asunto, contenido);
		return retorno;
	}

}
