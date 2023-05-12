package model.services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import db.DbException;
import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.entities.Erp;
import model.exceptions.ParametroInvalidoException;
import model.exceptions.TxtIntegridadeException;

public class ImportarErpService {

	private ErpService erpService = new ErpService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	parametros
	String entrada;
	String anoMes;
	String naoImportar;
	String arqEntradaDelimitador;
	String arqEntradaFormatoData;

	List<Erp> lista;
	Integer qtdeLidas = 0;
	Integer qtdeCorrompidas = 0;
	Integer qtdeDeletadas = 0;
	Integer qtdeIncluidas = 0;
	Integer qtdeNaoImportadas = 0;

	String origem;
	Integer sequencial;
	String tipoMovimento;
	Double codCentroCustos;
	String descCentroCustos;
	String codContaContabil;
	String descContaContabil;
	String codNatureza;
	String codMaterial;
	String descMovimento;
	String unidadeMedida;
	Double quantidade;
	Double precoUnitario;
	Double valorMovimento;
	String manfroOS;
	String frotaOuCC;
	String documentoErp;
	SimpleDateFormat sdf;
	Date dataMovimento;

	String sobreporPoliticas;
	String importar;
	String observacao;
	String validacoes;
	String politicas;
	String salvarOS_Material;
	String salvarCstg_IntVM;
	String salvarCstg_IntCM;
	String salvarCstg_IntDG;
	
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
	public Integer getQtdeNaoImportadas() {
		return qtdeNaoImportadas;
	}

	public void processarTXT(String origem) {
		try { 
			this.origem = origem;
			lerParametros();
			deletarPorOrigem(origem);
			sequencial = erpService.ultimoSequencial();
			lerErptxt(entrada, anoMes, origem);
			if (qtdeCorrompidas == 0) {
				gravarDadosErp();
			}
		} catch (DbException e) {
			Alertas.mostrarAlertas("Erro Banco Oracle", "Processo Cancelado", e.getMessage(),AlertType.ERROR);
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado", e.getMessage(),AlertType.ERROR);
		}

		if ((qtdeIncluidas > 0) && (qtdeLidas - qtdeIncluidas) == 0) {
			processoAtualService.atualizarEtapa("ImportarErp" + origem, "S");
		} else {
			processoAtualService.atualizarEtapa("ImportarErp" + origem, "N");
		}
		processoAtualService.atualizarEtapa("ValidarErp", "N");
		processoAtualService.atualizarEtapa("AplicarPoliticaErp", "N");
		processoAtualService.atualizarEtapa("ExportarErp", "N");
	}

	private void deletarPorOrigem(String origem) {
		qtdeDeletadas = erpService.deletarOrigem(origem);
	}

	private void lerErptxt(String entrada, String anoMesReferencia, String origem) {
		String linha = null;
		lista = new ArrayList<Erp>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(entrada),"UTF-8"))) {
			linha = br.readLine();
			while (linha != null) {
				qtdeLidas = qtdeLidas + 1;
				Erp dadosErp = null;
				dadosErp = converteRegistro(linha, anoMesReferencia, qtdeLidas);
				if (dadosErp != null) {
					if ((origem.equals("RM") || origem.equals("ED"))  &&
						(naoImportar.indexOf(dadosErp.getTipoMovimento()) >= 0) ){
						qtdeNaoImportadas = qtdeNaoImportadas + 1;
					} 
					else {
						lista.add(dadosErp);
					}
				}	
				linha = br.readLine();
			}
		} catch (TxtIntegridadeException e) {
			Alertas.mostrarAlertas("Erro de Integridade no Arquivo", "Processo de Leitura do TXT interrompido",
					e.getMessage(),	AlertType.ERROR);
		} catch (FileNotFoundException e) {
			Alertas.mostrarAlertas("Arquivo não encontrado " + origem, "Processo Cancelado",
					e.getMessage(), AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao Dados Erp" + origem,
					e.getMessage(),	AlertType.ERROR);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private void gravarDadosErp() {
		qtdeIncluidas = 0;
		for (Erp dadosErp : lista) {
			dadosErp.setSobreporPoliticas("N");
			Boolean atualizarEtapaDoProcesso = false;
			erpService.salvarOuAtualizar(dadosErp, atualizarEtapaDoProcesso);
			qtdeIncluidas = qtdeIncluidas + 1;
		}
	}

	private Erp converteRegistro(String linha, String anoMesReferencia, Integer numeroLinha) {
		String[] campos = linha.split(arqEntradaDelimitador);
		if ( numeroLinha == 1 ) {
			campos[0] = Utilitarios.excluiCaracterNaoEditavel(campos[0], 6);
		}
		Optional<ButtonType> continuar = null;
		try {
			if (origem.equals("RM")) { 	converteRegistroRM(campos); }
			if (origem.equals("ED")) { 	converteRegistroED(campos); }
			if (origem.equals("DF")) { 	converteRegistroDF(campos); }

			if (!anoMes.equals(anoMesReferencia)) {
				throw new TxtIntegridadeException("Registro nao Coerente com o Mês de Referencia");
			}
			Erp dadosErp = new Erp(origem, tipoMovimento, anoMes, codCentroCustos, descCentroCustos, codContaContabil,
					descContaContabil, codNatureza, codMaterial, descMovimento, unidadeMedida, quantidade, precoUnitario,
					valorMovimento, manfroOS, frotaOuCC, documentoErp, dataMovimento, sobreporPoliticas, importar, observacao, validacoes, politicas,
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
			continuar = Alertas.mostrarConfirmacao("Erro de Integridade no Arquivo",
					"Continuar Processo de Leitura do TXT ?",
					"Campo numerico esperado. \n \n" + e.getMessage() + "\n \n" +
					"na linha No.: " + numeroLinha.toString() + "\n" + "\n" + linha,
					AlertType.CONFIRMATION);
		} catch (ParseException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas.mostrarConfirmacao("Erro de Integridade no Arquivo",
					"Continuar Processo de Leitura do TXT ?",
					"Formato da Data errado. Esperado " + arqEntradaFormatoData + " \n \n" + e.getMessage() 	+ "\n \n" +
					"na linha No.: " + numeroLinha.toString() + "\n" + "\n" + linha,
					AlertType.CONFIRMATION);
		} catch (RuntimeException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas.mostrarConfirmacao("RuntimeException", "Erro na Importacao Dados Erp " + origem,
					e.getClass() + "\n" + e.getMessage(), AlertType.CONFIRMATION);
		} finally {
			if ((continuar != null) && (continuar.get() == ButtonType.CANCEL)) {
				throw new TxtIntegridadeException("Processo Interrompido pelo usuário");
			}
		}
		return null;
	}

	private void converteRegistroRM(String[] campos) throws ParseException {
		if (campos.length == 17) {
			tipoMovimento = campos[0];
			documentoErp = campos[1];
			anoMes = campos[2];
			codCentroCustos = Double.parseDouble(campos[3]);
			descCentroCustos = campos[4];
			codMaterial = campos[5];
			descMovimento = campos[6];
			unidadeMedida = campos[7];
			quantidade = Double.parseDouble(campos[8]);
			precoUnitario = Double.parseDouble(campos[9]);
			valorMovimento = Double.parseDouble(campos[10]);
			codContaContabil = campos[11];
			descContaContabil = campos[12];
			manfroOS = campos[13];
			frotaOuCC = campos[14];
			codNatureza = campos[15];
			sdf = new SimpleDateFormat(arqEntradaFormatoData);
			dataMovimento = (Date) sdf.parse(campos[16]);
			sequencial = sequencial + 1;
		} else {
			throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado 17 recebido " + campos.length );
		}
	}

	private void converteRegistroED(String[] campos) throws ParseException {
		if (campos.length == 17) {
			tipoMovimento = campos[0];
			documentoErp = campos[1];
			anoMes = campos[2];
			codCentroCustos = Double.parseDouble(campos[3]);
			descCentroCustos = campos[4];
			codMaterial = campos[5];
			descMovimento = campos[6];
			unidadeMedida = campos[7];
			quantidade = Double.parseDouble(campos[8]);
			precoUnitario = Double.parseDouble(campos[9]);
			valorMovimento = Double.parseDouble(campos[10]);
			codContaContabil = campos[11];
			descContaContabil = campos[12];
			manfroOS = campos[13];
			frotaOuCC = campos[14];
			codNatureza = campos[15];
			sdf = new SimpleDateFormat(arqEntradaFormatoData);
			dataMovimento = (Date) sdf.parse(campos[16]);
			sequencial = sequencial + 1;
		} else {
			throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado 17 recebido " + campos.length );
		}
	}

	private void converteRegistroDF(String[] campos) throws ParseException {
		if (campos.length == 12 || campos.length == 13 ) {
//			As vezes o usuário digita com um ";" no meio da descricao do movimento
			documentoErp = campos[0];
			codCentroCustos = Double.parseDouble(campos[1]);
			descCentroCustos = campos[2];
//			String naoUsadoCodDepto = campos[3];
			sdf = new SimpleDateFormat(arqEntradaFormatoData);
			dataMovimento = (Date) sdf.parse(campos[4]);
			codContaContabil = campos[5];
			descContaContabil = campos[6];
			valorMovimento = Double.parseDouble(campos[7]);
			manfroOS = campos[8];
			frotaOuCC = campos[9];
			descMovimento = ( campos.length == 12 ? campos[10] : campos[10] + "-" + campos[11] );
			anoMes = ( campos.length == 12 ? campos[11] : campos[12] );

			quantidade = 1.00;
			precoUnitario = valorMovimento;

			sequencial = sequencial + 1;
		} else {
			throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado (12 ou 13) recebido " + campos.length );
		}
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		arqEntradaFormatoData = (parametrosService.pesquisarPorChave("ArquivosTextos" , "ArqEntradaFormatoData")).getValor();
		String arqEntradaPasta = (parametrosService.pesquisarPorChave("ArquivosTextos" , "ArqEntradaPasta")).getValor();
		String arqEntradaTipo = (parametrosService.pesquisarPorChave("ArquivosTextos" , "ArqEntradaTipo")).getValor();
		entrada = arqEntradaPasta + "Erp" + origem + anoMes + arqEntradaTipo;
		naoImportar = (parametrosService.pesquisarPorChave("ImportarErp" , "NaoImportar" + origem)).getValor();
		arqEntradaDelimitador = (parametrosService.pesquisarPorChave("ArquivosTextos" , "ArqEntradaDelimitador")).getValor();
	}
}
