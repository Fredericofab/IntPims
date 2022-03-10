package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import db.DB;
import db.DbException;
import model.dao.Cstg_IntDGDao;
import model.entities.Cstg_IntDG;

public class Cstg_IntDGDaoJDBC implements Cstg_IntDGDao {

	private Connection conexao;

	public Cstg_IntDGDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Cstg_IntDG objeto, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO " +  usuarioPimsCS + ".cstg_intDG " +
										 "(Cd_empresa, dt_refer, cd_ccusto, cd_cta_con, qt_valor, instancia) " +
										 "VALUES (?,?,?,?,?,?)" );
			st.setString(1, objeto.getCdEmpresa());
			st.setDate(2, new java.sql.Date(objeto.getDtRefer().getTime()));
			st.setDouble(3, objeto.getCdCCusto());
			st.setString(4, objeto.getCdCtaCon());
			st.setDouble(5, objeto.getQtValor());
			st.setString(6, objeto.getInstancia());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTDG \n \n" + e.getMessage() + "\n \n" 
								+ "Inserindo " + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(Cstg_IntDG objeto, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE " +  usuarioPimsCS + ".cstg_intDG " 
					 					+ "SET qt_valor = ?  " 
										+ "WHERE dt_refer = ? AND cd_cta_con = ? AND cd_ccusto = ? ");
			st.setDouble(1, objeto.getQtValor());
			st.setDate(2, new java.sql.Date(objeto.getDtRefer().getTime()));
			st.setString(3, objeto.getCdCtaCon());
			st.setDouble(4, objeto.getCdCCusto());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela cstg_intDG  \n \n" + e.getMessage() + "\n \n" +
								  "Atualizando "+ objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM " +  usuarioPimsCS + ".cstg_intDG " +
										  "WHERE dt_refer BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') ");
			st.setString(1, dataInicio);
			st.setString(2, dataFim);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTDG \n \n" + e.getMessage() + "\n \n" 
								+ "Movimento entre " + dataInicio + " e " + dataFim );
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Cstg_IntDG pesquisarPorChave(String cdCtaCon, Date dtRefer, Double cdCCusto, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".cstg_intDG " 
										+ "WHERE dt_refer = ? AND cd_cta_con = ? AND cd_ccusto = ? ");
			st.setDate(1, new java.sql.Date(dtRefer.getTime()));
			st.setString(2, cdCtaCon);
			st.setDouble(3, cdCCusto);
			rs = st.executeQuery();
			if (rs.next()) {
				Cstg_IntDG cstg_intDG = instanciaDados(rs);
				return cstg_intDG;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela cstg_intDG \n \n" + e.getMessage() + "\n \n" +
								  "Pesquisando Conta " + cdCtaCon + " Data " + dtRefer + " CCusto " + cdCCusto);
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
	private Cstg_IntDG instanciaDados(ResultSet rs) throws SQLException {
		Cstg_IntDG cstg_intDG = new Cstg_IntDG();
		cstg_intDG.setCdEmpresa(rs.getString("Cd_empresa"));
		cstg_intDG.setDtRefer(new java.util.Date(rs.getTimestamp("dt_refer").getTime()));
		cstg_intDG.setCdCtaCon(rs.getString("cd_cta_con"));
		cstg_intDG.setCdCCusto(rs.getDouble("cd_ccusto"));
		cstg_intDG.setQtValor(rs.getDouble("qt_valor"));
		cstg_intDG.setInstancia(rs.getString("instancia"));
		return cstg_intDG;
	}
}
