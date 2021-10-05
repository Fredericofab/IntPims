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
import model.entities.Erp;
import model.exceptions.TxtIntegridadeException;

public class ImportarErpMTService {

	private ErpService erpService = new ErpService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
		
//	parametros
	String entrada;
	String anoMes;
	String arqEntradaDelimitador;
	
	String origem;
	Integer sequencial = erpService.ultimoSequencial();
	List<Erp> lista;
	Integer qtdeLidas = 0;
	Integer qtdeCorrompidas = 0;
	Integer qtdeDeletadas = 0;
	Integer qtdeIncluidas = 0;


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

	private void deletarPorOrigem(String origem) {
		qtdeDeletadas = erpService.deletarOrigem(origem);
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
			Alertas.mostrarAlertas("FileNotFoundException", "Erro na Importacao Dados Erp" + origem,
					"Arquivo não encontrado \n \n" + e.getMessage(), AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao Dados Erp" + origem, e.getMessage(), AlertType.ERROR);
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

	private Erp converteRegistro(String linha, String anoMesReferencia, Integer numeroLinha) throws TxtIntegridadeException {
		String[] campos = linha.split(arqEntradaDelimitador);
		Optional<ButtonType> continuar = null;
		try {
			if (campos.length == 15) {
				String anoMes = campos[0];
				Double codCentroCustos = Double.parseDouble(campos[1]);
				String descCentroCustos = campos[2];
				Double codContaContabil = Double.parseDouble(campos[3]);
				String descContaContabil = campos[4];
				Double codMaterial = Double.parseDouble(campos[5]);
				String descMaterialDespesa = campos[6];
				String unidadeMedida = campos[7];
				Double quantidade = Double.parseDouble(campos[8]);
				Double precoUnitario = Double.parseDouble(campos[9]);
				Double valorMovimento = Double.parseDouble(campos[10]);
				String referenciaOS = campos[11];
				Double manfroOS = Double.parseDouble(campos[12]);
				String documentoErp = campos[13];
				Date dataMovimento;
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					dataMovimento = (Date) sdf.parse(campos[14]);
				} catch (ParseException e) {
					throw new TxtIntegridadeException("Formato da Data de Movimento tem de ser dd/mm/aaaa");
				}

				String importar = "S";
				String observacao = "";
				String criticas = "";
				String salvarOS_Material = "N";
				String salvarCstg_IntVM = "N";
				String salvarCstg_IntCM = "N";
				String salvarCstg_IntDG = "N";
				sequencial = sequencial + 1;
				
				if (!anoMes.equals(anoMesReferencia)) {
					throw new TxtIntegridadeException("Registro nao Coerente com o Mês de Referencia");
				}
				Erp dadosErp = new Erp(origem,anoMes,codCentroCustos,descCentroCustos, 
						 codContaContabil,descContaContabil, 
						codMaterial,descMaterialDespesa,unidadeMedida,
						 quantidade,precoUnitario,valorMovimento,
						referenciaOS,manfroOS,documentoErp,dataMovimento,importar,observacao,criticas,
						salvarOS_Material,salvarCstg_IntVM,salvarCstg_IntCM,salvarCstg_IntDG,sequencial);
				return dadosErp;
			} else {
				throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado (15)");
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
		arqEntradaDelimitador  = (parametrosService.pesquisarPorChave("ImportarErpMT", "ArqEntradaDelimitador")).getValor();
		String arqEntradaPasta = (parametrosService.pesquisarPorChave("ImportarErpMT", "ArqEntradaPasta")).getValor();
		String arqEntradaNome  = (parametrosService.pesquisarPorChave("ImportarErpMT", "ArqEntradaNome")).getValor();
		String arqEntradaTipo  = (parametrosService.pesquisarPorChave("ImportarErpMT", "ArqEntradaTipo")).getValor();
		entrada = arqEntradaPasta + arqEntradaNome + anoMes + arqEntradaTipo ;
	}
}
