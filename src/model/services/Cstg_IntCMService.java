package model.services;

import model.dao.Cstg_IntCMDao;
import model.dao.FabricaDeDao;
import model.entities.Cstg_IntCM;

public class Cstg_IntCMService {

	Integer qtdeIncluida;
	Integer qtdeAtualizada;

	private Cstg_IntCMDao dao = FabricaDeDao.criarCstg_IntCMDao();
	
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
	
	public void salvarOuAtualizar(Cstg_IntCM cstg_intCM, String usuarioPimsCS) {
		Cstg_IntCM pesquisado = dao.pesquisarPorChave(cstg_intCM.getCdMater(), cstg_intCM.getDtRefer(), cstg_intCM.getCdCCusto(),  usuarioPimsCS);
		if (pesquisado == null) {
			dao.inserir(cstg_intCM,  usuarioPimsCS);
			qtdeIncluida += 1;
		} else {
			Double qtde1 = pesquisado.getQtMater();
			Double precoUnit1 = pesquisado.getPrecoUnit();
			Double qtde2 = cstg_intCM.getQtMater();
			Double precoUnit2 = cstg_intCM.getPrecoUnit();
			Double qtde = qtde1 + qtde2;
			Double precoUnit = ( ( qtde1 * precoUnit1 ) + (qtde2 * precoUnit2 ) ) / qtde;
			pesquisado.setQtMater(qtde);
			pesquisado.setPrecoUnit(precoUnit);
			dao.atualizar(pesquisado,  usuarioPimsCS);
			qtdeAtualizada += 1;
		}
	}
}
