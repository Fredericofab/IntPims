package model.services;

import java.util.List;

import model.dao.DadosFolhaDao;
import model.dao.FabricaDeDao;
import model.entities.DadosFolha;

public class DadosFolhaService {
	
	private DadosFolhaDao dao = FabricaDeDao.criarDadosFolhaDao();
	
	public List<DadosFolha> pesquisarTodos(){
		return dao.listarTodos();
			
	}

	public void salvarOuAtualizar(DadosFolha objeto) {
		if (dao.pesquisarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos(), objeto.getCodVerba()) == null ) {
			dao.inserir(objeto);
		}
		else {
			dao.atualizar(objeto);
		}
	}
	
	public void remover(DadosFolha objeto) {
		dao.deletarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos(), objeto.getCodVerba());
	}
}
