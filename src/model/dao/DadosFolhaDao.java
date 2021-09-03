package model.dao;

import java.util.List;
import model.entities.DadosFolha;

public interface DadosFolhaDao {
	
	void inserir(DadosFolha objeto);
	void atualizar(DadosFolha objeto);
	void deletarPorChave(String anoMes, String codCentroCustos, String codVerba);
	DadosFolha pesquisarPorChave(String anoMes, String codCentroCustos, String codVerba);
	List<DadosFolha> listarTodos();

}
