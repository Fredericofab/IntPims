package model.dao;

import db.DB;
import model.dao.impl.ProcessoAtualDaoJDBC;
import model.dao.impl.FolhaDaoJDBC;
import model.dao.impl.ParametrosDaoJDBC;
import model.dao.impl.CriticasErpDaoJDBC;
import model.dao.impl.ErpDaoJDBC;
import model.dao.impl.PimsGeralDaoJDBC;
import model.dao.impl.FuncionariosDaoJDBC;
import model.dao.impl.FolhaSumarizadaDaoJDBC;
import model.dao.impl.FuncionariosSumarizadosDaoJDBC;
import model.dao.impl.VerbasFolhaDaoJDBC;

public class FabricaDeDao {
	
	public static ProcessoAtualDao criarProcessoAtualDao() {
		return new ProcessoAtualDaoJDBC(DB.abrirConexao());
	}
	public static PimsGeralDao criarPimsGeralDao() {
		return new PimsGeralDaoJDBC(DB.abrirConexao());
	}
	
	public static VerbasFolhaDao criarVerbasFolhaDao() {
		return new VerbasFolhaDaoJDBC(DB.abrirConexao());
	}
	public static ParametrosDao criarParametrosDao() {
		return new ParametrosDaoJDBC(DB.abrirConexao());
	}
	public static CriticasErpDao criarCriticasErpDao() {
		return new CriticasErpDaoJDBC(DB.abrirConexao());
	}

	public static FolhaDao criarFolhaDao() {
		return new FolhaDaoJDBC(DB.abrirConexao());
	}
	public static FolhaSumarizadaDao criarFolhaSumarizadaDao() {
		return new FolhaSumarizadaDaoJDBC(DB.abrirConexao());
	}
	public static FuncionariosDao criarFuncionariosDao() {
		return new FuncionariosDaoJDBC(DB.abrirConexao());
	}
	public static FuncionariosSumarizadosDao criarFuncionariosSumarizadosDao() {
		return new FuncionariosSumarizadosDaoJDBC(DB.abrirConexao());
	}
	public static ErpDao criarErpDao() {
		return new ErpDaoJDBC(DB.abrirConexao());
	}


}