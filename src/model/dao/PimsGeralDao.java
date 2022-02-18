package model.dao;

import java.util.Date;

import model.entities.Cstg_IntPF;

public interface PimsGeralDao {
	//tabela CCUSTOS;
	Boolean existeCCustos(Double codCCustos, String usuarioPimsCS);
	
	//tabela APT_OS_HE
	Boolean existeApt_os_he(String numeroOS, String usuarioPimsCS);
	Date dataSaidaApt_os_he(String numeroOS, String usuarioPimsCS);
	Double codCCustoApt_os_he(String numeroOS, String usuarioPimsCS);
	Double codEquiptoApt_os_he(String numeroOS, String usuarioPimsCS);

	//tabela CSTG_INTFP		
	Integer deletarCstg_IntFP(String dataref, String usuarioPimsCS);
	void gravarCstg_IntFP(Cstg_IntPF objeto, String usuarioPimsCS);
}
