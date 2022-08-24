package model.dao;

import db.DB;
import model.dao.impl.DadosContabilDaoJDBC;
import model.dao.impl.Comp_MatDaoJDBC;
import model.dao.impl.Cstg_IntCMDaoJDBC;
import model.dao.impl.Cstg_IntDGDaoJDBC;
import model.dao.impl.Cstg_IntFPDaoJDBC;
import model.dao.impl.Cstg_IntVMDaoJDBC;
import model.dao.impl.ErpDaoJDBC;
import model.dao.impl.FatorMedidaDaoJDBC;
import model.dao.impl.FolhaDaoJDBC;
import model.dao.impl.FolhaSumarizadaDaoJDBC;
import model.dao.impl.FuncionariosDaoJDBC;
import model.dao.impl.FuncionariosSumarizadosDaoJDBC;
import model.dao.impl.OS_MaterialDaoJDBC;
import model.dao.impl.ParametrosDaoJDBC;
import model.dao.impl.PimsGeralDaoJDBC;
import model.dao.impl.PlcPrimDaoJDBC;
import model.dao.impl.PlcRatDaoJDBC;
import model.dao.impl.PoliticasErpDaoJDBC;
import model.dao.impl.ProcessoAtualDaoJDBC;
import model.dao.impl.VerbasFolhaDaoJDBC;

public class FabricaDeDao {
	
	public static ProcessoAtualDao criarProcessoAtualDao() {
		return new ProcessoAtualDaoJDBC(DB.abrirConexao());
	}

	public static VerbasFolhaDao criarVerbasFolhaDao() {
		return new VerbasFolhaDaoJDBC(DB.abrirConexao());
	}
	public static FatorMedidaDao criarFatorMedidaDao() {
		return new FatorMedidaDaoJDBC(DB.abrirConexao());
	}
	public static PoliticasErpDao criarPoliticasErpDao() {
		return new PoliticasErpDaoJDBC(DB.abrirConexao());
	}
	public static ParametrosDao criarParametrosDao() {
		return new ParametrosDaoJDBC(DB.abrirConexao());
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

	public static PimsGeralDao criarPimsGeralDao() {
		return new PimsGeralDaoJDBC(DB.abrirConexao());
	}
	public static Cstg_IntFPDao criarCstg_IntFPDao() {
		return new Cstg_IntFPDaoJDBC(DB.abrirConexao());
	}
	public static Cstg_IntVMDao criarCstg_IntVMDao() {
		return new Cstg_IntVMDaoJDBC(DB.abrirConexao());
	}
	public static Cstg_IntCMDao criarCstg_IntCMDao() {
		return new Cstg_IntCMDaoJDBC(DB.abrirConexao());
	}
	public static Cstg_IntDGDao criarCstg_IntDGDao() {
		return new Cstg_IntDGDaoJDBC(DB.abrirConexao());
	}
	public static OS_MaterialDao criarOS_MaterialDao() {
		return new OS_MaterialDaoJDBC(DB.abrirConexao());
	}
	public static Comp_MatDao criarComp_MatDao() {
		return new Comp_MatDaoJDBC(DB.abrirConexao());
	}
	public static PlcPrimDao criarPlcPrimDao() {
		return new PlcPrimDaoJDBC(DB.abrirConexao());
	}
	public static PlcRatDao criarPlcRatDao() {
		return new PlcRatDaoJDBC(DB.abrirConexao());
	}
	public static DadosContabilDao criarDadosContabilDao() {
		return new DadosContabilDaoJDBC(DB.abrirConexao());
	}

}