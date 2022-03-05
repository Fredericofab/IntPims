package model.services;

import java.util.List;

import model.entities.Erp;
import model.entities.PoliticasErp;

public class AplicarPoliticasErpService {

	private ErpService erpService = new ErpService();
	private PoliticasErpService politicasErpService = new PoliticasErpService();

//	parametros
	String usuarioPimsCS;
	String anoMes;

	List<Erp> listaErp;

	public Integer getTotalRegistros() {
		return erpService.getTotalRegistros();
	}

	public Integer getQtdeNaoCalculados() {
		return erpService.getQtdeNaoCalculados();
	}
	public Integer getQtdeIndefinidos() {
		return erpService.getQtdeIndefinidos();
	}
	public Integer getQtdeImportar(String tipo) {
		return erpService.getQtdeImportar(tipo);
	}
	public Integer getQtdeValorMaterial(String tipo) {
		return erpService.getQtdeValorMaterial(tipo);
	}

	public Integer getQtdeImpLiberadosOS() {
		return erpService.qtdeLiberadosOS();
	}
	public Integer getQtdeImpLiberadosCM() {
		return erpService.qtdeLiberadosCM();
	}
	public Integer getQtdeImpLiberadosDG() {
		return erpService.qtdeLiberadosDG();
	}
	public Integer getQtdeLiberadosVM() {
		return erpService.qtdeLiberadosVM();
	}


	public void aplicarTodas() {
		lerParametros();
		erpService.limparPoliticas();
		listaErp = erpService.pesquisarTodos();
		for (PoliticasErp politicaErp : politicasErpService.pesquisarTodos()) {
			if ( politicaErp.getFlagAtiva().toUpperCase().equals("S")) {
				aplicarPolitica(politicaErp);
			}
		}
	}
	
	private void aplicarPolitica(PoliticasErp politicaErp) {
		String clausulaWhere = politicaErp.getClausulaWhere();
		Integer codPolitica = politicaErp.getCodPolitica();
		List<Erp> listaErpDaPolitica = erpService.pesquisarQuemAtendeAPolitica(codPolitica, clausulaWhere);
		for (Erp erp : listaErpDaPolitica) {
			erp = atualizarRegistroErp(politicaErp, erp);
			erpService.salvarOuAtualizar(erp);
		}
	}

	private Erp atualizarRegistroErp(PoliticasErp politicaErp, Erp erp) {

		if (politicaErp.getImportar() != null) {
			erp.setImportar(politicaErp.getImportar());
		}

		if (politicaErp.getSalvarOS_Material() != null) {
			erp.setSalvarOS_Material(politicaErp.getSalvarOS_Material());
		}
		if (politicaErp.getSalvarCstg_IntVM() != null) {
			erp.setSalvarCstg_IntVM(politicaErp.getSalvarCstg_IntVM());
		}
		if (politicaErp.getSalvarCstg_IntCM() != null) {
			erp.setSalvarCstg_IntCM(politicaErp.getSalvarCstg_IntCM());
		}
		if (politicaErp.getSalvarCstg_IntDG() != null) {
			erp.setSalvarCstg_IntDG(politicaErp.getSalvarCstg_IntDG());
		}
		String essaPoliticaTxt = String.format("%04d", politicaErp.getCodPolitica()) + " ";
		String politicasDesseRegistros = erp.getPoliticas();
		if (politicasDesseRegistros == null) {
			erp.setPoliticas(essaPoliticaTxt);
		}
		else {
			if (politicasDesseRegistros.indexOf(essaPoliticaTxt) == -1) {
				erp.setPoliticas(politicasDesseRegistros + essaPoliticaTxt);
			}
		}
		return erp;
	}

	public void atualizarEstatisticasPorPolitica() {
		lerParametros();
		listaErp = erpService.pesquisarTodos();
		List<PoliticasErp> listaPoliticas = politicasErpService.pesquisarTodos();
		for (PoliticasErp politicaErp : listaPoliticas ) {
			String essaPolitica = politicaErp.getCodPolitica().toString();
			Integer qtdeAplicados = 0;
			for (Erp erp : listaErp) {
				String politicasAplicadas = erp.getPoliticas();
				if ((politicasAplicadas != null) && 
				    (politicasAplicadas.indexOf(essaPolitica) != -1)) {
					qtdeAplicados += 1;
				}
				politicaErp.setAnoMesAnalisado(anoMes);
				politicaErp.setRegistrosAplicados(qtdeAplicados);
				politicasErpService.salvarOuAtualizar(politicaErp);
			}
		}
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}
}
