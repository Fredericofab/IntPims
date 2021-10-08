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
import model.entities.Folha;
import model.entities.VerbasFolha;
import model.exceptions.TxtIntegridadeException;

public class ImportarFolhaService {

	private FolhaService folhaService = new FolhaService();
	private FolhaSumarizadaService folhaSumarizadaService = new FolhaSumarizadaService();
	private VerbasFolhaService verbasDaFolhaService = new VerbasFolhaService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
		
//	parametros
	String entrada;
	String anoMes;
	
	List<Folha> lista;
	Set<VerbasFolha> setVerbas;
	Integer qtdeLidas = 0;
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
			gravarDadosFolha();
		}

		if ((qtdeIncluidas > 0) && (qtdeLidas - qtdeIncluidas) == 0) {
			processoAtualService.atualizarEtapa("ImportarFolha","S");
		}
		else {
			processoAtualService.atualizarEtapa("ImportarFolha","N");
		}
		processoAtualService.atualizarEtapa("SumarizarFolha","N");
		processoAtualService.atualizarEtapa("ExportarFolha","N");
		processoAtualService.atualizarEtapa("VerbaAlterada","N");
		processoAtualService.atualizarEtapa("FolhaAlterada","N");
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

		try (BufferedReader br = new BufferedReader(new FileReader(entrada))) {
			linha = br.readLine();
			while (linha != null) {
				qtdeLidas = qtdeLidas + 1;
				Folha dadosFolha = null;
				dadosFolha = converteRegistro(linha, anoMesReferencia,qtdeLidas);
				if (dadosFolha != null) {
					lista.add(dadosFolha);
					VerbasFolha verbaFolha = new VerbasFolha(dadosFolha.getCodVerba(), dadosFolha.getDescVerba(), null);
					setVerbas.add(verbaFolha);
				}
				linha = br.readLine();
			}
		} catch (TxtIntegridadeException e) {
			Alertas.mostrarAlertas("TxtIntegridadeException", "Processo de Leitura do TXT interrompido", e.getMessage(),
					AlertType.ERROR);
		} catch (FileNotFoundException e) {
			Alertas.mostrarAlertas("FileNotFoundException", "Erro na Importacao Dados da Folha",
					"Arquivo n�o encontrado \n \n" + e.getMessage(), AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao Dados da Folha", e.getMessage(), AlertType.ERROR);
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

	private void gravarDadosFolha() {
		VerbasFolha verbaFolha;
		qtdeIncluidas = 0;
		Double codVerba;
		for (Folha dadosFolha : lista) {
			codVerba = dadosFolha.getCodVerba();
			verbaFolha = verbasDaFolhaService.pesquisarPorChave(codVerba);
			dadosFolha.setImportar(verbaFolha.getImportar());
			folhaService.salvarOuAtualizar(dadosFolha);
			qtdeIncluidas = qtdeIncluidas + 1;
		}
	}

	private Folha converteRegistro(String linha, String anoMesReferencia, Integer numeroLinha) throws TxtIntegridadeException {
		String[] campos = linha.split(",");
		Optional<ButtonType> continuar = null;
		try {
			if (campos.length == 6) {
				String anoMes = campos[0];
				Double codCentroCustos = Double.parseDouble(campos[1]);
				String descCentroCustos = campos[2];
				Double codVerba = Double.parseDouble(campos[3]);
				String descVerba = campos[4];
				Double valorVerba = Double.parseDouble(campos[5]);
				String importar = null;
				String observacao = null;
				if (!anoMes.equals(anoMesReferencia)) {
					throw new TxtIntegridadeException("Registro nao Coerente com o M�s de Referencia");
				}
				Folha dadosFolha = new Folha(anoMes, codCentroCustos, descCentroCustos, codVerba, descVerba,
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
				throw new TxtIntegridadeException("Processo Interrompido pelo usu�rio");
			}
		}
		return null;
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqEntradaPasta = (parametrosService.pesquisarPorChave("ImportarFolha", "ArqEntradaPasta")).getValor();
		String arqEntradaNome  = (parametrosService.pesquisarPorChave("ImportarFolha", "ArqEntradaNome")).getValor();
		String arqEntradaTipo  = (parametrosService.pesquisarPorChave("ImportarFolha", "ArqEntradaTipo")).getValor();
		entrada = arqEntradaPasta + arqEntradaNome + anoMes + arqEntradaTipo ;
	}
}
