package model.dao;

import java.util.List;

import model.entities.CriticasErp;

public interface CriticasErpDao {
	void inserir(CriticasErp objeto);
	void atualizar(CriticasErp objeto);
	void deletarPorChave(String tipoCritica, Integer codigoCrtica);
	CriticasErp pesquisarPorChave(String tipoCritica, Integer codigoCrtica);
	List<CriticasErp> listarTodos();

	Integer ultimoSequencial(String tipoCritica);
}
