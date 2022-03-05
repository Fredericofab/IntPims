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
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.ProcessoAtual;
import model.services.AplicarPoliticasErpService;
import model.services.ErpService;
import model.services.ExportarFolhaService;
import model.services.FatorMedidaService;
import model.services.FolhaService;
import model.services.FolhaSumarizadaService;
import model.services.FuncionariosService;
import model.services.FuncionariosSumarizadosService;
import model.services.MainViewService;
import model.services.ParametrosService;
import model.services.PoliticasErpService;
import model.services.ProcessoAtualService;
import model.services.VerbasFolhaService;

public class MainViewController implements Initializable {

	ProcessoAtualService processoAtualService = new ProcessoAtualService();
	ProcessoAtual processoAtual;

//	parametros
	String anoMes;

	@FXML
	private MenuBar menuBarPrincipal;

	@FXML
	private Menu menuProcessos;
	@FXML
	private MenuItem menuItemProcessoAtual;
	@FXML
	private CheckMenuItem menuItemImportarFolha;
	@FXML
	private CheckMenuItem menuItemSumarizarFolha;
	@FXML
	private CheckMenuItem menuItemExportarFolha;
	@FXML
	private CheckMenuItem menuItemImportarFuncionarios;
	@FXML
	private CheckMenuItem menuItemSumarizarFuncionarios;
	@FXML
	private CheckMenuItem menuItemImportarErpMT;
	@FXML
	private CheckMenuItem menuItemImportarErpCD;
	@FXML
	private CheckMenuItem menuItemImportarErpDG;
	@FXML
	private CheckMenuItem menuItemValidarErp;
	@FXML
	private CheckMenuItem menuItemAplicarPoliticasErp;
	@FXML
	private CheckMenuItem menuItemExportarErp;
	@FXML
	private MenuItem menuItemSair;

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
	private Menu menuTabelas;
	@FXML
	private MenuItem menuItemVerbaFolha;
	@FXML
	private MenuItem menuItemFatorMedida;
	@FXML
	private MenuItem menuItemPoliticasErp;
	@FXML
	private MenuItem menuItemParametros;

	@FXML
	private Menu menuAjuda;
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	private Menu menuSair;
	@FXML
	private MenuItem menuItemSair2;

	@FXML
	private void onMenuItemProcessoAtualAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ProcessoAtualView.fxml", "Controle do Processo Atual",
				paiStage, x -> {
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
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo", e.getMessage(),
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
	private void onMenuItemImportarErpMTAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ImportarErpView.fxml", "Importação Dados do ErpMT (Requisicao de Materiais)", paiStage,
		(ImportarErpViewController controle) -> {
			controle.setOrigem("MT");
		});

	}
	@FXML
	private void onMenuItemImportarErpCDAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ImportarErpView.fxml", "Importação Dados do ErpCD (Compra Direta)", paiStage,
				(ImportarErpViewController controle) -> {
					controle.setOrigem("CD");
		});
	}
	@FXML
	private void onMenuItemImportarErpDGAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ImportarErpView.fxml", "Importação Dados do ErpDG (Despesas Gerais)", paiStage,
				(ImportarErpViewController controle) -> {
					controle.setOrigem("DG");
		});
	}
	@FXML
	private void onMenuItemValidarErpAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ValidarErpView.fxml", "Validação dos dados importados do ERP", paiStage, x -> {
		});
	}
	@FXML
	private void onMenuItemAplicarPoliticasErpAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/AplicarPoliticasErpView.fxml", "Aplicar Politicas do ERP", paiStage,
				(AplicarPoliticasErpViewController controle) -> {
					controle.setAplicarPoliticaErpServico(new AplicarPoliticasErpService());
					controle.atualizarTableView();
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
	private void onMenuItemFatorMedidaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/FatorMedidaList.fxml", "Cadastro dos Fatores de Conversao das Medidas", paiStage,
				(FatorMedidaListController controle) -> {
					controle.setFatorMedidaServico(new FatorMedidaService());
					controle.atualizarTableView();
				});
	}
	@FXML
	private void onMenuItemPoliticasErpAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/PoliticasErpList.fxml", "Politicas do ERP", paiStage,
				(PoliticasErpListController controle) -> {
					controle.setPoliticasErpServico(new PoliticasErpService());
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
		lerParametros();
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
	
	private <T> void criarJanelaFilha(String caminhoDaView, String titulo, Stage paiStage,
			Consumer<T> acaoDeInicializacao) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			Pane pane = loader.load();

			T controller = loader.getController();
			acaoDeInicializacao.accept(controller);

			Stage janelaFilhaStage = new Stage();
			janelaFilhaStage.setTitle(titulo);
			janelaFilhaStage.setScene(new Scene(pane)); 
			janelaFilhaStage.setResizable(true); 
			janelaFilhaStage.initOwner(paiStage); 
			janelaFilhaStage.initModality(Modality.WINDOW_MODAL); 
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
	
	@FXML
	private void onMenuProcessosShown() {
		processoAtual = processoAtualService.pesquisarPorChave(anoMes);
		habilitarEtapas();
		checkProcessos();
	}
	private void habilitarEtapas() {
		if (processoAtual == null) {
			// Primeira utilizacao do sistema
			menuItemImportarFolha.setDisable(true);
			menuItemSumarizarFolha.setDisable(true);
			menuItemExportarFolha.setDisable(true);
			menuItemImportarFuncionarios.setDisable(true);
			menuItemSumarizarFuncionarios.setDisable(true);
			menuItemImportarErpMT.setDisable(true);
			menuItemImportarErpCD.setDisable(true);
			menuItemImportarErpDG.setDisable(true);
			menuItemValidarErp.setDisable(true);
			menuItemAplicarPoliticasErp.setDisable(true);
			menuItemExportarErp.setDisable(true);
		}
		else {
			menuItemImportarFolha.setDisable(false);
			menuItemImportarFuncionarios.setDisable(false);
			menuItemImportarErpMT.setDisable(false);
			menuItemImportarErpCD.setDisable(false);
			menuItemImportarErpDG.setDisable(false);

			menuItemSumarizarFolha.setDisable(processoAtual.getImportarFolha().equals("S") ? false : true);
			menuItemExportarFolha.setDisable(processoAtual.getSumarizarFolha().equals("S") ? false : true);
			menuItemSumarizarFuncionarios.setDisable(processoAtual.getImportarFuncionario().equals("S") ? false : true);
			if (processoAtual.getImportarErpMT().equals("S") &&
				processoAtual.getImportarErpCD().equals("S") &&
				processoAtual.getImportarErpDG().equals("S")) {
				menuItemValidarErp.setDisable(false);
			}
			else {
				menuItemValidarErp.setDisable(true);
			}
			menuItemAplicarPoliticasErp.setDisable(processoAtual.getValidarErp().equals("S") ? false : true);
			menuItemExportarErp.setDisable(processoAtual.getAplicarPoliticaErp().equals("S") ? false : true);
		}
	}
	private void checkProcessos() {
		if (processoAtual != null) {
			menuItemImportarFolha.setSelected(processoAtual.getImportarFolha().equals("S"));
			menuItemSumarizarFolha.setSelected(processoAtual.getSumarizarFolha().equals("S"));
			menuItemExportarFolha.setSelected(processoAtual.getExportarFolha().equals("S"));
			menuItemImportarFuncionarios.setSelected(processoAtual.getImportarFuncionario().equals("S"));
			menuItemSumarizarFuncionarios.setSelected(processoAtual.getSumarizarFuncionario().equals("S"));
			menuItemImportarErpMT.setSelected(processoAtual.getImportarErpMT().equals("S"));
			menuItemImportarErpCD.setSelected(processoAtual.getImportarErpCD().equals("S"));
			menuItemImportarErpDG.setSelected(processoAtual.getImportarErpDG().equals("S"));
			menuItemValidarErp.setSelected(processoAtual.getValidarErp().equals("S"));
			menuItemAplicarPoliticasErp.setSelected(processoAtual.getAplicarPoliticaErp().equals("S"));
			menuItemExportarErp.setSelected(processoAtual.getExportarErp().equals("S"));
		}
	}
	
	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}
}
