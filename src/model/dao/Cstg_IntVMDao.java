package model.dao;

import java.util.Date;

import model.entities.Cstg_IntVM;

public interface Cstg_IntVMDao {
	void inserir(Cstg_IntVM objeto,String usuarioPimsCS);
	Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS);
	Cstg_IntVM pesquisarPorChave(String cdMater, Date dtRefer, String usuarioPimsCS);
}
