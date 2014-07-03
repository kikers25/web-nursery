package com.guarderia.mail;

import javax.mail.PasswordAuthentication;

public class AutentificacionServidorCorreo extends javax.mail.Authenticator
{
	
	private static final String USERNAME_DEFECTO = "GuarderiaWeb@ono.com";

    private static final String PASSWORD_DEFECTO = "guarderia";

    public PasswordAuthentication getPasswordAuthentication()
    {

        return new PasswordAuthentication(USERNAME_DEFECTO, PASSWORD_DEFECTO);

    }

}