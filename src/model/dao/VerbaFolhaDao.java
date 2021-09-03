package model.dao;

import java.util.List;
import model.entities.VerbaFolha;

public interface VerbaFolhaDao {
	
	void inserir(VerbaFolha objeto);
	void atualizar(VerbaFolha objeto);
	void deletarPorChave(String codVerba);
	VerbaFolha pesquisarPorChave(String codVerba);
	List<VerbaFolha> listarTodos();
}
