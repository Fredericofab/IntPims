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
import model.entities.Parametros;
import model.services.ParametrosService;

public class ParametrosListController implements Initializable, DadosAlteradosListener {

	private ParametrosService servico;
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagExcluir;

	@FXML
	private TableView<Parametros> tableViewParametros;
	@FXML
	private TableColumn<Parametros, String> tableColumnSecao;
	@FXML
	private TableColumn<Parametros, String> tableColumnEntrada;
	@FXML
	private TableColumn<Parametros, String> tableColumnValor;
	@FXML
	private TableColumn<Parametros, String> tableColumnDescricao;

	@FXML
	private TableColumn<Parametros, Parametros> tableColumnEDIT;
	@FXML
	private TableColumn<Parametros, Parametros> tableColumnREMOVE;
	
	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btSair;

	private ObservableList<Parametros> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/ParametrosForm.fxml";
		Parametros entidade = new Parametros();
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

	public void setParametrosServico(ParametrosService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		inicializarComponentes();
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		flagIncluir = (parametrosService.pesquisarPorChave("Parametros", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("Parametros", "FlagAlterar")).getValor().toUpperCase();
		flagExcluir = (parametrosService.pesquisarPorChave("Parametros", "FlagExcluir")).getValor().toUpperCase();
	}

	private void inicializarComponentes() {
		tableColumnSecao.setCellValueFactory(new PropertyValueFactory<>("secao"));
		tableColumnEntrada.setCellValueFactory(new PropertyValueFactory<>("entrada"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		btIncluir.setDisable((flagIncluir.equals("N") ? true : false));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<Parametros> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewParametros.setItems(obsLista);

		if (flagAlterar.equals("S")) {
			initEditButtons();
		}
		if (flagExcluir.equals("S")) {
			initRemoveButtons();
		}
	}

	private void criarDialogoForm(Parametros entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();
//			Pane pane = loader.load();

			ParametrosFormController controller = loader.getController();
			controller.setParametros(entidade);
			controller.setParametrosService(new ParametrosService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Informe os Parametros");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Parametros, Parametros>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Parametros obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/ParametrosForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Parametros, Parametros>() {
			private final Button button = new Button("Excluir");


			@Override
			protected void updateItem(Parametros obj, boolean empty) {
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

	private void removeEntity(Parametros objeto) {
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
