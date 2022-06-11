package model.services;

import java.util.List;

import model.dao.FabricaDeDao;
import model.dao.PlcRatDao;
import model.entities.PlcRat;

public class PlcRatService {

	private PlcRatDao dao = FabricaDeDao.criarPlcRatDao();
	
	public List<PlcRat> pesquisarTodos(String anoMes, String usuarioPimsCS) {
		return dao.listarTodos(anoMes, usuarioPimsCS);
	}
	public List<PlcRat> pesquisarCCusto(String anoMes, String usuarioPimsCS, Double ccusto) {
		return dao.listarCCusto(anoMes, usuarioPimsCS, ccusto);
	}

}
