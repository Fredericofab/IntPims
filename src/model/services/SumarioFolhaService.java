package model.services;

import java.util.List;

import model.dao.SumarioFolhaDao;
import model.dao.FabricaDeDao;
import model.entities.SumarioFolha;

public class SumarioFolhaService {
	
	private SumarioFolhaDao dao = FabricaDeDao.criarSumarioFolhaDao();
	
	public List<SumarioFolha> pesquisarTodos(){
		return dao.listarTodos();
			
	}

	public void salvarOuAtualizar(SumarioFolha objeto) {
		if (dao.pesquisarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos()) == null ) {
			dao.inserir(objeto);
		}
		else {
			dao.atualizar(objeto);
		}
	}
	
	public void remover(SumarioFolha objeto) {
		dao.deletarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos());
	}
}
