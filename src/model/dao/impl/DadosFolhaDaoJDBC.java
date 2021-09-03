package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DadosFolhaDao;
import model.entities.DadosFolha;

public class DadosFolhaDaoJDBC implements DadosFolhaDao {
	private Connection conexao;

	public DadosFolhaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(DadosFolha objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("INSERT INTO dados_folha "
										+ "(Ano_Mes, Cod_Centro_Custos, Desc_Centro_Custos, Cod_Verba, Desc_Verba, "
										+ " Valor_Verba, Importar, Observacao) "
										+ " VALUES (?,?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getAnoMes());
			st.setString(2, objeto.getCodCentroCustos());
			st.setString(3, objeto.getDescCentroCustos());
			st.setString(4, objeto.getCodVerba());
			st.setString(5, objeto.getDescVerba());
			st.setDouble(6, objeto.getValorVerba());
			st.setString(7, objeto.getImportar());
			st.setString(8, objeto.getObservacao());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(DadosFolha objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("UPDATE dados_folha "
										+ "SET Importar = ?, Observacao = ?, "
										+ "    Desc_Centro_Custos =?, Desc_Verba = ?, Valor_Verba = ?" 
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ? AND Cod_Verba = ? ");
			st.setString(1, objeto.getImportar());
			st.setString(2, objeto.getObservacao());
			st.setString(3, objeto.getDescCentroCustos());
			st.setString(4, objeto.getDescVerba());
			st.setDouble(5, objeto.getValorVerba());
			
			st.setString(6, objeto.getAnoMes());
			st.setString(7, objeto.getCodCentroCustos());
			st.setString(8, objeto.getCodVerba());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String anoMes, String codCentroCustos, String codVerba) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM dados_folha "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ? AND Cod_Verba = ? ");
			st.setString(1, anoMes);
			st.setString(2, codCentroCustos);
			st.setString(3, codVerba);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public DadosFolha pesquisarPorChave(String anoMes, String codCentroCustos, String codVerba)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM dados_folha "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ? AND Cod_Verba = ? ");
			st.setString(1, anoMes);
			st.setString(2, codCentroCustos);
			st.setString(3, codVerba);
			rs = st.executeQuery();
			if (rs.next()) {
				DadosFolha dadosFolha = instanciaDadosFolha(rs);
				return dadosFolha;
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
	public List<DadosFolha> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM dados_folha ");
			rs = st.executeQuery();
			List<DadosFolha> lista = new ArrayList<DadosFolha>();
			while (rs.next()) {
				DadosFolha dadosFolha = instanciaDadosFolha(rs);
				lista.add(dadosFolha)
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

	private DadosFolha instanciaDadosFolha(ResultSet rs) throws SQLException {
		DadosFolha dadosFolha = new DadosFolha();
		dadosFolha.setAnoMes(rs.getString("Ano_Mes"));
		dadosFolha.setCodCentroCustos(rs.getString("Cod_Centro_Custos"));
		dadosFolha.setDescCentroCustos(rs.getString("Desc_Centro_Custos"));		
		dadosFolha.setCodVerba(rs.getString("Cod_Verba"));		
		dadosFolha.setDescVerba(rs.getString("Desc_Verba"));		
		dadosFolha.setValorVerba(rs.getDouble("Valor_Verba"));		
		dadosFolha.setImportar(rs.getString("Importar"));		
		dadosFolha.setObservacao(rs.getString("Observacao"));		
		return dadosFolha;
	}
}

