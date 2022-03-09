package model.dao;

import model.entities.OS_Material;

public interface OS_MaterialDao {
	Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS);
	void inserir(OS_Material objeto, String usuarioPimsCS);
}
