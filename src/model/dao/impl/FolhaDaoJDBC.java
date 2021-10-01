package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.FolhaDao;
import model.entities.Folha;

public class FolhaDaoJDBC implements FolhaDao {
	private Connection conexao;

	public FolhaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Folha objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("INSERT INTO folha "
										+ "(Ano_Mes, Cod_Centro_Custos, Desc_Centro_Custos, Cod_Verba, Desc_Verba, "
										+ " Valor_Verba, Importar, Observacao) "
										+ " VALUES (?,?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getAnoMes());
			st.setDouble(2, objeto.getCodCentroCustos());
			st.setString(3, objeto.getDescCentroCustos());
			st.setDouble(4, objeto.getCodVerba());
			st.setString(5, objeto.getDescVerba());
			st.setDouble(6, objeto.getValorVerba());
			st.setString(7, objeto.getImportar().toUpperCase());
			st.setString(8, objeto.getObservacao());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(Folha objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("UPDATE folha "
										+ "SET Importar = ?, Observacao = ?, "
										+ "    Desc_Centro_Custos =?, Desc_Verba = ?, Valor_Verba = ?" 
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ? AND Cod_Verba = ? ");
			st.setString(1, objeto.getImportar().toUpperCase());
			st.setString(2, objeto.getObservacao());
			st.setString(3, objeto.getDescCentroCustos());
			st.setString(4, objeto.getDescVerba());
			st.setDouble(5, objeto.getValorVerba());
			
			st.setString(6, objeto.getAnoMes());
			st.setDouble(7, objeto.getCodCentroCustos());
			st.setDouble(8, objeto.getCodVerba());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String anoMes, Double codCentroCustos, Double codVerba) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM folha "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ? AND Cod_Verba = ? ");
			st.setString(1, anoMes);
			st.setDouble(2, codCentroCustos);
			st.setDouble(3, codVerba);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Folha pesquisarPorChave(String anoMes, Double codCentroCustos, Double codVerba)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM folha "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ? AND Cod_Verba = ? ");
			st.setString(1, anoMes);
			st.setDouble(2, codCentroCustos);
			st.setDouble(3, codVerba);
			rs = st.executeQuery();
			if (rs.next()) {
				Folha dadosFolha = instanciaDadosFolha(rs);
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
	public List<Folha> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM folha ORDER BY Ano_Mes, Cod_Centro_Custos, Cod_Verba");
			rs = st.executeQuery();
			List<Folha> lista = new ArrayList<Folha>();
			while (rs.next()) {
				Folha dadosFolha = instanciaDadosFolha(rs);
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

	@Override
	public Integer deletarTodos() {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM folha ");
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Movimento da Folha  \n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}
	
	private Folha instanciaDadosFolha(ResultSet rs) throws SQLException {
		Folha dadosFolha = new Folha();
		dadosFolha.setAnoMes(rs.getString("Ano_Mes"));
		dadosFolha.setCodCentroCustos(rs.getDouble("Cod_Centro_Custos"));
		dadosFolha.setDescCentroCustos(rs.getString("Desc_Centro_Custos"));		
		dadosFolha.setCodVerba(rs.getDouble("Cod_Verba"));		
		dadosFolha.setDescVerba(rs.getString("Desc_Verba"));		
		dadosFolha.setValorVerba(rs.getDouble("Valor_Verba"));		
		dadosFolha.setImportar(rs.getString("Importar"));		
		dadosFolha.setObservacao(rs.getString("Observacao"));		
		return dadosFolha;
	}
}

