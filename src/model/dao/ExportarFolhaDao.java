package model.dao;

import model.entities.Cstg_IntPF;

public interface ExportarFolhaDao {
	Integer deletarCstg_IntFP(String dataref, String usuarioPimsCS);
	void gravarCstg_IntFP(Cstg_IntPF objeto, String usuarioPimsCS);
}
