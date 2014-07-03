package com.guarderia.DAO;

public class OracleFactoria extends DAOFactoria {

	@Override
	public AdultoDAO getAdultoDAO() {
		return new OracleAdultoDAO();
	}

	@Override
	public AlumnoDAO getAlumnoDAO() {
		return new OracleAlumnoDAO();
	}

	@Override
	public ClaseDAO getClaseDAO() {
		return new OracleClaseDAO();
	}

	@Override
	public ComedorDAO getComedorDAO() {
		return new OracleComedorDAO();
	}

	@Override
	public NoticiaDAO getNoticiaDAO() {
		return new OracleNoticiaDAO();
	}

	@Override
	public PerfilDAO getPerfilDAO() {
		return new OraclePerfilDAO();
	}

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new OracleUsuarioDAO();
	}

	@Override
	public ElementoCMSDAO getElementoCMSDAO() {
		return new OracleElementoCMSDAO();
	}

	@Override
	public GrupoCMSDAO getGrupoCMSDAO() {
		return new OracleGrupoCMSDAO();
	}

	@Override
	public CriterioDAO getCriterioDAO() {
		return new OracleCriterioDAO();
	}

	@Override
	public EvaluacionDAO getEvaluacionDAO() {
		return new OracleEvaluacionDAO();
	}

}
