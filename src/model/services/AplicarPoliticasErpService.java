package model.services;

import java.util.List;

import model.entities.Erp;
import model.entities.PoliticasErp;
import model.exceptions.IntegridadeException;

public class AplicarPoliticasErpService {

	private ErpService erpService = new ErpService();
	private PoliticasErpService politicasErpService = new PoliticasErpService();

//	parametros
	String usuarioPimsCS;
	String anoMes;

	List<Erp> listaErp;
	Integer qtdeAplicados;
	Integer qtdePendentes;
	Integer qtdeLiberados;
	Integer qtdeIgnorados;
	Integer qtdeLiberadosOS;
	Integer qtdeLiberadosCM;
	Integer qtdeLiberadosDG;

	public Integer getTotalRegistros() {
		return erpService.getTotalRegistros();
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

	public void aplicarUma(Integer codigo) {
		PoliticasErp politicaErp = politicasErpService.pesquisarPorChave(codigo);
		if (politicaErp == null) {
			throw new IntegridadeException("Politica Informada não Existe");
		}
		lerParametros();
		listaErp = erpService.pesquisarTodos();
		aplicarPolitica(politicaErp);
	}

	public void aplicarTodas() {
		lerParametros();
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
		listaErp = erpService.pesquisarQuemAtendeAPolitica(codPolitica, clausulaWhere);
		zerarContadores();
		for (Erp erp : listaErp) {
			qtdeAplicados += 1;
			if ((politicaErp.getImportar() != null) && (politicaErp.getImportar().equals("?"))) {
				qtdePendentes += 1;
			}
			if ((politicaErp.getImportar() != null) && (politicaErp.getImportar().equals("S"))) {
				qtdeLiberados += 1;
			}
			if ((politicaErp.getImportar() != null) && (politicaErp.getImportar().equals("N"))) {
				qtdeIgnorados += 1;
			}
			erp = atualizarRegistroErp(politicaErp, erp);
			erpService.salvarOuAtualizar(erp);
		}
		politicaErp = atualizarRegistroPoliticaErp(politicaErp);
		politicasErpService.salvarOuAtualizar(politicaErp);
	}

	public void atualizarEstatisticaPorPolitica() {
		for (PoliticasErp politicaErp : politicasErpService.pesquisarTodos()) {
			if (politicaErp.getFlagAtiva().toUpperCase().equals("S")) {
				String essaPoliticaTxt = String.format("%04d", politicaErp.getCodPolitica());
				zerarContadores();
				qtdePendentes = erpService.qtdeDessaCritica(essaPoliticaTxt, "?");
				qtdeLiberados = erpService.qtdeDessaCritica(essaPoliticaTxt, "S");
				qtdeIgnorados = erpService.qtdeDessaCritica(essaPoliticaTxt, "N");
				atualizarRegistroPoliticaErp(politicaErp);
				politicasErpService.salvarOuAtualizar(politicaErp);
			}
		}
	}

	private Erp atualizarRegistroErp(PoliticasErp politicaErp, Erp erp) {

		if (politicaErp.getImportar() != null) {
			erp.setImportar(politicaErp.getImportar());
		}

		if ((politicaErp.getSalvarOS_Material() != null)
				&& (!politicaErp.getSalvarOS_Material().toUpperCase().equals(" "))) {
			erp.setSalvarOS_Material(politicaErp.getSalvarOS_Material());
		}
		if ((politicaErp.getSalvarCstg_IntVM() != null)
				&& (!politicaErp.getSalvarCstg_IntVM().toUpperCase().equals(" "))) {
			erp.setSalvarCstg_IntVM(politicaErp.getSalvarCstg_IntVM());
		}
		if ((politicaErp.getSalvarCstg_IntCM() != null)
				&& (!politicaErp.getSalvarCstg_IntCM().toUpperCase().equals(" "))) {
			erp.setSalvarCstg_IntCM(politicaErp.getSalvarCstg_IntCM());
		}
		if ((politicaErp.getSalvarCstg_IntDG() != null)
				&& (!politicaErp.getSalvarCstg_IntDG().toUpperCase().equals(" "))) {
			erp.setSalvarCstg_IntDG(politicaErp.getSalvarCstg_IntDG());
		}
		String essaPoliticaTxt = String.format("%04d", politicaErp.getCodPolitica()) + " ";
		String criticasDesseRegistros = erp.getPoliticas();
		if (criticasDesseRegistros == null) {
			erp.setPoliticas(essaPoliticaTxt);
		} else {
			if (criticasDesseRegistros.indexOf(essaPoliticaTxt) == -1) {
				erp.setPoliticas(criticasDesseRegistros + essaPoliticaTxt);
			}
		}
		return erp;
	}

	private PoliticasErp atualizarRegistroPoliticaErp(PoliticasErp politicaErp) {
		politicaErp.setAnoMesAnalisado(anoMes);
		politicaErp.setRegistrosAplicados(qtdeAplicados);
		return politicaErp;
	}

	private void zerarContadores() {
		qtdeAplicados = 0;
		qtdePendentes = 0;
		qtdeLiberados = 0;
		qtdeIgnorados = 0;
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}

}
