package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.SumarioFolhaService;
import model.services.VerbaFolhaService;

public class MainViewController implements Initializable {

	@FXML
	private MenuBar menuBarPrincipal;
	
	@FXML
	private MenuItem menuItemDadosFolha;
		
	@FXML
	private MenuItem menuItemSumarioFolha;
	

	@FXML
	private MenuItem menuItemVerbaFolha;
	
	@FXML
	private MenuItem menuItemImportarFolha;

	@FXML
	private MenuItem menuItemSair;

	@FXML
	private MenuItem menuItemSobre;
		
	@FXML
	private void onMenuItemDadosFolhaAction() {
		Stage paiStage = paiStage();
		
		criarJanelaFilha("/gui/DadosFolhaList.fxml","Movimento Dados da Folha", paiStage, (DadosFolhaListController controle) -> {
			controle.setDadosFolhaServico(new DadosFolhaService());
			controle.atualizarTableView();
		});
	}
	
	@FXML
	private void onMenuItemSumarioFolhaAction() {
		Stage paiStage = paiStage();
		
		criarJanelaFilha("/gui/SumarioFolhaList.fxml","Sumario do Movimento Dados da Folha", paiStage, (SumarioFolhaListController controle) -> {
			controle.setSumarioFolhaServico(new SumarioFolhaService());
			controle.atualizarTableView();
		});
	}

	@FXML
	private void onMenuItemVerbaFolhaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/VerbaFolhaList.fxml", "Cadastro de Verbas da Folha", paiStage, (VerbaFolhaListController controle) -> {
			controle.setVerbaFolhaServico(new VerbaFolhaService());
			controle.atualizarTableView();
		});
	}

	
	@FXML
	private void onMenuItemImportarFolhaAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/ImportarFolhaView.fxml", "Importação Dados da Folha de Pagamento", paiStage, x -> {});
	}
	
	@FXML
	private void onMenuItemSobreAction() {
		Stage paiStage = paiStage();
		criarJanelaFilha("/gui/SobreView.fxml", "Sobre", paiStage, x -> {});
	}

	@FXML
	private void onMenuItemSairAction() {
		Stage paiStage = paiStage();
		paiStage.close();
	}

	private Stage paiStage() {
		Stage telaPrincial  = (Stage) menuBarPrincipal.getScene().getWindow();
		return 	telaPrincial;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
	
//	private synchronized <T> void carregarView(String caminhoDaView, Consumer<T> acaoDeInicializacao) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
//			VBox novoVbox = loader.load();
//
// 			Scene mainScene = Main.getMainScene();
//			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
//
//			Node mainMenu = mainVBox.getChildren().get(0);
//			mainVBox.getChildren().clear();
//			mainVBox.getChildren().add(mainMenu);
//			mainVBox.getChildren().addAll(novoVbox.getChildren());
//				
//			T controller = loader.getController();
//			acaoDeInicializacao.accept(controller);
//
//			}
//			catch (IOException e) {
//				Alertas.mostrarAlertas("IOException", "Erro carregando a View",
//									   e.getMessage(), AlertType.ERROR);
//			e.printStackTrace();
//			}
//		}
	
	private <T> void criarJanelaFilha(String caminhoDaView, String titulo, Stage paiStage,  Consumer<T> acaoDeInicializacao) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			Pane pane = loader.load();
			
			T controller = loader.getController();
			acaoDeInicializacao.accept(controller);
			
			Stage janelaFilhaStage = new Stage();
			janelaFilhaStage.setTitle(titulo);
			janelaFilhaStage.setScene(new Scene(pane));				//a cena é extamente a view carregada
			janelaFilhaStage.setResizable(true);					//pode ser redimencionada
			janelaFilhaStage.initOwner(paiStage);					//quem é o pai dessa janela
			janelaFilhaStage.initModality(Modality.WINDOW_MODAL); 	//fica preso na janela
			janelaFilhaStage.showAndWait();

		}
		catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro carregando Stage (Janela)", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
}


