package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.FatorMedidaDao;
import model.entities.FatorMedida;

public class FatorMedidaDaoJDBC implements FatorMedidaDao {
	private Connection conexao;

	public FatorMedidaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(FatorMedida objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO fator_medida "
										+ "(Cod_Material, Desc_Material, Unidade_Medida, Fator_Divisao) "
										+ " VALUES (?,?,?,?)" );
			st.setString(1, objeto.getCodMaterial());
			st.setString(2, objeto.getDescMaterial());
			st.setString(3, objeto.getUnidadeMedida());
			st.setDouble(4, objeto.getFatorDivisao());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Fator_Medida  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(FatorMedida objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE fator_medida "
										+ "SET Desc_Material = ?, Unidade_Medida = ?, Fator_Divisao = ?" 
										+ "WHERE Cod_Material = ? ");
			st.setString(1, objeto.getDescMaterial());
			st.setString(2, objeto.getUnidadeMedida());
			st.setDouble(3, objeto.getFatorDivisao());

			st.setString(4, objeto.getCodMaterial());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Fator_Medida  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String codMaterial) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM fator_medida "
										+ "WHERE Cod_Material = ? ");
			st.setString(1, codMaterial);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Fator_Medida \n \n" + e.getMessage() + "\n \n" 
					  + codMaterial);
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public FatorMedida pesquisarPorChave(String codMaterial)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM fator_medida "
										+ "WHERE Cod_Material = ? ");
			st.setString(1, codMaterial);
			rs = st.executeQuery();
			if (rs.next()) {
				FatorMedida fatorMedida = instanciaFatorMedida(rs);
				return fatorMedida;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Fator_Medida \n \n" + e.getMessage() + "\n \n" + codMaterial);
		}
		finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}

	}

	@Override
	public List<FatorMedida> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM fator_medida ORDER BY Cod_Material");
			rs = st.executeQuery();
			List<FatorMedida> lista = new ArrayList<FatorMedida>();
			while (rs.next()) {
				FatorMedida fatorMedida = instanciaFatorMedida(rs);
				lista.add(fatorMedida)
				;
			}
			return lista;
		} 
		catch (SQLException e) {
			throw new DbException("Tabela Fator_Medida \n \n" + e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	
	@Override
	public void atualizarNovos(Double defaultFatorDivisao) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE fator_medida "
										+ "SET Fator_Divisao = ?" 
										+ "WHERE Fator_Divisao = 0.00 ");
			st.setDouble(1, defaultFatorDivisao);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Fator_Medida \n \n" + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	
	private FatorMedida instanciaFatorMedida(ResultSet rs) throws SQLException {
		FatorMedida fatorMedida = new FatorMedida();
		fatorMedida.setCodMaterial(rs.getString("Cod_Material"));		
		fatorMedida.setDescMaterial(rs.getString("Desc_Material"));		
		fatorMedida.setUnidadeMedida(rs.getString("Unidade_Medida"));		
		fatorMedida.setFatorDivisao(rs.getDouble("Fator_Divisao"));		
		return fatorMedida;
	}

}

