package model.dao;

import java.util.List;
import model.entities.ControleProcesso;

public interface ControleProcessoDao {
	
	void inserir(ControleProcesso objeto);
	void atualizar(ControleProcesso objeto);
	void deletarPorChave(String anoMes);
	void deletarTodos();
	ControleProcesso pesquisarPorChave(String anoMes);
	List<ControleProcesso> listarTodos();
}
