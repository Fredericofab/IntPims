package model.dao;

import java.util.List;
import model.entities.ProcessoAtual;

public interface ProcessoAtualDao {
	void inserir(ProcessoAtual objeto);
	void atualizar(ProcessoAtual objeto);
	void deletarPorChave(String anoMes);
	ProcessoAtual pesquisarPorChave(String anoMes);
	List<ProcessoAtual> listarTodos();

	Integer deletarTodos();
}
