package com.guarderia.negocio;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.guarderia.DAO.AdultoDAO;
import com.guarderia.DAO.AlumnoDAO;
import com.guarderia.DAO.DAOFactoria;
import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.AlumnoBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase que realiza las operacion relativas al padre
 * @author Enrique Martín Martín
 */
public class GestionPadre {
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Contructor por defecto
	 */
	public GestionPadre(){
	}
	
	
	/**
	 * Selecciona un Adulto
	 * @param adulto Bean de tipo Adulto
	 * @return true si todo ha ido correctamente y false sino
	 */
	public boolean seleccionarPadre(AdultoBean adulto){
		boolean resultado = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		resultado = adultoDAO.buscarAdulto(adulto);
		return resultado;
	}
	
	/**
	 * Datos necesarios para el Menu del padre
	 * @param adulto Bean con los datos del padre
	 * @param hijos Lista que corresponde cada bean con los datos de cada hijo que está en la escuela
	 * @return true si se ha encontrado los datos del padre y de los hijos y false sino
	 */
	public boolean bienvenida(AdultoBean adulto,ArrayList hijos){
		boolean resultado = false;
		resultado = seleccionarPadre(adulto);
		if (resultado) log.info("Obtenido datos padre: " + adulto.getNombre() + "- dni-" + adulto.getDni());
		if (resultado ){
			resultado = buscarHijosDelPadre(adulto,hijos);
			for (int i=0;i<hijos.size();i++){ 
				AlumnoBean hijo = (AlumnoBean) hijos.get(i);
				log.info(hijo.getNombre() + " " + hijo.getPrimerApellido() + " " + hijo.getSegundoApellido() + " ");
			}
		}
		return resultado;
	}
	
	/**
	 * Busca los hijos del padre y los guarda en una lista
	 * @param adulto Bean con los datos del padre
	 * @param hijos Lista donde cada bean de tipo alumno correponden a los hijos del padre
	 * @return true si no ha habido ningún error en la busqueda y false sino
	 */
	public boolean buscarHijosDelPadre(AdultoBean adulto,ArrayList hijos){
		boolean resultado = false;
		AlumnoDAO alumnoDAO = DAOFactoria.getDAOFactoria().getAlumnoDAO();
		resultado = alumnoDAO.consultarHijosPadre(adulto, hijos);
		for (int i=0;i<hijos.size();i++){ 
			AlumnoBean hijo = (AlumnoBean) hijos.get(i);
			log.info(hijo.getNombre() + " " + hijo.getPrimerApellido() + " " + hijo.getSegundoApellido() + " ");
		}
		return resultado;
	}
	
	/**
	 * Busca los datos personales de los alumnos y los inserta en la lista
	 * @param alumnos Datos de los alumnos a buscar los padres
	 * @param padres Datos de los padres
	 * @return true si se ha realizado correctamente la búsqueda y false sino
	 */
	public boolean buscarPadresDeAlumnos(ArrayList alumnos, ArrayList padres){
		boolean resultado = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		AdultoBean padre;
		AlumnoBean alumno;
		for (int i=0;i<alumnos.size();i++){
			alumno = (AlumnoBean) alumnos.get(i);
			padre = new AdultoBean();
			if ( (alumno.getDniTutor1() != null) && (!alumno.getDniTutor1().equals("")) ){
				padre.setDni(alumno.getDniTutor1());
				if (adultoDAO.buscarAdulto(padre)){
					padres.add(padre);
					resultado = true;
				}
			}
			else{
				if ( (alumno.getDniTutor2() != null) && (!alumno.getDniTutor2().equals("")) ){
					padre.setDni(alumno.getDniTutor2());
					if (adultoDAO.buscarAdulto(padre)){
						padres.add(padre);
						resultado = true;
					}
				}
				else{ // Inserta un bean aunque no se haya encontrado padre
					padres.add(padre);
				}
			}
		}
		return resultado;
	}
	
	/**
	 * Busca todos los Padres
	 * @param padres Lista con todos los padres
	 * @return true si se ha realizado la consulta correctamente y false sino
	 */	
	public boolean buscarTodosLosPadres(ArrayList padres){
		boolean resultado = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		resultado = adultoDAO.buscarTipoAdulto(AdultoDAO.PADRE, padres);
		return resultado;
	}
	
	/**
	 * Convierte los datos de la peticion html en un objeto de tipo bean adulto
	 * @param request Petiticion de usuario
	 * @return Objeto de tipo adulto o el objeto vacio si ha habido algun error
	 */
	public AdultoBean recuperaDatosPadre(HttpServletRequest request){
		AdultoBean padre = new AdultoBean();
		String aux;
		
		aux = request.getParameter("dni");
		if (aux == null){
			AdultoBean auxPadre = (AdultoBean) request.getSession().getAttribute("padre");
			aux = auxPadre.getDni();
		}
		padre.setDni(aux);
		
		aux = request.getParameter("nombre") == null ? "" : (String)request.getParameter("nombre");
		padre.setNombre(aux);
		
		aux = request.getParameter("primerApellido") == null ? "" : (String)request.getParameter("primerApellido");
		padre.setPrimerApellido(aux);
		
		aux = request.getParameter("segundoApellido") == null ? "" : (String)request.getParameter("segundoApellido");
		padre.setSegundoApellido(aux);
		
		aux = request.getParameter("direccion") == null ? "" : (String)request.getParameter("direccion");
		padre.setDireccion(aux);
		
		aux = request.getParameter("cp") == null ? "" : (String)request.getParameter("cp");
		padre.setCodigoPostal(UtilidadesGenerales.convertirStringEnInteger(aux));
		
		aux = request.getParameter("localidad") == null ? "" : (String)request.getParameter("localidad");
		padre.setLocalidad(aux);
		
		aux = request.getParameter("provincia") == null ? "" : (String)request.getParameter("provincia");
		padre.setProvincia(aux);
		
		aux = request.getParameter("nacionalidad") == null ? "" : (String)request.getParameter("nacionalidad");
		padre.setNacionalidad(aux);
		
		aux = request.getParameter("email") == null ? "" : (String)request.getParameter("email");
		padre.setCorreoElectronico(aux);
		
		aux = request.getParameter("telefono") == null ? "" : (String)request.getParameter("telefono");
		padre.setTelefono(UtilidadesGenerales.convertirStringEnInteger(aux));
		
		aux = request.getParameter("fechaNacimiento") == null ? "" : (String)request.getParameter("fechaNacimiento");
		padre.setFechaNacimiento(UtilidadesGenerales.convertirStringEnGregorianCalendar(aux));
		
		aux = request.getParameter("nivelEstudios") == null ? "" : (String)request.getParameter("nivelEstudios");	
		padre.setNivelEstudios(aux);
		
		aux = request.getParameter("tipoAdulto") == null ? "" : (String)request.getParameter("tipoAdulto");	
		padre.setTipoAdulto(aux);
		
		return padre;
	}
	
	/**
	 * Actualiza los datos personales del padre
	 * @param padre Datos personales del padre
	 * @return true si se ha actualizado correctamente y false sino
	 */
	public boolean actualizarDatosPadre(AdultoBean padre){
		boolean retorno = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		padre.setTipoAdulto(AdultoDAO.PADRE);
		retorno = adultoDAO.actualizarAdulto(padre);
		return retorno;
	}

	/**
	 * Añade un padre
	 * @param padre Datos del padre
	 * @return true si se ha creado el padre y false sino
	 */
	public boolean anadir(AdultoBean padre) {
		boolean retorno = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		padre.setTipoAdulto(AdultoDAO.PADRE);
		retorno = adultoDAO.anadirAdulto(padre,AdultoDAO.PADRE);
		return retorno;
	}
	
	/**
	 * Busca todas las personas y las inserta en la lista
	 * @param personas Lista con los datos de todas las personas
	 */
	public boolean buscarTodasLasPersonas(ArrayList personas) {
		boolean resultado = false;
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		resultado = adultoDAO.buscarTodos(personas);
		return resultado;
	}	
	
	
}
