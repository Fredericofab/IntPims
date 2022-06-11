package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DadosContabilDao;
import model.entities.DadosContabil;

public class DadosContabilDaoJDBC implements DadosContabilDao {
	private Connection conexao;

	public DadosContabilDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(DadosContabil objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO DadosContabil "
										+ "(cc_Comprador, Cod_Conta, fg_tp_cta, Nivel, cc_Vendedor_Primeiro, cc_vendedor_ultimo, Taxa, Valor) VALUES (?,?,?,?,?,?,?,?)" );
			st.setDouble(1, objeto.getCcComprador());
			st.setString(2, objeto.getCdConta());
			st.setString(3, objeto.getFgTpCta());
			st.setString(4, objeto.getNivel());
			st.setDouble(5, objeto.getCcVendedorPrimeiro());
			st.setDouble(6, objeto.getCcVendedorUltimo());
			st.setDouble(7, objeto.getTaxa());
			st.setDouble(8, objeto.getValor());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela DadosContabil  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}


	@Override
	public List<DadosContabil> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM DadosContabil ORDER BY Cod_Centro_Custos, Cod_Conta");
			rs = st.executeQuery();
			List<DadosContabil> lista = new ArrayList<DadosContabil>();
			while (rs.next()) {
				DadosContabil dadosContabil = instanciaDadosContabil(rs);
				lista.add(dadosContabil)
				;
			}
			return lista;
		} 
		catch (SQLException e) {
			throw new DbException("Tabela DadosContabil \n \n" + e.getMessage());
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
			st = conexao.prepareStatement("DELETE FROM DadosContabil ");
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela DadosContabil \n \n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	private DadosContabil instanciaDadosContabil(ResultSet rs) throws SQLException {
		DadosContabil dadosContabil = new DadosContabil();
		dadosContabil.setCcComprador(rs.getDouble("cc_Comprador"));
		dadosContabil.setCdConta(rs.getString("Cod_Verba"));		
		dadosContabil.setFgTpCta(rs.getString("Fg_tp_cta"));		
		dadosContabil.setNivel(rs.getString("Nivel"));		
		dadosContabil.setCcVendedorPrimeiro(rs.getDouble("cc_vendedor_primeiro"));		
		dadosContabil.setCcVendedorUltimo(rs.getDouble("cc_vendedor_ultimo"));		
		dadosContabil.setTaxa(rs.getDouble("Taxa"));		
		dadosContabil.setValor(rs.getDouble("Valor"));		
		return dadosContabil;
	}

}

