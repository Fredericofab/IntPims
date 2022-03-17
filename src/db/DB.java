package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	private static Connection conexao = null;
	
	public static Connection abrirConexao() {
		if (conexao == null) {
			try {
				Properties propriedades =  lerPropriedades();
				String urlIGP = propriedades.getProperty("url");
				String usuarioIGP = propriedades.getProperty("usuario");
				String senhaIGP = propriedades.getProperty("senha"); 
				conexao = DriverManager.getConnection(urlIGP, usuarioIGP, senhaIGP);
			} 
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conexao;
	}
	
	public static void fecharConexao() {
		if (conexao != null) {
			try {
				conexao.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	
	private static Properties lerPropriedades() {
		
		try (FileInputStream fs = new FileInputStream("db.propriedades")) {
			Properties propriedades = new Properties();
			propriedades.load(fs);
			return propriedades;
		}
		catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void fecharStatement(Statement st){
		if (st != null) {
			try {
				st.close();
			} 
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void fecharResultSet(ResultSet rs){
		if (rs != null) {
			try {
				rs.close();
			} 
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

}
