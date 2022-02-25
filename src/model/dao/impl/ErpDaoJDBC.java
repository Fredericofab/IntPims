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
					+ "(origem,tipo_movimento,ano_Mes,"
							+ "cod_Centro_Custos,desc_Centro_Custos,"
							+ "cod_Conta_Contabil,desc_Conta_Contabil,"
							+ "cod_Material,desc_Movimento,unidade_Medida,"
							+ "quantidade,preco_Unitario,valor_Movimento,"
							+ "numero_OS,frota_ou_cc,documento_Erp,data_Movimento," + "importar,observacao,validacoes_OS,politicas,"
							+ "salvar_OS_Material,salvar_Cstg_IntVM,salvar_Cstg_intCM,salvar_Cstg_intDG," + "sequencial)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			st.setString(1, objeto.getOrigem().toUpperCase());
			st.setString(2, objeto.getTipoMovimento());
			st.setString(3, objeto.getAnoMes());
			st.setDouble(4, objeto.getCodCentroCustos());
			st.setString(5, objeto.getDescCentroCustos());
			st.setString(6, objeto.getCodContaContabil());
			st.setString(7, objeto.getDescContaContabil());
			st.setString(8, objeto.getCodMaterial());
			st.setString(9, objeto.getDescMovimento());
			st.setString(10, objeto.getUnidadeMedida());
			st.setDouble(11, objeto.getQuantidade());
			st.setDouble(12, objeto.getPrecoUnitario());
			st.setDouble(13, objeto.getValorMovimento());
			st.setString(14, objeto.getNumeroOS());
			st.setString(15, objeto.getFrotaOuCC());
			st.setString(16, objeto.getDocumentoErp());
			st.setDate(17, new java.sql.Date(objeto.getDataMovimento().getTime()));
			st.setString(18, objeto.getImportar());
			st.setString(19, objeto.getObservacao());
			st.setString(20, objeto.getValidacoesOS());
			st.setString(21, objeto.getPoliticas());
			st.setString(22, objeto.getSalvarOS_Material());
			st.setString(23, objeto.getSalvarCstg_IntVM());
			st.setString(24, objeto.getSalvarCstg_IntCM());
			st.setString(25, objeto.getSalvarCstg_IntDG());
			st.setInt(26, objeto.getSequencial());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Erp  \n \n" + e.getMessage() + "\n \n" + objeto);
		} finally {
			DB.fecharStatement(st);
		}
	}
	@Override
	public void atualizar(Erp objeto) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE Erp " + "SET origem = ?,	tipo_movimento = ?, ano_Mes = ?, "
					+ "cod_Centro_Custos = ?,	desc_Centro_Custos = ?, " + "cod_Conta_Contabil = ?,	desc_Conta_Contabil = ?, "
					+ "cod_Material = ?,	desc_Movimento = ?,	unidade_Medida = ?, "
					+ "quantidade = ?,	preco_Unitario = ?,	valor_Movimento = ?, "
					+ "numero_OS = ?,	frota_ou_cc = ?, documento_Erp = ?,	data_Movimento = ?,	"
					+ "importar = ?,	observacao = ?, validacoes_OS = ?,	politicas = ?,	"
					+ "salvar_OS_Material = ?, salvar_Cstg_IntVM = ?, salvar_Cstg_intCM = ?, salvar_Cstg_intDG = ?"
					+ "WHERE sequencial = ?");

			st.setString(1, objeto.getOrigem().toUpperCase());
			st.setString(2, objeto.getTipoMovimento());
			st.setString(3, objeto.getAnoMes());
			st.setDouble(4, objeto.getCodCentroCustos());
			st.setString(5, objeto.getDescCentroCustos());
			st.setString(6, objeto.getCodContaContabil());
			st.setString(7, objeto.getDescContaContabil());
			st.setString(8, objeto.getCodMaterial());
			st.setString(9, objeto.getDescMovimento());
			st.setString(10, objeto.getUnidadeMedida());
			st.setDouble(11, objeto.getQuantidade());
			st.setDouble(12, objeto.getPrecoUnitario());
			st.setDouble(13, objeto.getValorMovimento());
			st.setString(14, objeto.getNumeroOS());
			st.setString(15, objeto.getFrotaOuCC());
			st.setString(16, objeto.getDocumentoErp());
			st.setDate(17, new java.sql.Date(objeto.getDataMovimento().getTime()));
			st.setString(18, objeto.getImportar());
			st.setString(19, objeto.getObservacao());
			st.setString(20, objeto.getValidacoesOS());
			st.setString(21, objeto.getPoliticas());
			st.setString(22, objeto.getSalvarOS_Material());
			st.setString(23, objeto.getSalvarCstg_IntVM());
			st.setString(24, objeto.getSalvarCstg_IntCM());
			st.setString(25, objeto.getSalvarCstg_IntDG());
			st.setInt(26, objeto.getSequencial());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Erp  \n \n" + e.getMessage() + "\n \n" + objeto);
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
			throw new DbException("Tabela Erp \n \n" + e.getMessage() + "\n \n" + sequencial);
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
			throw new DbException("Tabela Erp \n \n" + e.getMessage() + "\n \n" + sequencial);
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
			throw new DbException("Tabela Erp \n \n" + e.getMessage());
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
			throw new DbException("Tabela Erp \n \n" +
								  "erro na consulta filtrada \n"  +  e.toString());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}
	@Override
	public List<Erp> pesquisarQuemAtendeAPolitica(Integer codPolitica, String clausulaWhere) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM Erp WHERE " + clausulaWhere);
			rs = st.executeQuery();
			List<Erp> lista = new ArrayList<Erp>();
			while (rs.next()) {
				Erp dadosErp = instanciaDadosErp(rs);
				lista.add(dadosErp);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +
								  "erro na consulta da politica " 
							 	  + String.format("%03d", codPolitica)
								  + "\n" + e.toString());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}
	@Override
	public void limparValidacoesOS() {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE Erp " + "SET validacoes_OS = null");
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Tabela Erp  \n \n" + e.getMessage() + "\n \n");
		} finally {
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
			throw new DbException("Tabela Erp \n \n" + e.getMessage());
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
			throw new DbException("Tabela Erp (" + origem + ") \n \n" + e.getMessage());
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
			throw new DbException("Tabela Erp \n \n" + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public Integer getTotalRegistros() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp");
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +  e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
	@Override
	public Integer getQtdeIndefinidos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE salvar_cstg_intVM IS NULL AND importar IS NULL");
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +  e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
	@Override
	public Integer getQtdeValorMaterial(String tipo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			if (tipo == null ) {
				st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE salvar_cstg_intVM is null");
			}
			else {
				st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE salvar_cstg_intVM = ?");
				st.setString(1, tipo);
			}
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +
								  "VM = " + tipo + " \n \n"+ e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
	@Override
	public Integer getQtdeImportar(String tipo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			if (tipo == null ) {
				st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE importar is null");
			}
			else {
				st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE importar = ?");
				st.setString(1, tipo);
			}
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +
								  "Importar = " + tipo + " \n \n"+ e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
	@Override
	public Integer qtdeLiberadosOS() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE salvar_os_material = 'S'");
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +
								  "erro na Contagem do Erp para Ordem de Servico = " + " \n" 
								  + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
	@Override
	public Integer qtdeLiberadosCM() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE salvar_cstg_intCM = 'S'");
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +
								  "erro na Contagem do Erp para CM Consumo de Material = " + " \n" 
								  + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
	@Override
	public Integer qtdeLiberadosDG() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE salvar_cstg_intDG = 'S'");
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +
								  "erro na Contagem do Erp para DG Despesas Gerais = " + " \n" 
								  + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}
	@Override
	public Integer qtdeLiberadosVM() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE salvar_cstg_intVM = 'S'");
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +
								  "erro na Contagem do Erp para VM Valores de Materiais = " + " \n" 
								  + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}


	@Override
	public Integer qtdeDessaCritica(String essaCriticaTxt, String importar) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			if (importar != null ) {
				st = conexao.prepareStatement("SELECT COUNT(*) FROM Erp WHERE importar = ? AND politicas LIKE ?");
				st.setString(1, importar);
				st.setString(2, "%" + essaCriticaTxt + "%");
			}
			rs = st.executeQuery();
			if (rs.next()) {
				int qtde = rs.getInt(1);
				return qtde;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Tabela Erp \n \n" +
								  "Importar = " + importar + " \n" +
								  "Critica  = " + essaCriticaTxt + " \n \n" 
								  + e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}


	private Erp instanciaDadosErp(ResultSet rs) throws SQLException {
		Erp dadosErp = new Erp();
		dadosErp.setAnoMes(rs.getString("Ano_Mes"));
		dadosErp.setTipoMovimento(rs.getString("Tipo_Movimento"));
		dadosErp.setOrigem(rs.getString("Origem"));
		dadosErp.setCodCentroCustos(rs.getDouble("Cod_Centro_Custos"));
		dadosErp.setDescCentroCustos(rs.getString("Desc_Centro_Custos"));
		dadosErp.setCodContaContabil(rs.getString("Cod_Conta_Contabil"));
		dadosErp.setDescContaContabil(rs.getString("Desc_Conta_Contabil"));
		dadosErp.setCodMaterial(rs.getString("Cod_Material"));
		dadosErp.setDescMovimento(rs.getString("Desc_Movimento"));
		dadosErp.setUnidadeMedida(rs.getString("Unidade_Medida"));
		dadosErp.setQuantidade(rs.getDouble("Quantidade"));
		dadosErp.setPrecoUnitario(rs.getDouble("Preco_Unitario"));
		dadosErp.setValorMovimento(rs.getDouble("Valor_Movimento"));
		dadosErp.setNumeroOS(rs.getString("Numero_OS"));
		dadosErp.setFrotaOuCC(rs.getString("Frota_Ou_CC"));
		dadosErp.setDocumentoErp(rs.getString("Documento_Erp"));
		dadosErp.setDataMovimento(new java.util.Date(rs.getTimestamp("Data_Movimento").getTime()));

		dadosErp.setImportar(rs.getString("Importar"));
		dadosErp.setObservacao(rs.getString("Observacao"));
		dadosErp.setValidacoesOS(rs.getString("validacoes_OS"));
		dadosErp.setPoliticas(rs.getString("Politicas"));
		dadosErp.setSalvarOS_Material(rs.getString("Salvar_OS_Material"));
		dadosErp.setSalvarCstg_IntVM(rs.getString("Salvar_Cstg_IntVM"));
		dadosErp.setSalvarCstg_IntCM(rs.getString("Salvar_Cstg_intCM"));
		dadosErp.setSalvarCstg_IntDG(rs.getString("Salvar_Cstg_intDG"));
		dadosErp.setSequencial(rs.getInt("Sequencial"));
		return dadosErp;
	}


}
