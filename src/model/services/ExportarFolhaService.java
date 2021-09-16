package model.services;

import java.util.List;

import model.dao.FabricaDeDao;
import model.dao.ProcessarFolhaDao;
import model.entities.Cstg_IntPF;
import model.entities.SumarioFolha;

public class ExportarFolhaService {

	private ProcessarFolhaDao dao = FabricaDeDao.criarProcessarFolhaDao();

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
	}

	private void gerarTxt() {
		Boolean oficial = true;

		SumarioFolhaService sumarioFolhaService = new SumarioFolhaService();
		sumarioFolhaService.gerarTxt(oficial);
		
		DadosFolhaService dadosFolhaService = new DadosFolhaService();
		dadosFolhaService.gerarTxt(oficial);

		VerbaFolhaService verbaFolhaService = new VerbaFolhaService();
		verbaFolhaService.gerarTxt(oficial);
	}

	private void deletarCstgIntFP(String dataref) {
		dao.deletarCstg_IntFP(dataref, usuarioPimsCS);
	}

	private void gravarCstgIntFP(String dataref, String usuarioPimsCS) {
		SumarioFolhaService sumarioFolhaService = new SumarioFolhaService();
		List<SumarioFolha> lista = sumarioFolhaService.pesquisarTodos();
		for (SumarioFolha sumarioFolha : lista) {
			Cstg_IntPF cstg_intfp = new Cstg_IntPF();
			cstg_intfp.setCdEmpresa(cdEmpresa);
			cstg_intfp.setDtRefer(dataref);
			cstg_intfp.setCdFunc(colocarPrefixo(prefixoMatr, sumarioFolha.getCodCentroCustos()));
			cstg_intfp.setQtValor(sumarioFolha.getTotalImportarSim());
			cstg_intfp.setInstancia(instancia);
			dao.gravarCstg_IntFP(cstg_intfp, usuarioPimsCS);
		}
	}

	private Double colocarPrefixo(String prefixo, String codCCusto) {
		Integer tamanho = codCCusto.length();
		String x = prefixo.repeat(10 - tamanho);
		Double cdFunc = Double.parseDouble((x + codCCusto));
		return cdFunc;
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes         = (parametrosService.pesquisarPorChave("AmbienteGeral", "AnoMes")).getValor();
		instancia      = (parametrosService.pesquisarPorChave("AmbienteOracle", "InstanciaPimsCS")).getValor();
		usuarioPimsCS  = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		cdEmpresa      = (parametrosService.pesquisarPorChave("AmbienteOracle", "EmpresaPadrao")).getValor();
		prefixoMatr    = (parametrosService.pesquisarPorChave("AmbienteOracle", "PrefixoMatricula")).getValor();
	}

}
