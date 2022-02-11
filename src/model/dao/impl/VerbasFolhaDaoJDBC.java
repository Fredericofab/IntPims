package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.VerbasFolhaDao;
import model.entities.VerbasFolha;

public class VerbasFolhaDaoJDBC implements VerbasFolhaDao {
	private Connection conexao;

	public VerbasFolhaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(VerbasFolha objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO verbas_folha "
										+ "(Cod_Verba, Desc_Verba, Tipo_Verba, Considerar_Referencia,  Importar) "
										+ " VALUES (?,?,?,?,?)" );
			st.setDouble(1, objeto.getCodVerba());
			st.setString(2, objeto.getDescVerba());
			st.setString(3, objeto.getTipoVerba());
			st.setString(4, objeto.getConsiderarReferencia());
			st.setString(5, objeto.getImportar());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Verbas_Folha  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(VerbasFolha objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE verbas_folha "
										+ "SET Importar = ?, Desc_Verba = ?, Tipo_Verba = ?, Considerar_Referencia = ?" 
										+ "WHERE Cod_Verba = ? ");
			st.setString(1, objeto.getImportar().toUpperCase());
			st.setString(2, objeto.getDescVerba());
			st.setString(3, objeto.getTipoVerba());
			st.setString(4, objeto.getConsiderarReferencia());

			st.setDouble(5, objeto.getCodVerba());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Verbas_Folha  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(Double codVerba) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM verbas_folha "
										+ "WHERE Cod_Verba = ? ");
			st.setDouble(1, codVerba);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Verbas_Folha \n \n" + e.getMessage() + "\n \n" 
					  + codVerba);
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public VerbasFolha pesquisarPorChave(Double codVerba)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM verbas_folha "
										+ "WHERE Cod_Verba = ? ");
			st.setDouble(1, codVerba);
			rs = st.executeQuery();
			if (rs.next()) {
				VerbasFolha verbaFolha = instanciaVerbaFolha(rs);
				return verbaFolha;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Verbas_Folha \n \n" + e.getMessage() + "\n \n" + codVerba);
		}
		finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}

	}

	@Override
	public List<VerbasFolha> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM verbas_folha ORDER BY Cod_Verba");
			rs = st.executeQuery();
			List<VerbasFolha> lista = new ArrayList<VerbasFolha>();
			while (rs.next()) {
				VerbasFolha verbaFolha = instanciaVerbaFolha(rs);
				lista.add(verbaFolha)
				;
			}
			return lista;
		} 
		catch (SQLException e) {
			throw new DbException("Tabela Verbas_Folha \n \n" + e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer contarVerbasSemDefinicao() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT count(*) contagem FROM verbas_folha WHERE Importar IS NULL");
			rs = st.executeQuery();
			rs.next();
			Integer contagem = rs.getInt("contagem");
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela Verbas_Folha \n \n" + e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}
	
	@Override
	public void atualizarNovos(String defaultImportar, String defaultConsiderarReferencia) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE verbas_folha "
										+ "SET Importar = ?, Considerar_Referencia = ?" 
										+ "WHERE Importar is null and  Considerar_Referencia is null ");
			st.setString(1, defaultImportar.toUpperCase());
			st.setString(2, defaultConsiderarReferencia.toUpperCase());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Verbas_Folha \n \n" + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	
	private VerbasFolha instanciaVerbaFolha(ResultSet rs) throws SQLException {
		VerbasFolha verbaFolha = new VerbasFolha();
		verbaFolha.setCodVerba(rs.getDouble("Cod_Verba"));		
		verbaFolha.setDescVerba(rs.getString("Desc_Verba"));		
		verbaFolha.setTipoVerba(rs.getString("Tipo_Verba"));		
		verbaFolha.setConsiderarReferencia(rs.getString("Considerar_Referencia"));		
		verbaFolha.setImportar(rs.getString("Importar"));		
		return verbaFolha;
	}

}

