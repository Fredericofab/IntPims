package model.services;

import model.dao.DadosContabilDao;
import model.dao.FabricaDeDao;
import model.entities.DadosContabil;

public class DadosContabilService {

	private DadosContabilDao dao = FabricaDeDao.criarDadosContabilDao();
	
	public void salvar(DadosContabil objeto) {
		dao.inserir(objeto);
	}
	public Integer deletarTodos() {
		return dao.deletarTodos();
	}


}
