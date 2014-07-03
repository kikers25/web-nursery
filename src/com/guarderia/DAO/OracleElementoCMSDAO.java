package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.Bean;
import com.guarderia.bean.ElementoCMS;
import com.guarderia.utils.UtilidadesGenerales;

public class OracleElementoCMSDAO implements ElementoCMSDAO {

	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	private final static String CONSULTA_ELEMENTO = "select * from cms_elemento where identificador = ?";
	private final static String CONSULTA_ELEMENTOS_POR_DIRECTORIO = "select * from cms_elemento where grupo = ? order by identificador";
	private final static String CONSULTA_ELEMENTOS_POR_DIRECTORIO_Y_TIPO = "select * from cms_elemento where grupo = ? and tipo_elemento = ?";
	private final static String CREAR_ELEMENTO = "insert into cms_elemento (identificador, nombre, nombre_fichero, descripcion, tipo_elemento, fecha_alta, fecha_modificacion, usuario_alta, usuario_modificacion, grupo) VALUES (?,?,?,?,?,?,?,?,?,?)";
	private final static String MODIFICAR_ELEMENTO = "update cms_elemento set identificador = ?, nombre = ?, nombre_fichero = ?, descripcion = ?, tipo_elemento = ?, fecha_alta = ?, fecha_modificacion = ?, usuario_alta = ?, usuario_modificacion = ?, grupo = ? where identificador = ?";
	private final static String ELIMINAR_ELEMENTO = "delete from cms_elemento where identificador = ?";
	
	@Override
	public Bean borrar(Object identificador_bean) {
		ElementoCMS elemento = null;
		if ( identificador_bean instanceof String ){
			elemento = (ElementoCMS) buscarPorId(identificador_bean);
			if (elemento != null){
				AccesoDatos ad = new AccesoDatos();
				ad.conexionBDPool();
				ArrayList lista = new ArrayList(1);
				lista.add(identificador_bean);
				if (ad.realizarActualizacionParametrizada(ELIMINAR_ELEMENTO, lista) ){
					if (ad.devolverNumeroActualizaciones() != 1)
						log.error("No se ha eliminado el elemento CMS con id: " + identificador_bean);
				}
				ad.desconexionBD();
			}
			else{
				log.error("No existe el elemento a borrar: " + identificador_bean);
			}
		}
		return elemento;
	}

	@Override
	public Bean buscarPorId(Object identificador_bean) {
		ElementoCMS elemento = null;
		if ( identificador_bean instanceof String){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add(identificador_bean);
			ad.realizarConsultaParametrizada(CONSULTA_ELEMENTO, lista);
			elemento = convertirResultSetEnBean(ad.devolverConsulta());
			ad.cerrarResulset();
			ad.desconexionBD();
		}
		return elemento;
	}

	@Override
	public void crear(Bean bean) {
		if ( bean instanceof ElementoCMS ){
			ElementoCMS elemento = (ElementoCMS) bean;
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(10);
			lista.add(elemento.getIdentificador());
			lista.add(elemento.getNombre());
			lista.add(elemento.getNombre_fichero());
			lista.add(elemento.getDescripcion());
			lista.add(elemento.getTipo_elemento());
			lista.add(elemento.getFecha_alta());
			lista.add(elemento.getFecha_modificacion());
			lista.add(elemento.getUsuario_alta());
			lista.add(elemento.getUsuario_modificacion());
			lista.add(elemento.getGrupo());
			if (ad.realizarActualizacionParametrizada(CREAR_ELEMENTO, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1)
					log.error("No se ha podido insertar el elemento");
			}
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if ( bean instanceof ElementoCMS ){
			ElementoCMS elemento = (ElementoCMS) bean;
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(10);
			lista.add(elemento.getIdentificador());
			lista.add(elemento.getNombre());
			lista.add(elemento.getNombre_fichero());
			lista.add(elemento.getDescripcion());
			lista.add(elemento.getTipo_elemento());
			lista.add(elemento.getFecha_alta());
			lista.add(elemento.getFecha_modificacion());
			lista.add(elemento.getUsuario_alta());
			lista.add(elemento.getUsuario_modificacion());
			lista.add(elemento.getGrupo());
			lista.add(elemento.getIdentificador());
			if (ad.realizarActualizacionParametrizada(MODIFICAR_ELEMENTO, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1)
					log.error("No se ha podido insertar el elemento");
			}
			ad.desconexionBD();
		}
	}

	@Override
	public List buscarElementosPorDirectorio(String identificador_directorio) {
		List elementos = null;
		if ( StringUtils.isNotBlank(identificador_directorio) ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add(identificador_directorio);
			ad.realizarConsultaParametrizada(CONSULTA_ELEMENTOS_POR_DIRECTORIO, lista);
			ResultSet rs = ad.devolverConsulta();
			elementos = new ArrayList<ElementoCMS>();
			boolean masElementos = true;
			while(masElementos){
				ElementoCMS elemento = convertirResultSetEnBean(rs);
				if (elemento != null){
					elementos.add(elemento);
				}
				else{
					masElementos = false;
				}
			}
			ad.cerrarResulset();
			ad.desconexionBD();
		}
		return elementos;
	}

	/**
	 * Conviete un ResultSet en un Bean de tipo elemento CMS
	 * @param rs ResultSet
	 * @return Bean de tipo elemento CMS
	 */
	private ElementoCMS convertirResultSetEnBean(ResultSet rs) {
		ElementoCMS elemento = null;
		try {
			if (rs.next() && rs != null){
				elemento = new ElementoCMS();
				elemento.setIdentificador(rs.getString("identificador"));
				elemento.setNombre(rs.getString("nombre"));
				elemento.setDescripcion(rs.getString("descripcion"));
				elemento.setTipo_elemento(rs.getInt("tipo_elemento"));
				elemento.setFecha_alta(UtilidadesGenerales
						.convertirDateEnGregorianCalendar(rs
								.getDate("fecha_alta")));
				elemento.setFecha_modificacion(UtilidadesGenerales
						.convertirDateEnGregorianCalendar(rs
								.getDate("fecha_modificacion")));
				elemento.setUsuario_alta(rs.getString("usuario_alta"));
				elemento.setUsuario_modificacion(rs.getString("usuario_modificacion"));
				elemento.setGrupo(rs.getString("grupo"));
				elemento.setNombre_fichero(rs.getString("nombre_fichero"));
			}
		} catch (SQLException e) {
			log.error("error: " + e);
			e.printStackTrace();
		}
		return elemento;
	}

	@Override
	public List buscarElementosPorDirectorioyTipo(
			String identificador_directorio, Integer tipo_elemento) {
		List elementos = null;
		if (StringUtils.isNotBlank(identificador_directorio)
				&& tipo_elemento != null) {
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(2);
			lista.add(identificador_directorio);
			lista.add(tipo_elemento);
			elementos = ad.realizarConsultaListaParametrizada(CONSULTA_ELEMENTOS_POR_DIRECTORIO_Y_TIPO, lista, ElementoCMS.class);
			ad.desconexionBD();
		}
		return elementos;
	}

}
