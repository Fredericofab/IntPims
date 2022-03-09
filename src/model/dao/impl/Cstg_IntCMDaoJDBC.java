package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import db.DB;
import db.DbException;
import model.dao.Cstg_IntCMDao;
import model.entities.Cstg_IntCM;

public class Cstg_IntCMDaoJDBC implements Cstg_IntCMDao {

	private Connection conexao;

	public Cstg_IntCMDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Cstg_IntCM objeto, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO " +  usuarioPimsCS + ".cstg_intCM " +
										 "(Fg_Origem, Cd_empresa, dt_refer, cd_mater, cd_ccusto, fg_tipo, qt_mater, qt_valor,de_mater, cd_equipto, instancia) " +
										 "VALUES (?,?,?,?,?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getFgOrigem());
			st.setString(2, objeto.getCdEmpresa());
			st.setDate(3, new java.sql.Date(objeto.getDtRefer().getTime()));
			st.setString(4, objeto.getCdMater());
			st.setDouble(5, objeto.getCdCCusto());
			st.setString(6, objeto.getFgTipo());
			st.setDouble(7, objeto.getQtMater());
			st.setDouble(8, objeto.getPrecoUnit());
			st.setString(9, objeto.getDeMater());
			st.setString(10, objeto.getCdEquipto());
			st.setString(11, objeto.getInstancia());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTCM \n \n" + e.getMessage() + "\n \n" 
								+ "Inserindo " + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(Cstg_IntCM objeto, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE " +  usuarioPimsCS + ".cstg_intCM " 
					 					+ "SET qt_mater = ?, qt_valor = ?  " 
										+ "WHERE dt_refer = ? AND cd_mater = ? AND cd_ccusto = ? ");
			st.setDouble(1, objeto.getQtMater());
			st.setDouble(2, objeto.getPrecoUnit());
			st.setDate(3, new java.sql.Date(objeto.getDtRefer().getTime()));
			st.setString(4, objeto.getCdMater());
			st.setDouble(5, objeto.getCdCCusto());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela cstg_intCM  \n \n" + e.getMessage() + "\n \n" +
								  "Atualizando "+ objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM " +  usuarioPimsCS + ".cstg_intCM " +
										  "WHERE dt_refer BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') ");
			st.setString(1, dataInicio);
			st.setString(2, dataFim);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTCM \n \n" + e.getMessage() + "\n \n" 
								+ "Movimento entre " + dataInicio + " e " + dataFim );
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Cstg_IntCM pesquisarPorChave(String cdMater, Date dtRefer, Double cdCCusto, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".cstg_intCM " 
										+ "WHERE dt_refer = ? AND cd_mater = ? AND cd_ccusto = ? ");
			st.setDate(1, new java.sql.Date(dtRefer.getTime()));
			st.setString(2, cdMater);
			st.setDouble(3, cdCCusto);
			rs = st.executeQuery();
			if (rs.next()) {
				Cstg_IntCM cstg_intCM = instanciaDados(rs);
				return cstg_intCM;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela cstg_intCM \n \n" + e.getMessage() + "\n \n" +
								  "Pesquisando Material " + cdMater + " Data " + dtRefer + " CCusto " + cdCCusto);
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	private Cstg_IntCM instanciaDados(ResultSet rs) throws SQLException {
		Cstg_IntCM cstg_intCM = new Cstg_IntCM();
		cstg_intCM.setFgOrigem(rs.getString("Fg_Origem"));
		cstg_intCM.setCdEmpresa(rs.getString("Cd_empresa"));
		cstg_intCM.setDtRefer(new java.util.Date(rs.getTimestamp("dt_refer").getTime()));
		cstg_intCM.setCdMater(rs.getString("cd_mater"));
		cstg_intCM.setCdCCusto(rs.getDouble("cd_ccusto"));
		cstg_intCM.setFgTipo(rs.getString("fg_tipo"));
		cstg_intCM.setQtMater(rs.getDouble("qt_mater"));
		cstg_intCM.setPrecoUnit(rs.getDouble("qt_valor"));
		cstg_intCM.setDeMater(rs.getString("de_mater"));
		cstg_intCM.setCdEquipto(rs.getString("cd_equipto"));
		cstg_intCM.setInstancia(rs.getString("instancia"));
		return cstg_intCM;
	}
}
