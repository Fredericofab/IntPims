package model.dao;

import java.util.List;

import model.entities.PoliticasErp;

public interface PoliticasErpDao {
	void inserir(PoliticasErp objeto);
	void atualizar(PoliticasErp objeto);
	void deletarPorChave(Integer codPolitica);
	PoliticasErp pesquisarPorChave(Integer codPolitica);
	List<PoliticasErp> listarTodos();
	void limparEstatisticas();
}
