package model.services;

import java.util.List;

import model.entities.Cstg_IntFP;
import model.entities.FolhaSumarizada;

public class ExportarFolhaService {

	private	FolhaSumarizadaService folhaSumarizadaService = new FolhaSumarizadaService();
	private FolhaService folhaService = new FolhaService();
	private VerbasFolhaService verbasDaFolhaService = new VerbasFolhaService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
	private Cstg_IntFPService cstg_IntFPService = new Cstg_IntFPService();

//	parametros
	String anoMes;
	String instancia;
	String usuarioPimsCS;
	String cdEmpresa;
	String prefixoMatr;
	
	String dataref;
	Integer qtdeProcessadaFP;

	public Integer processar() {
		lerParametros();
		dataref = "01/" + anoMes.substring(4, 6) + "/" + anoMes.substring(0, 4);
		deletarCstgIntFP();
		gravarCstgIntFP();
		gerarTxt();
		processoAtualService.atualizarEtapa("ExportarFolha","S");
		return qtdeProcessadaFP;
	}

	private void gerarTxt() {
		Boolean oficial = true;
		folhaSumarizadaService.gerarTxt(oficial);
		folhaService.gerarTxt(oficial);
		verbasDaFolhaService.gerarTxt(oficial);
	}

	private void deletarCstgIntFP() {
		cstg_IntFPService.deletarPeriodo(dataref, usuarioPimsCS);
	}

	private void gravarCstgIntFP() {
		qtdeProcessadaFP = 0;
		List<FolhaSumarizada> lista = folhaSumarizadaService.pesquisarTodos();
		for (FolhaSumarizada sumarioFolha : lista) {
			Cstg_IntFP cstg_intfp = new Cstg_IntFP();
			cstg_intfp.setCdEmpresa(cdEmpresa);
			cstg_intfp.setDtRefer(dataref);
			cstg_intfp.setCdFunc(colocarPrefixo(prefixoMatr, sumarioFolha.getCodCentroCustos()));
			cstg_intfp.setQtHoras(sumarioFolha.getTotalReferenciaSim());
			cstg_intfp.setQtValor(sumarioFolha.getTotalImportarSim());
			cstg_intfp.setInstancia(instancia);
			cstg_IntFPService.inserir(cstg_intfp, usuarioPimsCS);
			qtdeProcessadaFP += 1;
		}
	}

	private Double colocarPrefixo(String prefixo, Double codCCusto) {
		Double cdFunc = Double.parseDouble(prefixo) + codCCusto;
		return cdFunc;
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes         = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		instancia      = (parametrosService.pesquisarPorChave("AmbienteOracle", "InstanciaPimsCS")).getValor();
		usuarioPimsCS  = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		cdEmpresa      = (parametrosService.pesquisarPorChave("AmbienteOracle", "EmpresaPadrao")).getValor();
		prefixoMatr    = (parametrosService.pesquisarPorChave("AmbienteOracle", "PrefixoMatricula")).getValor();
	}

}
