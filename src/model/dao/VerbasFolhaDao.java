package model.dao;

import java.util.List;
import model.entities.VerbasFolha;

public interface VerbasFolhaDao {
	void inserir(VerbasFolha objeto);
	void atualizar(VerbasFolha objeto);
	void deletarPorChave(Double codVerba);
	VerbasFolha pesquisarPorChave(Double codVerba);
	List<VerbasFolha> listarTodos();
	
	Integer contarVerbasSemDefinicao();
}
