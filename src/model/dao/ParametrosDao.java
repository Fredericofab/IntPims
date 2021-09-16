package model.dao;

import java.util.List;
import model.entities.Parametros;

public interface ParametrosDao {
	
	void inserir(Parametros objeto);
	void atualizar(Parametros objeto);
	void deletarPorChave(String secao, String entrada);
	Parametros pesquisarPorChave(String secao, String entrada);
	List<Parametros> listarTodos();
}
