package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DadosContabilDao;
import model.entities.DadosContabil;

public class DadosContabilDaoJDBC implements DadosContabilDao {
	private Connection conexao;

	public DadosContabilDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(DadosContabil objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO DadosContabil "
										+ "(cc_Comprador, Cod_Conta, fg_tp_cta, "
										+ "cc_De01, cc_De02, cc_De03, cc_De04, cc_De05, cc_De06, cc_De07, cc_De08, cc_De09,cc_De10, "
										+ "taxa01, taxa02, taxa03, taxa04, taxa05, taxa06, taxa07, taxa08, taxa09, taxa10, "
										+ "taxa_Acum, valor_base, Valor_Adquirido) "
										+ "VALUES (?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?)");
			st.setDouble(1, objeto.getCcComprador());
			st.setString(2, objeto.getCdConta());
			st.setString(3, objeto.getFgTpCta());
			Double[] ccDe = objeto.getCcDe();
			Double[] taxa = objeto.getTaxa();
			for (int i = 1 ; i <= 10 ; i++) {
				st.setDouble(i+03, ccDe[i]);
				st.setDouble(i+13, taxa[i]);
			}
			st.setDouble(24, objeto.getTaxaAcum());
			st.setDouble(25, objeto.getValorBase());
			st.setDouble(26, objeto.getValorAdquirido());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela DadosContabil  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}


	@Override
	public List<DadosContabil> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM DadosContabil ORDER BY Cod_Centro_Custos, Cod_Conta");
			rs = st.executeQuery();
			List<DadosContabil> lista = new ArrayList<DadosContabil>();
			while (rs.next()) {
				DadosContabil dadosContabil = instanciaDadosContabil(rs);
				lista.add(dadosContabil)
				;
			}
			return lista;
		} 
		catch (SQLException e) {
			throw new DbException("Tabela DadosContabil \n \n" + e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarTodos() {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM DadosContabil ");
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("Tabela DadosContabil \n \n" + e.getMessage());
		}
		finally {
			DB.fecharStatement(st);
		}
	}

	private DadosContabil instanciaDadosContabil(ResultSet rs) throws SQLException {
		DadosContabil dadosContabil = new DadosContabil();
		dadosContabil.setCcComprador(rs.getDouble("cc_Comprador"));
		dadosContabil.setCdConta(rs.getString("Cod_Verba"));		
		dadosContabil.setFgTpCta(rs.getString("Fg_tp_cta"));		
		return dadosContabil;
	}

}

