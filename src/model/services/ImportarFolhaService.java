package model.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import gui.util.Alertas;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import model.dao.FabricaDeDao;
import model.dao.ProcessarFolhaDao;
import model.entities.DadosFolha;
import model.entities.VerbaFolha;
import model.exceptions.TxtIntegridadeException;

public class ImportarFolhaService {

	private ProcessarFolhaDao dao = FabricaDeDao.criarImportarFolhaDao();

	List<DadosFolha> lista;
	Set<VerbaFolha> set;
	Integer qtdeLidas = 0;
	Integer qtdeCorrompidas = 0;
	Integer qtdeVerbasDistintas = 0;
	Integer qtdeVerbasSemDefinicao = 0;
	Integer qtdeDeletadas = 0;
	Integer qtdeIncluidas = 0;

	public List<DadosFolha> getLista() {
		return lista;
	}

	public Set<VerbaFolha> getSet() {
		return set;
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

	public void processarTXT(String entrada, String anoMes) {
		deletarDadosFolhaAnoMes(anoMes);
		deletarSumarioFolhaAnoMes(anoMes);
		lerFolhaTXT(entrada, anoMes);
		if (set.size() > 0) {
			qtdeVerbasDistintas = set.size();
			gravarVerbasNovas();
		}
		contarVerbasSemDefinicao();
		if (qtdeVerbasSemDefinicao == 0 && qtdeCorrompidas == 0) {
			gravarDadosFolha();
		}
	}

	private void deletarDadosFolhaAnoMes(String anoMes) {
		qtdeDeletadas = dao.deletarDadosFolhaAnoMes(anoMes);
	}

	protected void deletarSumarioFolhaAnoMes(String anoMes) {
		dao.deletarSumarioFolhaAnoMes(anoMes);
	}

	private void lerFolhaTXT(String entrada, String anoMesReferencia) {
		String linha = null;
		lista = new ArrayList<DadosFolha>();
		set = new HashSet<>();

		try (BufferedReader br = new BufferedReader(new FileReader(entrada))) {
			linha = br.readLine();
			while (linha != null) {
				qtdeLidas = qtdeLidas + 1;
				DadosFolha dadosFolha = null;
				dadosFolha = converteRegistro(linha, anoMesReferencia,qtdeLidas);
				if (dadosFolha != null) {
					lista.add(dadosFolha);
					VerbaFolha verbaFolha = new VerbaFolha(dadosFolha.getCodVerba(), dadosFolha.getDescVerba(), null);
					set.add(verbaFolha);
				}
				linha = br.readLine();
			}
		} catch (TxtIntegridadeException e) {
			Alertas.mostrarAlertas("TxtIntegridadeException", "Processo de Leitura do TXT interrompido", e.getMessage(),
					AlertType.ERROR);
		} catch (FileNotFoundException e) {
			Alertas.mostrarAlertas("FileNotFoundException", "Erro na Importacao Dados da Folha",
					"Arquivo não encontrado \n \n" + e.getMessage(), AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao Dados da Folha", e.getMessage(), AlertType.ERROR);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private void gravarVerbasNovas() {
		VerbaFolhaService servico = new VerbaFolhaService();
		for (VerbaFolha verbaFolha : set) {
			if (servico.pesquisarPorChave(verbaFolha.getCodVerba()) == null) {
				servico.salvarOuAtualizar(verbaFolha);
			}
		}
	}

	private void contarVerbasSemDefinicao() {
		qtdeVerbasSemDefinicao = dao.contarVerbasSemDefinicao();
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

	private DadosFolha converteRegistro(String linha, String anoMesReferencia, Integer numeroLinha) throws TxtIntegridadeException {
		String[] campos = linha.split(",");
		Optional<ButtonType> continuar = null;
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
					throw new TxtIntegridadeException("Registro nao Coerente com o Mês de Referencia");
				}
				DadosFolha dadosFolha = new DadosFolha(anoMes, codCentroCustos, descCentroCustos, codVerba, descVerba,
						valorVerba, importar, observacao);
				return dadosFolha;
			} else {
				throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado (6)");
			}
		} catch (TxtIntegridadeException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas.mostrarConfirmacao("Erro de Integridade no Arquivo", "Continuar Processo de Leitura do TXT ?",
					e.getMessage() + "\n \n" +
					"na linha No.: " + numeroLinha.toString() + "\n" + "\n" + linha,
					AlertType.CONFIRMATION);
		} catch (NumberFormatException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas.mostrarConfirmacao("NumberFormatException", "Continuar Processo de Leitura do TXT ?", 
					"Campo numerico esperado. \n" + e.getMessage() + "\n \n" +
					"na linha No.: " + numeroLinha.toString() + "\n" + "\n" + linha,
					AlertType.CONFIRMATION);
		} catch (RuntimeException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas.mostrarConfirmacao("RuntimeException", "Erro na Importacao Dados da Folha",
					e.getClass() + "\n" + e.getMessage(), AlertType.CONFIRMATION);
//			e.printStackTrace();
		} finally {
			if ((continuar != null) && (continuar.get() == ButtonType.CANCEL)) {
				throw new TxtIntegridadeException("Processo Interrompido pelo usuário");
			}
		}
		return null;
	}
}
