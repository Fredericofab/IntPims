package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import db.DB;
import db.DbException;
import model.dao.Cstg_IntVMDao;
import model.entities.Cstg_IntVM;

public class Cstg_IntVMDaoJDBC implements Cstg_IntVMDao {

	private Connection conexao;

	public Cstg_IntVMDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Cstg_IntVM objeto, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO " +  usuarioPimsCS + ".cstg_intVM " +
										 "(Cd_empresa, dt_refer, cd_mater, qt_valor, instancia) " +
										 "VALUES (?,?,?,?,?)" );
			st.setString(1, objeto.getCdEmpresa());
			st.setDate(2, new java.sql.Date(objeto.getDtRefer().getTime()));
			st.setString(3, objeto.getCdMater());
			st.setDouble(4, objeto.getPrecoUnit());
			st.setString(5, objeto.getInstancia());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTVM \n \n" + e.getMessage() + "\n \n" 
								+ "Inserindo " + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM " +  usuarioPimsCS + ".cstg_intVM " +
										  "WHERE dt_refer BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') ");
			st.setString(1, dataInicio);
			st.setString(2, dataFim);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTVM \n \n" + e.getMessage() + "\n \n" 
								+ "Deletando Movimento entre " + dataInicio + " e " + dataFim );
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Cstg_IntVM pesquisarPorChave(String cdMater, Date dtRefer, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".cstg_intVM "
										+ "WHERE dt_refer =? AND cd_mater = ? ");
			st.setDate(1, new java.sql.Date(dtRefer.getTime()));
			st.setString(2, cdMater);
			rs = st.executeQuery();
			if (rs.next()) {
				Cstg_IntVM cstg_intVM = instanciaDados(rs);
				return cstg_intVM;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela cstg_intVM \n \n" + e.getMessage() + "\n \n" +
								  "Pesquisando Material " + cdMater + " Data " + dtRefer);
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	private Cstg_IntVM instanciaDados(ResultSet rs) throws SQLException {
		Cstg_IntVM cstg_intVM = new Cstg_IntVM();
		cstg_intVM.setCdEmpresa(rs.getString("Cd_empresa"));
		cstg_intVM.setCdMater(rs.getString("cd_mater"));
		cstg_intVM.setDtRefer(new java.util.Date(rs.getTimestamp("dt_refer").getTime()));
		cstg_intVM.setInstancia(rs.getString("instancia"));
		cstg_intVM.setPrecoUnit(rs.getDouble("qt_valor"));
		return cstg_intVM;
	}
}
