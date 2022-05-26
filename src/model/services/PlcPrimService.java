package model.services;

import java.util.List;

import model.dao.FabricaDeDao;
import model.dao.PlcPrimDao;
import model.entities.PlcPrim;

public class PlcPrimService {

	private PlcPrimDao dao = FabricaDeDao.criarPlcPrimDao();
	
	public List<PlcPrim> pesquisarTodos(String anoMes, String usuarioPimsCS) {
		return dao.listarTodos(anoMes, usuarioPimsCS);
	}

}
