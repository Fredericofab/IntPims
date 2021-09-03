package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;

public class TestesDeDesenvolvimento {
	
	public static void testesDeDesenvolvimento() {
		
		Connection conexao = DB.abrirConexao();
		Statement st = null;
		ResultSet rs =null;
		
		try {
			st = conexao.createStatement();
			rs = st.executeQuery("select * from dados_folha");
			while (rs.next()) {
				System.out.println(rs.getString("ano_mes") + " - " + rs.getString("cod_centro_custos" ) + " - " + rs.getString("cod_verba") );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
	//		DB.fecharConexao();
	//		System.out.println("fechou tudo");
		}
	}

}
