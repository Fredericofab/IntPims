package model.dao;

import java.util.Date;

public interface PimsGeralDao {
	//tabela CCUSTOS;
	Boolean existeCCustos(Double codCCustos, String usuarioPimsCS);
	
	//tabela APT_OS_HE
	Boolean existeApt_os_he(String numeroOS, String usuarioPimsCS);
	Date dataSaidaApt_os_he(String numeroOS, String usuarioPimsCS);
	Double codCCustoApt_os_he(String numeroOS, String usuarioPimsCS);
	Double codEquiptoApt_os_he(String numeroOS, String usuarioPimsCS);
}
