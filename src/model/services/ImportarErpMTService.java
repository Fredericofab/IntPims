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
import model.entities.Erp;
import model.exceptions.TxtIntegridadeException;

public class ImportarErpMTService {

	private ErpService ErpService = new ErpService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
		
//	parametros
	String entrada;
	String anoMes;
	
	List<Erp> lista;
	Integer qtdeLidas = 0;
	Integer qtdeCorrompidas = 0;
	Integer qtdeDeletadas = 0;
	Integer qtdeIncluidas = 0;
	String origem = "MT";


	public String getEntrada() {
		return entrada;
	}
	public List<Erp> getLista() {
		return lista;
	}
	public Integer getQtdeLidas() {
		return qtdeLidas;
	}
	public Integer getQtdeCorrompidas() {
		return qtdeCorrompidas;
	}
	public Integer getQtdeDeletadas() {
		return qtdeDeletadas;
	}
	public Integer getQtdeIncluidas() {
		return qtdeIncluidas;
	}

	public void processarTXT() {
		lerParametros();
		deletarPorOrigem();
		lerErpMTtxt(entrada, anoMes);
		if (qtdeCorrompidas == 0) {
			gravarDadosErp();
		}

		if ((qtdeLidas - qtdeIncluidas) == 0) {
			processoAtualService.atualizarEtapa("ImportarErpMT","S");
		}
		else {
			processoAtualService.atualizarEtapa("ImportarErpMT","N");
		}
		processoAtualService.atualizarEtapa("CriticarErp","N");
		processoAtualService.atualizarEtapa("ExportarErp","N");
	}

	private void deletarPorOrigem() {
		qtdeDeletadas = ErpService.deletarOrigem(origem);
	}
	
	private void lerErpMTtxt(String entrada, String anoMesReferencia) {
		String linha = null;
		lista = new ArrayList<Erp>();

		try (BufferedReader br = new BufferedReader(new FileReader(entrada))) {
			linha = br.readLine();
			while (linha != null) {
				qtdeLidas = qtdeLidas + 1;
				Erp dadosErp = null;
				dadosErp = converteRegistro(linha, anoMesReferencia,qtdeLidas);
				if (dadosErp != null) {
					lista.add(dadosErp);
				}
				linha = br.readLine();
			}
		} catch (TxtIntegridadeException e) {
			Alertas.mostrarAlertas("TxtIntegridadeException", "Processo de Leitura do TXT interrompido", e.getMessage(),
					AlertType.ERROR);
		} catch (FileNotFoundException e) {
			Alertas.mostrarAlertas("FileNotFoundException", "Erro na Importacao Dados ErpMT",
					"Arquivo não encontrado \n \n" + e.getMessage(), AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao Dados ErpMT", e.getMessage(), AlertType.ERROR);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}


	private void gravarDadosErp() {
		qtdeIncluidas = 0;
		for (Erp dadosErp : lista) {
			ErpService.salvarOuAtualizar(dadosErp);
			qtdeIncluidas = qtdeIncluidas + 1;
		}
	}

	private Erp converteRegistro(String linha, String anoMesReferencia, Integer numeroLinha) throws TxtIntegridadeException {
		String[] campos = linha.split(",");
		Optional<ButtonType> continuar = null;
		try {
			if (campos.length == 6) {
				String anoMes = campos[0];
				Double codCentroCustos = Double.parseDouble(campos[1]);
				String descCentroCustos = campos[2];
				String importar = null;
				String observacao = null;
				if (!anoMes.equals(anoMesReferencia)) {
					throw new TxtIntegridadeException("Registro nao Coerente com o Mês de Referencia");
				}
				Erp dadosErp = new Erp(anoMes, codCentroCustos, descCentroCustos, 
						 importar, observacao);
				return dadosErp;
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
			continuar = Alertas.mostrarConfirmacao("RuntimeException", "Erro na Importacao Dados ErpMT",
					e.getClass() + "\n" + e.getMessage(), AlertType.CONFIRMATION);
//			e.printStackTrace();
		} finally {
			if ((continuar != null) && (continuar.get() == ButtonType.CANCEL)) {
				throw new TxtIntegridadeException("Processo Interrompido pelo usuário");
			}
		}
		return null;
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqEntradaPasta = (parametrosService.pesquisarPorChave("ImportarErpMT", "ArqEntradaPasta")).getValor();
		String arqEntradaNome  = (parametrosService.pesquisarPorChave("ImportarErpMT", "ArqEntradaNome")).getValor();
		String arqEntradaTipo  = (parametrosService.pesquisarPorChave("ImportarErpMT", "ArqEntradaTipo")).getValor();
		entrada = arqEntradaPasta + arqEntradaNome + anoMes + arqEntradaTipo ;
	}
}
