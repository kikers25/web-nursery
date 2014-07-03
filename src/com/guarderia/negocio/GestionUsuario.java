
package com.guarderia.negocio;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.guarderia.DAO.AdultoDAO;
import com.guarderia.DAO.DAOFactoria;
import com.guarderia.DAO.PerfilDAO;
import com.guarderia.DAO.UsuarioDAO;
import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.PerfilBean;
import com.guarderia.bean.UsuarioBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase que realiza las operacion relativas al usuario
 * @author Enrique Martín Martín
 */
public class GestionUsuario {
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Contructor por defecto
	 */
	public GestionUsuario(){
	}
	
	/**
	 * Valida que el usuario y la contraseña son correctos y posteriormente obtiene el perfil
	 * @param usuario Bean con los datos del usuario
	 * @param perfil Bean con los datos del perfil relacionado con el usuario
	 * @return true si la validacion de usuario ha ido correctamente y el perfil del usuario y false sino
	 */
	public boolean validarUsuario(UsuarioBean usuario, PerfilBean perfil){
		boolean resultado = false;
		UsuarioDAO usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
		PerfilDAO perfilDAO = DAOFactoria.getDAOFactoria().getPerfilDAO();
		usuario.setUsuario(usuario.getUsuario().toLowerCase());
		resultado = usuarioDAO.validarUsuario(usuario);
		if (resultado == true){
			log.info("Obtengo usuario");
			perfil.setIdentificador(usuario.getIdentificadorPerfil());
			resultado = perfilDAO.consultaPerfil(perfil);
			
		}
		return resultado;
	}

	/**
	 * Verifica que no exista el usuario indicado
	 * @param usuario Datos del usuario
	 * @return true si no existe el usuario y false en caso contrario
	 */
	public boolean verificarExistenciaUsuario(UsuarioBean usuario) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
		resultado = usuarioDAO.consultaUsuario(usuario);
		resultado = !resultado;
		return resultado;
	}
	
	/**
	 * Crea un usuario con los datos de la persona y el usuario
	 * @param persona Datos de la persona
	 * @param usuarioSeleccionado Datos del usuario
	 */
	public void crearUsuario(AdultoBean persona, UsuarioBean usuario) {
		UsuarioDAO usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
		usuario.setDniUsuario(persona.getDni());
		usuarioDAO.crearUsuario(usuario);
	}
	
	/**
	 * Obtiene todos los usuarios y los datos de la personas vinculadas a los usuarios
	 * @param usuarios Lista de los usuarios
	 * @param personas Lista de las personas
	 */
	public void obtenerUsuarios(ArrayList usuarios, ArrayList personas) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
		AdultoDAO adultoDAO = DAOFactoria.getDAOFactoria().getAdultoDAO();
		resultado = usuarioDAO.consultaUsuarios(usuarios);
		if (resultado == true){
			for (int i=0;i<usuarios.size();i++){
				AdultoBean persona = new AdultoBean();
				UsuarioBean usuario = (UsuarioBean) usuarios.get(i);
				persona.setDni(usuario.getDniUsuario());
				adultoDAO.buscarAdulto(persona);
				personas.add(persona);
			}
		}
	}
	
	/**
	 * Actualiza los datos del usuario
	 * @param usuario Datos del usuario
	 */
	public void actualizarUsuario(UsuarioBean usuario) {
		UsuarioDAO usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
		usuarioDAO.actualizarUsuario(usuario);
	}
	
	/**
	 * Elimina el usuario
	 * @param usuario Datos del usuario a eliminar
	 */
	public void eliminarUsuario(UsuarioBean usuario) {
		UsuarioDAO usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
		usuarioDAO.eliminarUsuario(usuario);
		
	}

	/**
	 * Convierte los datos de la peticion html en un objeto de tipo bean usuario
	 * @param request Petiticion de usuario
	 * @return Objeto de tipo usuario o el objeto vacio si ha habido algun error
	 */
	public UsuarioBean recuperaDatosUsuario(HttpServletRequest request){
		UsuarioBean usuario = new UsuarioBean();
		UsuarioBean auxUsuario = (UsuarioBean) request.getSession().getAttribute("usuarioSeleccionado");
		String aux;
		
		aux = request.getParameter("usuario");
		if (aux == null && auxUsuario != null){
			aux = auxUsuario.getUsuario();
		}
		usuario.setUsuario(UtilidadesGenerales.obtenerString(aux));
		
		aux = request.getParameter("contrasena") == null ? "" : (String)request.getParameter("contrasena");
		usuario.setContrasena(UtilidadesGenerales.obtenerString(aux));
		
		aux = request.getParameter("perfil") == null ? "" : (String)request.getParameter("perfil");
		usuario.setIdentificadorPerfil(UtilidadesGenerales.convertirStringEnInteger(aux));
		
		aux = request.getParameter("dni");
		if (aux == null && auxUsuario != null){
			aux = auxUsuario.getDniUsuario();
		}
		usuario.setDniUsuario(UtilidadesGenerales.obtenerString(aux));
		
		return usuario;
	}

	
}
