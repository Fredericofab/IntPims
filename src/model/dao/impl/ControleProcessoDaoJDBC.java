package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.ControleProcessoDao;
import model.entities.ControleProcesso;

public class ControleProcessoDaoJDBC implements ControleProcessoDao {
	private Connection conexao;

	public ControleProcessoDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(ControleProcesso objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO controle_processo ("
										+ "Ano_Mes, "
										+ "Importar_Folha,Sumarizar_Folha, Exportar_Folha, "
										+ "Importar_Funcionario,Sumarizar_Funcionario,"
										+ "Importar_ErpMT, Importar_ErpCD, Importar_ErpDD, "
										+ "Criticar_Erp, Exportar_Erp) "
										+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getAnoMes());
			st.setString(2, objeto.getImportarFolha());
			st.setString(3, objeto.getSumarizarFolha());
			st.setString(4, objeto.getExportarFolha());
			st.setString(5, objeto.getImportarFuncionario());
			st.setString(6, objeto.getSumarizarFuncionario());
			st.setString(7, objeto.getImportarErpMT());
			st.setString(8, objeto.getImportarErpCD());
			st.setString(9, objeto.getImportarErpDD());
			st.setString(10, objeto.getCriticarErp());
			st.setString(11, objeto.getExportarErp());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(ControleProcesso objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE controle_processo SET  "
										+ "Importar_Folha = ? ,Sumarizar_Folha = ?, Exportar_Folha = ?, "
										+ "Importar_Funcionario = ?,Sumarizar_Funcionario = ?,"
										+ "Importar_ErpMT = ? , Importar_ErpCD = ?, Importar_ErpDD = ?, "
										+ "Criticar_Erp = ? , Exportar_Erp = ? "
										+ "WHERE Ano_Mes = ? ");
			st.setString(1, objeto.getImportarFolha());
			st.setString(2, objeto.getSumarizarFolha());
			st.setString(3, objeto.getExportarFolha());
			st.setString(4, objeto.getImportarFuncionario());
			st.setString(5, objeto.getSumarizarFuncionario());
			st.setString(6, objeto.getImportarErpMT());
			st.setString(7, objeto.getImportarErpCD());
			st.setString(8, objeto.getImportarErpDD());
			st.setString(9, objeto.getCriticarErp());
			st.setString(10, objeto.getExportarErp());

			st.setString(11, objeto.getAnoMes());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String anoMes) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM controle_processo "
										+ "WHERE Ano_Mes = ? ");
			st.setString(1, anoMes);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}
	
	@Override
	public void deletarTodos() {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM controle_processo ");
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public ControleProcesso pesquisarPorChave(String anoMes)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM controle_processo "
										+ "WHERE Ano_Mes = ? ");
			st.setString(1, anoMes);
			rs = st.executeQuery();
			if (rs.next()) {
				ControleProcesso controle_processo = instanciaControleProcesso(rs);
				return controle_processo;
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
	public List<ControleProcesso> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM controle_processo ORDER BY Ano_Mes");
			rs = st.executeQuery();
			List<ControleProcesso> lista = new ArrayList<ControleProcesso>();
			while (rs.next()) {
				ControleProcesso controle_processo = instanciaControleProcesso(rs);
				lista.add(controle_processo)
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

	private ControleProcesso instanciaControleProcesso(ResultSet rs) throws SQLException {
		ControleProcesso controle_processo = new ControleProcesso();
		controle_processo.setAnoMes(rs.getString("Ano_Mes"));		
		controle_processo.setImportarFolha(rs.getString("Importar_Folha"));		
		controle_processo.setSumarizarFolha(rs.getString("Sumarizar_Folha"));		
		controle_processo.setExportarFolha(rs.getString("Exportar_Folha"));		
		controle_processo.setImportarFuncionario(rs.getString("Importar_Funcionario"));		
		controle_processo.setSumarizarFuncionario(rs.getString("Sumarizar_Funcionario"));		
		controle_processo.setImportarErpMT(rs.getString("Importar_ErpMT"));		
		controle_processo.setImportarErpCD(rs.getString("Importar_ErpCD"));		
		controle_processo.setImportarErpDD(rs.getString("Importar_ErpDD"));		
		controle_processo.setCriticarErp(rs.getString("Criticar_Erp"));		
		controle_processo.setExportarErp(rs.getString("Exportar_Erp"));		
		return controle_processo;
	}
}

