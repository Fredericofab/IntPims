package model.dao;

import java.util.List;
import model.entities.Folha;

public interface FolhaDao {
	void inserir(Folha objeto);
	void atualizar(Folha objeto);
	void deletarPorChave(String anoMes, Double codCentroCustos, Double codVerba);
	Folha pesquisarPorChave(String anoMes, Double codCentroCustos, Double codVerba);
	List<Folha> listarTodos();

	Integer deletarTodos();
}
