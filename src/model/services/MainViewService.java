package model.services;

import model.dao.FabricaDeDao;
import model.dao.ParametrosDao;

public class MainViewService {
	
	private ParametrosDao parametrosDao;

	public void testarAcessoBanco() {
		parametrosDao = FabricaDeDao.criarParametrosDao();
	}
}
