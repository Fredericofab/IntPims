package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.CriticaErpDao;
import model.entities.CriticaErp;

public class CriticaErpDaoJDBC implements CriticaErpDao {
	private Connection conexao;

	public CriticaErpDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(CriticaErp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO criticas_erp "
										+ "(tipo_critica,cod_critica,nome_critica, desc_critica, "
										+ " flag_ativa,ano_mes_analisado, "
										+ " registros_analisados, registros_liberados, registros_ignorados, registros_pendentes, "
										+ " clausula_where, "
										+ " importar, salvar_os_material, "
										+ " salvar_cstg_intVM, salvar_cstg_intCM ,salvar_cstg_intDG) "
										+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getTipoCritica());
			st.setInt(2, objeto.getCodigoCritica());
			st.setString(3, objeto.getNomeCritica());
			st.setString(4, objeto.getDescCritica());
			st.setString(5, objeto.getFlagAtiva());
			st.setString(6, objeto.getAnoMesAnalisado());
			st.setInt(7, objeto.getRegistrosAnalisados());
			st.setInt(8, objeto.getRegistrosLiberados());
			st.setInt(9, objeto.getRegistrosIgnorados());
			st.setInt(10, objeto.getRegistrosPendentes());
			st.setString(11, objeto.getClausulaWhere());
			st.setString(12, objeto.getImportar());			
			st.setString(13, objeto.getSalvarOS_Material());			
			st.setString(14, objeto.getSalvarCstg_IntVM());			
			st.setString(15, objeto.getSalvarCstg_IntCM());			
			st.setString(16, objeto.getSalvarCstg_IntDG());			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(CriticaErp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE criticas_erp "
										+ "SET nome_critica = ?, desc_critica = ?, flag_ativa =?, ano_mes_analisado = ?,  "
										+ "registros_analisados = ?, registros_liberados = ?, registros_ignorados = ?, registros_pendentes = ?, "
										+ "clausula_where = ?, "
										+ " importar = ?, salvar_os_material = ?, "
										+ " salvar_cstg_intVM = ?, salvar_cstg_intCM = ?, salvar_cstg_intDG = ? "
										+ "WHERE tipo_critica = ? AND cod_critica = ? ");
			st.setString(1, objeto.getNomeCritica());
			st.setString(2, objeto.getDescCritica());
			st.setString(3, objeto.getFlagAtiva());
			st.setString(4, objeto.getAnoMesAnalisado());
			st.setInt(5, objeto.getRegistrosAnalisados());
			st.setInt(6, objeto.getRegistrosLiberados());
			st.setInt(7, objeto.getRegistrosIgnorados());
			st.setInt(8, objeto.getRegistrosPendentes());
			st.setString(9, objeto.getClausulaWhere());
			st.setString(10, objeto.getImportar());			
			st.setString(11, objeto.getSalvarOS_Material());			
			st.setString(12, objeto.getSalvarCstg_IntVM());			
			st.setString(13, objeto.getSalvarCstg_IntCM());			
			st.setString(14, objeto.getSalvarCstg_IntDG());			
			st.setString(15, objeto.getTipoCritica());
			st.setInt(16, objeto.getCodigoCritica());
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
	public CriticaErp pesquisarPorChave(String tipoCritica, Integer codigoCrtica) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM criticas_erp "
										+ "WHERE tipo_critica = ? AND cod_critica = ? ");
			st.setString(1, tipoCritica);
			st.setInt(2, codigoCrtica);
			rs = st.executeQuery();
			if (rs.next()) {
				CriticaErp criticasErp = instanciaCriticasErp(rs);
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
	public List<CriticaErp> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM criticas_erp ORDER BY tipo_critica, cod_critica");
			rs = st.executeQuery();
			List<CriticaErp> lista = new ArrayList<CriticaErp>();
			while (rs.next()) {
				CriticaErp criticasErp = instanciaCriticasErp(rs);
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
	
	private CriticaErp instanciaCriticasErp(ResultSet rs) throws SQLException {
		CriticaErp criticasErp = new CriticaErp();
		criticasErp.setNomeCritica(rs.getString("nome_critica"));
		criticasErp.setDescCritica(rs.getString("desc_critica"));
		criticasErp.setFlagAtiva(rs.getString("flag_ativa"));
		criticasErp.setAnoMesAnalisado(rs.getString("ano_mes_analisado"));
		criticasErp.setRegistrosAnalisados(rs.getInt("registros_analisados"));
		criticasErp.setRegistrosLiberados(rs.getInt("registros_liberados"));
		criticasErp.setRegistrosIgnorados(rs.getInt("registros_ignorados"));
		criticasErp.setRegistrosPendentes(rs.getInt("registros_pendentes"));
		criticasErp.setClausulaWhere(rs.getString("clausula_where"));
		criticasErp.setTipoCritica(rs.getString("tipo_critica"));
		criticasErp.setCodigoCritica(rs.getInt("cod_critica"));

		criticasErp.setImportar(rs.getString("importar"));			
		criticasErp.setSalvarOS_Material(rs.getString("salvar_os_material"));			
		criticasErp.setSalvarCstg_IntVM(rs.getString("salvar_cstg_intVM"));			
		criticasErp.setSalvarCstg_IntCM(rs.getString("salvar_cstg_intCM"));			
		criticasErp.setSalvarCstg_IntDG(rs.getString("salvar_cstg_intDG"));			
	
		return criticasErp;
	}


}

