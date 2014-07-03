package com.guarderia.negocio;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.guarderia.DAO.ComedorDAO;
import com.guarderia.DAO.DAOFactoria;
import com.guarderia.bean.MenuBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase que realiza las operacion relativas al comedor
 * @author Enrique Martín Martín
 *
 */
public class GestionComedor {
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Contructor por defecto
	 */
	public GestionComedor(){
	}

		/**
	 * Crea un menú en un día determinado
	 * @param menu Datos de la comida
	 * @return true si se ha creado el menu
	 */
	public boolean crearMenu(MenuBean menu) {
		boolean retorno = false;
		ComedorDAO comedorDAO = DAOFactoria.getDAOFactoria().getComedorDAO();
		// Verifico que no exista comida para esa fecha
		MenuBean aux = new MenuBean();
		aux.setFecha(menu.getFecha());
		if (comedorDAO.consultaEnFecha(aux) == false){
			// Recupero El mayor identificador
			int max = comedorDAO.consultaMayorIdentificador();
			max++;
			menu.setIdentificador(new Integer(max));
			// Añado el menú en base de datos
			retorno = comedorDAO.crearMenu(menu);
		}
		return retorno;
	}
	
	/**
	 * Modifica los datos del menú
	 * @param menu Datos del menu
	 * @return true si se ha modificador y false sino
	 */
	public boolean modificarMenu(MenuBean menu) {
		boolean retorno = false;
		boolean bModificar = false;
		ComedorDAO comedorDAO = DAOFactoria.getDAOFactoria().getComedorDAO();
		// Verifico que no exista comida para esa fecha y si existe
		// que sea la misma que la que intento modificar
		MenuBean aux = new MenuBean();
		aux.setFecha(menu.getFecha());
		if (comedorDAO.consultaEnFecha(aux) == false){
			bModificar = true;
		}
		else{
			if (aux.getIdentificador().intValue() == menu.getIdentificador().intValue()){
				bModificar = true;
			}
		}
		if (bModificar == true){
			// Modifico el menú en base de datos
			retorno = comedorDAO.modificarMenu(menu);
		}
		return retorno;
	}
	
	/**
	 * Elimina un menú
	 * @param menu Datos del menú a eliminar
	 * @return true si se ha eliminado y false sino
	 */
	public boolean eliminarMenu(MenuBean menu) {
		boolean retorno = false;
		ComedorDAO comedorDAO = DAOFactoria.getDAOFactoria().getComedorDAO();
		// Verifico que no exista comida para esa fecha y si existe
		// que sea la misma que la que intento modificar
		MenuBean aux = new MenuBean();
		aux.setFecha(menu.getFecha());
		if (comedorDAO.consultaEnFecha(aux) == true){
			retorno = comedorDAO.eliminarMenu(menu);
		}
		else{
			log.info("No existe ningún menú en la fecha: " + UtilidadesGenerales.convertirGregorianCalendarEnString(menu.getFecha()));
		}
		return retorno;
	}

	/**
	 * Consulta todos los días de comedor de un mes
	 * @param comedor Lista los menús existentes en el mes
	 * @param numeroMes Numero de mes a buscar
	 * @param numeroAno Numero de año a buscar
	 */
	public void consultarMesComedor(ArrayList comedor, int numeroMes, int numeroAno) {
		ComedorDAO comedorDAO = DAOFactoria.getDAOFactoria().getComedorDAO();
		GregorianCalendar calendarioInicio = new GregorianCalendar();
		GregorianCalendar calendarioFin = new GregorianCalendar();
		calendarioInicio.set(GregorianCalendar.YEAR, numeroAno);
		calendarioFin.set(GregorianCalendar.YEAR, numeroAno);
		calendarioInicio.set(GregorianCalendar.MONTH, numeroMes);
		calendarioFin.set(GregorianCalendar.MONTH, numeroMes);
		calendarioInicio.set(GregorianCalendar.DATE, 1);
		calendarioFin.set(GregorianCalendar.DATE,calendarioFin.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		/*System.out.println("Fecha Inicio = " +  calendarioInicio.get(Calendar.DATE)
 			 				+ "-" + (calendarioInicio.get(Calendar.MONTH)+1)
 							+ "-" + calendarioInicio.get(Calendar.YEAR));
		System.out.println("Fecha Fin = " +  calendarioFin.get(Calendar.DATE)
	 				+ "-" + (calendarioFin.get(Calendar.MONTH)+1)
					+ "-" + calendarioFin.get(Calendar.YEAR));*/
		comedorDAO.consultaEntreFechas(comedor,calendarioInicio,calendarioFin);
	}
		
	/**
	 * Convierte los datos de la peticion html en un objeto de tipo bean comida
	 * @param request Petiticion de usuario
	 * @return Objeto de tipo comida o el objeto vacio si ha habido algun error
	 */
	public MenuBean recuperaDatosComida(HttpServletRequest request){
		MenuBean comida = new MenuBean();
		MenuBean auxComedor = (MenuBean) request.getSession().getAttribute("menu");
		String aux;
		
		aux = request.getParameter("identificador");
		if ( (aux == null) && (auxComedor != null) ){
			comida.setIdentificador(auxComedor.getIdentificador());
		}
		else{
			comida.setIdentificador(UtilidadesGenerales.convertirStringEnInteger(aux));
		}
		
		aux = request.getParameter("primerPlato") == null ? "" : (String)request.getParameter("primerPlato");
		comida.setPrimerPlato(UtilidadesGenerales.obtenerString(aux));
		
		aux = request.getParameter("segundoPlato") == null ? "" : (String)request.getParameter("segundoPlato");
		comida.setSegundoPlato(UtilidadesGenerales.obtenerString(aux));
		
		aux = request.getParameter("postre") == null ? "" : (String)request.getParameter("postre");
		comida.setPostre(UtilidadesGenerales.obtenerString(aux));
		
		aux = request.getParameter("fecha") == null ? "" : (String)request.getParameter("fecha");
		comida.setFecha(UtilidadesGenerales.convertirStringEnGregorianCalendar(aux));
		
		return comida;
	}

}
