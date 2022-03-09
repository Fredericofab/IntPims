package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import db.DB;
import db.DbException;
import model.dao.Cstg_IntDGDao;
import model.entities.Cstg_IntDG;

public class Cstg_IntDGDaoJDBC implements Cstg_IntDGDao {

	private Connection conexao;

	public Cstg_IntDGDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Cstg_IntDG objeto, String usuarioPimsCS) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atualizar(Cstg_IntDG objeto, String usuarioPimsCS) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer deletarPeriodo(String dataInicio, String dataFim, String usuarioPimsCS) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM " +  usuarioPimsCS + ".cstg_intDG " +
										  "WHERE dt_refer BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') ");
			st.setString(1, dataInicio);
			st.setString(2, dataFim);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela CSTG_INTDG \n \n" + e.getMessage() + "\n \n" 
								+ "Movimento entre " + dataInicio + " e " + dataFim );
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Cstg_IntDG pesquisarPorChave(String cdCtaCon, Date dtRefer, Double cdCCusto, String usuarioPimsCS) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
