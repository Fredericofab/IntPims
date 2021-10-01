package model.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import gui.util.Alertas;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import model.entities.Funcionarios;
import model.entities.VerbasFolha;
import model.exceptions.TxtIntegridadeException;

public class ImportarFuncionariosService {

	private ParametrosService parametrosService = new ParametrosService();
	private FuncionariosService funcionariosService = new FuncionariosService();
	private FuncionariosSumarizadosService funcionariosSumarizadosService = new FuncionariosSumarizadosService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
		
//	prametros
	String entrada;
	String anoMes;
	
	List<Funcionarios> lista;
	Set<VerbasFolha> set;
	Integer qtdeLidas = 0;
	Integer qtdeCorrompidas = 0;
	Integer qtdeVerbasDistintas = 0;
	Integer qtdeVerbasSemDefinicao = 0;
	Integer qtdeDeletadas = 0;
	Integer qtdeIncluidas = 0;

	public String getEntrada() {
		return entrada;
	}
	public List<Funcionarios> getLista() {
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
		deletarTodosFuncionarios();
		deletarTodosSumarioFuncionarios();
		lerFuncionariosTXT(entrada, anoMes);
		if (qtdeCorrompidas == 0) {		
			gravarFuncionarios();
			processoAtualService.atualizarEtapa("ImportarFuncionario","S");
			}
		else {
			processoAtualService.atualizarEtapa("ImportarFuncionario","N");
			processoAtualService.atualizarEtapa("SumarizarFuncionario","N");
		}
	}

	private void deletarTodosSumarioFuncionarios() {
		funcionariosSumarizadosService.deletarTodos();
	}
	
	private void deletarTodosFuncionarios() {
		qtdeDeletadas = funcionariosService.deletarTodos();
	}
	
	private void lerFuncionariosTXT(String entrada, String anoMesReferencia) {
		String linha = null;
		lista = new ArrayList<Funcionarios>();

		try (BufferedReader br = new BufferedReader(new FileReader(entrada))) {
			linha = br.readLine();
			while (linha != null) {
				qtdeLidas = qtdeLidas + 1;
				Funcionarios funcionarios = null;
				funcionarios = converteRegistro(linha, anoMesReferencia,qtdeLidas);
				if (funcionarios != null) {
					lista.add(funcionarios);
				}
				linha = br.readLine();
			}
		} catch (TxtIntegridadeException e) {
			Alertas.mostrarAlertas("TxtIntegridadeException", "Processo de Leitura do TXT interrompido", e.getMessage(),
					AlertType.ERROR);
		} catch (FileNotFoundException e) {
			Alertas.mostrarAlertas("FileNotFoundException", "Erro na Importacao Dados dos Funcionarios",
					"Arquivo não encontrado \n \n" + e.getMessage(), AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao dos Funcionarios", e.getMessage(), AlertType.ERROR);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private void gravarFuncionarios() {
		qtdeIncluidas = 0;
		for (Funcionarios funcionarios : lista) {
			funcionariosService.salvarOuAtualizar(funcionarios);
			qtdeIncluidas = qtdeIncluidas + 1;
		}
	}

	private Funcionarios converteRegistro(String linha, String anoMesReferencia, Integer numeroLinha) throws TxtIntegridadeException {
		String[] campos = linha.split(",");
		Optional<ButtonType> continuar = null;
		try {
			if (campos.length == 5) {
				String anoMes = campos[0];
				Double codCentroCustos = Double.parseDouble(campos[1]);
				String descCentroCustos = campos[2];
				Double codFuncionario = Double.parseDouble(campos[3]);
				String descFuncionario = campos[4];
				if (!anoMes.equals(anoMesReferencia)) {
					throw new TxtIntegridadeException("Registro nao Coerente com o Mês de Referencia");
				}
				Funcionarios funcionarios = new Funcionarios(anoMes, codCentroCustos, descCentroCustos,
						codFuncionario, descFuncionario);
				return funcionarios;
			} else {
				throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado (5)");
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
			continuar = Alertas.mostrarConfirmacao("RuntimeException", "Erro na Importacao dados dos Funcionarios",
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
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqEntradaPasta = (parametrosService.pesquisarPorChave("ImportarFuncionarios", "ArqEntradaPasta")).getValor();
		String arqEntradaNome  = (parametrosService.pesquisarPorChave("ImportarFuncionarios", "ArqEntradaNome")).getValor();
		String arqEntradaTipo  = (parametrosService.pesquisarPorChave("ImportarFuncionarios", "ArqEntradaTipo")).getValor();
		entrada = arqEntradaPasta + arqEntradaNome + anoMes + arqEntradaTipo ;
	}
}
