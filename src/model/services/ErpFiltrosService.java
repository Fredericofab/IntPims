package model.services;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.FabricaDeDao;
import model.dao.ProcessoAtualDao;
import model.entities.ProcessoAtual;
import model.exceptions.ParametroInvalidoException;

public class ErpFiltrosService {

	private ProcessoAtualDao dao = FabricaDeDao.criarProcessoAtualDao();

	String anoMes;

	
	public void salvarFiltro(String importar, String valorMaterial, String sobreporPolitica, Boolean liberacaoDupla, String politica, String validacaoOS, String filtro) {
		String clausulaWhere;
		if (filtro != null) {
			clausulaWhere = filtro;
		} else {
			clausulaWhere = montarFiltro(importar, valorMaterial, sobreporPolitica, liberacaoDupla, politica, validacaoOS);
		}
		gravarWhere(clausulaWhere);
	}	
	
	private String montarFiltro(String importar, String valorMaterial, String sobreporPolitica, Boolean liberacaoDupla, String politica, String validacaoOS) {
		String clausulaWhere  = "";
		String filtroImportar = "";;
		String filtroValorMaterial = "";;
		String filtroSobreporPolitica = "";;
		String filtroPolitica  = "";
		String filtroValidacaoOS  = "";
		String filtroLiberacaoDupla = "";

		if (importar != null) {
			if (importar.toUpperCase().equals("X") ) {
				filtroImportar =  "IMPORTAR IS NULL" ;
			}
			else {
				filtroImportar =  "IMPORTAR = '" + importar + "'" ;
			}
		}
		if (valorMaterial != null) {
			if (valorMaterial.toUpperCase().equals("X") ) {
				filtroValorMaterial =  "SALVAR_CSTG_INTVM IS NULL" ;
			}
			else {
				filtroValorMaterial =  "SALVAR_CSTG_INTVM = '" + valorMaterial + "'" ;
			}
		}
		
		if (liberacaoDupla) {
			filtroLiberacaoDupla = "(( SALVAR_CSTG_INTCM = 'S' AND SALVAR_CSTG_INTDG  = 'S') OR " +
									"( SALVAR_CSTG_INTCM = 'S' AND SALVAR_OS_MATERIAL = 'S') OR " +
									"( SALVAR_CSTG_INTDG = 'S' AND SALVAR_OS_MATERIAL = 'S')   )" ;
		}
		filtroSobreporPolitica = (sobreporPolitica != null) ? "SOBREPOR_POLITICAS = '" + sobreporPolitica + "'" : "";
		filtroPolitica = (politica != null) ? "POLITICAS LIKE '%" + politica + "%'" : "";
		filtroValidacaoOS = (validacaoOS != null) ? "VALIDACOES_OS = '" + validacaoOS + "'" : "";
		
		clausulaWhere = null;
		if (importar != null ) {
			clausulaWhere = filtroImportar;
		}
		if (valorMaterial != null ) {
			clausulaWhere = ( clausulaWhere == null ) ? filtroValorMaterial : clausulaWhere + " AND " + filtroValorMaterial;
		}
		if (sobreporPolitica != null ) {
			clausulaWhere = ( clausulaWhere == null ) ? filtroSobreporPolitica : clausulaWhere + " AND " + filtroSobreporPolitica;
		}
		if (politica != null ) {
			clausulaWhere = ( clausulaWhere == null ) ? filtroPolitica : clausulaWhere + " AND " + filtroPolitica;
		}
		if (validacaoOS != null ) {
			clausulaWhere = ( clausulaWhere == null ) ? filtroValidacaoOS : clausulaWhere + " AND " + filtroValidacaoOS;
		}
		if (liberacaoDupla ) {
			clausulaWhere = ( clausulaWhere == null ) ? filtroLiberacaoDupla : clausulaWhere + " AND " + filtroLiberacaoDupla;
		}

		return clausulaWhere;
	}

	
	private void gravarWhere(String clausulaWhere) {
		try {
			lerParametros();
			ProcessoAtualService processoAtualService = new ProcessoAtualService();
			ProcessoAtual processoAtual = processoAtualService.pesquisarPorChave(anoMes);
			processoAtual.setFiltroErp(clausulaWhere);
			dao.atualizar(processoAtual);
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. GravarWhere do Filtro", e.getMessage(),AlertType.ERROR);
		}
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}

}
