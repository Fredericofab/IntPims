package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SumarioFolhaDao;
import model.entities.SumarioFolha;

public class SumarioFolhaDaoJDBC implements SumarioFolhaDao {
	private Connection conexao;

	public SumarioFolhaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(SumarioFolha objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("INSERT INTO sumario_folha "
										+ "(Ano_Mes, Cod_Centro_Custos, Desc_Centro_Custos, Qtde_Importar_Sim, Total_Importar_Sim, "
										+ " Qtde_Importar_Nao, Total_Importar_Nao) "
										+ " VALUES (?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getAnoMes());
			st.setString(2, objeto.getCodCentroCustos());
			st.setString(3, objeto.getDescCentroCustos());
			st.setDouble(4, objeto.getQdteImportarSim());
			st.setDouble(5, objeto.getTotalImportarSim());
			st.setDouble(6, objeto.getQdteImportarNao());
			st.setDouble(7, objeto.getTotalImportarNao());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(SumarioFolha objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("UPDATE sumario_folha "
										+ "SET Desc_Centro_Custos =?, Qtde_Importar_Sim = ?, Total_Importar_Sim = ?" 
										+ "Qtde_Importar_Nao = ?, Total_Importar_Nao = "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ?");
			st.setString(1, objeto.getDescCentroCustos());
			st.setDouble(2, objeto.getQdteImportarSim());
			st.setDouble(3, objeto.getTotalImportarSim());
			st.setDouble(4, objeto.getQdteImportarNao());
			st.setDouble(5, objeto.getTotalImportarNao());
			
			st.setString(6, objeto.getAnoMes());
			st.setString(7, objeto.getCodCentroCustos());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String anoMes, String codCentroCustos) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM sumario_folha "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ?");
			st.setString(1, anoMes);
			st.setString(2, codCentroCustos);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public SumarioFolha pesquisarPorChave(String anoMes, String codCentroCustos)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM sumario_folha "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ?");
			st.setString(1, anoMes);
			st.setString(2, codCentroCustos);
			rs = st.executeQuery();
			if (rs.next()) {
				SumarioFolha sumarioFolha = instanciaSumarioFolha(rs);
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
	public List<SumarioFolha> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM sumario_folha ");
			rs = st.executeQuery();
			List<SumarioFolha> lista = new ArrayList<SumarioFolha>();
			while (rs.next()) {
				SumarioFolha sumarioFolha = instanciaSumarioFolha(rs);
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

	private SumarioFolha instanciaSumarioFolha(ResultSet rs) throws SQLException {
		SumarioFolha sumarioFolha = new SumarioFolha();
		sumarioFolha.setAnoMes(rs.getString("Ano_Mes"));
		sumarioFolha.setCodCentroCustos(rs.getString("Cod_Centro_Custos"));
		sumarioFolha.setDescCentroCustos(rs.getString("Desc_Centro_Custos"));
		sumarioFolha.setQdteImportarSim(rs.getDouble("Qtde_Importar_Sim"));
		sumarioFolha.setTotalImportarSim(rs.getDouble("Total_Importar_Sim"));
		sumarioFolha.setQdteImportarNao(rs.getDouble("Qtde_Importar_Nao"));
		sumarioFolha.setTotalImportarNao(rs.getDouble("Total_Importar_Nao"));
		return sumarioFolha;
	}
}

