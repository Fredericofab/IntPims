package model.services;

import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.entities.Erp;
import model.entities.PoliticasErp;
import model.exceptions.ParametroInvalidoException;

public class AplicarPoliticasErpService {

	private ErpService erpService = new ErpService();
	private PoliticasErpService politicasErpService = new PoliticasErpService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	parametros
	String usuarioPimsCS;
	String anoMes;

	List<Erp> listaErp;

	Integer totalRegistros;
	Integer qtdeNaoCalculados;
	Integer qtdeIndefinidos;
	Integer qtdeImportarS;
	Integer qtdeImportarN;
	Integer qtdeImportarI;
	Integer qtdeValorMaterialS;
	Integer qtdeValorMaterialN;
	Integer qtdeValorMaterialI;
	Integer qtdeImpLiberadosOS;
	Integer qtdeImpLiberadosCM;
	Integer qtdeImpLiberadosDG;
	Integer qtdeImpLiberacaoDupla;
	
	public Integer getTotalRegistros() {
		return totalRegistros;
	}
	public Integer getQtdeNaoCalculados() {
		return qtdeNaoCalculados;
	}
	public Integer getQtdeIndefinidos() {
		return qtdeIndefinidos;
	}
	public Integer getQtdeImportarS() {
		return qtdeImportarS;
	}
	public Integer getQtdeImportarN() {
		return qtdeImportarN;
	}
	public Integer getQtdeImportarI() {
		return qtdeImportarI;
	}
	public Integer getQtdeValorMaterialS() {
		return qtdeValorMaterialS;
	}
	public Integer getQtdeValorMaterialN() {
		return qtdeValorMaterialN;
	}
	public Integer getQtdeValorMaterialI() {
		return qtdeValorMaterialI;
	}

	public Integer getQtdeImpLiberadosOS() {
		return qtdeImpLiberadosOS;
	}
	public Integer getQtdeImpLiberadosCM() {
		return qtdeImpLiberadosCM;
	}
	public Integer getQtdeImpLiberadosDG() {
		return qtdeImpLiberadosDG;
	}
	public Integer getQtdeImpLiberacaoDupla() {
		return qtdeImpLiberacaoDupla;
	}

	public void aplicarTodas() {
		try {
			lerParametros();
			erpService.limparPoliticas();
			listaErp = erpService.pesquisarTodos();
			for (PoliticasErp politicaErp : politicasErpService.pesquisarTodos()) {
				if ( politicaErp.getFlagAtiva().toUpperCase().equals("S")) {
					aplicarPolitica(politicaErp);
				}
			}
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. Aplicar Politicas", e.getMessage(),AlertType.ERROR);
		}
	}
	
	private void aplicarPolitica(PoliticasErp politicaErp) {
		String clausulaWhere = politicaErp.getClausulaWhere();
		Integer codPolitica = politicaErp.getCodPolitica();
		List<Erp> listaErpDaPolitica = erpService.pesquisarQuemAtendeAPolitica(codPolitica, clausulaWhere);
		for (Erp erp : listaErpDaPolitica) {
			if (erp.getSobreporPoliticas() == null || erp.getSobreporPoliticas().equals("N")) {
				erp = atualizarRegistroErp(politicaErp, erp);
				Boolean atualizarEtapaDoProcesso = false;
				erpService.salvarOuAtualizar(erp, atualizarEtapaDoProcesso);
			}
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

	public void atualizarEstatisticaGeral() {
		totalRegistros = erpService.getTotalRegistros();
		qtdeNaoCalculados = erpService.getQtdeNaoCalculados();
		qtdeIndefinidos = erpService.getQtdeIndefinidos();
		qtdeImportarS = erpService.getQtdeImportar("S");
		qtdeImportarN = erpService.getQtdeImportar("N");
		qtdeImportarI = erpService.getQtdeImportar("?");
		qtdeValorMaterialS = erpService.getQtdeValorMaterial("S");
		qtdeValorMaterialN = erpService.getQtdeValorMaterial("N");
		qtdeValorMaterialI = erpService.getQtdeValorMaterial("?");
		qtdeImpLiberadosOS = erpService.qtdeLiberadosOS();
		qtdeImpLiberadosCM = erpService.qtdeLiberadosCM();
		qtdeImpLiberadosDG = erpService.qtdeLiberadosDG();
		qtdeImpLiberacaoDupla = erpService.qtdeLiberacaoDupla();

		if (qtdeIndefinidos + qtdeValorMaterialI + qtdeImportarI + qtdeImpLiberacaoDupla == 0) {
			processoAtualService.atualizarEtapa("AplicarPoliticaErp", "S");
		}
		else {
			processoAtualService.atualizarEtapa("AplicarPoliticaErp", "N");
			processoAtualService.atualizarEtapa("ExportarErpVM", "N");
			processoAtualService.atualizarEtapa("ExportarErpCM", "N");
			processoAtualService.atualizarEtapa("ExportarErpDG", "N");
			processoAtualService.atualizarEtapa("ExportarErpOS", "N");
		}
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
				Boolean atualizarEtapaDoProcesso = false;
				politicasErpService.salvarOuAtualizar(politicaErp, atualizarEtapaDoProcesso );
			}
		}
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}
}
