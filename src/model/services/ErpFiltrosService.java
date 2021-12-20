package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.FabricaDeDao;
import model.dao.ProcessoAtualDao;
import model.entities.ProcessoAtual;

public class ErpFiltrosService {

	private ProcessoAtualDao dao = FabricaDeDao.criarProcessoAtualDao();

	String anoMes;

	public List<String> criarListaCamposOracle() {
		List<String> listaCamposOracle = new ArrayList<String>();
		listaCamposOracle.add("Origem da Importacao		ORIGEM ");
		listaCamposOracle.add("Ano e Mes de Referencia	ANO_MES ");
		listaCamposOracle.add("Codigo Centro de Custos	COD_CENTRO_CUSTOS ");
		listaCamposOracle.add("Descricao Centro de Custos	DESC_CENTRO_CUSTOS ");
		listaCamposOracle.add("Codigo Conta Contabil		COD_CONTA_CONTABIL ");
		listaCamposOracle.add("Descricao Conta Contabil	DESC_CONTA_CONTABIL ");
		listaCamposOracle.add("Codigo Material			COD_MATERIAL ");
		listaCamposOracle.add("Desc.  Material / Movimento	DESC_MOVIMENTO ");	
		listaCamposOracle.add("Unidade de Medida			UNIDADE_MEDIDA ");
		listaCamposOracle.add("Quantidade				QUANTIDADE ");
		listaCamposOracle.add("Preco Unitario				PRECO_UNITARIO ");
		listaCamposOracle.add("Valor do Movimento		VALOR_MOVIMENTO ");
		listaCamposOracle.add("Referencia da O.S			REFERENCIA_OS ");
		listaCamposOracle.add("Numero da O.S.			NUMERO_OS ");
		listaCamposOracle.add("Documento no ERP			DOCUMENTO_ERP ");
		listaCamposOracle.add("Data Movimento			DATA_MOVIMENTO ");
		listaCamposOracle.add("Flag Importar				IMPORTAR ");
		listaCamposOracle.add("Observacao				OBSERVACAO ");
		listaCamposOracle.add("Criticas					CRITICAS ");
		listaCamposOracle.add("Flag Salvar Material na OS	SALVAR_OS_MATERIAL ");
		listaCamposOracle.add("Flag Salvar Valor do Material	SALVAR_CSTG_INTVM ");
		listaCamposOracle.add("Flag Salvar Consumo Material	SALVAR_CSTG_INTCM ");
		listaCamposOracle.add("Flag Salvar Despesas Gerais	SALVAR_CSTG_INTDG ");
		listaCamposOracle.add("Numero do Registro		SEQUENCIAL ");
		return listaCamposOracle;
	}
	
	public void salvarFiltro(String importar, String critica, String filtro) {
		String clausulaWhere;
		if (filtro != null) {
			clausulaWhere = filtro;
		} else {
			clausulaWhere = montarFiltro(importar, critica);
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

	private String montarFiltro(String importar, String critica) {
		String clausulaWhere  = "";
		String filtroImportar = "";;
		String filtroCritica  = "";

		if (importar != null) {
			if (importar.toLowerCase().equals("x") ) {
				filtroImportar =  ("IMPORTAR IS NULL") ;
			}
			else {
				filtroImportar =  ("IMPORTAR = '" + importar + "'") ;
			}
		}
		filtroCritica = (critica != null) ? ("CRITICAS LIKE '%" + critica + "%'") : "";

		if (importar != null && critica != null) {
			clausulaWhere = filtroImportar + " AND " + filtroCritica;
		} else {
			clausulaWhere = filtroImportar + filtroCritica;
		}
		return clausulaWhere;
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}

}
