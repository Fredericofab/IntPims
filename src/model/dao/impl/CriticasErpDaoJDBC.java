package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.CriticasErpDao;
import model.entities.CriticasErp;

public class CriticasErpDaoJDBC implements CriticasErpDao {
	private Connection conexao;

	public CriticasErpDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(CriticasErp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO criticas_erp "
										+ "(tipo_critica,cod_critica,desc_critica, "
										+ " flag_ativa,ano_mes_analisado, "
										+ " registros_analisados, registros_atualizados, registros_pendentes, "
										+ " clausula_where, clausula_set) "
										+ " VALUES (?,?,?,?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getTipoCritica());
			st.setInt(2, objeto.getCodigoCritica());
			st.setString(3, objeto.getDescCritica());
			st.setString(4, objeto.getFlagAtiva());
			st.setString(5, objeto.getAnoMesAnalisado());
			st.setInt(6, objeto.getRegistrosAnalisados());
			st.setInt(7, objeto.getRegistrosAtualizados());
			st.setInt(8, objeto.getRegistrosPendentes());
			st.setString(9, objeto.getClausulaWhere());
			st.setString(10, objeto.getClausulaSet());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(CriticasErp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE criticas_erp "
										+ "SET desc_critica = ?, flag_ativa =?, ano_mes_analisado = ?,  "
										+ "registros_analisados = ?, registros_atualizados = ?, registros_pendentes =?, "
										+ "clausula_where = ?, clausula_set =? "
										+ "WHERE tipo_critica = ? AND cod_critica = ? ");
			st.setString(1, objeto.getDescCritica());
			st.setString(2, objeto.getFlagAtiva());
			st.setString(3, objeto.getAnoMesAnalisado());
			st.setInt(4, objeto.getRegistrosAnalisados());
			st.setInt(5, objeto.getRegistrosAtualizados());
			st.setInt(6, objeto.getRegistrosPendentes());
			st.setString(7, objeto.getClausulaWhere());
			st.setString(8, objeto.getClausulaSet());
			st.setString(9, objeto.getTipoCritica());
			st.setInt(10, objeto.getCodigoCritica());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String tipoCritica, Integer codigoCrtica) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM criticas_erp "
										+ "WHERE tipo_critica = ? AND cod_critica = ? ");
			st.setString(1, tipoCritica);
			st.setInt(2, codigoCrtica);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public CriticasErp pesquisarPorChave(String tipoCritica, Integer codigoCrtica) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM criticas_erp "
										+ "WHERE tipo_critica = ? AND cod_critica = ? ");
			st.setString(1, tipoCritica);
			st.setInt(2, codigoCrtica);
			rs = st.executeQuery();
			if (rs.next()) {
				CriticasErp criticasErp = instanciaCriticasErp(rs);
				return criticasErp;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("erro na Pesquisa " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}

	}

	@Override
	public List<CriticasErp> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM criticas_erp ORDER BY tipo_critica, cod_critica");
			rs = st.executeQuery();
			List<CriticasErp> lista = new ArrayList<CriticasErp>();
			while (rs.next()) {
				CriticasErp criticasErp = instanciaCriticasErp(rs);
				lista.add(criticasErp)
				;
			}
			return lista;
		} 
		catch (SQLException e) {
			throw new DbException("erro na consulta todos - " + e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer ultimoSequencial(String tipoCritica) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT MAX(cod_critica) "
										+ "FROM criticas_erp WHERE tipo_critica = ?");
			st.setString(1, tipoCritica);
			rs = st.executeQuery();
			if (rs.next()) {
				int ultimoSequencial = rs.getInt(1);
				return ultimoSequencial;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("erro na Pesquisa do Ultimo Sequencial " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	
	private CriticasErp instanciaCriticasErp(ResultSet rs) throws SQLException {
		CriticasErp criticasErp = new CriticasErp();
		criticasErp.setDescCritica(rs.getString("desc_critica"));
		criticasErp.setFlagAtiva(rs.getString("flag_ativa"));
		criticasErp.setAnoMesAnalisado(rs.getString("ano_mes_analisado"));
		criticasErp.setRegistrosAnalisados(rs.getInt("registros_analisados"));
		criticasErp.setRegistrosAtualizados(rs.getInt("registros_atualizados"));
		criticasErp.setRegistrosPendentes(rs.getInt("registros_pendentes"));
		criticasErp.setClausulaWhere(rs.getString("clausula_where"));
		criticasErp.setClausulaSet(rs.getString("clausula_set"));
		criticasErp.setTipoCritica(rs.getString("tipo_critica"));
		criticasErp.setCodigoCritica(rs.getInt("cod_critica"));
		return criticasErp;
	}


}

