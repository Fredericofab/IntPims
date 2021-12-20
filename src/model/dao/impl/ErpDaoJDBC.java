package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.ErpDao;
import model.entities.Erp;

public class ErpDaoJDBC implements ErpDao {
	private Connection conexao;

	public ErpDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Erp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO Erp "
					+ "(origem,ano_Mes,"
							+ "cod_Centro_Custos,desc_Centro_Custos,"
							+ "cod_Conta_Contabil,desc_Conta_Contabil,"
							+ "cod_Material,desc_Movimento,unidade_Medida,"
							+ "quantidade,preco_Unitario,valor_Movimento,"
							+ "referencia_OS,numero_OS,documento_Erp,data_Movimento," + "importar,observacao,criticas,"
							+ "salvar_OS_Material,salvar_Cstg_IntVM,salvar_Cstg_intCM,salvar_Cstg_intDG," + "sequencial)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			st.setString(1, objeto.getOrigem().toUpperCase());
			st.setString(2, objeto.getAnoMes());
			st.setDouble(3, objeto.getCodCentroCustos());
			st.setString(4, objeto.getDescCentroCustos());
			st.setDouble(5, objeto.getCodContaContabil());
			st.setString(6, objeto.getDescContaContabil());
			st.setDouble(7, objeto.getCodMaterial());
			st.setString(8, objeto.getDescMovimento());
			st.setString(9, objeto.getUnidadeMedida());
			st.setDouble(10, objeto.getQuantidade());
			st.setDouble(11, objeto.getPrecoUnitario());
			st.setDouble(12, objeto.getValorMovimento());
			st.setString(13, objeto.getReferenciaOS());
			st.setDouble(14, objeto.getNumeroOS());
			st.setString(15, objeto.getDocumentoErp());
			st.setDate(16, new java.sql.Date(objeto.getDataMovimento().getTime()));
			st.setString(17, objeto.getImportar().toUpperCase());
			st.setString(18, objeto.getObservacao());
			st.setString(19, objeto.getCriticas());
			st.setString(20, objeto.getSalvarOS_Material().toUpperCase());
			st.setString(21, objeto.getSalvarCstg_IntVM().toUpperCase());
			st.setString(22, objeto.getSalvarCstg_IntCM().toUpperCase());
			st.setString(23, objeto.getSalvarCstg_IntDG().toUpperCase());
			st.setInt(24, objeto.getSequencial());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException("Erro na Insercao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(Erp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE Erp " + "SET origem = ?,	ano_Mes = ?, "
					+ "cod_Centro_Custos = ?,	desc_Centro_Custos = ?, " + "cod_Conta_Contabil = ?,	desc_Conta_Contabil = ?, "
					+ "cod_Material = ?,	desc_Movimento = ?,	unidade_Medida = ?, "
					+ "quantidade = ?,	preco_Unitario = ?,	valor_Movimento = ?, "
					+ "referencia_OS = ?,	numero_OS = ?,	documento_Erp = ?,	data_Movimento = ?,	"
					+ "importar = ?,	observacao = ?,	criticas = ?,	"
					+ "salvar_OS_Material = ?, salvar_Cstg_IntVM = ?, salvar_Cstg_intCM = ?, salvar_Cstg_intDG = ?"
					+ "WHERE sequencial = ?");

			st.setString(1, objeto.getOrigem().toUpperCase());
			st.setString(2, objeto.getAnoMes());
			st.setDouble(3, objeto.getCodCentroCustos());
			st.setString(4, objeto.getDescCentroCustos());
			st.setDouble(5, objeto.getCodContaContabil());
			st.setString(6, objeto.getDescContaContabil());
			st.setDouble(7, objeto.getCodMaterial());
			st.setString(8, objeto.getDescMovimento());
			st.setString(9, objeto.getUnidadeMedida());
			st.setDouble(10, objeto.getQuantidade());
			st.setDouble(11, objeto.getPrecoUnitario());
			st.setDouble(12, objeto.getValorMovimento());
			st.setString(13, objeto.getReferenciaOS());
			st.setDouble(14, objeto.getNumeroOS());
			st.setString(15, objeto.getDocumentoErp());
			st.setDate(16, new java.sql.Date(objeto.getDataMovimento().getTime()));
			st.setString(17, objeto.getImportar());
			st.setString(18, objeto.getObservacao());
			st.setString(19, objeto.getCriticas());
			st.setString(20, objeto.getSalvarOS_Material());
			st.setString(21, objeto.getSalvarCstg_IntVM());
			st.setString(22, objeto.getSalvarCstg_IntCM());
			st.setString(23, objeto.getSalvarCstg_IntDG());
			st.setInt(24, objeto.getSequencial());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro na Atualizacao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorChave(Integer sequencial) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM Erp WHERE Sequencial = ? ");
			st.setInt(1, sequencial);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("erro na delecao " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Erp pesquisarPorChave(Integer sequencial) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM Erp WHERE Sequencial = ?");
			st.setInt(1, sequencial);
			rs = st.executeQuery();
			if (rs.next()) {
				Erp dadosErp = instanciaDadosErp(rs);
				return dadosErp;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("erro na Pesquisa " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}

	}

	@Override
	public List<Erp> listarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM Erp ORDER BY Sequencial");
			rs = st.executeQuery();
			List<Erp> lista = new ArrayList<Erp>();
			while (rs.next()) {
				Erp dadosErp = instanciaDadosErp(rs);
				lista.add(dadosErp);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException("erro na consulta todos - " + e.getMessage());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarTodos() {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM Erp ");
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Movimento da Erp  \n" + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer deletarPorOrigem(String origem) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM Erp WHERE Origem = ? ");
			st.setString(1, origem);
			st.executeUpdate();
			Integer contagem = st.getUpdateCount();
			return contagem;
		} catch (SQLException e) {
			throw new DbException("erro na delecao do Movimento da Erp por Origem \n" + e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Integer ultimoSequencial() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT MAX(sequencial) FROM Erp");
			rs = st.executeQuery();
			if (rs.next()) {
				int ultimoSequencial = rs.getInt(1);
				return ultimoSequencial;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("erro na Pesquisa do Ultimo Sequencial " + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public Integer qtdeTotal(String importar) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			if (importar == null ) {
				st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE importar is null");
			}
			else {
				st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE importar = ?");
				st.setString(1, importar);
			}
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("erro na Contagem do Erp para Importar = " + importar + " \n" 
		+ e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
	
	@Override
	public List<Erp> listarCriticaFiltrada(String tipoCritica, Integer codigoCritica, String filtro) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM Erp WHERE " + filtro);
			rs = st.executeQuery();
			List<Erp> lista = new ArrayList<Erp>();
			while (rs.next()) {
				Erp dadosErp = instanciaDadosErp(rs);
				lista.add(dadosErp);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException("erro na consulta da critica " 
								+ tipoCritica + String.format("%03d", codigoCritica)
								+ "\n" + e.toString());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public List<Erp> listarTodosFiltrado(String filtro) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM Erp WHERE " + filtro);
			rs = st.executeQuery();
			List<Erp> lista = new ArrayList<Erp>();
			while (rs.next()) {
				Erp dadosErp = instanciaDadosErp(rs);
				lista.add(dadosErp);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException("erro na consulta filtrada \n"  +  e.toString());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}


	private Erp instanciaDadosErp(ResultSet rs) throws SQLException {
		Erp dadosErp = new Erp();
		dadosErp.setAnoMes(rs.getString("Ano_Mes"));
		dadosErp.setOrigem(rs.getString("Origem"));
		dadosErp.setCodCentroCustos(rs.getDouble("Cod_Centro_Custos"));
		dadosErp.setDescCentroCustos(rs.getString("Desc_Centro_Custos"));
		dadosErp.setCodContaContabil(rs.getDouble("Cod_Conta_Contabil"));
		dadosErp.setDescContaContabil(rs.getString("Desc_Conta_Contabil"));
		dadosErp.setCodMaterial(rs.getDouble("Cod_Material"));
		dadosErp.setDescMovimento(rs.getString("Desc_Movimento"));
		dadosErp.setUnidadeMedida(rs.getString("Unidade_Medida"));
		dadosErp.setQuantidade(rs.getDouble("Quantidade"));
		dadosErp.setPrecoUnitario(rs.getDouble("Preco_Unitario"));
		dadosErp.setValorMovimento(rs.getDouble("Valor_Movimento"));
		dadosErp.setReferenciaOS(rs.getString("Referencia_OS"));
		dadosErp.setNumeroOS(rs.getDouble("Numero_OS"));
		dadosErp.setDocumentoErp(rs.getString("Documento_Erp"));
		dadosErp.setDataMovimento(new java.util.Date(rs.getTimestamp("Data_Movimento").getTime()));

		dadosErp.setImportar(rs.getString("Importar"));
		dadosErp.setObservacao(rs.getString("Observacao"));
		dadosErp.setCriticas(rs.getString("Criticas"));
		dadosErp.setSalvarOS_Material(rs.getString("Salvar_OS_Material"));
		dadosErp.setSalvarCstg_IntVM(rs.getString("Salvar_Cstg_IntVM"));
		dadosErp.setSalvarCstg_IntCM(rs.getString("Salvar_Cstg_intCM"));
		dadosErp.setSalvarCstg_IntDG(rs.getString("Salvar_Cstg_intDG"));
		dadosErp.setSequencial(rs.getInt("Sequencial"));
		return dadosErp;
	}


}
