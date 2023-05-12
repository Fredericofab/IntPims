package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.Cstg_IntFPDao;
import model.entities.Cstg_IntFP;

public class Cstg_IntFPDaoJDBC implements Cstg_IntFPDao {

	private Connection conexao;

	public Cstg_IntFPDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}
	
	@Override
	public Integer deletarPeriodo(String dataref, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM " +  usuarioPimsCS + ".cstg_intFP " +
										  "WHERE TO_CHAR(dt_refer,'dd/mm/yyyy') = ? ");
			st.setString(1, dataref);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTFP \n \n" + e.getMessage() + "\n \n" 
								+ "Data de Referencia " + dataref);
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void inserir(Cstg_IntFP objeto, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO " +  usuarioPimsCS + ".cstg_intFP " +
										 "(Cd_empresa, dt_refer, cd_func, qt_horas, qt_valor, instancia) " +
										 "VALUES (?,TO_DATE(?,'dd/mm/yyyy'),?,?,?,?)" );
			st.setString(1, objeto.getCdEmpresa());
			st.setString(2, objeto.getDtRefer());
			st.setDouble(3, objeto.getCdFunc());
			st.setDouble(4, objeto.getQtHoras());
			st.setDouble(5, objeto.getQtValor());
			st.setString(6, objeto.getInstancia());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTFP \n \n" + e.getMessage() + "\n \n" 
								+ "Registro " + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}
}
