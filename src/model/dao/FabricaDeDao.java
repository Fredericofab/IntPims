package model.dao;

import db.DB;
import model.dao.impl.DadosFolhaDaoJDBC;
import model.dao.impl.ImportarFolhaDaoJDBC;
import model.dao.impl.VerbaFolhaDaoJDBC;

public class FabricaDeDao {
	
	public static DadosFolhaDao criarDadosFolhaDao() {
		return new DadosFolhaDaoJDBC(DB.abrirConexao());
	}

	public static VerbaFolhaDao criarVerbaFolhaDao() {
		return new VerbaFolhaDaoJDBC(DB.abrirConexao());
	}
	
	public static ImportarFolhaDao criarImportarFolhaDao() {
		return new ImportarFolhaDaoJDBC(DB.abrirConexao());
	}

}