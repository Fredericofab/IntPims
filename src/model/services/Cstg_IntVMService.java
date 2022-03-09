package model.services;

import model.dao.Cstg_IntVMDao;
import model.dao.FabricaDeDao;
import model.entities.Cstg_IntVM;

public class Cstg_IntVMService {

	Integer qtdeIncluida;
	Integer qtdeAtualizada;

	private Cstg_IntVMDao dao = FabricaDeDao.criarCstg_IntVMDao();
	
	public Integer getQtdeIncluida() {
		return qtdeIncluida;
	}
	public void setQtdeIncluida(Integer qtdeIncluida) {
		this.qtdeIncluida = qtdeIncluida;
	}
	public Integer getQtdeAtualizada() {
		return qtdeAtualizada;
	}
	public void setQtdeAtualizada(Integer qtdeAtualizada) {
		this.qtdeAtualizada = qtdeAtualizada;
	}

	public Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS) {
		return dao.deletarPeriodo(dataInicio, dataFim, usuarioPimsCS);
	}
	
	public void salvarOuAtualizar(Cstg_IntVM cstg_intVM, String usuarioPimsCS) {
		Cstg_IntVM pesquisado = dao.pesquisarPorChave(cstg_intVM.getCdMater(), cstg_intVM.getDtRefer(), usuarioPimsCS);
		if (pesquisado == null) {
			dao.inserir(cstg_intVM, usuarioPimsCS);
			qtdeIncluida += 1;
		} else {
			// Mesmo material, mesma data. Mantem o preço unitário já salvo.
			qtdeAtualizada += 1;
		}
	}

}
