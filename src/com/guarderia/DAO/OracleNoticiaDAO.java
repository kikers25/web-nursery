package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.Bean;
import com.guarderia.bean.NoticiaBean;
import com.guarderia.utils.UtilidadesGenerales;

public class OracleNoticiaDAO implements NoticiaDAO {
	
	private final static String CONSULTA_NOTICIAS_HABILITADAS = "SELECT * FROM NOTICIA WHERE HABILITADO LIKE 'Y' ORDER BY FECHA DESC";
	private final static String CONSULTA_NOTICIAS = "SELECT * FROM NOTICIA ORDER BY FECHA DESC";
	private final static String CONSULTA_MAXIMO_IDENTIFICADOR_NOTICIA = "SELECT MAX(IDENTIFICADOR) FROM NOTICIA";
	private final static String CREAR_NOTICIA = "INSERT INTO NOTICIA (IDENTIFICADOR, HABILITADO, DESCRIPCION, FOTO, TITULO, SUBTITULO, RESUMEN, FECHA, FECHA_CREACION, FECHA_MODIFICACION) VALUES (?,?,?,?,?,?,?,?,?,?)";
	private final static String ACTUALIZAR_NOTICIA = "UPDATE NOTICIA  SET IDENTIFICADOR = ?, HABILITADO = ?, DESCRIPCION = ?, FOTO = ?, TITULO = ?, SUBTITULO = ?, RESUMEN = ?, FECHA = ?, FECHA_CREACION = ?, FECHA_MODIFICACION = ? WHERE IDENTIFICADOR = ?";
	private final static String BORRAR = "DELETE FROM NOTICIA WHERE IDENTIFICADOR = ?";
	private final static String CONSULTA_NOTICIA = "SELECT * FROM NOTICIA WHERE IDENTIFICADOR = ?";
	
	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * Constructor por defecto que inicializa los atributos
	 */
	public OracleNoticiaDAO(){
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.NoticiaDAO#buscarNoticiasHabilitadas(java.util.ArrayList)
	 */
	public boolean buscarNoticiasHabilitadas(ArrayList noticias){
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(0);
			log.info(CONSULTA_NOTICIAS_HABILITADAS);
			if (ad.realizarConsultaParametrizada(CONSULTA_NOTICIAS_HABILITADAS, lista)){
				ResultSet rs = ad.devolverConsulta();
				resultado = true;
				noticias.clear();
				boolean bAux = true;
				while (bAux == true){
					NoticiaBean noticia = new NoticiaBean();
					bAux = convertirResultSetEnBean(rs, noticia);
					if (bAux){
						log.info("Se ha obtenido una noticia: " + noticia.getTitulo());
						noticias.add(noticia);
					}
					else
						log.info("No se ha obtenido ninguna noticia");
				}
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				noticias.clear();
			}
			ad.desconexionBD();
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.NoticiaDAO#buscarNoticias(java.util.ArrayList)
	 */
	public boolean buscarNoticias(ArrayList noticias){
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(0);
			log.info(CONSULTA_NOTICIAS);
			if (ad.realizarConsultaParametrizada(CONSULTA_NOTICIAS, lista)){
				ResultSet rs = ad.devolverConsulta();
				resultado = true;
				noticias.clear();
				boolean bAux = true;
				while (bAux == true){
					NoticiaBean noticia = new NoticiaBean();
					bAux = convertirResultSetEnBean(rs, noticia);
					if (bAux){
						log.info("Se ha obtenido una noticia: " + noticia.getTitulo());
						noticias.add(noticia);
					}
					else
						log.info("No se ha obtenido ninguna noticia");
				}
				UtilidadesGenerales.cerrarResulset(rs);
			}
			else{
				noticias.clear();
			}
			ad.desconexionBD();
		}
		return resultado;
	}

	/* (non-Javadoc)
	 * @see com.guarderia.DAO.NoticiaDAO#buscarMaximoIdentificador()
	 */
	public int buscarMaximoIdentificador(){
		int max = -1;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList(0);
			log.info(CONSULTA_MAXIMO_IDENTIFICADOR_NOTICIA);
			if (ad.realizarConsultaParametrizada(CONSULTA_MAXIMO_IDENTIFICADOR_NOTICIA, lista)){
				ResultSet rs = ad.devolverConsulta();
				try {
					if (rs.next()){
						max = rs.getInt(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ad.desconexionBD();
		}
		return max;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.NoticiaDAO#modificarNoticia(com.guarderia.bean.NoticiaBean)
	 */
	public boolean modificarNoticia(NoticiaBean noticia) {
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList();
			lista.add(noticia.getIdentificador());
			lista.add(UtilidadesGenerales.convertirBooleanEnString(noticia.getHabilitado()));
			lista.add(noticia.getDescripcion());
			lista.add(noticia.getFoto());
			lista.add(noticia.getTitulo());
			lista.add(noticia.getSubtitulo());
			lista.add(noticia.getResumen());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(noticia.getFecha()));
			lista.add(noticia.getFechaCreacion());
			lista.add(noticia.getFechaModificacion());
			lista.add(noticia.getIdentificador());
			log.info(ACTUALIZAR_NOTICIA);
			if (ad.realizarActualizacionParametrizada(ACTUALIZAR_NOTICIA, lista)){
				int n = ad.devolverNumeroActualizaciones();
				log.info("Se ha actualizado n = " + n);
				if (n == 1) resultado = true;
			}
			ad.desconexionBD();
		}
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.guarderia.DAO.NoticiaDAO#crearNoticia(com.guarderia.bean.NoticiaBean)
	 */
	public boolean crearNoticia(NoticiaBean noticia) {
		boolean resultado = false;
		AccesoDatos ad = new AccesoDatos();
		if ( ad.conexionBDPool() ){
			ArrayList lista = new ArrayList();
			lista.add(noticia.getIdentificador());
			lista.add(UtilidadesGenerales.convertirBooleanEnString(noticia.getHabilitado()));
			lista.add(noticia.getDescripcion());
			lista.add(noticia.getFoto());
			lista.add(noticia.getTitulo());
			lista.add(noticia.getSubtitulo());
			lista.add(noticia.getResumen());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(noticia.getFecha()));
			lista.add(noticia.getFechaCreacion());
			lista.add(noticia.getFechaModificacion());
			log.info(CREAR_NOTICIA);
			if (ad.realizarActualizacionParametrizada(CREAR_NOTICIA, lista)){
				int n = ad.devolverNumeroActualizaciones();
				log.info("Se ha insertado n = " + n);
				if (n == 1) resultado = true;
			}
			ad.desconexionBD();
		}
		return resultado;
	}

	/* (non-Javadoc)
	 * @see com.guarderia.DAO.NoticiaDAO#convertirResultSetEnBean(java.sql.ResultSet, com.guarderia.bean.NoticiaBean)
	 */
	public boolean convertirResultSetEnBean(ResultSet conjunto, NoticiaBean noticia) {
		boolean resultado = false;
		String aux;
		int i=1;
		try {
			if (conjunto.next()){
				aux = conjunto.getString(i++);
				noticia.setIdentificador(UtilidadesGenerales.convertirStringEnInteger(aux));
				aux = conjunto.getString(i++);
				noticia.setHabilitado(UtilidadesGenerales.convertirStringEnBoolean(aux));
				aux = conjunto.getString(i++);
				noticia.setDescripcion(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				noticia.setFoto(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				noticia.setTitulo(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				noticia.setSubtitulo(UtilidadesGenerales.obtenerString(aux));
				aux = conjunto.getString(i++);
				noticia.setResumen(UtilidadesGenerales.obtenerString(aux));
				noticia.setFecha(UtilidadesGenerales.convertirDateEnGregorianCalendar(conjunto.getDate(i++)));
				noticia.setFechaCreacion(UtilidadesGenerales.obtenerTimeStamp(conjunto.getTimestamp(i++)));
				noticia.setFechaModificacion(UtilidadesGenerales.obtenerTimeStamp(conjunto.getTimestamp(i++)));
				resultado = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultado;
	}

	@Override
	public Bean borrar(Object identificadorBean) {
		NoticiaBean noticia = null;
		if ( identificadorBean instanceof Number ){
			noticia = (NoticiaBean) buscarPorId(identificadorBean);
			if (noticia != null){
				AccesoDatos ad = new AccesoDatos();
				ad.conexionBDPool();
				ArrayList lista = new ArrayList(1);
				lista.add(identificadorBean);
				if (ad.realizarActualizacionParametrizada(BORRAR, lista)){
					if (ad.devolverNumeroActualizaciones() != 1){
						log.error("No se ha podido borrar la noticia " + identificadorBean);
					}
				}
				ad.desconexionBD();
			}
		}
		return noticia;
	}

	@Override
	public Bean buscarPorId(Object identificadorBean) {
		NoticiaBean noticia = null;
		if ( identificadorBean instanceof Number ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add(identificadorBean);
			if (ad.realizarConsultaParametrizada(CONSULTA_NOTICIA, lista)){
				ResultSet rs = ad.devolverConsulta();
				noticia = new NoticiaBean();
				convertirResultSetEnBean(rs, noticia);
				if (noticia == null){
					log.info("No se ha encontrado ninguna noticia con el identificador " + identificadorBean);
				}
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return noticia;
	}

	@Override
	public void crear(Bean bean) {
		if ( bean instanceof NoticiaBean ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList();
			NoticiaBean noticia = (NoticiaBean) bean;
			lista.add(noticia.getIdentificador());
			lista.add(UtilidadesGenerales.convertirBooleanEnString(noticia.getHabilitado()));
			lista.add(noticia.getDescripcion());
			lista.add(noticia.getFoto());
			lista.add(noticia.getTitulo());
			lista.add(noticia.getSubtitulo());
			lista.add(noticia.getResumen());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(noticia.getFecha()));
			lista.add(noticia.getFechaCreacion());
			lista.add(noticia.getFechaModificacion());
			if (ad.realizarActualizacionParametrizada(CREAR_NOTICIA, lista)){
				if (ad.devolverNumeroActualizaciones() != 1)
					log.error("no se ha podido crear la noticia");
			}
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if ( bean instanceof NoticiaBean ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			NoticiaBean noticia = (NoticiaBean) bean;
			ArrayList lista = new ArrayList();
			lista.add(noticia.getIdentificador());
			lista.add(UtilidadesGenerales.convertirBooleanEnString(noticia.getHabilitado()));
			lista.add(noticia.getDescripcion());
			lista.add(noticia.getFoto());
			lista.add(noticia.getTitulo());
			lista.add(noticia.getSubtitulo());
			lista.add(noticia.getResumen());
			lista.add(UtilidadesGenerales.convertirGregorianCalendarEnString(noticia.getFecha()));
			lista.add(noticia.getFechaCreacion());
			lista.add(noticia.getFechaModificacion());
			lista.add(noticia.getIdentificador());
			if (ad.realizarActualizacionParametrizada(ACTUALIZAR_NOTICIA, lista)){
				if (ad.devolverNumeroActualizaciones() != 1)
					log.error("No se ha modificado la noticia con identificador " + noticia.getIdentificador());
			}
			ad.desconexionBD();
		}
	}
	
}
