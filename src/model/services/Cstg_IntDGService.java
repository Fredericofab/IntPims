package model.services;

import model.dao.Cstg_IntDGDao;
import model.dao.FabricaDeDao;
import model.entities.Cstg_IntDG;

public class Cstg_IntDGService {

	Integer qtdeIncluida;
	Integer qtdeAtualizada;

	private Cstg_IntDGDao dao = FabricaDeDao.criarCstg_IntDGDao();
	
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
	
	public void salvarOuAtualizar(Cstg_IntDG cstg_intDG, String usuarioPimsCS) {
		Cstg_IntDG pesquisado = dao.pesquisarPorChave(cstg_intDG.getCdCtaCon(), cstg_intDG.getDtRefer(), cstg_intDG.getCdCCusto(), usuarioPimsCS);
		if (pesquisado == null) {
			dao.inserir(cstg_intDG, usuarioPimsCS);
			qtdeIncluida += 1;
		} else {
			Double valor1 = pesquisado.getQtValor();
			Double valor2 = cstg_intDG.getQtValor();
			Double valor = valor1 + valor2;
			pesquisado.setQtValor(valor);;
			dao.atualizar(pesquisado,  usuarioPimsCS);
			qtdeAtualizada += 1;
		}
	}

}
