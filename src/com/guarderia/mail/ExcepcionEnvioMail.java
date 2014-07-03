package com.guarderia.mail;

public class ExcepcionEnvioMail {

    public static void ManageException (Exception e)
    {

        System.out.println ("Se ha producido una exception");

        System.out.println (e.getMessage());

        e.printStackTrace(System.out);

    }
}
