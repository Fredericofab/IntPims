package model.dao;

import java.util.List;
import model.entities.FatorMedida;

public interface FatorMedidaDao {
	void inserir(FatorMedida objeto);
	void atualizar(FatorMedida objeto);
	void deletarPorChave(String codMaterial);
	FatorMedida pesquisarPorChave(String codMaterial);
	List<FatorMedida> listarTodos();
	void atualizarNovos(Double defaultFatorDivisao);
	}
