package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import db.DbIntegridadeException;
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
import model.entities.VerbaFolha;
import model.services.VerbaFolhaService;

public class VerbaFolhaListController implements Initializable, DadosAlteradosListener {

	private VerbaFolhaService servico;

	@FXML
	private TableView<VerbaFolha> tableViewVerbaFolha;
	@FXML
	private TableColumn<VerbaFolha, String> tableColumnCodVerba;
	@FXML
	private TableColumn<VerbaFolha, String> tableColumnDescVerba;
	@FXML
	private TableColumn<VerbaFolha, String> tableColumnImportar;
	@FXML
	private TableColumn<VerbaFolha, VerbaFolha> tableColumnEDIT;
	@FXML
	private TableColumn<VerbaFolha, VerbaFolha> tableColumnREMOVE;
	
	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btSair;

	private ObservableList<VerbaFolha> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/VerbaFolhaForm.fxml";
		VerbaFolha entidade = new VerbaFolha();
		criarDialogoForm(entidade, caminhoDaView, parentStage);
	}

	@FXML
	public void onGerarTxtAction(ActionEvent evento) {
		servico.gerarTxt();
	}

	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	public void setVerbaFolhaServico(VerbaFolhaService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		tableColumnCodVerba.setCellValueFactory(new PropertyValueFactory<>("codVerba"));
		tableColumnDescVerba.setCellValueFactory(new PropertyValueFactory<>("descVerba"));
		tableColumnImportar.setCellValueFactory(new PropertyValueFactory<>("importar"));
		tableColumnCodVerba.setStyle("-fx-alignment: CENTER-RIGHT");
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<VerbaFolha> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewVerbaFolha.setItems(obsLista);
		initEditButtons();
		initRemoveButtons();
	}

	private void criarDialogoForm(VerbaFolha entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();
//			Pane pane = loader.load();

			VerbaFolhaFormController controller = loader.getController();
			controller.setVerbaFolha(entidade);
			controller.setVerbaFolhaService(new VerbaFolhaService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Informe os Dados da Folha");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<VerbaFolha, VerbaFolha>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(VerbaFolha obj, boolean empty) {
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
		tableColumnREMOVE.setCellFactory(param -> new TableCell<VerbaFolha, VerbaFolha>() {
			private final Button button = new Button("Excluir");


			@Override
			protected void updateItem(VerbaFolha obj, boolean empty) {
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

	private void removeEntity(VerbaFolha objeto) {
		Optional<ButtonType> clicado = Alertas.mostrarConfirmacao("Confirmacao", null, "Tem certeza da delecao?", AlertType.CONFIRMATION);
		if (clicado.get() == ButtonType.OK) {
			if (servico == null) {
				throw new IllegalStateException("O service foi passado nulo pelo outro programador!");
			}
			try {
				servico.remover(objeto);
				atualizarTableView();
			} catch (DbIntegridadeException e) {
				Alertas.mostrarAlertas("Erro removendo Objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
