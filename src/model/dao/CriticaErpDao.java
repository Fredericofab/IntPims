package model.dao;

import java.util.List;

import model.entities.CriticaErp;

public interface CriticaErpDao {
	void inserir(CriticaErp objeto);
	void atualizar(CriticaErp objeto);
	void deletarPorChave(String tipoCritica, Integer codigoCrtica);
	CriticaErp pesquisarPorChave(String tipoCritica, Integer codigoCrtica);
	List<CriticaErp> listarTodos();

	Integer ultimoSequencial(String tipoCritica);
}
