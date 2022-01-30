package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.FolhaSumarizadaDao;
import model.entities.FolhaSumarizada;

public class FolhaSumarizadaDaoJDBC implements FolhaSumarizadaDao {
	private Connection conexao;

	public FolhaSumarizadaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(FolhaSumarizada objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("INSERT INTO folha_sumarizada "
										+ "(Ano_Mes, Cod_Centro_Custos, Desc_Centro_Custos, Qtde_Importar_Sim, Total_Importar_Sim, "
										+ " Qtde_Importar_Nao, Total_Importar_Nao, Total_Referencia_Sim) "
										+ " VALUES (?,?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getAnoMes());
			st.setDouble(2, objeto.getCodCentroCustos());
			st.setString(3, objeto.getDescCentroCustos());
			st.setDouble(4, objeto.getQdteImportarSim());
			st.setDouble(5, objeto.getTotalImportarSim());
			st.setDouble(6, objeto.getQdteImportarNao());
			st.setDouble(7, objeto.getTotalImportarNao());
			st.setDouble(8, objeto.getTotalReferenciaSim());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(FolhaSumarizada objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("UPDATE folha_sumarizada "
										+ "SET Desc_Centro_Custos =?, Qtde_Importar_Sim = ?, Total_Importar_Sim = ?" 
										+ "Qtde_Importar_Nao = ?, Total_Importar_Nao = ?, Total_Referencia_sim = ?"
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ?");
			st.setString(1, objeto.getDescCentroCustos());
			st.setDouble(2, objeto.getQdteImportarSim());
			st.setDouble(3, objeto.getTotalImportarSim());
			st.setDouble(4, objeto.getQdteImportarNao());
			st.setDouble(5, objeto.getTotalImportarNao());
			st.setDouble(6, objeto.getTotalReferenciaSim());
			
			st.setString(7, objeto.getAnoMes());
			st.setDouble(8, objeto.getCodCentroCustos());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String anoMes, Double codCentroCustos) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM folha_sumarizada "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ?");
			st.setString(1, anoMes);
			st.setDouble(2, codCentroCustos);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public FolhaSumarizada pesquisarPorChave(String anoMes, Double codCentroCustos)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM folha_sumarizada "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ?");
			st.setString(1, anoMes);
			st.setDouble(2, codCentroCustos);
			rs = st.executeQuery();
			if (rs.next()) {
				FolhaSumarizada sumarioFolha = instanciaSumarioFolha(rs);
				return sumarioFolha;
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
	public List<FolhaSumarizada> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM folha_sumarizada ORDER BY Ano_Mes, Cod_Centro_Custos");
			rs = st.executeQuery();
			List<FolhaSumarizada> lista = new ArrayList<FolhaSumarizada>();
			while (rs.next()) {
				FolhaSumarizada sumarioFolha = instanciaSumarioFolha(rs);
				lista.add(sumarioFolha)
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
	public Integer deletarTodos() {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM folha_sumarizada ");
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Sumario da Movimento \n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}
	
	private FolhaSumarizada instanciaSumarioFolha(ResultSet rs) throws SQLException {
		FolhaSumarizada sumarioFolha = new FolhaSumarizada();
		sumarioFolha.setAnoMes(rs.getString("Ano_Mes"));
		sumarioFolha.setCodCentroCustos(rs.getDouble("Cod_Centro_Custos"));
		sumarioFolha.setDescCentroCustos(rs.getString("Desc_Centro_Custos"));
		sumarioFolha.setQdteImportarSim(rs.getInt("Qtde_Importar_Sim"));
		sumarioFolha.setTotalImportarSim(rs.getDouble("Total_Importar_Sim"));
		sumarioFolha.setQdteImportarNao(rs.getInt("Qtde_Importar_Nao"));
		sumarioFolha.setTotalImportarNao(rs.getDouble("Total_Importar_Nao"));
		sumarioFolha.setTotalReferenciaSim(rs.getDouble("Total_Referencia_sim"));
		return sumarioFolha;
	}
}

