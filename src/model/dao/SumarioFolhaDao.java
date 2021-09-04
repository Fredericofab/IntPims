package model.dao;

import java.util.List;
import model.entities.SumarioFolha;

public interface SumarioFolhaDao {
	
	void inserir(SumarioFolha objeto);
	void atualizar(SumarioFolha objeto);
	void deletarPorChave(String anoMes, String codCentroCustos);
	SumarioFolha pesquisarPorChave(String anoMes, String codCentroCustos);
	List<SumarioFolha> listarTodos();

}
