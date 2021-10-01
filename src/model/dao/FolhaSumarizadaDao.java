package model.dao;

import java.util.List;
import model.entities.FolhaSumarizada;

public interface FolhaSumarizadaDao {
	void inserir(FolhaSumarizada objeto);
	void atualizar(FolhaSumarizada objeto);
	void deletarPorChave(String anoMes, Double codCentroCustos);
	FolhaSumarizada pesquisarPorChave(String anoMes, Double codCentroCustos);
	List<FolhaSumarizada> listarTodos();

	Integer deletarTodos();
}
