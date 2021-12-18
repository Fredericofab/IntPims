package model.dao;

import java.util.List;
import model.entities.Erp;

public interface ErpDao {
	void inserir(Erp objeto);
	void atualizar(Erp objeto);
	void deletarPorChave(Integer sequencial);
	Erp pesquisarPorChave(Integer sequencial);
	List<Erp> listarTodos();
	List<Erp> listarFiltrado(String tipoCritica, Integer codigoCritica, String filtro);

	Integer deletarTodos();
	Integer deletarPorOrigem(String origem);
	Integer ultimoSequencial();
	
	Integer qtdeTotal(String importar);
	}
