package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.OS_MaterialDao;
import model.entities.OS_Material;

public class OS_MaterialDaoJDBC implements OS_MaterialDao {

	private Connection conexao;

	public OS_MaterialDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(OS_Material objeto, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO " +  usuarioPimsCS + ".OS_Material " +
										 "(No_boletim, dt_aplicacao, cd_material, de_material, no_req_ext, qt_usada, vl_material, fg_captado, cd_Mat_Cstg, instancia) " +
										 "VALUES (?,?,?,?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getNoBoletim());
			st.setDate(2, new java.sql.Date(objeto.getDtAplicacao().getTime()));
			st.setDouble(3, objeto.getCdMaterial());
			st.setString(4, objeto.getDeMaterial());
			st.setString(5, objeto.getNoReqExt());
			st.setDouble(6, objeto.getQtUsada());
			st.setDouble(7, objeto.getQtValor());
			st.setString(8, objeto.getFgCaptado());
			st.setString(9, objeto.getCdMatCstg());
			st.setString(10, objeto.getInstancia());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela OS_Material \n \n" + e.getMessage() + "\n \n" 
								+ "Inserindo " + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM " +  usuarioPimsCS + ".OS_Material " +
										  "WHERE dt_aplicacao BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') ");
			st.setString(1, dataInicio);
			st.setString(2, dataFim);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela OS_Material \n \n" + e.getMessage() + "\n \n" 
								+ "Movimento entre " + dataInicio + " e " + dataFim );
		}
		finally {
			DB.fecharStatement(st);
		}
	}
}
