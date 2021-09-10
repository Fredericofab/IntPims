package model.dao;

import java.util.List;
import model.entities.Parametros;

public interface ParametrosDao {
	
	void inserir(Parametros objeto);
	void atualizar(Parametros objeto);
	void deletarPorChave(String Secao, String Entrada);
	Parametros pesquisarPorChave(String Secao, String Entrada);
	List<Parametros> listarTodos();
}
