package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.ProcessarFolhaDao;
import model.entities.Cstg_IntPF;

public class ProcessarFolhaDaoJDBC implements ProcessarFolhaDao {
	private Connection conexao;

	public ProcessarFolhaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public Integer contarVerbasSemDefinicao() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT count(*) contagem FROM verba_folha WHERE Importar IS NULL");
			rs = st.executeQuery();
			rs.next();
			Integer contagem = rs.getInt("contagem");
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Erro na Contagem das Verbas Sem Definicao " + e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}
	
	@Override
	public Integer deletarDadosFolhaAnoMes(String anoMes) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM dados_folha WHERE Ano_Mes = ? ");
			st.setString(1, anoMes);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Movimento da Folha " + anoMes + "\n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarSumarioFolhaAnoMes(String anoMes) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM sumario_folha WHERE Ano_Mes = ? ");
			st.setString(1, anoMes);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Sumario da Movimento " + anoMes + "\n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
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
}


