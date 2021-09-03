package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.ImportarFolhaDao;

public class ImportarFolhaDaoJDBC implements ImportarFolhaDao {
	private Connection conexao;

	public ImportarFolhaDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public Integer contarVerbasSemDefinicao() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT count(*) contagem FROM verba_folha WHERE Importar IS NULL");
			rs = st.executeQuery();
			rs.next();
			Integer contagem = rs.getInt("contagem");
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Erro na Contagem das Verbas Sem Definicao " + e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	
	@Override
	public Integer deletarDadosFolhaAnoMes(String anoMes) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM dados_folha WHERE Ano_Mes = ? ");
			st.setString(1, anoMes);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Movimento " + anoMes + "\n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}
	
	
}

