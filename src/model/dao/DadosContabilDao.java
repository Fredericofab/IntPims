package model.dao;

import java.util.List;

import model.entities.DadosContabil;

public interface DadosContabilDao {
	void inserir(DadosContabil objeto);
	List<DadosContabil> listarTodos();
	Integer deletarTodos();
}
