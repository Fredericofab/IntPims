package model.services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import db.DbException;
import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.entities.Folha;
import model.entities.VerbasFolha;
import model.exceptions.TxtIntegridadeException;

public class ImportarFolhaService {

	private FolhaService folhaService = new FolhaService();
	private FolhaSumarizadaService folhaSumarizadaService = new FolhaSumarizadaService();
	private VerbasFolhaService verbasDaFolhaService = new VerbasFolhaService();
	private PimsGeralService pimsGeralService = new PimsGeralService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	parametros
	String usuarioPimsCS;
	String entrada;
	String anoMes;
	String naoImportar;
	String arqEntradaDelimitador;

	List<Folha> lista;
	Set<VerbasFolha> setVerbas;
	Integer qtdeLidas = 0;
	Integer qtdeNaoImportadas = 0;
	Integer qtdeCorrompidas = 0;
	Integer qtdeVerbasDistintas = 0;
	Integer qtdeVerbasSemDefinicao = 0;
	Integer qtdeDeletadas = 0;
	Integer qtdeIncluidas = 0;

	public String getEntrada() {
		return entrada;
	}
	public List<Folha> getLista() {
		return lista;
	}
	public Set<VerbasFolha> getSetVerbas() {
		return setVerbas;
	}
	public Integer getQtdeLidas() {
		return qtdeLidas;
	}
	public Integer getQtdeNaoImportadas() {
		return qtdeNaoImportadas;
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

	public void processarTXT() {
		try {
			lerParametros();
			deletarTodosFolha();
			deletarTodosFolhaSumarizada();
			lerFolhaTXT(entrada, anoMes);
			if (setVerbas.size() > 0) {
				qtdeVerbasDistintas = setVerbas.size();
				gravarVerbasNovas();
			}
			contarVerbasSemDefinicao();
			if (qtdeVerbasSemDefinicao == 0 && qtdeCorrompidas == 0) {
				incluirDescCC();
				gravarDadosFolha();
			}
		} catch (DbException e) {
			Alertas.mostrarAlertas("Erro Banco Oracle", "Processo Cancelado", e.getMessage(),
					AlertType.ERROR);
		}
		
		if ((qtdeIncluidas > 0) && (qtdeLidas - qtdeIncluidas - qtdeNaoImportadas) == 0) {
			processoAtualService.atualizarEtapa("ImportarFolha", "S");
		} else {
			processoAtualService.atualizarEtapa("ImportarFolha", "N");
		}
		processoAtualService.atualizarEtapa("SumarizarFolha", "N");
		processoAtualService.atualizarEtapa("ExportarFolha", "N");
	}
	
	private void deletarTodosFolhaSumarizada() {
		folhaSumarizadaService.deletarTodos();
	}
	private void deletarTodosFolha() {
		qtdeDeletadas = folhaService.deletarTodos();
	}

	private void lerFolhaTXT(String entrada, String anoMesReferencia) {
		String linha = null;
		lista = new ArrayList<Folha>();
		setVerbas = new HashSet<>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(entrada),"UTF-8"))) {
			linha = br.readLine();
			while (linha != null) {
				qtdeLidas = qtdeLidas + 1;
				Folha dadosFolha = null;
				dadosFolha = converteRegistro(linha, anoMesReferencia, qtdeLidas);
				if (dadosFolha != null) {
					if (naoImportar.indexOf(dadosFolha.getTipoVerba()) >= 0) {
						qtdeNaoImportadas = qtdeNaoImportadas + 1;
					}
					else {
						lista.add(dadosFolha);
						VerbasFolha verbaFolha = new VerbasFolha(dadosFolha.getCodVerba(), dadosFolha.getDescVerba(),
								dadosFolha.getTipoVerba(), null, null);
						setVerbas.add(verbaFolha);
					}
				}
				linha = br.readLine();
			}
		} catch (TxtIntegridadeException e) {
			Alertas.mostrarAlertas("Erro de Integridade no Arquivo", "Processo de Leitura do TXT interrompido",
					e.getMessage(), AlertType.ERROR);
		} catch (FileNotFoundException e) {
			Alertas.mostrarAlertas("Arquivo não encontrado", "Processo Cancelado",
					e.getMessage(), AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao Dados da Folha",
					e.getMessage(), AlertType.ERROR);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private void gravarVerbasNovas() {
		for (VerbasFolha verbaFolha : setVerbas) {
			if (verbasDaFolhaService.pesquisarPorChave(verbaFolha.getCodVerba()) == null) {
				verbasDaFolhaService.salvarOuAtualizar(verbaFolha);
			}
		}
	}
	private void contarVerbasSemDefinicao() {
		qtdeVerbasSemDefinicao = verbasDaFolhaService.contarVerbasSemDefinicao();
	}

	private void incluirDescCC() {
		for (Folha dadosFolha : lista) {
			if (dadosFolha.getDescCentroCustos() == null || dadosFolha.getDescCentroCustos().equals("")) {
				String descCC =  pimsGeralService.descricaoCCustos(dadosFolha.getCodCentroCustos(), usuarioPimsCS);
				if ( descCC != null ) {
					dadosFolha.setDescCentroCustos(descCC);
				}
			}	
		}
	}

	
	private void gravarDadosFolha() {
		VerbasFolha verbaFolha;
		qtdeIncluidas = 0;
		Double codVerba;
		for (Folha dadosFolha : lista) {
			codVerba = dadosFolha.getCodVerba();
			verbaFolha = verbasDaFolhaService.pesquisarPorChave(codVerba);
			if (verbaFolha != null) {
				dadosFolha.setImportar(verbaFolha.getImportar());
				dadosFolha.setConsiderarReferencia(verbaFolha.getConsiderarReferencia());
				Boolean atualizarEtapaDoProcesso = false;
				folhaService.salvarOuAtualizar(dadosFolha, atualizarEtapaDoProcesso);
				qtdeIncluidas = qtdeIncluidas + 1;
			}
		}
	}

	private Folha converteRegistro(String linha, String anoMesReferencia, Integer numeroLinha) {
		String[] campos = linha.split(arqEntradaDelimitador);
		if ( numeroLinha == 1 ) {
			campos[0] = Utilitarios.excluiCaracterNaoEditavel(campos[0], 6);
		}
		Optional<ButtonType> continuar = null;
		try {
			if (campos.length == 8) {
				String anoMes = campos[0];
				Double codCentroCustos = Double.parseDouble(campos[1]);
				String descCentroCustos = campos[2];
				Double codVerba = Double.parseDouble(campos[3]);
				String descVerba = campos[4];
				String tipoVerba = campos[5];
				Double referenciaVerba = Double.parseDouble(campos[6]);
				Double valorVerba = Double.parseDouble(campos[7]);
				String importar = null;
				String considerarReferencia = null;
				String observacao = null;
				if (!anoMes.equals(anoMesReferencia)) {
					throw new TxtIntegridadeException("Registro nao Coerente com o Mês de Referencia");
				}
				Folha dadosFolha = new Folha(anoMes, codCentroCustos, descCentroCustos, codVerba, descVerba, valorVerba,
						referenciaVerba, tipoVerba, importar, considerarReferencia, observacao);
				return dadosFolha;
			} else {
				throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado (8)");
			}
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
		} catch (RuntimeException e) {
			qtdeCorrompidas = qtdeCorrompidas + 1;
			continuar = Alertas.mostrarConfirmacao("RuntimeException", "Erro na Importacao Dados da Folha",
					e.getClass() + "\n" + e.getMessage(), AlertType.CONFIRMATION);
		} finally {
			if ((continuar != null) && (continuar.get() == ButtonType.CANCEL)) {
				throw new TxtIntegridadeException("Processo Interrompido pelo usuário");
			}
		}
		return null;
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS  = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqEntradaPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqEntradaPasta")).getValor();
		String arqEntradaTipo = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqEntradaTipo")).getValor();
		entrada = arqEntradaPasta + "Folha" + anoMes + arqEntradaTipo;
		naoImportar = (parametrosService.pesquisarPorChave("ImportarFolha", "NaoImportar")).getValor();
		arqEntradaDelimitador = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqEntradaDelimitador")).getValor();
	}
}
