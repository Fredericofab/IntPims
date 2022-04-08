package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
	public void atualizar(OS_Material objeto, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE " +  usuarioPimsCS + ".OS_Material " 
					 					+ "SET qt_usada = ?, vl_material = ?  " 
										+ "WHERE No_boletim = ? AND dt_aplicacao = ? AND cd_material = ? "
					 					+ "AND de_material = ? AND no_req_ext = ?");
			st.setDouble(1, objeto.getQtUsada());
			st.setDouble(2, objeto.getQtValor());
			st.setString(3, objeto.getNoBoletim());
			st.setDate(4, new java.sql.Date(objeto.getDtAplicacao().getTime()));
			st.setDouble(5, objeto.getCdMaterial());
			st.setString(6, objeto.getDeMaterial());
			st.setString(7, objeto.getNoReqExt());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela OS_Material  \n \n" + e.getMessage() + "\n \n" +
								  "Atualizando "+ objeto);
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

	@Override
	public OS_Material pesquisarPorChave(String noBoletim, Date dtAplicacao, Double cdMaterial, String deMaterial,
			String noReqExt, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".OS_Material " 
										+ "WHERE No_boletim = ? AND dt_aplicacao = ? AND cd_material = ? "
										+ "AND de_material = ? AND no_req_ext = ?");
			st.setString(1, noBoletim);
			st.setDate(2, new java.sql.Date(dtAplicacao.getTime()));
			st.setDouble(3, cdMaterial);
			st.setString(4, deMaterial);
			st.setString(5, noReqExt);
			rs = st.executeQuery();
			if (rs.next()) {
				OS_Material os_Material = instanciaDados(rs);
				return os_Material;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela OS_Material \n \n" + e.getMessage() + "\n \n" +
								  "Pesquisando Material " + cdMaterial + " Data " + dtAplicacao + " Boletim " + noBoletim);
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	private OS_Material instanciaDados(ResultSet rs) throws SQLException {
		OS_Material os_Material = new OS_Material();
		os_Material.setNoBoletim(rs.getString("No_boletim"));
		os_Material.setDtAplicacao(new java.util.Date(rs.getTimestamp("dt_aplicacao").getTime()));
		os_Material.setCdMaterial(rs.getDouble("cd_material"));
		os_Material.setDeMaterial(rs.getString("de_material"));
		os_Material.setNoReqExt(rs.getString("no_req_ext"));
		os_Material.setQtUsada(rs.getDouble("qt_usada"));
		os_Material.setQtValor(rs.getDouble("vl_material"));
		os_Material.setFgCaptado(rs.getString("fg_captado"));
		os_Material.setCdMatCstg(rs.getString("cd_Mat_Cstg"));
		os_Material.setInstancia(rs.getString("instancia"));
		return os_Material;
	}
}
