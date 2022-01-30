package model.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.entities.Folha;
import model.entities.FolhaSumarizada;

public class SumarizarFolhaService {
	
	private ParametrosService parametrosService = new ParametrosService();
	private FolhaSumarizadaService folhaSumarizadaService = new FolhaSumarizadaService();
	private FolhaService folhaService = new FolhaService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	Parametros
	String anoMes;

	List<Folha> lista;
	Map<Double, FolhaSumarizada> map;
	Integer qtdeLidas = 0;
	Integer qtdeCCustos = 0;
	Double valorExportarSim = 0.00;
	Double valorExportarNao = 0.00;
	Double referenciaExportarSim = 0.00;
	Double valorTotal = 0.00;

	public List<Folha> getLista() {
		return lista;
	}
	public Map<Double, FolhaSumarizada> getMap() {
		return map;
	}
	public Integer getQtdeLidas() {
		return qtdeLidas;
	}
	public Integer getQtdeCCustos() {
		return qtdeCCustos;
	}
	public Double getValorTotal() {
		return valorTotal;
	}
	public Double getValorExportarSim() {
		return valorExportarSim;
	}
	public Double getValorExportarNao() {
		return valorExportarNao;
	}
	public Double getReferenciaExportarSim() {
		return referenciaExportarSim;
	}

	public void processar() {
		lerParametros();
		deletarTodosFolhaSumarizada();
		sumarizarFolha();
		gravarSumarioFolha();
		processoAtualService.atualizarEtapa("SumarizarFolha","S");
		processoAtualService.atualizarEtapa("ExportarFolha","N");
		processoAtualService.atualizarEtapa("VerbaAlterada","N");
		processoAtualService.atualizarEtapa("FolhaAlterada","N");
	}

	private void deletarTodosFolhaSumarizada() {
		folhaSumarizadaService.deletarTodos();
	}
	
	private void sumarizarFolha() {
		lista = folhaService.pesquisarTodos();
		Double codCCusto;
		map = new HashMap<Double, FolhaSumarizada>();
		for (Folha dadosFolha : lista) {
			qtdeLidas = qtdeLidas + 1;

			codCCusto = dadosFolha.getCodCentroCustos();

			FolhaSumarizada sumarioFolha = new FolhaSumarizada();
			if (map.containsKey(codCCusto)) {
				sumarioFolha = map.get(codCCusto);
			} else {
				sumarioFolha.setAnoMes(dadosFolha.getAnoMes());
				sumarioFolha.setCodCentroCustos(dadosFolha.getCodCentroCustos());
				sumarioFolha.setDescCentroCustos(dadosFolha.getDescCentroCustos());
				sumarioFolha.setQdteImportarSim(0);
				sumarioFolha.setTotalImportarSim(0.00);
				sumarioFolha.setQdteImportarNao(0);
				sumarioFolha.setTotalImportarNao(0.00);
				sumarioFolha.setTotalReferenciaSim(0.00);
				qtdeCCustos = qtdeCCustos + 1;
				
			}

			if (dadosFolha.getImportar().equals("S")) {
				sumarioFolha.setQdteImportarSim(sumarioFolha.getQdteImportarSim() + 1);
				sumarioFolha.setTotalImportarSim(sumarioFolha.getTotalImportarSim() + dadosFolha.getValorVerba());
				valorExportarSim = valorExportarSim + dadosFolha.getValorVerba();
				if (dadosFolha.getConsiderarReferencia().equals("S")) {
					sumarioFolha.setTotalReferenciaSim(sumarioFolha.getTotalReferenciaSim() + dadosFolha.getReferenciaVerba());
				}
			} else {
				sumarioFolha.setQdteImportarNao(sumarioFolha.getQdteImportarNao() + 1);
				sumarioFolha.setTotalImportarNao(sumarioFolha.getTotalImportarNao() + dadosFolha.getValorVerba());
				valorExportarNao = valorExportarNao + dadosFolha.getValorVerba();
			}
			map.put(codCCusto, sumarioFolha);
		}
		valorTotal = valorExportarSim + valorExportarNao;
	}

	private void gravarSumarioFolha() {
		for (Double chave : map.keySet()) {
			folhaSumarizadaService.salvarOuAtualizar(map.get(chave));
		}
	}

	private void lerParametros() {
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}
}
