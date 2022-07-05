package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.scene.control.Alert.AlertType;
import model.dao.ErpDao;
import model.dao.FabricaDeDao;
import model.entities.Erp;
import model.entities.ProcessoAtual;
import model.exceptions.ParametroInvalidoException;

public class ErpService {

	private ErpDao dao = FabricaDeDao.criarErpDao();
	
	private ParametrosService parametrosService = new ParametrosService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	parametros
	String saida;
	String valorIncoerente;
	String anoMes;

	public List<Erp> pesquisarTodos() {
		return dao.listarTodos();
	}
	public void salvarOuAtualizar(Erp objeto, Boolean atualizarEtapaDoProcesso) {
		if (dao.pesquisarPorChave(objeto.getSequencial()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
		if (atualizarEtapaDoProcesso ) 	reatualizarEtapaDoProcesso();
	}
	public void remover(Erp objeto) {
		dao.deletarPorChave(objeto.getSequencial());
		reatualizarEtapaDoProcesso();
	}
	public Integer ultimoSequencial() {
		return dao.ultimoSequencial();
	}
	public Integer deletarOrigem(String origem) {
		return dao.deletarPorOrigem(origem);
	}
	public List<Erp> pesquisarTodosFiltrado(String filtro) {
		return dao.listarTodosFiltrado(filtro);
	}
	public void limparValidacoesOS() {
		dao.limparValidacoesOS();
	}
	public void limparPoliticas() {
		dao.limparPoliticas();
	}
	public void executarScript(String linha) {
		dao.executarScript(linha);
	}
	public void recalcularValor() {
		lerParametros(false);
		Double limiteSuperior = 1.00 + Double.parseDouble(valorIncoerente)/100;
		Double limiteInferior = 1.00 - Double.parseDouble(valorIncoerente)/100;
		List<Erp> lista = pesquisarTodos();
		for (Erp erp : lista) {
			Double calculo = ( erp.getValorMovimento() == 0.00 ? 0.00 : erp.getQuantidade() * erp.getPrecoUnitario() / erp.getValorMovimento());
			if ( calculo > limiteSuperior || calculo < limiteInferior ) {
				if (erp.getObservacao() == null) {
					erp.setObservacao("Preço Unitario Anterior: " + erp.getPrecoUnitario());
				}
				else{
					erp.setObservacao("Preço Unitario Anterior: " + erp.getPrecoUnitario() + " . " + erp.getObservacao());
				}
				erp.setPrecoUnitario(erp.getValorMovimento() / erp.getQuantidade());
				dao.atualizar(erp);
			}	 
		}
	}

	
	public void gerarTxt(Boolean oficial) {
		try {
			lerParametros(oficial);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String filtro = lerFiltroMovimentoErp();
			List<Erp> lista = ((filtro == null || oficial ) ? pesquisarTodos() : pesquisarTodosFiltrado(filtro));
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
				bw.write("TArq,TMov,anoMes," +
						 "CCustos,DescCCustos,"   +
						 "CContabil,DescCContabil,Natureza" +
						 "Material,DescMaterial,UN," +
						 "Qtde,PUnit,VTotal," +
						 "NumeroOS,FrotaOuCC,NumeroERP,Data," +
						 "IMP,Observacao,Validacoes,Politicas," +
						 "OS,VM,CM,DG," +
						 "NumReg");
	
				bw.newLine();
				for (Erp dadosErp : lista) {
	
					String linha = dadosErp.getOrigem() + "," + 
								   dadosErp.getTipoMovimento() + "," + 
								   dadosErp.getAnoMes() + "," + 
								   String.format("%.0f", dadosErp.getCodCentroCustos()) + "," + 
								   dadosErp.getDescCentroCustos() + "," + 
								   dadosErp.getCodContaContabil() + "," + 
								   dadosErp.getDescContaContabil() + "," + 
								   dadosErp.getCodNatureza() + "," + 
								   dadosErp.getCodMaterial() + "," + 
								   dadosErp.getDescMovimento().replace(",",".") + "," + 
								   dadosErp.getUnidadeMedida() + "," + 
								   Utilitarios.formatarNumeroDecimal4SemMilhar('.').format(dadosErp.getQuantidade())  + "," + 
								   Utilitarios.formatarNumeroDecimal4SemMilhar('.').format(dadosErp.getPrecoUnitario())  + "," + 
								   Utilitarios.formatarNumeroDecimal4SemMilhar('.').format(dadosErp.getValorMovimento())  + "," + 
								   dadosErp.getNumeroOS() + "," + 
								   dadosErp.getFrotaOuCC() + "," + 
								   dadosErp.getDocumentoErp() + "," + 
								   sdf.format(dadosErp.getDataMovimento()) + "," + 
								   dadosErp.getImportar() + "," + 
								   dadosErp.getObservacao() + "," + 
								   dadosErp.getValidacoesOS() + "," + 
								   dadosErp.getPoliticas() + "," + 
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
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. Gerando Erp TXT", e.getMessage(),AlertType.ERROR);
		}
	}

	private String lerFiltroMovimentoErp() {
		ProcessoAtualService processoAtualService = new ProcessoAtualService();
		ProcessoAtual processoAtual = processoAtualService.pesquisarPorChave(anoMes);
		return processoAtual.getFiltroErp();
	}
	
	private void lerParametros(Boolean oficial) {
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaPasta")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaTipo")).getValor();

		if (oficial) {
			saida = arqSaidaPasta + "DadosErp" + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + "DadosErp" + anoMes + arqSaidaTipo ;
		}
		
		valorIncoerente = (parametrosService.pesquisarPorChave("ValidarErp", "ValorIncoerente")).getValor();
	}

	private void reatualizarEtapaDoProcesso() {
		processoAtualService.atualizarEtapa("ValidarErp","N");
		processoAtualService.atualizarEtapa("AplicarPoliticaErp","N");
		processoAtualService.atualizarEtapa("ExportarErpVM","N");
		processoAtualService.atualizarEtapa("ExportarErpCM","N");
		processoAtualService.atualizarEtapa("ExportarErpDG","N");
		processoAtualService.atualizarEtapa("ExportarErpOS","N");
	}
	
//usadas no AplicarPoliticasErpService	
	public List<Erp> pesquisarQuemAtendeAPolitica(Integer codPolitica, String clausulaWhere) {
			return dao.pesquisarQuemAtendeAPolitica(codPolitica, clausulaWhere);
	}
	public Integer getTotalRegistros() {
		return dao.getTotalRegistros();
	}
	public Integer getQtdeNaoCalculados() {
		return dao.getQtdeNaoCalculados();
	}
	public Integer getQtdeIndefinidos() {
		return dao.getQtdeIndefinidos();
	}
	public Integer getQtdeImportar(String tipo) {
		return dao.getQtdeImportar(tipo);
	}
	public Integer getQtdeValorMaterial(String tipo) {
		return dao.getQtdeValorMaterial(tipo);
	}
	public Integer qtdeDessaCritica(String essaCriticaTxt, String importar) {
		return dao.qtdeDessaCritica(essaCriticaTxt, importar);
	}
	public Integer qtdeLiberadosOS() {
		return dao.qtdeLiberadosOS();
	}
	public Integer qtdeLiberadosCM() {
		return dao.qtdeLiberadosCM();
	}
	public Integer qtdeLiberadosDG() {
		return dao.qtdeLiberadosDG();
	}
	public Integer qtdeLiberacaoDupla() {
		return dao.qtdeLiberacaoDupla();
	}
	public Integer qtdeLiberadosVM() {
		return dao.qtdeLiberadosVM();
	}

}
