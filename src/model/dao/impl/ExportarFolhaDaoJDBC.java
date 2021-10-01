package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.ExportarFolhaDao;
import model.entities.Cstg_IntPF;

public class ExportarFolhaDaoJDBC implements ExportarFolhaDao {
	private Connection conexao;

	public ExportarFolhaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}
	
	@Override
	public Integer deletarCstg_IntFP(String dataref, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM " +  usuarioPimsCS + ".cstg_intFP " +
										  "WHERE TO_CHAR(dt_refer,'dd/mm/yyyy') = ? ");
			st.setString(1, dataref);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do CSTG_INTFP " + dataref + "\n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void gravarCstg_IntFP(Cstg_IntPF cstg_IntFP, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO " +  usuarioPimsCS + ".cstg_intFP " +
										 "(Cd_empresa, dt_refer, cd_func, qt_valor, instancia) " +
										 "VALUES (?,?,?,?,?)" );
			st.setString(1, cstg_IntFP.getCdEmpresa());
			st.setString(2, cstg_IntFP.getDtRefer());
			st.setDouble(3, cstg_IntFP.getCdFunc());
			st.setDouble(4, cstg_IntFP.getQtValor());
			st.setString(5, cstg_IntFP.getInstancia());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Insercao do CSTG_INTFP " + "\n" + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}
}


