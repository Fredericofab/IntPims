package model.services;

import java.util.List;

import model.entities.Cstg_IntPF;
import model.entities.FolhaSumarizada;

public class ExportarFolhaService {

	private	FolhaSumarizadaService folhaSumarizadaService = new FolhaSumarizadaService();
	private FolhaService folhaService = new FolhaService();
	private VerbasFolhaService verbasDaFolhaService = new VerbasFolhaService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
	private PimsGeralService pimsGeralService = new PimsGeralService();

//	parametros
	String anoMes;
	String instancia;
	String usuarioPimsCS;
	String cdEmpresa;
	String prefixoMatr;

	public void processar() {
		lerParametros();
		String dataref = "01/" + anoMes.substring(4, 6) + "/" + anoMes.substring(0, 4);
		deletarCstgIntFP(dataref);
		gravarCstgIntFP(dataref, usuarioPimsCS);
		gerarTxt();
		processoAtualService.atualizarEtapa("ExportarFolha","S");
		processoAtualService.atualizarEtapa("VerbaAlterada","N");
		processoAtualService.atualizarEtapa("FolhaAlterada","N");
	}

	private void gerarTxt() {
		Boolean oficial = true;
		folhaSumarizadaService.gerarTxt(oficial);
		folhaService.gerarTxt(oficial);
		verbasDaFolhaService.gerarTxt(oficial);
	}

	private void deletarCstgIntFP(String dataref) {
		pimsGeralService.deletarCstg_IntFP(dataref, usuarioPimsCS);
	}

	private void gravarCstgIntFP(String dataref, String usuarioPimsCS) {
		List<FolhaSumarizada> lista = folhaSumarizadaService.pesquisarTodos();
		for (FolhaSumarizada sumarioFolha : lista) {
			Cstg_IntPF cstg_intfp = new Cstg_IntPF();
			cstg_intfp.setCdEmpresa(cdEmpresa);
			cstg_intfp.setDtRefer(dataref);
			cstg_intfp.setCdFunc(colocarPrefixo(prefixoMatr, sumarioFolha.getCodCentroCustos()));
			cstg_intfp.setQtHoras(sumarioFolha.getTotalReferenciaSim());
			cstg_intfp.setQtValor(sumarioFolha.getTotalImportarSim());
			cstg_intfp.setInstancia(instancia);
			pimsGeralService.gravarCstg_IntFP(cstg_intfp, usuarioPimsCS);
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
