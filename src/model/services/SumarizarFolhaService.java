package model.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.dao.FabricaDeDao;
import model.dao.ImportarFolhaDao;
import model.entities.DadosFolha;
import model.entities.SumarioFolha;
import model.entities.VerbaFolha;

public class SumarizarFolhaService {

	private ImportarFolhaDao dao = FabricaDeDao.criarImportarFolhaDao();

	List<DadosFolha> lista;
	Set<VerbaFolha> set;
	Map<String, SumarioFolha> map;
	Integer qtdeLidas = 0;
	Integer qtdeCorrompidas = 0;
	Integer qtdeVerbasDistintas = 0;
	Integer qtdeVerbasSemDefinicao = 0;
	Integer qtdeDeletadas = 0;
	Integer qtdeIncluidas = 0;
	Integer qtdeSumarioDeletadas = 0;
	Integer qtdeSumarioIncluidas  = 0;

	public List<DadosFolha> getLista() {
		return lista;
	}
	public Set<VerbaFolha> getSet() {
		return set;
	}
	public Map<String, SumarioFolha> getMap() {
		return map;
	}

	public Integer getQtdeLidas() {
		return qtdeLidas;
	}
	public Integer getQtdeCorrompidas() {
		return qtdeCorrompidas;
	}
	public Integer getqtdeVerbasDistintas() {
		return qtdeVerbasDistintas;
	}
	public Integer getQtdeVerbasSemDefinicao() {
		return qtdeVerbasSemDefinicao;
	}
	public Integer getQtdeDeletadas() {
		return qtdeDeletadas;
	}
	public Integer getQtdeIncluidas() {
		return qtdeIncluidas;
	}
	public Integer getQtdeSumarioDeletadas() {
		return qtdeDeletadas;
	}
	public Integer getQtdeSumarioIncluidas() {
		return qtdeIncluidas;
	}

	public void processar() {
		String AnoMes = "Fred";
			deletarSumarioFolhaAnoMes(AnoMes);
			sumarizarFolha();
			gravarSumarioFolha();
	}


	private void deletarSumarioFolhaAnoMes(String anoMes) {
		qtdeSumarioDeletadas = dao.deletarSumarioFolhaAnoMes(anoMes);
	}
	

	private void gravarSumarioFolha() {
		SumarioFolhaService sumarioFolhaService = new SumarioFolhaService();
		qtdeSumarioIncluidas = 0;
		for ( String chave : map.keySet() ) {
			sumarioFolhaService.salvarOuAtualizar(map.get(chave));
			qtdeSumarioIncluidas = qtdeSumarioIncluidas + 1;
		}
	}

	private void sumarizarFolha() {
		String codCCusto;
		map = new HashMap<String, SumarioFolha>();
		for (DadosFolha dadosFolha : lista) {
			
			codCCusto = dadosFolha.getCodCentroCustos();
			
			SumarioFolha sumarioFolha = new SumarioFolha();
			if (map.containsKey(codCCusto)) {
				sumarioFolha = map.get(codCCusto);
			}
			else {
				sumarioFolha.setAnoMes(dadosFolha.getAnoMes());
				sumarioFolha.setCodCentroCustos(dadosFolha.getCodCentroCustos());
				sumarioFolha.setDescCentroCustos(dadosFolha.getDescCentroCustos());
				sumarioFolha.setQdteImportarSim(0);
				sumarioFolha.setTotalImportarSim(0.00);
				sumarioFolha.setQdteImportarNao(0);
				sumarioFolha.setTotalImportarNao(0.00);
			}
			
			if (dadosFolha.getImportar().equals("S")) {
				sumarioFolha.setQdteImportarSim(sumarioFolha.getQdteImportarSim() + 1);
				sumarioFolha.setTotalImportarSim(sumarioFolha.getTotalImportarSim() + dadosFolha.getValorVerba());
			}
			else {
				sumarioFolha.setQdteImportarNao(sumarioFolha.getQdteImportarNao() + 1);
				sumarioFolha.setTotalImportarNao(sumarioFolha.getTotalImportarNao() + dadosFolha.getValorVerba());
			}
			map.put(codCCusto, sumarioFolha);
		}
	}


}
