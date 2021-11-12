package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.ErpDao;
import model.dao.FabricaDeDao;
import model.entities.Erp;

public class ErpService {

	private ErpDao dao = FabricaDeDao.criarErpDao();
	
	private ParametrosService parametrosService = new ParametrosService();

//	parametros
	String saida;

	public List<Erp> pesquisarTodos() {
		return dao.listarTodos();
	}

	public void salvarOuAtualizar(Erp objeto) {
		if (dao.pesquisarPorChave(objeto.getSequencial()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
	}

	public void remover(Erp objeto) {
		dao.deletarPorChave(objeto.getSequencial());
	}

	public Integer deletarTodos() {
		return dao.deletarTodos();
	}

	public Integer deletarOrigem(String origem) {
		return dao.deletarPorOrigem(origem);
	}

	public Integer ultimoSequencial() {
		return dao.ultimoSequencial();
	}
	
	public Integer qtdeTotal() {
		return dao.qtdeTotal();
	}

	public Integer qtdeImportarS() {
		return dao.qtdeImportarS();
	}
	public Integer qtdeImportarN() {
		return dao.qtdeImportarN();
	}
	public Integer qtdeImportarIndefinido() {
		return dao.qtdeImportarIndefinido();
	}

	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<Erp> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("origem,anoMes," +
					 "codCentroCustos,descCentroCustos,"   +
					 "codContaContabil,descContaContabil," +
					 "codMaterial,descMaterialDespesa,unidadeMedida," +
					 "quantidade,precoUnitario,valorMovimento," +
					 "referenciaOS,manfroOS,documentoErp,dataMovimento," +
					 "importar,observacao,criticas," +
					 "salvarOS_Material,salvarCstg_IntVM,salvarCstg_intCM,salvarCstg_intDG," +
					 "sequencial");
			bw.newLine();
			for (Erp dadosErp : lista) {
				String linha = dadosErp.getOrigem() + "," + 
							   dadosErp.getAnoMes() + "," + 
							   String.format("%.0f", dadosErp.getCodCentroCustos()) + "," + 
							   dadosErp.getDescCentroCustos() + "," + 
							   String.format("%.0f",dadosErp.getCodContaContabil()) + "," + 
							   dadosErp.getDescContaContabil() + "," + 
							   String.format("%.0f",dadosErp.getCodMaterial()) + "," + 
							   dadosErp.getDescMovimento() + "," + 
							   dadosErp.getUnidadeMedida() + "," + 
							   String.format("%.0f",dadosErp.getQuantidade()) + "," + 
							   String.format("%.0f",dadosErp.getPrecoUnitario()) + "," + 
							   String.format("%.0f",dadosErp.getValorMovimento()) + "," + 
							   dadosErp.getReferenciaOS() + "," + 
							   String.format("%.0f",dadosErp.getNumeroOS()) + "," + 
							   dadosErp.getDocumentoErp() + "," + 
							   dadosErp.getDataMovimento() + "," + 
							   dadosErp.getImportar() + "," + 
							   dadosErp.getObservacao() + "," + 
							   dadosErp.getCriticas() + "," + 
							   dadosErp.getSalvarOS_Material() + "," + 
							   dadosErp.getSalvarCstg_IntVM() + "," + 
							   dadosErp.getSalvarCstg_IntCM() + "," + 
							   dadosErp.getSalvarCstg_IntDG() + "," + 
							   dadosErp.getSequencial();
				bw.write(linha);
				bw.newLine();
			}
			if (! oficial) {
				Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida, AlertType.INFORMATION);
			}
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
		}
	}

	private void lerParametros(Boolean oficial) {
		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("DadosErp", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = (parametrosService.pesquisarPorChave("DadosErp", "ArqSaidaNome")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("DadosErp", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
		}
	}
}
