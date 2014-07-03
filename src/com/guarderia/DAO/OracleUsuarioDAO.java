package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.Bean;
import com.guarderia.bean.UsuarioBean;
import com.guarderia.utils.UtilidadesGenerales;

/**
 * Clase para el acceso a la base de datos y mas especificamente a la tabla Usuario
 * @author Enrique Martín Martín
 */
public class OracleUsuarioDAO implements UsuarioDAO {
	
	private final static String CONSULTA_USUARIO = "SELECT * FROM USUARIO WHERE USUARIO LIKE ?";
	private final static String CONSULTA_USUARIO_POR_ID = "SELECT * FROM USUARIO WHERE USUARIO = ?";
	private final static String CONSULTA_USUARIOS = "SELECT * FROM USUARIO ORDER BY USUARIO";
	private final static String CREAR_USUARIO = "INSERT INTO USUARIO (USUARIO, CONTRASENA, DESCRIPCION, DNI, IDENTIFICADOR_PERFIL) VALUES (?,?,?,?,?)";
	private final static String ACTUALIZAR_USUARIO = "UPDATE USUARIO SET USUARIO = ?, CONTRASENA = ?, DESCRIPCION = ?, DNI = ?, IDENTIFICADOR_PERFIL = ? WHERE USUARIO LIKE ?";
	private final static String ELIMINAR_USUARIO = "DELETE FROM USUARIO WHERE USUARIO LIKE ?";
	
	/**
	 * Acceso a la Base de datos
	 */
	//private AccesoDatos ad;
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Constructor por defecto que inicializa los atributos
	 *
	 */
	public OracleUsuarioDAO(){
		//this.ad = AccesoDatos.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.UsuarioDAO#validarUsuario(com.guarderia.bean.UsuarioBean)
	 */
	public boolean validarUsuario(UsuarioBean usuario){
		String contrasena;
		boolean bResultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(usuario.getUsuario().toLowerCase());
			contrasena = usuario.getContrasena();
			if (ad.realizarConsultaParametrizada(CONSULTA_USUARIO, lista)){
				ResultSet rs = ad.devolverConsulta();
				boolean retorno = convertirResultSetEnBean(rs,usuario);
				UtilidadesGenerales.cerrarResulset(rs);
				ad.desconexionBD();
				if (retorno == true){
					if (usuario.getContrasena().equals(contrasena)){
						bResultado = true;
					}
					else{
						log.info("Diferente contraseña");
					}
				}
			}
			else{
				log.info("No ha encontrado el usuario");
				usuario = null;
			}
		}
		return bResultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.UsuarioDAO#consultaUsuario(com.guarderia.bean.UsuarioBean)
	 */
	public boolean consultaUsuario(UsuarioBean usuario) {
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(usuario.getUsuario().toLowerCase());
			if (ad.realizarConsultaParametrizada(CONSULTA_USUARIO, lista)){
				ResultSet rs = ad.devolverConsulta();
				resultado = convertirResultSetEnBean(rs,usuario);
				UtilidadesGenerales.cerrarResulset(rs);
				ad.desconexionBD();
			}
			else{
				log.info("No ha encontrado el usuario");
				usuario = null;
			}
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.UsuarioDAO#consultaUsuarios(java.util.ArrayList)
	 */
	public boolean consultaUsuarios(ArrayList usuarios) {
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(0);
			if (ad.realizarConsultaParametrizada(CONSULTA_USUARIOS, lista)){
				resultado = true;
				ResultSet rs = ad.devolverConsulta();
				usuarios.clear();
				boolean bAux = true;
				while (bAux == true){
					UsuarioBean usuario = new UsuarioBean();
					bAux = convertirResultSetEnBean(rs, usuario);
					if (bAux){
						log.info("Se ha obtenido el dato de un usuario: " + usuario.getUsuario() + " ");
						usuarios.add(usuario);
					}
				}
				UtilidadesGenerales.cerrarResulset(rs);
			}
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.UsuarioDAO#crearUsuario(com.guarderia.bean.UsuarioBean)
	 */
	public boolean crearUsuario(UsuarioBean usuario) {
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(5);
			lista.add(usuario.getUsuario());
			lista.add(usuario.getContrasena());
			lista.add(usuario.getDescripcion());
			lista.add(usuario.getDniUsuario());
			lista.add(usuario.getIdentificadorPerfil());
			if (ad.realizarActualizacionParametrizada(CREAR_USUARIO, lista)){
				int n = ad.devolverNumeroActualizaciones();
				if (n == 1){
					resultado = true;
					log.info("Creado Nuevo usuario");
				}
			}
			ad.desconexionBD();
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.UsuarioDAO#actualizarUsuario(com.guarderia.bean.UsuarioBean)
	 */
	public boolean actualizarUsuario(UsuarioBean usuario) {
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(6);
			lista.add(usuario.getUsuario());
			lista.add(usuario.getContrasena());
			lista.add(usuario.getDescripcion());
			lista.add(usuario.getDniUsuario());
			lista.add(usuario.getIdentificadorPerfil());
			lista.add(usuario.getUsuario());
			if (ad.realizarActualizacionParametrizada(ACTUALIZAR_USUARIO, lista)){
				int n = ad.devolverNumeroActualizaciones();
				if (n == 1){
					resultado = true;
					log.info("Actualizado usuario");
				}
			}
			ad.desconexionBD();
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.UsuarioDAO#eliminarUsuario(com.guarderia.bean.UsuarioBean)
	 */
	public boolean eliminarUsuario(UsuarioBean usuario) {
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add(usuario.getUsuario());
			if (ad.realizarActualizacionParametrizada(ELIMINAR_USUARIO, lista)){
				int n = ad.devolverNumeroActualizaciones();
				if (n == 1){
					resultado = true;
					log.info("Eliminado usuario");
				}
			}
			ad.desconexionBD();
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.UsuarioDAO#convertirResultSetEnBean(java.sql.ResultSet, com.guarderia.bean.UsuarioBean)
	 */
	public boolean convertirResultSetEnBean(ResultSet conjunto,UsuarioBean usuario){
		boolean resultado = false;
		String aux;
		int i=1;
		try {
			if(conjunto.next()){
				aux = conjunto.getString(i++);
				usuario.setUsuario(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				usuario.setContrasena(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				usuario.setDescripcion(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				usuario.setDniUsuario(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				usuario.setIdentificadorPerfil(UtilidadesGenerales.convertirStringEnInteger(aux));
				resultado = true;
			}
			else{
				log.info("No hay datos en el ResultSet para convertirlo en Bean de tipo Usuario");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultado;
	}

	@Override
	public Bean borrar(Object identificadorBean) {
		UsuarioBean usuario = null;
		if ( identificadorBean instanceof String ){
			usuario = (UsuarioBean) this.buscarPorId(identificadorBean);
			if (usuario != null){
				AccesoDatos ad = new AccesoDatos();
				ad.conexionBDPool();
				ArrayList lista = new ArrayList(1);
				lista.add(usuario.getUsuario());
				if (ad.realizarActualizacionParametrizada(ELIMINAR_USUARIO, lista)){
					if (ad.devolverNumeroActualizaciones() != 1){
						log.error("No se ha podido eliminar al usuario con identificador " + identificadorBean);
					}
				}
			}
		}
		return usuario;
	}

	@Override
	public Bean buscarPorId(Object identificadorBean) {
		UsuarioBean usuario = null;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(1);
			lista.add( ((String)identificadorBean).toLowerCase() );
			if (ad.realizarConsultaParametrizada(CONSULTA_USUARIO_POR_ID, lista)){
				ResultSet rs = ad.devolverConsulta();
				convertirResultSetEnBean(rs,usuario);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return usuario;
	}

	@Override
	public void crear(Bean bean) {
		if ( bean instanceof UsuarioBean ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			UsuarioBean usuario = (UsuarioBean) bean;
			ArrayList lista = new ArrayList(5);
			lista.add(usuario.getUsuario());
			lista.add(usuario.getContrasena());
			lista.add(usuario.getDescripcion());
			lista.add(usuario.getDniUsuario());
			lista.add(usuario.getIdentificadorPerfil());
			if (ad.realizarActualizacionParametrizada(CREAR_USUARIO, lista)){
				if (ad.devolverNumeroActualizaciones() != 1){
					log.error("No se pudo crear el usuario " + usuario.getUsuario());
				}
			}
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if ( bean instanceof UsuarioBean ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			UsuarioBean usuario = (UsuarioBean) bean;
			ArrayList lista = new ArrayList(6);
			lista.add(usuario.getUsuario());
			lista.add(usuario.getContrasena());
			lista.add(usuario.getDescripcion());
			lista.add(usuario.getDniUsuario());
			lista.add(usuario.getIdentificadorPerfil());
			lista.add(usuario.getUsuario());
			if (ad.realizarActualizacionParametrizada(ACTUALIZAR_USUARIO, lista)){
				if (ad.devolverNumeroActualizaciones() != 1){
					log.error("No se ha podido actualizar el usaurio " + usuario.getUsuario());
				}
			}
			ad.desconexionBD();
		}
	}

}
