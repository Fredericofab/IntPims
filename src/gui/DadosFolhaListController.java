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
import model.entities.DadosFolha;
import model.services.DadosFolhaService;

public class DadosFolhaListController implements Initializable, DadosAlteradosListener {

	private DadosFolhaService servico;

	@FXML
	private TableView<DadosFolha> tableViewDadosFolha;

	@FXML
	private TableColumn<DadosFolha, String> tableColumnAnoMes;

	@FXML
	private TableColumn<DadosFolha, String> tableColumnCodCentroCustos;

	@FXML
	private TableColumn<DadosFolha, String> tableColumnDescCentroCustos;

	@FXML
	private TableColumn<DadosFolha, String> tableColumnCodVerba;

	@FXML
	private TableColumn<DadosFolha, String> tableColumnDescVerba;

	@FXML
	private TableColumn<DadosFolha, Double> tableColumnValorVerba;

	@FXML
	private TableColumn<DadosFolha, String> tableColumnImportar;

	@FXML
	private TableColumn<DadosFolha, String> tableColumnObservacao;

	@FXML
	private TableColumn<DadosFolha, DadosFolha> tableColumnEDIT;

	@FXML
	private TableColumn<DadosFolha, DadosFolha> tableColumnREMOVE;

	@FXML
	private Button btIncluir;

	@FXML
	private Button btSair;

	private ObservableList<DadosFolha> obsLista;

	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/DadosFolhaForm.fxml";
		DadosFolha entidade = new DadosFolha();
		criarDialogoForm(entidade, caminhoDaView, parentStage);
	}

	public void setDadosFolhaServico(DadosFolhaService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		tableColumnAnoMes.setCellValueFactory(new PropertyValueFactory<>("anoMes"));
		tableColumnCodCentroCustos.setCellValueFactory(new PropertyValueFactory<>("codCentroCustos"));
		tableColumnDescCentroCustos.setCellValueFactory(new PropertyValueFactory<>("descCentroCustos"));
		tableColumnCodVerba.setCellValueFactory(new PropertyValueFactory<>("codVerba"));
		tableColumnDescVerba.setCellValueFactory(new PropertyValueFactory<>("descVerba"));
		tableColumnValorVerba.setCellValueFactory(new PropertyValueFactory<>("valorVerba"));
		Utilitarios.formatarTableColumnDouble(tableColumnValorVerba, 2);
		tableColumnImportar.setCellValueFactory(new PropertyValueFactory<>("importar"));
		tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<DadosFolha> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewDadosFolha.setItems(obsLista);
		initEditButtons();
		initRemoveButtons();
	}

	private void criarDialogoForm(DadosFolha entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();
//			Pane pane = loader.load();

			DadosFolhaFormController controller = loader.getController();
			controller.setDadosFolha(entidade);
			controller.setDadosFolhaService(new DadosFolhaService());
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<DadosFolha, DadosFolha>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(DadosFolha obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/DadosFolhaForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<DadosFolha, DadosFolha>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(DadosFolha obj, boolean empty) {
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

	private void removeEntity(DadosFolha objeto) {
		Optional<ButtonType> clicado = Alertas.mostrarConfirmacao("Confirmacao", "Tem certeza da delecao?");
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
