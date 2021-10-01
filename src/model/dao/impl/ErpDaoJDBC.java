package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.ErpDao;
import model.entities.Erp;

public class ErpDaoJDBC implements ErpDao {
	private Connection conexao;

	public ErpDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Erp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO Erp "
										+ "(Sequencial,Ano_Mes, Origem, Cod_Centro_Custos, Desc_Centro_Custos, Cod_Conta_Contabil, Desc_Conta_Contabil, "
										+ " Familia_Material, Importar, Observacao) "
										+ " VALUES (?,?,?,?,?,?,?,?,?,?)" , 
										+ Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, objeto.getSequencial());
			st.setString(2, objeto.getAnoMes());
			st.setString(3, objeto.getOrigem());
			st.setDouble(4, objeto.getCodCentroCustos());
			st.setString(5, objeto.getDescCentroCustos());
			st.setDouble(6, objeto.getCodContaContabil());
			st.setString(7, objeto.getDescContaContabil());
			st.setDouble(8, objeto.getFamiliaMaterial());
			st.setString(9, objeto.getImportar().toUpperCase());
			st.setString(10, objeto.getObservacao());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(Erp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE Erp "
										+ "SET Ano_Mes = ?, Origem = ?, "
										+ "Cod_Centro_Custos = ?, Desc_Centro_Custos = ?, " 
										+ "Cod_Conta_Contabil = ?,   Desc_Conta_Contabil = ?, "
										+ "Familia_Material = ?, "
										+ "Importar = ?, Observacao = ? "
										+ "WHERE Sequencial = ? ");
			
			st.setString(1, objeto.getAnoMes());
			st.setString(2, objeto.getOrigem());
			st.setDouble(3, objeto.getCodCentroCustos());
			st.setString(4, objeto.getDescCentroCustos());
			st.setDouble(5, objeto.getCodContaContabil());
			st.setString(6, objeto.getDescContaContabil());
			st.setDouble(7, objeto.getFamiliaMaterial());
			st.setString(8, objeto.getImportar().toUpperCase());
			st.setString(9, objeto.getObservacao());
			st.setInt(10, objeto.getSequencial());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(Integer sequencial) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM Erp WHERE Sequencial = ? ");
			st.setInt(1, sequencial);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Erp pesquisarPorChave(Integer sequencial)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM Erp WHERE Sequencial = ?");
			st.setInt(1, sequencial);
			rs = st.executeQuery();
			if (rs.next()) {
				Erp dadosErp = instanciaDadosErp(rs);
				return dadosErp;
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
	public List<Erp> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM Erp ORDER BY Sequencial");
			rs = st.executeQuery();
			List<Erp> lista = new ArrayList<Erp>();
			while (rs.next()) {
				Erp dadosErp = instanciaDadosErp(rs);
				lista.add(dadosErp)
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
			st = conexao.prepareStatement("DELETE FROM Erp ");
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Movimento da Erp  \n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarPorOrigem(String origem) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM Erp WHER Origem = ? ");
			st.setString(1, origem);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Movimento da Erp por Origem \n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}
	
	@Override
	public Integer ultimoSequencial() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT MAX(sequencial) FROM Erp");
			rs = st.executeQuery();
			if (rs.next()) {
				int ultimoSequencial = rs.getInt(1);
				return ultimoSequencial;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("erro na Pesquisa do Ultimo Sequencial " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	private Erp instanciaDadosErp(ResultSet rs) throws SQLException {
		Erp dadosErp = new Erp();
		dadosErp.setSequencial(rs.getInt("Sequencial"));
		dadosErp.setAnoMes(rs.getString("Ano_Mes"));
		dadosErp.setOrigem(rs.getString("Origem"));
		dadosErp.setCodCentroCustos(rs.getDouble("Cod_Centro_Custos"));
		dadosErp.setDescCentroCustos(rs.getString("Desc_Centro_Custos"));		
		dadosErp.setCodContaContabil(rs.getDouble("Cod_Conta_Contabil"));		
		dadosErp.setDescContaContabil(rs.getString("Desc_Conta_Contabil"));		
		dadosErp.setFamiliaMaterial(rs.getDouble("Familia_Material"));		
		dadosErp.setImportar(rs.getString("Importar"));		
		dadosErp.setObservacao(rs.getString("Observacao"));		
		return dadosErp;
	}


}

