package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.ParametrosDao;
import model.entities.Parametros;

public class ParametrosDaoJDBC implements ParametrosDao {
	private Connection conexao;

	public ParametrosDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Parametros objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO parametros "
										+ "(Secao, Entrada, Valor, Descricao) "
										+ " VALUES (?,?,?,?)" );
			st.setString(1, objeto.getSecao());
			st.setString(2, objeto.getEntrada());
			st.setString(3, objeto.getValor());
			st.setString(4, objeto.getDescricao());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(Parametros objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("UPDATE parametros "
										+ "SET Valor = ?, Descricao = ?" 
										+ "WHERE Secao = ? and Entrada = ? ");
			st.setString(1, objeto.getValor());
			st.setString(2, objeto.getDescricao());

			st.setString(3, objeto.getSecao());
			st.setString(4, objeto.getEntrada());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String secao, String entrada) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM parametros "
										+ "WHERE Secao = ? and Entrada = ? ");
			st.setString(1, secao);
			st.setString(2, entrada);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Parametros pesquisarPorChave(String secao, String entrada)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM parametros "
										+ "WHERE Secao = ? and Entrada = ? ");
			st.setString(1, secao);
			st.setString(2, entrada);
			rs = st.executeQuery();
			if (rs.next()) {
				Parametros parametros = instanciaParametros(rs);
				return parametros;
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
	public List<Parametros> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM parametros ORDER BY Secao, Entrada");
			rs = st.executeQuery();
			List<Parametros> lista = new ArrayList<Parametros>();
			while (rs.next()) {
				Parametros parametros = instanciaParametros(rs);
				lista.add(parametros)
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

	private Parametros instanciaParametros(ResultSet rs) throws SQLException {
		Parametros parametros = new Parametros();
		parametros.setSecao(rs.getString("Secao"));		
		parametros.setEntrada(rs.getString("Entrada"));		
		parametros.setValor(rs.getString("Valor"));	
		parametros.setDescricao(rs.getString("Descricao"));	
		return parametros;
	}
}

