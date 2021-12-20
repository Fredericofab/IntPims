package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DadosAlteradosListener;
import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.VerbasFolha;
import model.services.ParametrosService;
import model.services.VerbasFolhaService;

public class VerbasFolhaListController implements Initializable, DadosAlteradosListener {

	private VerbasFolhaService servico;
	private ParametrosService parametrosService = new ParametrosService();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagExcluir;

	@FXML
	private TableView<VerbasFolha> tableViewVerbaFolha;
	@FXML
	private TableColumn<VerbasFolha, Double> tableColumnCodVerba;
	@FXML
	private TableColumn<VerbasFolha, String> tableColumnDescVerba;
	@FXML
	private TableColumn<VerbasFolha, String> tableColumnImportar;
	@FXML
	private TableColumn<VerbasFolha, VerbasFolha> tableColumnEDIT;
	@FXML
	private TableColumn<VerbasFolha, VerbasFolha> tableColumnREMOVE;
	
	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btSair;

	private ObservableList<VerbasFolha> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		System.out.println("onBtIncluirAction");
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/VerbaFolhaForm.fxml";
		VerbasFolha entidade = new VerbasFolha();
		criarDialogoForm(entidade, caminhoDaView, parentStage);
	}
	@FXML
	public void onGerarTxtAction(ActionEvent evento) {
		Boolean oficial = false;
		servico.gerarTxt(oficial);
	}
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	public void setVerbaFolhaServico(VerbasFolhaService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		inicializarComponentes();
	}
	
	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("VerbasDaFolha", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("VerbasDaFolha", "FlagAlterar")).getValor().toUpperCase();
		flagExcluir = (parametrosService.pesquisarPorChave("VerbasDaFolha", "FlagExcluir")).getValor().toUpperCase();
	}

	private void inicializarComponentes() {
		tableColumnCodVerba.setCellValueFactory(new PropertyValueFactory<>("codVerba"));
		tableColumnDescVerba.setCellValueFactory(new PropertyValueFactory<>("descVerba"));
		tableColumnImportar.setCellValueFactory(new PropertyValueFactory<>("importar"));
		
		Utilitarios.formatarTableColumnDouble(tableColumnCodVerba, 0);

		tableColumnCodVerba.setStyle("-fx-alignment: CENTER-RIGHT");
		
		btIncluir.setDisable((flagIncluir.equals("N") ? true : false));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<VerbasFolha> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewVerbaFolha.setItems(obsLista);

		if (flagAlterar.equals("S")) {
			initEditButtons();
		}
		if (flagExcluir.equals("S")) {
			initRemoveButtons();
		}
	}

	private void criarDialogoForm(VerbasFolha entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();

			VerbasFolhaFormController controller = loader.getController();
			controller.setVerbaFolha(entidade);
			controller.setVerbaFolhaService(new VerbasFolhaService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Verbas da Folha");
			dialogoStage.setScene(new Scene(pane));
			dialogoStage.setResizable(false);
			dialogoStage.initOwner(parentStage);
			dialogoStage.initModality(Modality.WINDOW_MODAL);
			dialogoStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alertas.mostrarAlertas("IOException", "Erro carregando View", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDadosAlterados() {
		atualizarTableView();
	}

	private void initEditButtons() {
		// codigo adaptado da material do curso Udemy
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<VerbasFolha, VerbasFolha>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(VerbasFolha obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/VerbaFolhaForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<VerbasFolha, VerbasFolha>() {
			private final Button button = new Button("Excluir");


			@Override
			protected void updateItem(VerbasFolha obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(VerbasFolha objeto) {
		Optional<ButtonType> clicado = Alertas.mostrarConfirmacao("Confirmacao", null, "Tem certeza da delecao?", AlertType.CONFIRMATION);
		if (clicado.get() == ButtonType.OK) {
			if (servico == null) {
				throw new IllegalStateException("O service foi passado nulo pelo outro programador!");
			}
			try {
				servico.remover(objeto);
				atualizarTableView();
				
			} catch (DbException e) {
				Alertas.mostrarAlertas("Erro removendo Objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
