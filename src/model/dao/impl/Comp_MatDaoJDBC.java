package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.Comp_MatDao;
import model.entities.Comp_Mat;

public class Comp_MatDaoJDBC implements Comp_MatDao {

	private Connection conexao;

	public Comp_MatDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Comp_Mat objeto, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO " +  usuarioPimsCS + ".comp_mat " +
										 "(Cd_Compo, Cd_mat_ini, cd_mat_fim, instancia, rowversion) " +
										 "VALUES (?,?,?,?,?)" );
			st.setDouble(1, objeto.getCdCompo());
			st.setString(2, objeto.getCdMatIni());
			st.setString(3, objeto.getCdMatFim());
			st.setString(4, objeto.getInstancia());
			st.setDouble(5, objeto.getRowVersion());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela COMP_MAT \n \n" + e.getMessage() + "\n \n" 
								+ "Inserindo " + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public List<Comp_Mat> listarTodosDoTipo(String tipo, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT m.cd_compo, m.cd_mat_ini, m.cd_mat_fim, m.instancia, m.rowVersion "  + 
										  "FROM  " + usuarioPimsCS + ".comp_mat m, " + usuarioPimsCS + ".componen c " + 
										  "WHERE m.cd_compo = c.cd_compo " + 
										  "AND c.fg_tp_comp = ?");
//			st.setString(1, usuarioPimsCS);
//			st.setString(2, usuarioPimsCS);
			st.setString(1, tipo);
			rs = st.executeQuery();
			List<Comp_Mat> lista = new ArrayList<Comp_Mat>();
			while (rs.next()) {
				Comp_Mat comp_Mat = instanciarComp_Mat(rs);
				lista.add(comp_Mat);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException("Tabela COMP_MAT \n \n" + e.getMessage());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarRowVersion(Double AnoMesReferencia, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM " +  usuarioPimsCS + ".comp_mat " +
										  "WHERE rowVersion = ?");
			st.setDouble(1, AnoMesReferencia);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela COMP_MAT \n \n" + e.getMessage() + "\n \n" 
								+ "Ano e Mes de Referencia " + AnoMesReferencia );
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	private Comp_Mat instanciarComp_Mat(ResultSet rs) throws SQLException {
		Comp_Mat comp_Mat = new Comp_Mat();
		comp_Mat.setCdCompo(rs.getDouble("cd_compo"));
		comp_Mat.setCdMatIni(rs.getString("cd_mat_ini"));
		comp_Mat.setCdMatFim(rs.getString("cd_mat_fim"));
		comp_Mat.setInstancia(rs.getString("instancia"));
		comp_Mat.setRowVersion(rs.getDouble("rowVersion"));
		return comp_Mat;
	}

}
