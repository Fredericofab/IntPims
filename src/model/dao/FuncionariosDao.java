package model.dao;

import java.util.List;
import model.entities.Funcionarios;

public interface FuncionariosDao {
	void inserir(Funcionarios objeto);
	void atualizar(Funcionarios objeto);
	void deletarPorChave(String anoMes, Double codCentroCustos, Double codFuncionario);
	Funcionarios pesquisarPorChave(String anoMes, Double codCentroCustos, Double codFuncionario);
	List<Funcionarios> listarTodos();

	Integer deletarTodos();
}
