package com.guarderia.DAO;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.Bean;
import com.guarderia.bean.GrupoCMS;

public class OracleGrupoCMSDAO implements GrupoCMSDAO {

	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	private final static String CONSULTA_GRUPO = "select * from cms_grupo where identificador = ?";
	private final static String CREAR_GRUPO = "insert into cms_grupo (identificador, nombre, path_contenido) VALUES (?,?,?)";
	private final static String MODIFICAR_GRUPO = "update cms_grupo set identificador = ?, nombre = ?, path_contenido = ?) VALUES (?,?,?) where identificador = ?";
	private final static String ELIMINAR_GRUPO = "delete from cms_grupo where identificador = ?";
	
	@Override
	public Bean borrar(Object identificador_bean) {
		GrupoCMS grupo = null;
		if ( identificador_bean instanceof String ){
			grupo = (GrupoCMS) buscarPorId(identificador_bean);
			if (grupo != null){
				AccesoDatos ad = new AccesoDatos();
				ad.conexionBDPool();
				ArrayList lista = new ArrayList(1);
				lista.add(identificador_bean);
				if (ad.realizarActualizacionParametrizada(ELIMINAR_GRUPO, lista) ){
					if (ad.devolverNumeroActualizaciones() != 1)
						log.error("No se ha eliminado el grupo CMS con id: " + identificador_bean);
				}
				ad.desconexionBD();
			}
			else{
				log.error("No existe el grupo a borrar: " + identificador_bean);
			}
		}
		return grupo;
	}

	@Override
	public Bean buscarPorId(Object identificador_bean) {
		GrupoCMS grupo = null;
		if ( identificador_bean instanceof String){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add(identificador_bean);
			grupo = (GrupoCMS) ad.realizarConsultaParametrizada(CONSULTA_GRUPO, lista, GrupoCMS.class);
			ad.desconexionBD();
		}
		return grupo;
	}

	@Override
	public void crear(Bean bean) {
		if ( bean instanceof GrupoCMS ){
			GrupoCMS grupo = (GrupoCMS) bean;
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(10);
			lista.add(grupo.getIdentificador());
			lista.add(grupo.getNombre());
			lista.add(grupo.getPath_contenido());
			if (ad.realizarActualizacionParametrizada(CREAR_GRUPO, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1)
					log.error("No se ha podido insertar el grupo");
			}
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if ( bean instanceof GrupoCMS ){
			GrupoCMS grupo = (GrupoCMS) bean;
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(10);
			lista.add(grupo.getIdentificador());
			lista.add(grupo.getNombre());
			lista.add(grupo.getPath_contenido());
			lista.add(grupo.getIdentificador());
			if (ad.realizarActualizacionParametrizada(MODIFICAR_GRUPO, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1)
					log.error("No se ha podido modificar el grupo");
			}
			ad.desconexionBD();
		}

	}

}
