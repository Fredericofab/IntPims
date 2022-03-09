package model.dao;

import model.entities.Cstg_IntFP;

public interface Cstg_IntFPDao {
	Integer deletarPeriodo(String dataref, String usuarioPimsCS);
	void inserir(Cstg_IntFP objeto, String usuarioPimsCS);
}
