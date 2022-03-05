package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.PoliticasErpDao;
import model.entities.PoliticasErp;

public class PoliticasErpDaoJDBC implements PoliticasErpDao {
	private Connection conexao;

	public PoliticasErpDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(PoliticasErp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO politicas_erp "
										+ "(cod_politica,nome_politica, desc_politica, "
										+ " flag_ativa,  clausula_where, "
										+ " importar, salvar_os_material, "
										+ " salvar_cstg_intVM, salvar_cstg_intCM ,salvar_cstg_intDG, "
										+ " ano_mes_analisado, registros_aplicados) "
										+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)" );
			st.setInt(1, objeto.getCodPolitica());
			st.setString(2, objeto.getNomePolitica());
			st.setString(3, objeto.getDescPolitica());
			st.setString(4, objeto.getFlagAtiva());
			st.setString(5, objeto.getClausulaWhere());
			st.setString(6, objeto.getImportar());			
			st.setString(7, objeto.getSalvarOS_Material());			
			st.setString(8, objeto.getSalvarCstg_IntVM());			
			st.setString(9, objeto.getSalvarCstg_IntCM());			
			st.setString(10, objeto.getSalvarCstg_IntDG());			
			st.setString(11, objeto.getAnoMesAnalisado());			
			st.setInt(12, objeto.getRegistrosAplicados());			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Politicas_Erp  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(PoliticasErp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE politicas_erp "
										+ "SET nome_politica = ?, desc_politica = ?, flag_ativa =?, "
										+ "clausula_where = ?, "
										+ " importar = ?, salvar_os_material = ?, "
										+ " salvar_cstg_intVM = ?, salvar_cstg_intCM = ?, salvar_cstg_intDG = ?, "
										+ " ano_mes_analisado = ?, registros_aplicados = ? "
										+ "WHERE cod_politica = ? ");
			st.setString(1, objeto.getNomePolitica());
			st.setString(2, objeto.getDescPolitica());
			st.setString(3, objeto.getFlagAtiva());
			st.setString(4, objeto.getClausulaWhere());
			st.setString(5, objeto.getImportar());			
			st.setString(6, objeto.getSalvarOS_Material());			
			st.setString(7, objeto.getSalvarCstg_IntVM());			
			st.setString(8, objeto.getSalvarCstg_IntCM());			
			st.setString(9, objeto.getSalvarCstg_IntDG());			
			st.setString(10, objeto.getAnoMesAnalisado());			
			st.setInt(11, objeto.getRegistrosAplicados());			
			st.setInt(12, objeto.getCodPolitica());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Politicas_Erp  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(Integer codPolitica) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM politicas_erp "
										+ "WHERE cod_politica = ? ");
			st.setInt(1, codPolitica);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Politicas_Erp  \n \n" + e.getMessage() + "\n \n" + "Politica : " + codPolitica);
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public PoliticasErp pesquisarPorChave(Integer codPolitica) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM politicas_erp "
										+ "WHERE cod_politica = ? ");
			st.setInt(1, codPolitica);
			rs = st.executeQuery();
			if (rs.next()) {
				PoliticasErp politicasErp = instanciaPoliticasErp(rs);
				return politicasErp;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Politicas_Erp  \n \n" + e.getMessage() + "\n \n" + "Politica : " + codPolitica);
		}
		finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}

	}

	@Override
	public List<PoliticasErp> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM politicas_erp ORDER BY cod_politica");
			rs = st.executeQuery();
			List<PoliticasErp> lista = new ArrayList<PoliticasErp>();
			while (rs.next()) {
				PoliticasErp politicasErp = instanciaPoliticasErp(rs);
				lista.add(politicasErp)
				;
			}
			return lista;
		} 
		catch (SQLException e) {
			throw new DbException("Tabela Politicas_Erp  \n \n" + e.getMessage() );
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public void limparEstatisticas() {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE politicas_erp "
										+ "SET ano_mes_analisado = null, registros_aplicados = null");
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Politicas_Erp  \n \n" + e.getMessage() );
		} finally {
			DB.fecharStatement(st);
		}
	}
	
	private PoliticasErp instanciaPoliticasErp(ResultSet rs) throws SQLException {
		PoliticasErp politicasErp = new PoliticasErp();
		politicasErp.setNomePolitica(rs.getString("nome_politica"));
		politicasErp.setDescPolitica(rs.getString("desc_politica"));
		politicasErp.setFlagAtiva(rs.getString("flag_ativa"));
		politicasErp.setClausulaWhere(rs.getString("clausula_where"));
		politicasErp.setCodPolitica(rs.getInt("cod_politica"));

		politicasErp.setImportar(rs.getString("importar"));			
		politicasErp.setSalvarOS_Material(rs.getString("salvar_os_material"));			
		politicasErp.setSalvarCstg_IntVM(rs.getString("salvar_cstg_intVM"));			
		politicasErp.setSalvarCstg_IntCM(rs.getString("salvar_cstg_intCM"));			
		politicasErp.setSalvarCstg_IntDG(rs.getString("salvar_cstg_intDG"));			
		
		politicasErp.setAnoMesAnalisado(rs.getString("ano_mes_analisado"));		
		politicasErp.setRegistrosAplicados(rs.getInt("registros_aplicados"));		

		return politicasErp;
	}
}

