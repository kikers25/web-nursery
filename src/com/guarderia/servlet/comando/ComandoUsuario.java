package com.guarderia.servlet.comando;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.guarderia.bean.AdultoBean;
import com.guarderia.bean.PerfilBean;
import com.guarderia.bean.UsuarioBean;
import com.guarderia.negocio.GestionProfesor;
import com.guarderia.negocio.GestionUsuario;
import com.guarderia.utils.UtilidadesWeb;

/**
 * Implementación de la clase Servlet para el Servlet: ControladorUsuario
 * @web.servlet  name="ControladorUsuario"  display-name="ControladorUsuario" 
 * @web.servlet-mapping  url-pattern="/ControladorUsuario"
 */
public class ComandoUsuario extends com.guarderia.servlet.comando.Comando {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public static final String COMANDO_LOGEO = "7";
	private static final String COMANDO_LOGOUT = "8";
	public static final String COMANDO_MENU_PRIVADO = "16";
	private static final String COMANDO_VINCULAR_USUARIO = "32a";
	private static final String COMANDO_CREAR_USUARIO = "32b";
	private static final String COMANDO_MOSTRAR_USUARIOS = "33";
	private static final String COMANDO_VENTANA_MODIFICAR_USUARIO = "45";
	private static final String COMANDO_MODIFICAR_USUARIO = "45a";
	private static final String COMANDO_ELIMINAR_USUARIO = "46";

	
	
	/**
	 * Atributo utilizada para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * Mensaje por defecto cuando ha fallado el logeo
	 */
	private String MENSAJE_ERROR = "Usuario y/o contrasena incorrectos";
	
	/**
	 * Objeto de Negocio para las acciones con el usuario
	 * @uml.property  name="usuarioBO"
	 * @uml.associationEnd  
	 */
	private GestionUsuario usuarioBO;
	
	/**
	 * Objeto de Negocio para las acciones con el profesor
	 * @uml.property  name="profesorBO"
	 * @uml.associationEnd  
	 */
	private GestionProfesor profesorBO;
	
	public ComandoUsuario (){
		usuarioBO = new GestionUsuario();
		profesorBO = new GestionProfesor();
	}
	
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarPeticion(request, response);
	}
		
	public String procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuarioBean usuario = new UsuarioBean();
		PerfilBean perfil = new PerfilBean();
		
		if (opcion.equals(COMANDO_LOGEO)){ // Logeo de Usuario
			
			/*
			 * Obtenemos usuario y contraseña de la petición
			 */
			log.info("Logeo");
			String sUsuario=(request.getParameter("usuario")== null ? "" : request.getParameter("usuario"));
			String sContrasena=(request.getParameter("contrasena")== null ? "" : request.getParameter("contrasena"));
			
			usuario.setUsuario(sUsuario);
			usuario.setContrasena(sContrasena);
			/*
			 * validamos el usuario y la contraseña
			 */
			if (usuarioBO.validarUsuario(usuario,perfil)){ // Si se ha validado el usuario
				usuario.setContrasena("");
				log.info("Encontrado usuario y perfil");
				request.getSession().setAttribute("usuario", usuario);
				request.getSession().setAttribute("perfil", perfil);
			}
			else{
				anadirMensajeError(request, response, MENSAJE_ERROR);
				log.info("NO Encontrado usuario");
			}
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		if (opcion.equals(COMANDO_LOGOUT)){ // Salir de Sesion de usuario
			usuario = request.getSession().getAttribute("usuario") == null ? null : (UsuarioBean)request.getSession().getAttribute("usuario");
			perfil = request.getSession().getAttribute("perfil") == null ? null : (PerfilBean)request.getSession().getAttribute("perfil");
			if (usuario!=null){
				request.getSession().removeAttribute("usuario");
			}
			if (perfil!=null){
				request.getSession().removeAttribute("perfil");
			}
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		if (opcion.equals(COMANDO_MENU_PRIVADO)){ // Redireccion a Menu Privado
			// Cambiamos el fichero al que debe redireccionar dependiendo del perfil:
			// Administrador, Padre o Profesor
			log.info("Va al menu privado");
			usuario = request.getSession().getAttribute("usuario") == null ? null : (UsuarioBean)request.getSession().getAttribute("usuario");
			perfil = request.getSession().getAttribute("perfil") == null ? null : (PerfilBean)request.getSession().getAttribute("perfil");
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		if (opcion.equals(COMANDO_VINCULAR_USUARIO)){ // Verificar Usuario y Ventana Vincular persona a usuario
			usuario = usuarioBO.recuperaDatosUsuario(request);
			if (usuarioBO.verificarExistenciaUsuario(usuario)){ // Comprueba que no exista el usuario
				ArrayList personas = new ArrayList();
				if (profesorBO.buscarTodasLasPersonas(personas)){
					request.getSession().setAttribute("personas", personas);
					request.getSession().setAttribute("usuarioSeleccionado", usuario);
					anadirMensajeCorrecto(request, response, "Seleccione la persona vincula al usuario");
				}
			}
			else{
				anadirMensajeError(request, response, "El usuario ya existe");
				//anadirOtraAccion("32");
				UtilidadesWeb.obtenerDatosOpcion(request, "32");
			}
		}
		if (opcion.equals(COMANDO_CREAR_USUARIO)){ // Crear Usuario
			int nSeleccionado = recuperaNumeroSeleccionado(request, response, "numeroPersona");
			ArrayList personas = (ArrayList) request.getSession().getAttribute("personas");
			AdultoBean persona = (AdultoBean) personas.get(nSeleccionado);
			UsuarioBean usuarioSeleccionado = (UsuarioBean) request.getSession().getAttribute("usuarioSeleccionado");
			usuarioBO.crearUsuario(persona,usuarioSeleccionado);
			anadirMensajeCorrecto(request, response, "Se ha creado el usuario");
			UtilidadesWeb.irMenuPrincipal(request, response);
		}
		if (opcion.equals(COMANDO_MOSTRAR_USUARIOS)){ // Mostrar usuarios
			mostrarUsuarios(request, response, opcion);
		}
		if (opcion.equals(COMANDO_VENTANA_MODIFICAR_USUARIO)){ // Ventana Modificar Usuario
			ArrayList usuarios = (ArrayList) request.getSession().getAttribute("usuarios");
			ArrayList personas = (ArrayList) request.getSession().getAttribute("personas");
			int nUsuario = recuperaNumeroSeleccionado(request, response, "numeroUsuario");
			usuario = (UsuarioBean) usuarios.get(nUsuario);
			AdultoBean persona = (AdultoBean) personas.get(nUsuario);
			request.getSession().setAttribute("usuarioSeleccionado", usuario);
			request.setAttribute("personaSeleccionada", persona);
		}
		if (opcion.equals(COMANDO_MODIFICAR_USUARIO)){ // Modificar Usuario
			usuario = usuarioBO.recuperaDatosUsuario(request);
			usuarioBO.actualizarUsuario(usuario);
			request.removeAttribute("usuarioSeleccionado");
			anadirMensajeCorrecto(request, response, "Se ha actualizado el usuario");
			mostrarUsuarios(request, response, opcion);
		}
		if (opcion.equals(COMANDO_ELIMINAR_USUARIO)){ // Eliminar Usuario
			ArrayList usuarios = (ArrayList) request.getSession().getAttribute("usuarios");
			int nUsuario = recuperaNumeroSeleccionado(request, response, "numeroUsuario");
			usuario = (UsuarioBean) usuarios.get(nUsuario);
			usuarioBO.eliminarUsuario(usuario);
			anadirMensajeCorrecto(request, response, "Se ha eliminado el usuario");
			mostrarUsuarios(request, response, opcion);
		}
		return ficheroRedireccion;
	}

	/**
	 * Muestra los usuarios
	 * @param request
	 * @param response
	 * @param opcion
	 */
	private void mostrarUsuarios(HttpServletRequest request, HttpServletResponse response, String opcion) {
		ArrayList usuarios = new ArrayList();
		ArrayList personas = new ArrayList();
		usuarioBO.obtenerUsuarios(usuarios,personas);
		request.getSession().setAttribute("personas", personas);
		request.getSession().setAttribute("usuarios", usuarios);
		UtilidadesWeb.obtenerDatosOpcion(request, COMANDO_MOSTRAR_USUARIOS); 
		anadirVolver(request, response, opcion);
	}
	
	
}
