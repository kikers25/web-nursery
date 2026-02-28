package com.guarderia.mail;

import javax.mail.PasswordAuthentication;

import com.guarderia.utils.GestorSecretos;

public class AutentificacionServidorCorreo extends javax.mail.Authenticator
{

    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(
                GestorSecretos.obtenerUsuarioMail(),
                GestorSecretos.obtenerContrasenaMail());
    }

}