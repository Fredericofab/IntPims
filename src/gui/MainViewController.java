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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.services.DadosFolhaService;
import model.services.ExportarFolhaService;
import model.services.ParametrosService;
import model.services.SumarioFolhaService;
import model.services.VerbaFolhaService;

public class MainViewController implements Initializable {

	@FXML
	private MenuBar menuBarPrincipal;

	@FXML
	private MenuItem menuItemControleProcesso;
	@FXML
	private MenuItem menuItemImportarFolha;
	@FXML
	private MenuItem menuItemImportarFuncionarios;
	@FXML
	private MenuItem menuItemSumarizarFolha;
	@FXML
	private MenuItem menuItemSumarizarFuncionarios;
	@FXML
	private MenuItem menuItemExportarFolha;
	@FXML
	private MenuItem menuItemSair;

	@FXML
	private MenuItem menuItemVerbaFolha;
	@FXML
	private MenuItem menuItemParametros;

	@FXML
	private MenuItem menuItemDadosFolha;
	@FXML
	private MenuItem menuItemFuncionarios;
	@FXML
	private MenuItem menuItemFolhaSumarizada;
	@FXML
	private MenuItem menuItemFuncionariosSumarizados;

	@FXML
	private MenuItem menuItemSobre;

	@FXML
	private void onMenuItemControleProcessoAction() {
		System.out.println("onMenuItemControleProcessoAction");
	}

	@FXML
	private void onMenuItemImportarFolhaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ImportarFolhaView.fxml", "Importação Dados da Folha de Pagamento", paiStage, x -> {
		});
	}

	@FXML
	private void onMenuItemImportarFuncionariosAction() {
		System.out.println("onMenuItemImportarFuncionariosAction");
	}

	@FXML
	private void onMenuItemSumarizarDadosFolhaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/SumarizarFolhaView.fxml", "Sumarização da Folha", paiStage, x -> {
		});
	}

	@FXML
	private void onMenuItemSumarizarFuncionariosAction() {
		System.out.println("onMenuItemSumarizarFuncionariosAction");
	}

	@FXML
	private void onMenuItemExportarDadosFolhaAction() {
		try {
			ExportarFolhaService servico = new ExportarFolhaService();
			servico.processar();
			Alertas.mostrarAlertas(null, "Registros gravados com Sucesso no CSTG_INTFP", null , AlertType.INFORMATION);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo CSTG_INTFP", e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	private void onMenuItemSairAction() {
		Stage paiStage = paiStage();
		paiStage.close();
	}

	@FXML
	private void onMenuItemVerbaFolhaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/VerbaFolhaList.fxml", "Cadastro de Verbas da Folha", paiStage,
				(VerbaFolhaListController controle) -> {
					controle.setVerbaFolhaServico(new VerbaFolhaService());
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
	private void onMenuItemDadosFolhaAction() {
		Stage paiStage = paiStage();

		criarJanelaFilha("/gui/DadosFolhaList.fxml", "Movimento Dados da Folha", paiStage,
				(DadosFolhaListController controle) -> {
					controle.setDadosFolhaServico(new DadosFolhaService());
					controle.atualizarTableView();
				});
	}

	@FXML
	private void onMenuItemFuncionariosAction() {
		System.out.println("onMenuItemFuncionariosAction");
	}

	@FXML
	private void onMenuItemFolhaSumarizadaAction() {
		Stage paiStage = paiStage();

		criarJanelaFilha("/gui/SumarioFolhaList.fxml", "Sumario do Movimento Dados da Folha", paiStage,
				(SumarioFolhaListController controle) -> {
					controle.setSumarioFolhaServico(new SumarioFolhaService());
					controle.atualizarTableView();
				});
	}

	@FXML
	private void onMenuItemFuncionariosSumarizadosAction() {
		System.out.println("onMenuItemFuncionariosSumarizadosAction");
	}

	@FXML
	private void onMenuItemSobreAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/SobreView.fxml", "Sobre", paiStage, x -> {
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
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
			janelaFilhaStage.setScene(new Scene(pane)); // a cena é extamente a view carregada
			janelaFilhaStage.setResizable(true); // pode ser redimencionada
			janelaFilhaStage.initOwner(paiStage); // quem é o pai dessa janela
			janelaFilhaStage.initModality(Modality.WINDOW_MODAL); // fica preso na janela
			janelaFilhaStage.showAndWait();

		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro carregando Stage (Janela)", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	private Stage paiStage() {
		Stage telaPrincial = (Stage) menuBarPrincipal.getScene().getWindow();
		return telaPrincial;
	}

}
