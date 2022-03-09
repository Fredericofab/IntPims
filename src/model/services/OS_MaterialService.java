package model.services;

import model.dao.FabricaDeDao;
import model.dao.OS_MaterialDao;
import model.entities.OS_Material;

public class OS_MaterialService {

	Integer qtdeIncluida;

	private OS_MaterialDao dao = FabricaDeDao.criarOS_MaterialDao();
	
	public Integer getQtdeIncluida() {
		return qtdeIncluida;
	}
	public void setQtdeIncluida(Integer qtdeIncluida) {
		this.qtdeIncluida = qtdeIncluida;
	}

	public Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS) {
		return dao.deletarPeriodo(dataInicio, dataFim, usuarioPimsCS);
	}
	
	public void inserir(OS_Material os_Material, String usuarioPimsCS) {
		dao.inserir(os_Material, usuarioPimsCS);
		qtdeIncluida += 1;
	}
}
