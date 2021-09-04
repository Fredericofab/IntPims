package model.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.FabricaDeDao;
import model.dao.ImportarFolhaDao;
import model.entities.DadosFolha;
import model.entities.SumarioFolha;
import model.entities.VerbaFolha;

public class ImportarFolhaService {

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

	public void processarTXT(String entrada, String anoMes) {
		alimentarColecoes(entrada, anoMes);
		if (set.size() > 0) {
			qtdeVerbasDistintas = set.size();
			gravarVerbasNovas();
		}
		contarVerbasSemDefinicao();
		if (qtdeVerbasSemDefinicao == 0 && qtdeCorrompidas == 0) {
			deletarDadosFolhaAnoMes(anoMes);
			gravarDadosFolha();
			sumarizarFolha();
			deletarSumarioFolhaAnoMes(anoMes);
			gravarSumarioFolha();
		}
	}


	private void deletarDadosFolhaAnoMes(String anoMes) {
		qtdeDeletadas = dao.deletarDadosFolhaAnoMes(anoMes);
	}
	private void deletarSumarioFolhaAnoMes(String anoMes) {
		qtdeSumarioDeletadas = dao.deletarSumarioFolhaAnoMes(anoMes);
	}
	
	private void gravarVerbasNovas() {
		VerbaFolhaService servico = new VerbaFolhaService();
		for (VerbaFolha verbaFolha : set) {
			if (servico.pesquisarPorChave(verbaFolha.getCodVerba()) == null) {
				servico.salvarOuAtualizar(verbaFolha);
			}
		}
	}

	private void gravarDadosFolha() {
		DadosFolhaService dadosFolhaService = new DadosFolhaService();
		VerbaFolhaService verbaFolhaService = new VerbaFolhaService();
		VerbaFolha verbaFolha;
		qtdeIncluidas = 0;
		String codVerba;
		for (DadosFolha dadosFolha : lista) {
			codVerba = dadosFolha.getCodVerba();
			verbaFolha = verbaFolhaService.pesquisarPorChave(codVerba);
			dadosFolha.setImportar(verbaFolha.getImportar());
			dadosFolhaService.salvarOuAtualizar(dadosFolha);
			qtdeIncluidas = qtdeIncluidas + 1;
		}
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
				sumarioFolha.setQdteImportarSim(0.00);
				sumarioFolha.setTotalImportarSim(0.00);
				sumarioFolha.setQdteImportarNao(0.00);
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

	private void contarVerbasSemDefinicao() {
		qtdeVerbasSemDefinicao = dao.contarVerbasSemDefinicao();
	}


	private void alimentarColecoes(String entrada, String anoMesReferencia) {
		String linha = null;
		lista = new ArrayList<DadosFolha>();
		set = new HashSet<>();

		try (BufferedReader br = new BufferedReader(new FileReader(entrada))) {
			linha = br.readLine();

			while (linha != null) {
				qtdeLidas = qtdeLidas + 1;
				DadosFolha dadosFolha = null;
				dadosFolha = converteRegistro(linha, anoMesReferencia);
				if (dadosFolha != null) {
					lista.add(dadosFolha);
					VerbaFolha verbaFolha = new VerbaFolha(dadosFolha.getCodVerba(), dadosFolha.getDescVerba(), null);
					set.add(verbaFolha);
				}
				linha = br.readLine();
			}
		} catch (FileNotFoundException e) {
			Alertas.mostrarAlertas("FileNotFoundException", "Erro na Importacao Dados da Folha",
					"Arquivo não encontrado \n \n" + e.getMessage(),
					AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao Dados da Folha", e.getMessage(), AlertType.ERROR);
		} catch (RuntimeException e) {
			System.out.println("FRED");
			e.printStackTrace();
		}
	}

	private DadosFolha converteRegistro(String linha, String anoMesReferencia) {
		String[] campos = linha.split(",");
		try {
			if (campos.length == 6) {
				String anoMes = campos[0];
				String codCentroCustos = campos[1];
				String descCentroCustos = campos[2];
				String codVerba = campos[3];
				String descVerba = campos[4];
				Double valorVerba = Double.parseDouble(campos[5]);
				String importar = null;
				String observacao = null;
				if (!anoMes.equals(anoMesReferencia)) {
					qtdeCorrompidas = qtdeCorrompidas + 1;
					Integer numeroLinhaErro = lista.size();
					Alertas.mostrarAlertas("Integridade", "Registro nao Coerente com o Mês de Referencia",
							"AnoMes de Referencia = " + anoMesReferencia + "\n" +
							"AnoMes no Arquivo    = " + anoMes + "\n" +
							"na linha No.: " + numeroLinhaErro.toString() + "\n" + "\n" +
							linha,	AlertType.ERROR);
				}
				DadosFolha dadosFolha = new DadosFolha(anoMes, codCentroCustos, descCentroCustos, codVerba, descVerba,
						valorVerba, importar, observacao);
				return dadosFolha;
			} else {
				qtdeCorrompidas = qtdeCorrompidas + 1;
				Integer numeroLinhaErro = lista.size();
				Alertas.mostrarAlertas("Integridade", "Quantidade de Campos Diferente do Esperado (6) ",
						"Quantidade de Campos Encontrados: " + campos.length + "\n" + "na linha No.: "
								+ numeroLinhaErro.toString() + "\n" + "\n" + linha,
						AlertType.ERROR);
			}
		} catch (NumberFormatException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			Integer numeroLinhaErro = lista.size();
			Alertas.mostrarAlertas("RuntimeException", "Erro na Importacao Dados da Folha", 
					"Campo numerico esperado. \n" + e.getMessage() + "\n"
					+ "na linha No.: " + numeroLinhaErro.toString() + "\n" + "\n" + linha, AlertType.ERROR);
		} catch (RuntimeException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			Alertas.mostrarAlertas("RuntimeException", "Erro na Importacao Dados da Folha",
					e.getClass() + "\n" + e.getMessage() , AlertType.ERROR);
//			e.printStackTrace();
		} finally {

		}
		return null;
	}
}
