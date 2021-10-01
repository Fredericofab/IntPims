package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.FuncionariosSumarizadosDao;
import model.entities.FuncionariosSumarizados;

public class FuncionariosSumarizadosDaoJDBC implements FuncionariosSumarizadosDao {
	private Connection conexao;

	public FuncionariosSumarizadosDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(FuncionariosSumarizados objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("INSERT INTO funcionarios_sumarizados "
										+ "(Ano_Mes, Cod_Centro_Custos, Desc_Centro_Custos, Qtde_Funcionarios) "
										+ " VALUES (?,?,?,?)" );
			st.setString(1, objeto.getAnoMes());
			st.setDouble(2, objeto.getCodCentroCustos());
			st.setString(3, objeto.getDescCentroCustos());
			st.setDouble(4, objeto.getQdteFuncionarios());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(FuncionariosSumarizados objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("UPDATE funcionarios_sumarizados "
										+ "SET Desc_Centro_Custos =?, Qtde_Funcionarios = ?" 
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ?");
			st.setString(1, objeto.getDescCentroCustos());
			st.setDouble(2, objeto.getQdteFuncionarios());
			
			st.setString(3, objeto.getAnoMes());
			st.setDouble(4, objeto.getCodCentroCustos());
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
			st = conexao.prepareStatement("DELETE FROM funcionarios_sumarizados "
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
	public FuncionariosSumarizados pesquisarPorChave(String anoMes, Double codCentroCustos)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM funcionarios_sumarizados "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ?");
			st.setString(1, anoMes);
			st.setDouble(2, codCentroCustos);
			rs = st.executeQuery();
			if (rs.next()) {
				FuncionariosSumarizados sumarioFuncionarios = instanciaSumarioFuncionarios(rs);
				return sumarioFuncionarios;
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
	public List<FuncionariosSumarizados> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM funcionarios_sumarizados ORDER BY Ano_Mes, Cod_Centro_Custos");
			rs = st.executeQuery();
			List<FuncionariosSumarizados> lista = new ArrayList<FuncionariosSumarizados>();
			while (rs.next()) {
				FuncionariosSumarizados sumarioFuncionarios = instanciaSumarioFuncionarios(rs);
				lista.add(sumarioFuncionarios)
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
			st = conexao.prepareStatement("DELETE FROM funcionarios_sumarizados ");
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Sumario de Funcionarios \n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}
	
	private FuncionariosSumarizados instanciaSumarioFuncionarios(ResultSet rs) throws SQLException {
		FuncionariosSumarizados funcionariosSumarizados = new FuncionariosSumarizados();
		funcionariosSumarizados.setAnoMes(rs.getString("Ano_Mes"));
		funcionariosSumarizados.setCodCentroCustos(rs.getDouble("Cod_Centro_Custos"));
		funcionariosSumarizados.setDescCentroCustos(rs.getString("Desc_Centro_Custos"));
		funcionariosSumarizados.setQdteFuncionarios(rs.getInt("Qtde_Funcionarios"));
		return funcionariosSumarizados;
	}
}

