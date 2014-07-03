package com.guarderia.DAO;

import java.util.List;
import com.guarderia.bean.Intervalo_Edad;

public interface CriterioDAO extends DAO {

	/**
	 * Busca todos los criterios de un intervalo de edad y los devuelve. Los criterios
	 * incluyen el Bloque y el Área de Conocimiento de los criterios.
	 * @param intervalo
	 * @return
	 */
	public abstract List buscarCriterioCompletoPorEdad(Intervalo_Edad intervalo);
	
}
