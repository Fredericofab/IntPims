package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.PlcPrimDao;
import model.entities.PlcPrim;

public class PlcPrimDaoJDBC implements PlcPrimDao {

	private Connection conexao;

	public PlcPrimDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public List<PlcPrim> listarTodos(String anoMes, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".plcPrim " +
										  "WHERE fg_pla_cst = 'C' AND to_Char(dt_histori,'yyyymm') = ?");
			st.setString(1, anoMes);
			rs = st.executeQuery();
			List<PlcPrim> lista = new ArrayList<PlcPrim>();
			while (rs.next()) {
				PlcPrim plcPrim = instanciaDados(rs);
				lista.add(plcPrim);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException("Tabela PlcPrim \n \n" + e.getMessage());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	private PlcPrim instanciaDados(ResultSet rs) throws SQLException {
		PlcPrim plcPrim = new PlcPrim();
		plcPrim.setCdCCusto(rs.getDouble("Cd_CCusto"));
		plcPrim.setCdConta(rs.getString("Cd_Conta"));
		plcPrim.setFgTpCta(rs.getString("Fg_Tp_Cta"));
		plcPrim.setVlDire(rs.getDouble("VL_Dire"));
		return plcPrim;
	}

}
