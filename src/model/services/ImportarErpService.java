package model.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.entities.CriticaErp;
import model.entities.Erp;
import model.exceptions.TxtIntegridadeException;

public class ImportarErpService {

	private ErpService erpService = new ErpService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	parametros
	String entrada;
	String anoMes;
	String arqEntradaDelimitador;
	String arqEntradaFormatoData;

	String origem;
	Integer sequencial;
	List<Erp> lista;
	Integer qtdeLidas = 0;
	Integer qtdeCorrompidas = 0;
	Integer qtdeDeletadas = 0;
	Integer qtdeIncluidas = 0;
	
	Double codCentroCustos;
	String descCentroCustos;
	Double codContaContabil;
	String descContaContabil;
	Double codMaterial;
	String descMovimento;
	String unidadeMedida;
	Double quantidade;
	Double precoUnitario;
	Double valorMovimento;
	String referenciaOS;
	Double manfroOS;
	String documentoErp;
	SimpleDateFormat sdf;
	Date dataMovimento;

	String importar = " ";
	String observacao = "";
	String criticas = "";
	String salvarOS_Material = " ";
	String salvarCstg_IntVM = " ";
	String salvarCstg_IntCM = " ";
	String salvarCstg_IntDG = " ";


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

	public void processarTXT(String origem) {
		this.origem = origem;
		lerParametros();
		deletarPorOrigem(origem);
		sequencial = erpService.ultimoSequencial();
		lerErptxt(entrada, anoMes);
		if (qtdeCorrompidas == 0) {
			gravarDadosErp();
		}

		if ((qtdeIncluidas > 0) && (qtdeLidas - qtdeIncluidas) == 0) {
			processoAtualService.atualizarEtapa("ImportarErp" + origem, "S");
		} else {
			processoAtualService.atualizarEtapa("ImportarErp" + origem, "N");
		}
		processoAtualService.atualizarEtapa("CriticarErp", "N");
		processoAtualService.atualizarEtapa("ExportarErp", "N");
		apagarAnaliseAnteriores();
		zerarContadorDeCriticas();
	}

	private void deletarPorOrigem(String origem) {
		qtdeDeletadas = erpService.deletarOrigem(origem);
	}

	private void lerErptxt(String entrada, String anoMesReferencia) {
		String linha = null;
		lista = new ArrayList<Erp>();

		try (BufferedReader br = new BufferedReader(new FileReader(entrada))) {
			linha = br.readLine();
			while (linha != null) {
				qtdeLidas = qtdeLidas + 1;
				Erp dadosErp = null;
				dadosErp = converteRegistro(linha, anoMesReferencia, qtdeLidas);
				if (dadosErp != null) {
					lista.add(dadosErp);
				}
				linha = br.readLine();
			}
		} catch (TxtIntegridadeException e) {
			Alertas.mostrarAlertas("TxtIntegridadeException", "Processo de Leitura do TXT interrompido", e.getMessage(),
					AlertType.ERROR);
		} catch (FileNotFoundException e) {
			Alertas.mostrarAlertas("FileNotFoundException", "Erro na Importacao Dados Erp" + origem,
					"Arquivo não encontrado \n \n" + e.getMessage(), AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao Dados Erp" + origem, e.getMessage(),
					AlertType.ERROR);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private void gravarDadosErp() {
		qtdeIncluidas = 0;
		for (Erp dadosErp : lista) {
			erpService.salvarOuAtualizar(dadosErp);
			qtdeIncluidas = qtdeIncluidas + 1;
		}
	}

	private Erp converteRegistro(String linha, String anoMesReferencia, Integer numeroLinha)
			throws TxtIntegridadeException {
		String[] campos = linha.split(arqEntradaDelimitador);
		Optional<ButtonType> continuar = null;
		try {
			if (origem.equals("MT")) { 	converteRegistroMT(campos); }
			if (origem.equals("CD")) { 	converteRegistroCD(campos); }
			if (origem.equals("DG")) { 	converteRegistroDG(campos); }

			if (!anoMes.equals(anoMesReferencia)) {
				throw new TxtIntegridadeException("Registro nao Coerente com o Mês de Referencia");
			}
			Erp dadosErp = new Erp(origem, anoMes, codCentroCustos, descCentroCustos, codContaContabil,
					descContaContabil, codMaterial, descMovimento, unidadeMedida, quantidade, precoUnitario,
					valorMovimento, referenciaOS, manfroOS, documentoErp, dataMovimento, importar, observacao, criticas,
					salvarOS_Material, salvarCstg_IntVM, salvarCstg_IntCM, salvarCstg_IntDG, sequencial);
			return dadosErp;
		} catch (TxtIntegridadeException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas.mostrarConfirmacao("Erro de Integridade no Arquivo",
					"Continuar Processo de Leitura do TXT ?",
					e.getMessage() + "\n \n" + "na linha No.: " + numeroLinha.toString() + "\n" + "\n" + linha,
					AlertType.CONFIRMATION);
		} catch (NumberFormatException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas.mostrarConfirmacao("NumberFormatException", "Continuar Processo de Leitura do TXT ?",
					"Campo numerico esperado. \n" + e.getMessage() + "\n \n" + "na linha No.: " + numeroLinha.toString()
							+ "\n" + "\n" + linha,
					AlertType.CONFIRMATION);
		} catch (ParseException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas
					.mostrarConfirmacao("ParseException", "Continuar Processo de Leitura do TXT ?",
							"Formato da Data errado. Esperado " + arqEntradaFormatoData + " \n" + e.getMessage()
									+ "\n \n" + "na linha No.: " + numeroLinha.toString() + "\n" + "\n" + linha,
							AlertType.CONFIRMATION);
		} catch (RuntimeException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas.mostrarConfirmacao("RuntimeException", "Erro na Importacao Dados Erp" + origem,
					e.getClass() + "\n" + e.getMessage(), AlertType.CONFIRMATION);
		} finally {
			if ((continuar != null) && (continuar.get() == ButtonType.CANCEL)) {
				throw new TxtIntegridadeException("Processo Interrompido pelo usuário");
			}
		}
		return null;
	}

	private void converteRegistroMT(String[] campos) throws ParseException {
		if (campos.length == 15) {
			anoMes = campos[0];
			codCentroCustos = Double.parseDouble(campos[1]);
			descCentroCustos = campos[2];
			codContaContabil = Double.parseDouble(campos[3]);
			descContaContabil = campos[4];
			codMaterial = Double.parseDouble(campos[5]);
			descMovimento = campos[6];
			unidadeMedida = campos[7];
			quantidade = Double.parseDouble(campos[8]);
			precoUnitario = Double.parseDouble(campos[9]);
			valorMovimento = Double.parseDouble(campos[10]);
			referenciaOS = campos[11];
			manfroOS = Double.parseDouble(campos[12]);
			documentoErp = campos[13];
			sdf = new SimpleDateFormat(arqEntradaFormatoData);

			dataMovimento = (Date) sdf.parse(campos[14]);
			sequencial = sequencial + 1;
		} else {
			throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado (15)");
		}
	}

	private void converteRegistroCD(String[] campos) throws ParseException {
		if (campos.length == 15) {
			anoMes = campos[0];
			codCentroCustos = Double.parseDouble(campos[1]);
			descCentroCustos = campos[2];
			codContaContabil = Double.parseDouble(campos[3]);
			descContaContabil = campos[4];
			codMaterial = Double.parseDouble(campos[5]);
			descMovimento = campos[6];
			unidadeMedida = campos[7];
			quantidade = Double.parseDouble(campos[8]);
			precoUnitario = Double.parseDouble(campos[9]);
			valorMovimento = Double.parseDouble(campos[10]);
			referenciaOS = campos[11];
			manfroOS = Double.parseDouble(campos[12]);
			documentoErp = campos[13];
			sdf = new SimpleDateFormat(arqEntradaFormatoData);

			dataMovimento = (Date) sdf.parse(campos[14]);
			sequencial = sequencial + 1;
		} else {
			throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado (15)");
		}
	}

	private void converteRegistroDG(String[] campos) throws ParseException {
		if (campos.length == 13) {
			anoMes = campos[0];
			codCentroCustos = Double.parseDouble(campos[1]);
			descCentroCustos = campos[2];
			codContaContabil = Double.parseDouble(campos[3]);
			descContaContabil = campos[4];
			descMovimento = campos[5];
			quantidade = Double.parseDouble(campos[6]);
			precoUnitario = Double.parseDouble(campos[7]);
			valorMovimento = Double.parseDouble(campos[8]);
			referenciaOS = campos[9];
			manfroOS = Double.parseDouble(campos[10]);
			documentoErp = campos[11];
			sdf = new SimpleDateFormat(arqEntradaFormatoData);

			dataMovimento = (Date) sdf.parse(campos[12]);
			sequencial = sequencial + 1;
	
			codMaterial = 0.00;
			unidadeMedida = "";

			
		} else {
			throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado (13)");
		}
	}

	private void zerarContadorDeCriticas() {
		CriticaErpService criticasErpService = new CriticaErpService();
		List<CriticaErp> lista = criticasErpService.pesquisarTodos();
		for (CriticaErp criticasErp : lista) {
			criticasErp.setAnoMesAnalisado(null);
			criticasErp.setRegistrosAnalisados(0);
			criticasErp.setRegistrosLiberados(0);
			criticasErp.setRegistrosIgnorados(0);
			criticasErp.setRegistrosPendentes(0);
			criticasErpService.salvarOuAtualizar(criticasErp);
		}
	}

	private void apagarAnaliseAnteriores() {
		List<Erp> lista = erpService.pesquisarTodos();	
		for (Erp erp : lista ) {
			erp.setImportar(null);
			erp.setSalvarOS_Material(null);
			erp.setSalvarCstg_IntVM(null);
			erp.setSalvarCstg_IntCM(null);
			erp.setSalvarCstg_IntDG(null);
			erpService.salvarOuAtualizar(erp);
		}
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		arqEntradaDelimitador = (parametrosService.pesquisarPorChave("ImportarErp" + origem, "ArqEntradaDelimitador"))
				.getValor();
		arqEntradaFormatoData = (parametrosService.pesquisarPorChave("ImportarErp" + origem, "ArqEntradaFormatoData"))
				.getValor();
		String arqEntradaPasta = (parametrosService.pesquisarPorChave("ImportarErp" + origem, "ArqEntradaPasta"))
				.getValor();
		String arqEntradaNome = (parametrosService.pesquisarPorChave("ImportarErp" + origem, "ArqEntradaNome"))
				.getValor();
		String arqEntradaTipo = (parametrosService.pesquisarPorChave("ImportarErp" + origem, "ArqEntradaTipo"))
				.getValor();
		entrada = arqEntradaPasta + arqEntradaNome + anoMes + arqEntradaTipo;
	}
}
