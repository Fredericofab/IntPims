package model.dao;

import java.util.List;
import model.entities.FuncionariosSumarizados;

public interface FuncionariosSumarizadosDao {
	void inserir(FuncionariosSumarizados objeto);
	void atualizar(FuncionariosSumarizados objeto);
	void deletarPorChave(String anoMes, Double codCentroCustos);
	FuncionariosSumarizados pesquisarPorChave(String anoMes, Double codCentroCustos);
	List<FuncionariosSumarizados> listarTodos();

	Integer deletarTodos();
}
