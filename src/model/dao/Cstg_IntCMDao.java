package model.dao;

import java.util.Date;

import model.entities.Cstg_IntCM;

public interface Cstg_IntCMDao {
	void inserir(Cstg_IntCM objeto, String usuarioPimsCS);
	void atualizar(Cstg_IntCM objeto, String usuarioPimsCS);
	Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS);
	Cstg_IntCM pesquisarPorChave(String cdMater, Date dtRefer, Double cdCCusto, String usuarioPimsCS);
}
