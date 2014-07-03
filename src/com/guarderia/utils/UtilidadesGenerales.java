package com.guarderia.utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Clase que aglutina todos las funcionalidades genericas de las clases: log4j
 */
public class UtilidadesGenerales {
	
	public static final String CAMPO_COUNT_RESULSET = "resultado";
	
 	/**
	 * Cierra el resultset
	 * @param rs Resultset a cerrar
	 */
 	public static void cerrarResulset(ResultSet rs){
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 	
 	/**
 	 * Convierte una clase de tipo Date en una de tipo GregorianCalendar
 	 * @param d Objeto de tipo Date
 	 * @return Objeto de tipo GregorianCalendar si ha ido todo correcto o null
 	 */
 	public static GregorianCalendar convertirDateEnGregorianCalendar(Date d){
 		GregorianCalendar calendar = null;
 		if (d != null){
	 		calendar=new GregorianCalendar();
			calendar.setTime(d);
 		}
 		return calendar;
 	}
 	
 	/**
 	 * Obtiene un GregorianCalendar de la fecha actual
 	 * @return GregorianCalendar
 	 */
 	public static GregorianCalendar obtenerGregorianCalendarFechaActual(){
 		java.util.Date now = new java.util.Date(); //fecha actual
 		GregorianCalendar gregorian = new GregorianCalendar();
 		gregorian.setTime(now);
 		return gregorian;
 	}
 	
 	/**
 	 * Convierte un String en un Integer
 	 * @param s Cadena a convertir en entero
 	 * @return Objeto de tipo Integer si todo ha ido correctamente o null
 	 */
 	public static Integer convertirStringEnInteger(String s){
 		Integer entero = null;
 		if ( (s != null) && (!s.equals("")) ){
 			try{
 				entero = new Integer(s);
 			} catch (NumberFormatException e) {
 				e.printStackTrace();
 				entero = null;
 			}
 		}
 		return entero;
 	}
 	
 	/**
 	 * Obtiene una cadena de caracteres con el curso actual de la guarderia
 	 * @return 
 	 */
 	public static String obtenerCursoActual(){
 		String cadena = "";
 		GregorianCalendar fechaActual = new GregorianCalendar();
 		int anoActual = fechaActual.get(Calendar.YEAR);
 		if (fechaActual.get(Calendar.MONTH) <= 7){ // A partir de Agosto se toma como otro curso
 			anoActual--;
 		}
 		cadena = anoActual + "-" + (anoActual + 1);
 		System.out.println("curso = " + cadena);
 		return cadena;
 	}
	
 	/**
 	 * Obtiene una cadena no nula de una cadena
 	 * @param cadena Cadena de caracteres que puede tener como valor null
 	 * @return Cadena vacia si la cadena de origen vale null o la cadena de origen
 	 */
 	public static String obtenerString(String cadena){
 		String retorno = "";
 		if ( (cadena != null) && (!cadena.equalsIgnoreCase("null")) ) retorno = cadena;
 		return retorno;
 	}
 	
 	/**
 	 * Obtiene una timestamp de un Timestamp o de la hora del sistema
 	 * @param time Timestamp
 	 * @return Timestamp
 	 */
 	public static Timestamp obtenerTimeStamp(Timestamp  time){
 		Timestamp retorno = new Timestamp ( System.currentTimeMillis (  )  ) ;
 		if (time != null){
 			retorno = time;
 		}
 		return retorno;
 	}
 	
 	/**
 	 * Convierte una cadena en boolean
 	 * @param cadena cadena de caracteres a convertir en boolean
 	 * @return true si cadena tiene como valor Y y false en cualquier otro caso
 	 */
 	public static Boolean convertirStringEnBoolean(String cadena){
 		Boolean retorno = Boolean.FALSE;
 		if (cadena.equals("Y")){
			retorno = Boolean.TRUE;
		}
 		return retorno;
 	}
 	
 	/**
 	 * Convierte una clase de tipo boolean a String
 	 * @param booleano booleano a convertir en string
 	 * @return true si cadena tiene como valor Y y false en cualquier otro caso
 	 */
 	public static String convertirBooleanEnString(Boolean booleano){
 		String retorno = "N";
 		if (booleano.booleanValue() == true){
			retorno = "Y";
		}
 		return retorno;
 	}
 	
 	/**
 	 * Convierte una clase de tipo String en una de tipo GregorianCalendar
 	 * @param cadena Objeto de tipo String
 	 * @return Objeto de tipo GregorianCalendar si ha ido todo correcto o null
 	 */
 	public static GregorianCalendar convertirStringEnGregorianCalendar(String cadena){
 		GregorianCalendar calendar = null;
 		if (cadena != null){
 			String combinedPattern = "dd-MM-yyyy";
 			SimpleDateFormat datetimeParser = new SimpleDateFormat(combinedPattern);
	 		calendar=new GregorianCalendar();
			try {
				calendar.setTime(datetimeParser.parse(cadena));
			} catch (ParseException e) {
				e.printStackTrace();
			}
 		}
 		return calendar;
 	}
 	
 	/**
 	 * Convierte una clase de tipo String en una de tipo GregorianCalendar teniendo el cuenta
 	 * el número de caracteres se utiliza un patrón u otro
 	 * @param cadena Objeto de tipo String
 	 * @return Objeto de tipo GregorianCalendar si ha ido todo correcto o null
 	 */
 	public static GregorianCalendar convertirStringEnGregorianCalendar2(String cadena){
 		GregorianCalendar calendar = null;
 		if (cadena != null){
 			String combinedPattern = null;
 			if (cadena.length() == 8){
 				combinedPattern = "dd/MM/yy";
 			}
 			else{
 				combinedPattern = "dd-MM-yyyy";
 			}
 			SimpleDateFormat datetimeParser = new SimpleDateFormat(combinedPattern);
	 		calendar=new GregorianCalendar();
			try {
				calendar.setTime(datetimeParser.parse(cadena));
			} catch (ParseException e) {
				e.printStackTrace();
			}
 		}
 		return calendar;
 	}
 	
 	/**
 	 * Convierte una clase GregorianCalendar en un String dd-mm-aaaa
 	 * @param calendar El GregorianCalendar a convertir en string
 	 * @return La fecha en formato dd-mm-aaaa
 	 */
 	public static String convertirGregorianCalendarEnString(GregorianCalendar calendar){
 		String retorno = null;
 		if (calendar != null){
 			if (calendar.get(Calendar.DATE) < 10){
 				retorno = "0" + calendar.get(Calendar.DATE);
 			}
 			else{
 				retorno = "" + calendar.get(Calendar.DATE);
 			}
 			if ((calendar.get(Calendar.MONTH)+1) < 10){
 				retorno += "-0" + (calendar.get(Calendar.MONTH)+1);
 			}
 			else{
 				retorno += "-" + (calendar.get(Calendar.MONTH)+1);
 			}
 			retorno += "-" + calendar.get(Calendar.YEAR);
 		}
 		return retorno;
 	}
 	
 	/**
 	 * Convierte un GregorianCalendar en un String dd de mes, año
 	 * @param calendar
 	 * @return La Cadena con la fecha formateada
 	 */
 	public static String convertirGregorianCalendarEnString2(GregorianCalendar calendar){
 		String[] meses=new String[13];
 		meses[1]="Enero";
 		meses[2]="Febrero";
 		meses[3]="Marzo";
 		meses[4]="Abril";
 		meses[5]="Mayo";
 		meses[6]="Junio";
 		meses[7]="Julio";
 		meses[8]="Agosto";
 		meses[9]="Septiembre";
 		meses[10]="Octubre";
 		meses[11]="Noviembre";
 		meses[12]="Diciembre";
 		String retorno = null;
 		if (calendar != null){
 			retorno = "" + calendar.get(Calendar.DATE);
 			retorno += " de " + meses[calendar.get(Calendar.MONTH)+1];
 			retorno += ", " + calendar.get(Calendar.YEAR);
 		}
 		return retorno;
 	}
 	
 	/**
 	 * Convierte un GregorianCalendar en un String dia de mes de año
 	 * @param calendar
 	 * @return La Cadena con la fecha formateada
 	 */
 	public static String convertirGregorianCalendarEnString3(GregorianCalendar calendar){
 		String[] meses=new String[13];
 		meses[1]="Enero";
 		meses[2]="Febrero";
 		meses[3]="Marzo";
 		meses[4]="Abril";
 		meses[5]="Mayo";
 		meses[6]="Junio";
 		meses[7]="Julio";
 		meses[8]="Agosto";
 		meses[9]="Septiembre";
 		meses[10]="Octubre";
 		meses[11]="Noviembre";
 		meses[12]="Diciembre";
 		String retorno = null;
 		if (calendar != null){
 			retorno = "" + calendar.get(Calendar.DATE);
 			retorno += " de " + meses[calendar.get(Calendar.MONTH)+1];
 			retorno += " de " + calendar.get(Calendar.YEAR);
 		}
 		return retorno;
 	}
 	
 	/**
 	 * Crea un Timestamp con la fecha actual
 	 * @return TimeStamp de la fecha actual
 	 */
 	public static Timestamp crearTimestampFechaActual(){
 		Timestamp time;
 		java.util.Date utilDate = new java.util.Date(); //fecha actual
 		long lnMilisegundos = utilDate.getTime();
 		time = new java.sql.Timestamp(lnMilisegundos);
 		return time;
 	}
 	
 	/**
 	 * Obtiene el nombre del perfil a través del numero
 	 * @param nPerfil numero identificador del perfil
 	 * @return Cadena de caracteres con el nombre del perfil
 	 */
 	public static String nombrePerfil(Integer nPerfil){
 		int i = nPerfil.intValue();
 		String perfil = "";
 		switch (i){
	 		case 1:
	 			perfil = "Administrador";
	 			break;
	 		case 2:
	 			perfil = "Profesor";
	 			break;
	 		case 3:
	 			perfil = "Padre";
	 			break;
	 		default:
	 			perfil = "Padre";
 		}
 		return perfil;
 	}
 	
 	/**
 	 * Obtiene la edad a partir de una fecha
 	 * @param fechaNacimiento Fecha de nacimiento
 	 * @return Años en forma de entero
 	 */
 	public static int calcularEdad(GregorianCalendar fechaNacimiento) {
 	     Calendar now = new GregorianCalendar();
 	     int edad = now.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);
 	     if((fechaNacimiento.get(Calendar.MONTH) > now.get(Calendar.MONTH))
 	       || (fechaNacimiento.get(Calendar.MONTH) == now.get(Calendar.MONTH)
 	       && fechaNacimiento.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH)))
 	     {
 	    	edad--;
 	     }
 	     return edad;
 	   }
 	

	public static int obtenerNumeroTrimestre() {
		Calendar now = new GregorianCalendar();
		int iMes = now.get(Calendar.MONTH);
		// Si estamos en Enero, Febreo o Marzo
		if (iMes <= 2) {
			// Estamos en la segunda evaluación
			return 2;
		}
		// Si estamos en Abril, Mayo, Junio, Julio o Agosto
		if (iMes <= 6) {
			// Estamos en la tercera evaluación
			return 3;
		}
		// Si estamos en otro mes
		// Estamos en la primera evaluación
		return 1;
	}

 	public static void main (String[] args){
 		GregorianCalendar cumple = new GregorianCalendar();
 		cumple.set(2008, 01, 01);
 		System.out.println("Edad de un niño nacido el 1 Enero de 2008: " + UtilidadesGenerales.calcularEdad(cumple));
 		System.out.println("Evaluación " + UtilidadesGenerales.obtenerNumeroTrimestre());
 	}

 	/**
 	 * Hay más de cero en el campo count del Resulset
 	 * @param rs Resulset
 	 * @return true si count(*) > 0 y false en cualquier otro caso
 	 */
	public static boolean isResulSetConResultado(ResultSet rs) {
		try {
			if (rs.next() && rs != null){
				if (rs.getInt(UtilidadesGenerales.CAMPO_COUNT_RESULSET) > 0)
					return true;
			}
		} catch (SQLException e) {}
		return false;
	}
	
	/**
	 * A partir del trimestre en el que estemos devuelve una cadena de texto
	 * @param trimestre
	 * @return Cadena Vacio o el trimestre correspondiente
	 */
	public static String textoTrimestre(int trimestre){
		String texto = "";
		if ( trimestre >= 1 && trimestre <= 3 ){
			if (trimestre == 1){
				texto ="Primer ";
			}
			if (trimestre == 2){
				texto ="Segundo ";
			}
			if (trimestre == 3){
				texto ="Tercer ";
			}
			texto += "trimestre";
		}
		return texto;
	}
}
