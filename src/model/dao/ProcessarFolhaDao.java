package model.dao;

import model.entities.Cstg_IntPF;

public interface ProcessarFolhaDao {
	Integer contarVerbasSemDefinicao();
	Integer deletarDadosFolhaAnoMes(String anoMes);
	Integer deletarSumarioFolhaAnoMes(String anoMes);
	Integer deletarCstg_IntFP(String dataref, String usuarioPimsCS);
	void gravarCstg_IntFP(Cstg_IntPF objeto, String usuarioPimsCS);
}
