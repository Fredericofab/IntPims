package model.dao;

import java.util.Date;

import model.entities.OS_Material;

public interface OS_MaterialDao {
	void inserir(OS_Material objeto, String usuarioPimsCS);
	void atualizar(OS_Material objeto, String usuarioPimsCS);
	Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS);
	OS_Material pesquisarPorChave(String noBoletim, Date dtAplicacao, Double cdMaterial,String deMaterial, String noReqExt, String usuarioPimsCS);

}
