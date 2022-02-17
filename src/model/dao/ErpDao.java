package model.dao;

import java.util.List;
import model.entities.Erp;

public interface ErpDao {
	void inserir(Erp objeto);
	void atualizar(Erp objeto);
	void deletarPorChave(Integer sequencial);
	Erp pesquisarPorChave(Integer sequencial);
	List<Erp> listarTodos();
	List<Erp> pesquisarQuemAtendeAPolitica(Integer codPolitica, String clausulaWhere);
	List<Erp> listarTodosFiltrado(String clausulaWhere);

	Integer deletarTodos();
	Integer deletarPorOrigem(String origem);
	Integer ultimoSequencial();
	
	Integer qtdeTotal(String importar);
	Integer qtdeLiberadosOS();
	Integer qtdeLiberadosCM();
	Integer qtdeLiberadosDG();
	Integer qtdeLiberadosVM();
	Integer qtdeDessaCritica(String essaCriticaTxt, String importar);
	}
