package model.services;

import model.dao.FabricaDeDao;
import model.dao.OS_MaterialDao;
import model.entities.OS_Material;

public class OS_MaterialService {

	Integer qtdeIncluida;
	Integer qtdeAtualizada;

	private OS_MaterialDao dao = FabricaDeDao.criarOS_MaterialDao();
	
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
	
	public void salvarOuAtualizar(OS_Material os_Material, String usuarioPimsCS) {
		OS_Material pesquisado = dao.pesquisarPorChave(os_Material.getNoBoletim(), os_Material.getDtAplicacao(), os_Material.getCdMaterial(), os_Material.getDeMaterial(), os_Material.getNoReqExt(), usuarioPimsCS);
		if (pesquisado == null) {
			dao.inserir(os_Material, usuarioPimsCS);
			qtdeIncluida += 1;
		} else {
			Double qtde1 = pesquisado.getQtUsada();
			Double precoUnit1 = pesquisado.getQtValor();
			Double qtde2 = os_Material.getQtUsada();
			Double precoUnit2 = os_Material.getQtValor();
			Double qtde = qtde1 + qtde2;
			Double precoUnit = ( ( qtde1 * precoUnit1 ) + (qtde2 * precoUnit2 ) ) / qtde;
			pesquisado.setQtUsada(qtde);
			pesquisado.setQtValor(precoUnit);
			dao.atualizar(pesquisado,  usuarioPimsCS);
			qtdeAtualizada += 1;
		}
	}

}
