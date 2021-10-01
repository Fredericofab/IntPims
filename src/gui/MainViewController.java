package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import db.DbException;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.ProcessoAtual;
import model.services.ErpService;
import model.services.ExportarFolhaService;
import model.services.FolhaService;
import model.services.FolhaSumarizadaService;
import model.services.FuncionariosService;
import model.services.FuncionariosSumarizadosService;
import model.services.MainViewService;
import model.services.ParametrosService;
import model.services.ProcessoAtualService;
import model.services.VerbasFolhaService;

public class MainViewController implements Initializable {

//	parametros
	String anoMes;

	@FXML
	private MenuBar menuBarPrincipal;

	@FXML
	private Menu menuProcessos;
	@FXML
	private MenuItem menuItemProcessoAtual;
	@FXML
	private MenuItem menuItemImportarFolha;
	@FXML
	private MenuItem menuItemSumarizarFolha;
	@FXML
	private MenuItem menuItemExportarFolha;
	@FXML
	private MenuItem menuItemImportarFuncionarios;
	@FXML
	private MenuItem menuItemSumarizarFuncionarios;
	@FXML
	private MenuItem menuItemImportarErp;
	@FXML
	private MenuItem menuItemAnalisarErp;
	@FXML
	private MenuItem menuItemExportarErp;
	
	@FXML
	private Menu menuTabelas;
	@FXML
	private MenuItem menuItemVerbaFolha;
	@FXML
	private MenuItem menuItemParametros;
	@FXML
	private MenuItem menuItemCriticasErp;
	
	@FXML
	private Menu menuMovimentos;
	@FXML
	private MenuItem menuItemFolha;
	@FXML
	private MenuItem menuItemFolhaSumarizada;
	@FXML
	private MenuItem menuItemFuncionarios;
	@FXML
	private MenuItem menuItemFuncionariosSumarizados;
	@FXML
	private MenuItem menuItemErp;

	@FXML
	private Menu menuAjuda;
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	private Menu menuSair;
	@FXML
	private MenuItem menuItemSair;

	@FXML
	private void onMenuProcessosShown() {
		habilitarEtapas();
	}
	@FXML
	private void onMenuItemProcessoAtualAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ProcessoAtualView.fxml", "Controle do Processo Atual", paiStage, x -> {
		});
	}
	@FXML
	private void onMenuItemImportarFolhaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ImportarFolhaView.fxml", "Importação Dados da Folha de Pagamento", paiStage, x -> {
		});
	}
	@FXML
	private void onMenuItemSumarizarFolhaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/SumarizarFolhaView.fxml", "Sumarização da Folha", paiStage, x -> {
		});
	}
	@FXML
	private void onMenuItemExportarFolhaAction() {
		try {
			ExportarFolhaService servico = new ExportarFolhaService();
			servico.processar();
			Alertas.mostrarAlertas(null, "Registros gravados com Sucesso no CSTG_INTFP", null, AlertType.INFORMATION);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo CSTG_INTFP", e.getMessage(),
					AlertType.ERROR);
		}
	}
	@FXML
	private void onMenuItemImportarFuncionariosAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ImportarFuncionariosView.fxml", "Importação Funcionarios Cadastrados", paiStage, x -> {
		});
	}
	@FXML
	private void onMenuItemSumarizarFuncionariosAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/SumarizarFuncionariosView.fxml", "Sumarização dos Funcionarios", paiStage, x -> {
		});
	}

	@FXML
	private void onMenuItemImportarErpAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ImportarErpView.fxml", "Importação Dados do ERP", paiStage, x -> {
		});
	}
	@FXML
	private void onMenuItemAnalisarErpAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/AnalisarErpView.fxml", "Analise do ERP", paiStage, x -> {
		});
	}
	@FXML
	private void onMenuItemExportarErpAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ExportarErpView.fxml", "Exportação dos Dados do ERP", paiStage, x -> {
		});
	}

	
	@FXML
	private void onMenuItemVerbaFolhaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/VerbaFolhaList.fxml", "Cadastro de Verbas da Folha", paiStage,
				(VerbasFolhaListController controle) -> {
					controle.setVerbaFolhaServico(new VerbasFolhaService());
					controle.atualizarTableView();
				});
	}
	@FXML
	private void onMenuItemParametrosAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ParametrosList.fxml", "Parametros do Sistema", paiStage,
				(ParametrosListController controle) -> {
					controle.setParametrosServico(new ParametrosService());
					controle.atualizarTableView();
				});
	}
	@FXML
	private void onMenuItemCriticasErpAction() {
//		Stage paiStage = paiStage();
//		criarJanelaFilha("/gui/CriticasErpList.fxml", "Criticas do ERP", paiStage,
//				(CriticasErpListController controle) -> {
//					controle.setCriticasErpServico(new CriticasErpService());
//					controle.atualizarTableView();
//				});
	}
	
	@FXML
	private void onMenuItemFolhaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/FolhaList.fxml", "Movimento Dados da Folha", paiStage,
				(FolhaListController controle) -> {
					controle.setFolhaServico(new FolhaService());
					controle.atualizarTableView();
				});
	}
	@FXML
	private void onMenuItemFolhaSumarizadaAction() {
		Stage paiStage = paiStage();

		criarJanelaFilha("/gui/FolhaSumarizadaList.fxml", "Sumario do Movimento Dados da Folha", paiStage,
				(FolhaSumarizadaListController controle) -> {
					controle.setFolhaSumarizadaServico(new FolhaSumarizadaService());
					controle.atualizarTableView();
				});
	}
	@FXML
	private void onMenuItemFuncionariosAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/FuncionariosList.fxml", "Cadastro de Funcionarios", paiStage,
				(FuncionariosListController controle) -> {
					controle.setFuncionariosServico(new FuncionariosService());
					controle.atualizarTableView();
				});
	}
	@FXML
	private void onMenuItemFuncionariosSumarizadosAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/FuncionariosSumarizadosList.fxml", "Sumario do Funcionarios", paiStage,
				(FuncionariosSumarizadosListController controle) -> {
					controle.setFuncionariosSumarizadosServico(new FuncionariosSumarizadosService());
					controle.atualizarTableView();
				});
	}
	@FXML
	private void onMenuItemErpAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ErpList.fxml", "Movimento Dados do ERP", paiStage,
				(ErpListController controle) -> {
					controle.setErpServico(new ErpService());
					controle.atualizarTableView();
				});
	}

	@FXML
	private void onMenuItemSobreAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/SobreView.fxml", "Sobre", paiStage, x -> {
		});
	}
	@FXML
	private void onMenuItemSairAction() {
		Stage paiStage = paiStage();
		paiStage.close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		testarAcessoBanco();
	}

	private void testarAcessoBanco() {
		try {
			MainViewService servico = new MainViewService();
			servico.testarAcessoBanco();
		}
		catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro no Acesso ao Banco Oracle", e.getMessage(), AlertType.ERROR);
			menuProcessos.setDisable(true);
			menuTabelas.setDisable(true);
			menuMovimentos.setDisable(true);
		}
	}
	private void habilitarEtapas() {
		lerParametros();
		ProcessoAtual processoAtual = pegarProcessoAtual();
		menuItemSumarizarFolha.setDisable(processoAtual.getImportarFolha().equals("S") ? false : true);
		menuItemExportarFolha.setDisable(processoAtual.getSumarizarFolha().equals("S") ? false : true);
		menuItemSumarizarFuncionarios.setDisable(processoAtual.getImportarFuncionario().equals("S") ? false : true);
	}
	private ProcessoAtual pegarProcessoAtual() {
		ProcessoAtual processoAtual = new ProcessoAtual();
		processoAtual.setAnoMes(anoMes);
		ProcessoAtualService processoAtualService = new ProcessoAtualService();
		return processoAtualService.pesquisarPorChave(processoAtual);
	}
	private <T> void criarJanelaFilha(String caminhoDaView, String titulo, Stage paiStage,
			Consumer<T> acaoDeInicializacao) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			Pane pane = loader.load();

			T controller = loader.getController();
			acaoDeInicializacao.accept(controller);

			Stage janelaFilhaStage = new Stage();
			janelaFilhaStage.setTitle(titulo);
			janelaFilhaStage.setScene(new Scene(pane)); // a cena é exatamente a view carregada
			janelaFilhaStage.setResizable(true); // pode ser redimensionada
			janelaFilhaStage.initOwner(paiStage); // quem é o pai dessa janela
			janelaFilhaStage.initModality(Modality.WINDOW_MODAL); // fica preso na janela
			janelaFilhaStage.showAndWait();

		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro carregando Stage (Janela)", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	private Stage paiStage() {
		Stage telaPrincipal = (Stage) menuBarPrincipal.getScene().getWindow();
		return telaPrincipal;
	}
	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}
}
