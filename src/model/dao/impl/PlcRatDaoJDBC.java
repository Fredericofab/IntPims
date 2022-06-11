package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.PlcRatDao;
import model.entities.PlcRat;

public class PlcRatDaoJDBC implements PlcRatDao {

	private Connection conexao;

	public PlcRatDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public List<PlcRat> listarTodos(String anoMes, String usuarioPimsCS) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".plcRat " +
										  "WHERE fg_pla_cst = 'C' AND to_Char(dt_histori,'yyyymm') = ?");
			st.setString(1, anoMes);
			rs = st.executeQuery();
			List<PlcRat> lista = new ArrayList<PlcRat>();
			while (rs.next()) {
				PlcRat plcRat = instanciaDados(rs);
				lista.add(plcRat);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException("Tabela PlcRat \n \n" + e.getMessage());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public List<PlcRat> listarCCusto(String anoMes, String usuarioPimsCS, Double ccusto) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM " +  usuarioPimsCS + ".plcRat " +
										  "WHERE fg_pla_cst = 'C' AND to_Char(dt_histori,'yyyymm') = ?" +
										  "AND cd_CCPara = ?");
			st.setString(1, anoMes);
			st.setDouble(2, ccusto);
			rs = st.executeQuery();
			List<PlcRat> lista = new ArrayList<PlcRat>();
			while (rs.next()) {
				PlcRat plcRat = instanciaDados(rs);
				lista.add(plcRat);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException("Tabela PlcRat \n \n" + e.getMessage());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	
	private PlcRat instanciaDados(ResultSet rs) throws SQLException {
		PlcRat plcRat = new PlcRat();
		plcRat.setCdCCPara(rs.getDouble("Cd_CCPara"));
		plcRat.setFgFxVr(rs.getString("Fg_Fx_Vr"));
		plcRat.setCdCCDe(rs.getDouble("Cd_CCDe"));
		plcRat.setVlTaxa(rs.getDouble("Vl_Taxa"));
		plcRat.setVlConsum(rs.getDouble("VL_Consum"));
		return plcRat;
	}


}
