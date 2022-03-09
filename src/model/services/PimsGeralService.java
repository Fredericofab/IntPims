package model.services;

import java.util.Date;

import model.dao.FabricaDeDao;
import model.dao.PimsGeralDao;

public class PimsGeralService {

	private PimsGeralDao dao = FabricaDeDao.criarPimsGeralDao();

	//tabela CCUSTOS (1)		
	public Boolean existeCCustos(Double codCCustos, String usuarioPimsCS) {
		return dao.existeCCustos(codCCustos, usuarioPimsCS);
	}

	//TABELA APT_OS_HE (4)
	public Boolean existeApt_os_he(String numeroOS, String usuarioPimsCS) {
		return dao.existeApt_os_he(numeroOS, usuarioPimsCS);
	}
	public Double codEquiptoApt_os_he(String numeroOS, String usuarioPimsCS) {
		return dao.codEquiptoApt_os_he(numeroOS, usuarioPimsCS);
	}				
	public Double codCCustoApt_os_he(String numeroOS, String usuarioPimsCS) {
		return dao.codCCustoApt_os_he(numeroOS, usuarioPimsCS);
	}
	public Date dataSaidaApt_os_he(String numeroOS, String usuarioPimsCS) {
		return dao.dataSaidaApt_os_he(numeroOS, usuarioPimsCS);
	}
}
