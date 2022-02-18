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
			throw new DbException("Tabela CSTG_INTFP \n \n" + e.getMessage() + "\n \n" 
								+ "Data de Referencia " + dataref);
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
										 "(Cd_empresa, dt_refer, cd_func, qt_horas, qt_valor, instancia) " +
										 "VALUES (?,?,?,?,?,?)" );
			st.setString(1, cstg_IntFP.getCdEmpresa());
			st.setString(2, cstg_IntFP.getDtRefer());
			st.setDouble(3, cstg_IntFP.getCdFunc());
			st.setDouble(4, cstg_IntFP.getQtHoras());
			st.setDouble(5, cstg_IntFP.getQtValor());
			st.setString(6, cstg_IntFP.getInstancia());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTFP \n \n" + e.getMessage() + "\n \n" 
								+ "Registro " + cstg_IntFP);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Boolean existeCCustos(Double codCCustos, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".ccustos WHERE cd_ccusto = ?");
			st.setDouble(1, codCCustos);
			rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw new DbException("Tabela CCUSTOS \n \n" + e.getMessage() + "\n \n" 
								+ "Centro de Custos: "+ codCCustos);
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public Boolean existeApt_os_he(String numeroOS, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".apt_os_he WHERE no_boletim = ?");
			st.setString(1, numeroOS);
			rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw new DbException("Tabela APT_OS_HE \n \n" + e.getMessage() + "\n \n" 
					+ "Numero da OS " + numeroOS);
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public Date dataSaidaApt_os_he(String numeroOS, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".apt_os_he WHERE no_boletim = ?");
			st.setString(1, numeroOS);
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
			throw new DbException("Tabela APT_OS_HE (Data de Saida) \n \n" + e.getMessage() + "\n \n" 
					+ "Numero da OS " + numeroOS);
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public Double codCCustoApt_os_he(String numeroOS, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".apt_os_he WHERE no_boletim = ?");
			st.setString(1, numeroOS);
			rs = st.executeQuery();
			if (rs.next()) {
				return rs.getDouble("cd_ccusto");
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela APT_OS_HE (Centro de Custos) \n \n" + e.getMessage() + "\n \n" 
					+ "Numero da OS " + numeroOS);
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public Double codEquiptoApt_os_he(String numeroOS, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".apt_os_he WHERE no_boletim = ?");
			st.setString(1, numeroOS);
			rs = st.executeQuery();
			if (rs.next()) {
				return rs.getDouble("cd_equipto");
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela APT_OS_HE (Equipamentos) \n \n" + e.getMessage() + "\n \n" 
					+ "Numero da OS " + numeroOS);
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
}


