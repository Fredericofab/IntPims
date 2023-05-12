package model.dao;

import java.util.List;

import model.entities.Comp_Mat;

public interface Comp_MatDao {
	void inserir(Comp_Mat objeto, String usuarioPimsCS);
	List<Comp_Mat> listarTodosDoTipo(String tipo, String usuarioPimsCS);
	List<Double> listarComponentes(String tipo, String usuarioPimsCS);
	Integer deletarRowVersion(Double anoMesReferencia, String usuarioPimsCS);
	Comp_Mat pesquisarPorChave(Double cdCompo, String cdMatIni, String usuarioPimsCS);
}
