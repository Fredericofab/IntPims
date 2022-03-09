package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.ProcessoAtualDao;
import model.entities.ProcessoAtual;

public class ProcessoAtualDaoJDBC implements ProcessoAtualDao {
	private Connection conexao;

	public ProcessoAtualDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(ProcessoAtual objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO processo_Atual ("
										+ "Ano_Mes, "
										+ "Importar_Folha,Sumarizar_Folha, Exportar_Folha, "
										+ "Importar_Funcionario,Sumarizar_Funcionario,"
										+ "Importar_ErpMT, Importar_ErpCD, Importar_ErpDD, "
										+ "Validar_Erp, aplicar_Politica_Erp, " 
										+ "Exportar_ErpVM, Exportar_ErpCM, Exportar_ErpDG, Exportar_ErpOS,"
										+ "Filtro_Erp) "
										+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );
			st.setString(1, objeto.getAnoMes());
			st.setString(2, objeto.getImportarFolha());
			st.setString(3, objeto.getSumarizarFolha());
			st.setString(4, objeto.getExportarFolha());
			st.setString(5, objeto.getImportarFuncionario());
			st.setString(6, objeto.getSumarizarFuncionario());
			st.setString(7, objeto.getImportarErpRM());
			st.setString(8, objeto.getImportarErpED());
			st.setString(9, objeto.getImportarErpDF());
			st.setString(10, objeto.getValidarErp());
			st.setString(11, objeto.getAplicarPoliticaErp());
			st.setString(12, objeto.getExportarErpVM());
			st.setString(13, objeto.getExportarErpCM());
			st.setString(14, objeto.getExportarErpDG());
			st.setString(15, objeto.getExportarErpOS());
			st.setString(16, objeto.getFiltroErp());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Processo_Atual  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(ProcessoAtual objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE processo_Atual SET  "
										+ "Importar_Folha = ? ,Sumarizar_Folha = ?, Exportar_Folha = ?, "
										+ "Importar_Funcionario = ?,Sumarizar_Funcionario = ?,"
										+ "Importar_ErpMT = ? , Importar_ErpCD = ?, Importar_ErpDD = ?, "
										+ "Validar_Erp = ? , aplicar_Politica_Erp = ? , "
										+ "Exportar_ErpVM = ? , Exportar_ErpCM = ? , Exportar_ErpDG = ? , Exportar_ErpOS = ? , "
										+ "Filtro_Erp = ? "
										+ "WHERE Ano_Mes = ? ");
			st.setString(1, objeto.getImportarFolha());
			st.setString(2, objeto.getSumarizarFolha());
			st.setString(3, objeto.getExportarFolha());
			st.setString(4, objeto.getImportarFuncionario());
			st.setString(5, objeto.getSumarizarFuncionario());
			st.setString(6, objeto.getImportarErpRM());
			st.setString(7, objeto.getImportarErpED());
			st.setString(8, objeto.getImportarErpDF());
			st.setString(9, objeto.getValidarErp());
			st.setString(10, objeto.getAplicarPoliticaErp());
			st.setString(11, objeto.getExportarErpVM());
			st.setString(12, objeto.getExportarErpCM());
			st.setString(13, objeto.getExportarErpDG());
			st.setString(14, objeto.getExportarErpOS());
			st.setString(15, objeto.getFiltroErp());

			st.setString(16, objeto.getAnoMes());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Processo_Atual  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String anoMes) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM processo_Atual "
										+ "WHERE Ano_Mes = ? ");
			st.setString(1, anoMes);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Processo_Atual \n \n" + e.getMessage() + "\n \n" + anoMes );
		}
		finally {
			DB.fecharStatement(st);
		}
	}
	
	@Override
	public Integer deletarTodos() {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM processo_Atual ");
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela Processo_Atual \n \n" + e.getMessage());
					}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public ProcessoAtual pesquisarPorChave(String anoMes)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM processo_Atual "
										+ "WHERE Ano_Mes = ? ");
			st.setString(1, anoMes);
			rs = st.executeQuery();
			if (rs.next()) {
				ProcessoAtual processo_Atual = instanciaControleProcesso(rs);
				return processo_Atual;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Processo_Atual \n \n" + e.getMessage() + "\n \n" + anoMes );
		}
		finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public List<ProcessoAtual> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM processo_Atual ORDER BY Ano_Mes");
			rs = st.executeQuery();
			List<ProcessoAtual> lista = new ArrayList<ProcessoAtual>();
			while (rs.next()) {
				ProcessoAtual processo_Atual = instanciaControleProcesso(rs);
				lista.add(processo_Atual)
				;
			}
			return lista;
		} 
		catch (SQLException e) {
			throw new DbException("Tabela Processo_Atual \n \n" + e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	private ProcessoAtual instanciaControleProcesso(ResultSet rs) throws SQLException {
		ProcessoAtual processoAtual = new ProcessoAtual();
		processoAtual.setAnoMes(rs.getString("Ano_Mes"));		
		processoAtual.setImportarFolha(rs.getString("Importar_Folha"));		
		processoAtual.setSumarizarFolha(rs.getString("Sumarizar_Folha"));		
		processoAtual.setExportarFolha(rs.getString("Exportar_Folha"));		
		processoAtual.setImportarFuncionario(rs.getString("Importar_Funcionario"));		
		processoAtual.setSumarizarFuncionario(rs.getString("Sumarizar_Funcionario"));		
		processoAtual.setImportarErpRM(rs.getString("Importar_ErpMT"));		
		processoAtual.setImportarErpED(rs.getString("Importar_ErpCD"));		
		processoAtual.setImportarErpDF(rs.getString("Importar_ErpDD"));		
		processoAtual.setValidarErp(rs.getString("Validar_Erp"));		
		processoAtual.setAplicarPoliticaErp(rs.getString("aplicar_Politica_Erp"));		
		processoAtual.setExportarErpVM(rs.getString("Exportar_ErpVM"));		
		processoAtual.setExportarErpCM(rs.getString("Exportar_ErpCM"));		
		processoAtual.setExportarErpDG(rs.getString("Exportar_ErpDG"));		
		processoAtual.setExportarErpOS(rs.getString("Exportar_ErpOS"));		
		processoAtual.setFiltroErp(rs.getString("Filtro_Erp"));		
		return processoAtual;
	}
}

