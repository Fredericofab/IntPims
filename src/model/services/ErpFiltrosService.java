package model.services;

import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.scene.control.Alert.AlertType;
import model.dao.FabricaDeDao;
import model.dao.ProcessoAtualDao;
import model.entities.ProcessoAtual;
import model.exceptions.ParametroInvalidoException;

public class ErpFiltrosService {

	private ProcessoAtualDao dao = FabricaDeDao.criarProcessoAtualDao();

// parametros
	String anoMes;
	String valorIncoerenteTxt;

	public void salvarFiltro(String importar, String valorMaterial, String sobreporPolitica, Boolean liberacaoDupla,
			Boolean valorIncoerente, String politica, String validacaoOS, String filtro) {
		try {
			lerParametros();
			String clausulaWhere;
			if (filtro != null) {
				clausulaWhere = filtro;
			} else {
				clausulaWhere = montarFiltro(importar, valorMaterial, sobreporPolitica, liberacaoDupla, valorIncoerente,
						politica, validacaoOS);
			}
			gravarWhere(clausulaWhere);
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado.",
					e.getMessage(), AlertType.ERROR);
		}

	}

	private String montarFiltro(String importar, String valorMaterial, String sobreporPolitica, Boolean liberacaoDupla,
			Boolean valorIncoerente, String politica, String validacaoOS) {
		String clausulaWhere = "";
		String filtroImportar = "";
		;
		String filtroValorMaterial = "";
		;
		String filtroSobreporPolitica = "";
		;
		String filtroPolitica = "";
		String filtroValidacaoOS = "";
		String filtroLiberacaoDupla = "";
		String filtroValorIncoerente = "";

		if (importar != null) {
			if (importar.toUpperCase().equals("X")) {
				filtroImportar = "IMPORTAR IS NULL";
			} else {
				filtroImportar = "IMPORTAR = '" + importar + "'";
			}
		}
		if (valorMaterial != null) {
			if (valorMaterial.toUpperCase().equals("X")) {
				filtroValorMaterial = "SALVAR_CSTG_INTVM IS NULL";
			} else {
				filtroValorMaterial = "SALVAR_CSTG_INTVM = '" + valorMaterial + "'";
			}
		}

		if (liberacaoDupla) {
			filtroLiberacaoDupla = "(( SALVAR_CSTG_INTCM = 'S' AND SALVAR_CSTG_INTDG  = 'S') OR "
					+ "( SALVAR_CSTG_INTCM = 'S' AND SALVAR_OS_MATERIAL = 'S') OR "
					+ "( SALVAR_CSTG_INTDG = 'S' AND SALVAR_OS_MATERIAL = 'S')   )";
		}

		if (valorIncoerente) {
			Double limiteSuperior = 1.00 + Double.parseDouble(valorIncoerenteTxt) / 100;
			Double limiteInferior = 1.00 - Double.parseDouble(valorIncoerenteTxt) / 100;
			String formula = "(( QUANTIDADE * PRECO_UNITARIO ) / VALOR_MOVIMENTO )";
			String limiteSuptxt = Utilitarios.formatarNumeroDecimal4SemMilhar('.').format(limiteSuperior);
			String limiteInftxt = Utilitarios.formatarNumeroDecimal4SemMilhar('.').format(limiteInferior);

			filtroValorIncoerente = "VALOR_MOVIMENTO = 0.00  OR " +
			                        "(" + formula + " > " + limiteSuptxt + ") OR " +
			                        "(" + formula + " < " + limiteInftxt + ")";
		}

		filtroSobreporPolitica = (sobreporPolitica != null) ? "SOBREPOR_POLITICAS = '" + sobreporPolitica + "'" : "";
		filtroPolitica = (politica != null) ? "POLITICAS LIKE '%" + politica + "%'" : "";
		filtroValidacaoOS = (validacaoOS != null) ? "VALIDACOES_OS = '" + validacaoOS + "'" : "";

		clausulaWhere = null;
		if (importar != null) {
			clausulaWhere = filtroImportar;
		}
		if (valorMaterial != null) {
			clausulaWhere = (clausulaWhere == null) ? filtroValorMaterial
					: clausulaWhere + " AND " + filtroValorMaterial;
		}
		if (sobreporPolitica != null) {
			clausulaWhere = (clausulaWhere == null) ? filtroSobreporPolitica
					: clausulaWhere + " AND " + filtroSobreporPolitica;
		}
		if (politica != null) {
			clausulaWhere = (clausulaWhere == null) ? filtroPolitica : clausulaWhere + " AND " + filtroPolitica;
		}
		if (validacaoOS != null) {
			clausulaWhere = (clausulaWhere == null) ? filtroValidacaoOS : clausulaWhere + " AND " + filtroValidacaoOS;
		}
		if (liberacaoDupla) {
			clausulaWhere = (clausulaWhere == null) ? filtroLiberacaoDupla
					: clausulaWhere + " AND " + filtroLiberacaoDupla;
		}
		if (valorIncoerente) {
			clausulaWhere = (clausulaWhere == null) ? filtroValorIncoerente
					: clausulaWhere + " AND " + filtroValorIncoerente;
		}

		return clausulaWhere;
	}

	private void gravarWhere(String clausulaWhere) {
		ProcessoAtualService processoAtualService = new ProcessoAtualService();
		ProcessoAtual processoAtual = processoAtualService.pesquisarPorChave(anoMes);
		processoAtual.setFiltroErp(clausulaWhere);
		dao.atualizar(processoAtual);
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		valorIncoerenteTxt = (parametrosService.pesquisarPorChave("ValidarErp", "ValorIncoerente")).getValor();
	}

}
