package model.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.entities.DadosFolha;
import model.entities.SumarioFolha;

public class SumarizarFolhaService {
	
//	Parametros
	String anoMes;

	List<DadosFolha> lista;
	Map<String, SumarioFolha> map;
	Integer qtdeLidas = 0;
	Integer qtdeCCustos = 0;;
	Double valorExportarSim = 0.00;
	Double valorExportarNao = 0.00;
	Double valorTotal = 0.00;

	public List<DadosFolha> getLista() {
		return lista;
	}
	public Map<String, SumarioFolha> getMap() {
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

	public void processar() {
		lerParametros();
		deletarSumarioFolhaTodos();
		sumarizarFolha();
		gravarSumarioFolha();
	}

	private void deletarSumarioFolhaTodos() {
		ImportarFolhaService importarFolhaService = new ImportarFolhaService();
		importarFolhaService.deletarSumarioFolhaTodos();
	}

	private void sumarizarFolha() {
		DadosFolhaService dadosFolhaService = new DadosFolhaService();
		lista = dadosFolhaService.pesquisarTodos();
		String codCCusto;
		map = new HashMap<String, SumarioFolha>();
		for (DadosFolha dadosFolha : lista) {
			qtdeLidas = qtdeLidas + 1;
			;
			codCCusto = dadosFolha.getCodCentroCustos();

			SumarioFolha sumarioFolha = new SumarioFolha();
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
				qtdeCCustos = qtdeCCustos + 1;
				;
			}

			if (dadosFolha.getImportar().equals("S")) {
				sumarioFolha.setQdteImportarSim(sumarioFolha.getQdteImportarSim() + 1);
				sumarioFolha.setTotalImportarSim(sumarioFolha.getTotalImportarSim() + dadosFolha.getValorVerba());
				valorExportarSim = valorExportarSim + dadosFolha.getValorVerba();
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
		SumarioFolhaService sumarioFolhaService = new SumarioFolhaService();
		for (String chave : map.keySet()) {
			sumarioFolhaService.salvarOuAtualizar(map.get(chave));
		}
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("AmbienteGeral", "AnoMes")).getValor();
	}
}
