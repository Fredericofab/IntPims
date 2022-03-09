package model.dao;

import java.util.Date;

import model.entities.Cstg_IntDG;

public interface Cstg_IntDGDao {
	void inserir(Cstg_IntDG objeto, String usuarioPimsCS);
	void atualizar(Cstg_IntDG objeto, String usuarioPimsCS);
	Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS);
	Cstg_IntDG pesquisarPorChave(String cdCtaCon, Date dtRefer, Double cdCCusto, String usuarioPimsCS);
}
