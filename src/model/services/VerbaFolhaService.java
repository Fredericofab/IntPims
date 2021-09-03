package model.services;

import java.util.List;

import model.dao.VerbaFolhaDao;
import model.dao.FabricaDeDao;
import model.entities.VerbaFolha;

public class VerbaFolhaService {
	
	private VerbaFolhaDao dao = FabricaDeDao.criarVerbaFolhaDao();
	
	public List<VerbaFolha> pesquisarTodos(){
		return dao.listarTodos();
	}
	
	public VerbaFolha pesquisarPorChave(String codVerba) {
		return dao.pesquisarPorChave(codVerba);
	};
	
	public void salvarOuAtualizar(VerbaFolha objeto) {
		if (dao.pesquisarPorChave(objeto.getCodVerba()) == null ) {
			dao.inserir(objeto);
		}
		else {
			dao.atualizar(objeto);
		}
	}
	
	public void remover(VerbaFolha objeto) {
		dao.deletarPorChave(objeto.getCodVerba());
	}
}
