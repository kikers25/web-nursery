package com.guarderia.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.guarderia.bbdd.AccesoDatos;
import com.guarderia.bean.Area_Conocimiento;
import com.guarderia.bean.Bean;
import com.guarderia.bean.Bloque;
import com.guarderia.bean.Criterio;
import com.guarderia.bean.Intervalo_Edad;
import com.guarderia.utils.UtilidadesGenerales;

public class OracleCriterioDAO implements CriterioDAO {

	/**
	 * Para escribir trazas
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	private final static String CONSULTA = "select * from criterio where identificador = ?";
	private final static String CONSULTA_POR_EDAD_DE_TODOS_LOS_CRITERIOS_COMPLETOS = 
		"select c.identificador, c.nombre, c.bloque as identificador_bloque, b.nombre as nombre_bloque, b.descripcion as descripcion_bloque,"+ 
		" ac.identificador as identificador_area, ac.nombre as nombre_area, ac.descripcion as descripcion_area"+
		" from criterio c, bloque b, area_conocimiento ac"+
		" where 	c.bloque = b.identificador" +
		"	and b.area_conocimiento = ac.identificador" +
		"	and c.identificador in" + 
		"		(select criterio from criterio_para_intervalo where intervalo_edad = ?)" +
		" order by c.identificador";
	private final static String CREAR = "insert into criterio (identificador, nombre, bloque) VALUES (?,?,?)";
	private final static String MODIFICAR = "update criterio set identificador = ?, nombre = ?, bloque = ?  where identificador = ?";
	private final static String ELIMINAR = "delete from criterio where identificador = ?";
	
	@Override
	public Bean borrar(Object identificador_bean) {
		Criterio criterio = null;
		if ( identificador_bean instanceof Integer ){
			criterio = (Criterio) buscarPorId(identificador_bean);
			if (criterio != null){
				AccesoDatos ad = new AccesoDatos();
				ad.conexionBDPool();
				ArrayList lista = new ArrayList(1);
				lista.add(identificador_bean);
				if (ad.realizarActualizacionParametrizada(ELIMINAR, lista) ){
					if (ad.devolverNumeroActualizaciones() != 1)
						log.error("No se ha eliminado el criterio con id: " + identificador_bean);
				}
				ad.desconexionBD();
			}
			else{
				log.error("No existe el criterio a borrar: " + identificador_bean);
			}
		}
		return criterio;
	}

	@Override
	public Bean buscarPorId(Object identificador_bean) {
		Criterio criterio = null;
		if ( identificador_bean instanceof Integer ){
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(1);
			lista.add(identificador_bean);
			if (ad.realizarConsultaParametrizada(CONSULTA, lista) ){
				ResultSet rs = ad.devolverConsulta();
				criterio = convertirResultSetEnBean(rs, true);
				UtilidadesGenerales.cerrarResulset(rs);
			}
			ad.desconexionBD();
		}
		return criterio;
	}

	/**
	 * Convierte el resulset en un Bean de tipo Criterio
	 * @param rs
	 * @param mover_cursor
	 * @return
	 */
	private Criterio convertirResultSetEnBean(ResultSet rs, boolean mover_cursor) {
		Criterio criterio = null;
		if (rs != null){
			try {
				if (rs.next() || mover_cursor == false){
					criterio = new Criterio();
					criterio.setIdentificador(rs.getInt("identificador"));
					criterio.setNombre(rs.getString("nombre"));
					if (rs.getString("bloque")!=null && mover_cursor){
						Bloque bloque = new Bloque();
						bloque.setIdentificador(rs.getInt("bloque"));
						criterio.setBloque(bloque);
					}
				}
			} catch (SQLException e) {
				log.error("Error: " + e);
				e.printStackTrace();
			}
		}
		return criterio;
	}

	@Override
	public void crear(Bean bean) {
		if ( bean instanceof Criterio ){
			Criterio criterio = (Criterio) bean;
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(3);
			lista.add(criterio.getIdentificador());
			lista.add(criterio.getNombre());
			if (criterio.getBloque() != null){
				lista.add(criterio.getBloque().getIdentificador());
			}
			else{
				lista.add(null);
			}
			if (ad.realizarActualizacionParametrizada(CREAR, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1)
					log.error("No se ha creado el criterio con nombre: " + criterio.getNombre());
			}
			ad.desconexionBD();
		}
	}

	@Override
	public void modificiar(Bean bean) {
		if ( bean instanceof Criterio ){
			Criterio criterio = (Criterio) bean;
			AccesoDatos ad = new AccesoDatos();
			ad.conexionBDPool();
			ArrayList lista = new ArrayList(4);
			lista.add(criterio.getIdentificador());
			lista.add(criterio.getNombre());
			if (criterio.getBloque() != null){
				lista.add(criterio.getBloque().getIdentificador());
			}
			else{
				lista.add(null);
			}
			lista.add(criterio.getIdentificador());
			if (ad.realizarActualizacionParametrizada(MODIFICAR, lista) ){
				if (ad.devolverNumeroActualizaciones() != 1)
					log.error("No se ha actualizado el criterio con nombre: " + criterio.getNombre());
			}
			ad.desconexionBD();
		}

	}

	@Override
	public List buscarCriterioCompletoPorEdad(Intervalo_Edad intervalo) {
		List lista_criterios = null;
		if (intervalo != null){
			if ( intervalo.getIdentificador() != null ){
				AccesoDatos ad = new AccesoDatos();
				ad.conexionBDPool();
				ArrayList lista = new ArrayList(1);
				lista.add(intervalo.getIdentificador());
				if (ad.realizarConsultaParametrizada(CONSULTA_POR_EDAD_DE_TODOS_LOS_CRITERIOS_COMPLETOS, lista) ){
					ResultSet rs = ad.devolverConsulta();
					lista_criterios = new ArrayList<Criterio>();
					boolean bAux = true;
					while (bAux == true){
						Criterio criterio = convertirResultSetEnBeanCompleto(rs);
						if (criterio != null){
							criterio.setIntervalo_edad(intervalo);
							lista_criterios.add(criterio);
						}
						else{
							bAux = false;
						}
						
					}
				}
				ad.desconexionBD();
			}
		}
		return lista_criterios;
	}

	/**
	 * Convierte el Resultset en un bean de tipo Criterio añadiendole el bloque y el area de
	 * conocimiento
	 * @param rs Resultset
	 * @return Bean de tipo Criterio
	 */
	private Criterio convertirResultSetEnBeanCompleto(ResultSet rs) {
		Criterio criterio = null;
		try {
			if (rs.next() && rs != null){
				criterio = new Criterio();
				criterio.setIdentificador(rs.getInt("identificador"));
				criterio.setNombre(rs.getString("nombre"));
					//convertirResultSetEnBean(rs, false);
				if (rs.getString("IDENTIFICADOR_BLOQUE") != null){
					Bloque bloque = new Bloque();
					bloque.setIdentificador(rs.getInt("IDENTIFICADOR_BLOQUE"));
					bloque.setNombre(rs.getString("NOMBRE_BLOQUE"));
					bloque.setDescripcion(rs.getString("DESCRIPCION_BLOQUE"));
					criterio.setBloque(bloque);
				}
				if (rs.getString("IDENTIFICADOR_AREA") != null){
					Area_Conocimiento area = new Area_Conocimiento();
					area.setIdentificador(rs.getInt("IDENTIFICADOR_AREA"));
					area.setNombre(rs.getString("NOMBRE_AREA"));
					area.setDescripcion(rs.getString("DESCRIPCION_AREA"));
					criterio.setArea_conocimiento(area);
				}
				
			}
		} catch (SQLException e) {
			log.error("No se ha creado de forma correcta el criterio: "+ e);
			e.printStackTrace();
		}
		return criterio;
	}
	
	public static void main (String [] args){
		BasicConfigurator.configure();
		OracleCriterioDAO criterioDAO = new OracleCriterioDAO();
		Intervalo_Edad intervalo = new Intervalo_Edad();
		intervalo.setIdentificador(7);
		intervalo.setInicio(2);
		intervalo.setFin(3);
		intervalo.setDescripcion("2-3 años");
		List lista = criterioDAO.buscarCriterioCompletoPorEdad(intervalo);
		int i = 0;
		for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
			i++;
			Criterio criterio = (Criterio) iterator.next();
			System.out.println("Criterio " + i + " con nombre " + criterio.getNombre() + " y bloque " + criterio.getBloque().getNombre());
		}
	}

}
