package model.services;

import java.util.Date;

import model.dao.FabricaDeDao;
import model.dao.PimsGeralDao;
import model.entities.Cstg_IntPF;

public class PimsGeralService {

	private PimsGeralDao dao = FabricaDeDao.criarPimsGeralDao();

	public Boolean existeCCustos(Double codCCustos, String usuarioPimsCS) {
		return dao.existeCCustos(codCCustos, usuarioPimsCS);
	}
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
	public Integer deletarCstg_IntFP(String dataref, String usuarioPimsCS) {
		return dao.deletarCstg_IntFP(dataref, usuarioPimsCS);
	}
	public void gravarCstg_IntFP(Cstg_IntPF objeto, String usuarioPimsCS) {
		dao.gravarCstg_IntFP(objeto, usuarioPimsCS);
	};
}
