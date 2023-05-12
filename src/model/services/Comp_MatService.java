package model.services;

import java.util.List;

import model.dao.Comp_MatDao;
import model.dao.FabricaDeDao;
import model.entities.Comp_Mat;

public class Comp_MatService {

	Integer qtdeIncluida;

	private Comp_MatDao dao = FabricaDeDao.criarComp_MatDao();

	public Integer getQtdeIncluida() {
		return qtdeIncluida;
	}

	public void setQtdeIncluida(Integer qtdeIncluida) {
		this.qtdeIncluida = qtdeIncluida;
	}

	public Integer deletarRowVersion(Double anoMesReferencia, String usuarioPimsCS) {
		return dao.deletarRowVersion(anoMesReferencia, usuarioPimsCS);
	}

	public void inserir(Comp_Mat comp_mat, String usuarioPimsCS) {
		Comp_Mat pesquisado = dao.pesquisarPorChave(comp_mat.getCdCompo(), comp_mat.getCdMatIni(), usuarioPimsCS);
		if (pesquisado == null) {
			dao.inserir(comp_mat, usuarioPimsCS);
			qtdeIncluida += 1;
		}	
	}
	
	public List<Comp_Mat> listarTodosDoTipo(String tipo, String usuarioPimsCS) {
		return dao.listarTodosDoTipo(tipo, usuarioPimsCS);
	}

	public Integer deletarPeriodo(Double anoMesReferencia, String usuarioPimsCS) {
		return dao.deletarRowVersion(anoMesReferencia, usuarioPimsCS);
	}

	public List<Double> listarComponentes(String tipo, String usuarioPimsCS) {
		return dao.listarComponentes(tipo, usuarioPimsCS);
	}

}
