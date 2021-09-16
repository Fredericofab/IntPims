package model.dao;

import db.DB;
import model.dao.impl.ControleProcessoDaoJDBC;
import model.dao.impl.DadosFolhaDaoJDBC;
import model.dao.impl.ParametrosDaoJDBC;
import model.dao.impl.ProcessarFolhaDaoJDBC;
import model.dao.impl.SumarioFolhaDaoJDBC;
import model.dao.impl.VerbaFolhaDaoJDBC;

public class FabricaDeDao {
	
	public static VerbaFolhaDao criarVerbaFolhaDao() {
		return new VerbaFolhaDaoJDBC(DB.abrirConexao());
	}

	public static ControleProcessoDao criarControleProcessoDao() {
		return new ControleProcessoDaoJDBC(DB.abrirConexao());
	}

	public static ParametrosDao criarParametrosDao() {
		return new ParametrosDaoJDBC(DB.abrirConexao());
	}
	
	public static DadosFolhaDao criarDadosFolhaDao() {
		return new DadosFolhaDaoJDBC(DB.abrirConexao());
	}

	public static SumarioFolhaDao criarSumarioFolhaDao() {
		return new SumarioFolhaDaoJDBC(DB.abrirConexao());
	}

	public static ProcessarFolhaDao criarProcessarFolhaDao() {
		return new ProcessarFolhaDaoJDBC(DB.abrirConexao());
	}

}