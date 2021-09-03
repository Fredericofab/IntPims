package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.VerbaFolhaDao;
import model.entities.VerbaFolha;

public class VerbaFolhaDaoJDBC implements VerbaFolhaDao {
	private Connection conexao;

	public VerbaFolhaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(VerbaFolha objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("INSERT INTO verba_folha "
										+ "(Cod_Verba, Desc_Verba, Importar) "
										+ " VALUES (?,?,?)" );
			st.setString(1, objeto.getCodVerba());
			st.setString(2, objeto.getDescVerba());
			st.setString(3, objeto.getImportar());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(VerbaFolha objeto) {
		PreparedStatement st = null;
		try {
//			System.out.println(objeto.toString());
			st = conexao.prepareStatement("UPDATE verba_folha "
										+ "SET Importar = ?, Desc_Verba = ?" 
										+ "WHERE Cod_Verba = ? ");
			st.setString(1, objeto.getImportar());
			st.setString(2, objeto.getDescVerba());

			st.setString(3, objeto.getCodVerba());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(String codVerba) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM verba_folha "
										+ "WHERE Cod_Verba = ? ");
			st.setString(1, codVerba);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public VerbaFolha pesquisarPorChave(String codVerba)  {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM verba_folha "
										+ "WHERE Cod_Verba = ? ");
			st.setString(1, codVerba);
			rs = st.executeQuery();
			if (rs.next()) {
				VerbaFolha verbaFolha = instanciaVerbaFolha(rs);
				return verbaFolha;
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
	public List<VerbaFolha> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM verba_folha ");
			rs = st.executeQuery();
			List<VerbaFolha> lista = new ArrayList<VerbaFolha>();
			while (rs.next()) {
				VerbaFolha verbaFolha = instanciaVerbaFolha(rs);
				lista.add(verbaFolha)
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

	private VerbaFolha instanciaVerbaFolha(ResultSet rs) throws SQLException {
		VerbaFolha verbaFolha = new VerbaFolha();
		verbaFolha.setCodVerba(rs.getString("Cod_Verba"));		
		verbaFolha.setDescVerba(rs.getString("Desc_Verba"));		
		verbaFolha.setImportar(rs.getString("Importar"));		
		return verbaFolha;
	}
}

