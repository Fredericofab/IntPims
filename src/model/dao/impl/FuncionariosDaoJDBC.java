package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.FuncionariosDao;
import model.entities.Funcionarios;

public class FuncionariosDaoJDBC implements FuncionariosDao {
	private Connection conexao;

	public FuncionariosDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Funcionarios objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO funcionarios "
										+ "(Ano_Mes, Cod_Centro_Custos, Desc_Centro_Custos, "
										+ " Cod_Funcionario, Desc_Funcionario) "
										+ " VALUES (?,?,?,?,?)" );
			st.setString(1, objeto.getAnoMes());
			st.setDouble(2, objeto.getCodCentroCustos());
			st.setString(3, objeto.getDescCentroCustos());
			st.setDouble(4, objeto.getCodFuncionario());
			st.setString(5, objeto.getDescFuncionario());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Funcionarios  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(Funcionarios objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE funcionarios "
										+ "SET Desc_Centro_Custos =?, Desc_Funcionario = ?" 
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ? AND Cod_Funcionario = ? ");
			st.setString(1, objeto.getDescCentroCustos());
			st.setString(2, objeto.getDescFuncionario());
			
			st.setString(3, objeto.getAnoMes());
			st.setDouble(4, objeto.getCodCentroCustos());
			st.setDouble(5, objeto.getCodFuncionario());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Funcionarios  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String anoMes, Double codCentroCustos, Double codFuncionario) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM funcionarios "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ? AND Cod_Funcionario = ? ");
			st.setString(1, anoMes);
			st.setDouble(2, codCentroCustos);
			st.setDouble(3, codFuncionario);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Funcionarios \n \n" + e.getMessage() + "\n \n" 
					  + anoMes + " - " + codCentroCustos + " - " + codFuncionario);
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Funcionarios pesquisarPorChave(String anoMes, Double codCentroCustos, Double codFuncionario)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM funcionarios "
										+ "WHERE Ano_Mes = ? AND Cod_Centro_Custos = ? AND Cod_Funcionario = ? ");
			st.setString(1, anoMes);
			st.setDouble(2, codCentroCustos);
			st.setDouble(3, codFuncionario);
			rs = st.executeQuery();
			if (rs.next()) {
				Funcionarios funcionarios = instanciaFuncionarios(rs);
				return funcionarios;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Funcionarios \n \n" + e.getMessage() + "\n \n" 
		  			  + anoMes + " - " + codCentroCustos + " - " + codFuncionario);
		}
		finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}

	}

	@Override
	public List<Funcionarios> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM funcionarios ORDER BY Ano_Mes, Cod_Centro_Custos, Cod_Funcionario");
			rs = st.executeQuery();
			List<Funcionarios> lista = new ArrayList<Funcionarios>();
			while (rs.next()) {
				Funcionarios funcionarios = instanciaFuncionarios(rs);
				lista.add(funcionarios)
				;
			}
			return lista;
		} 
		catch (SQLException e) {
			throw new DbException("Tabela Funcionarios \n \n" + e.getMessage());
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
			st = conexao.prepareStatement("DELETE FROM funcionarios ");
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela Funcionarios \n \n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}
	
	private Funcionarios instanciaFuncionarios(ResultSet rs) throws SQLException {
		Funcionarios funcionarios = new Funcionarios();
		funcionarios.setAnoMes(rs.getString("Ano_Mes"));
		funcionarios.setCodCentroCustos(rs.getDouble("Cod_Centro_Custos"));
		funcionarios.setDescCentroCustos(rs.getString("Desc_Centro_Custos"));		
		funcionarios.setCodFuncionario(rs.getDouble("Cod_Funcionario"));		
		funcionarios.setDescFuncionario(rs.getString("Desc_Funcionario"));		
		return funcionarios;
	}
}

