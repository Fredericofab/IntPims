package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.FabricaDeDao;
import model.dao.ProcessoAtualDao;
import model.entities.ProcessoAtual;

public class ErpFiltrosService {

	private ProcessoAtualDao dao = FabricaDeDao.criarProcessoAtualDao();

	String anoMes;

	
	public void salvarFiltro(String importar, String politica, String filtro) {
		String clausulaWhere;
		if (filtro != null) {
			clausulaWhere = filtro;
		} else {
			clausulaWhere = montarFiltro(importar, politica);
		}
		gravarWhere(clausulaWhere);
	}	
	
	private void gravarWhere(String clausulaWhere) {
		lerParametros();
		ProcessoAtualService processoAtualService = new ProcessoAtualService();
		ProcessoAtual processoAtual = processoAtualService.pesquisarPorChave(anoMes);
		processoAtual.setFiltroErp(clausulaWhere);
		dao.atualizar(processoAtual);
	}

	private String montarFiltro(String importar, String politica) {
		String clausulaWhere  = "";
		String filtroImportar = "";;
		String filtroPolitica  = "";

		if (importar != null) {
			if (importar.toLowerCase().equals("x") ) {
				filtroImportar =  ("IMPORTAR IS NULL") ;
			}
			else {
				filtroImportar =  ("IMPORTAR = '" + importar + "'") ;
			}
		}
		filtroPolitica = (politica != null) ? ("POLITICAS LIKE '%" + politica + "%'") : "";

		if (importar != null && politica != null) {
			clausulaWhere = filtroImportar + " AND " + filtroPolitica;
		} else {
			clausulaWhere = filtroImportar + filtroPolitica;
		}
		return clausulaWhere;
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}

}
