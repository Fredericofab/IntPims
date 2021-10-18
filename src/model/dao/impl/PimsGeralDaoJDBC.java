package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import db.DB;
import db.DbException;
import model.dao.PimsGeralDao;
import model.entities.Cstg_IntPF;

public class PimsGeralDaoJDBC implements PimsGeralDao {
	private Connection conexao;

	public PimsGeralDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}
	
	@Override
	public Integer deletarCstg_IntFP(String dataref, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM " +  usuarioPimsCS + ".cstg_intFP " +
										  "WHERE TO_CHAR(dt_refer,'dd/mm/yyyy') = ? ");
			st.setString(1, dataref);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do CSTG_INTFP " + dataref + "\n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void gravarCstg_IntFP(Cstg_IntPF cstg_IntFP, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO " +  usuarioPimsCS + ".cstg_intFP " +
										 "(Cd_empresa, dt_refer, cd_func, qt_valor, instancia) " +
										 "VALUES (?,?,?,?,?)" );
			st.setString(1, cstg_IntFP.getCdEmpresa());
			st.setString(2, cstg_IntFP.getDtRefer());
			st.setDouble(3, cstg_IntFP.getCdFunc());
			st.setDouble(4, cstg_IntFP.getQtValor());
			st.setString(5, cstg_IntFP.getInstancia());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao do CSTG_INTFP " + "\n" + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}


	@Override
	public Boolean existeApt_os_he(Double numeroOS, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".apt_os_he WHERE no_boletim = ?");
			st.setDouble(1, numeroOS);
			rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw new DbException("erro na Pesquisa " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}


	@Override
	public Date dataSaidaApt_os_he(Double numeroOS, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".apt_os_he WHERE no_boletim = ?");
			st.setDouble(1, numeroOS);
			rs = st.executeQuery();
			if (rs.next()) {
				if (rs.getDate("dt_saida") == null) {
					return null;
				}
				Date dataSaida = new java.util.Date(rs.getTimestamp("dt_saida").getTime());
				return dataSaida;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("erro na Pesquisa " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
}


