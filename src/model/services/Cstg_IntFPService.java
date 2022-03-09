package model.services;

import model.dao.Cstg_IntFPDao;
import model.dao.FabricaDeDao;
import model.entities.Cstg_IntFP;


public class Cstg_IntFPService {

	private Cstg_IntFPDao dao = FabricaDeDao.criarCstg_IntFPDao();

	public Integer deletarPeriodo(String dataref, String usuarioPimsCS) {
		return dao.deletarPeriodo(dataref, usuarioPimsCS);
	}
	public void inserir(Cstg_IntFP objeto, String usuarioPimsCS) {
		dao.inserir(objeto, usuarioPimsCS);
	}

}
