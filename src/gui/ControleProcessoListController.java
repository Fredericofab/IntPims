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
import model.entities.ControleProcesso;
import model.services.ControleProcessoService;
import model.services.ParametrosService;

public class ControleProcessoListController implements Initializable, DadosAlteradosListener {

	private ControleProcessoService servico;

//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagExcluir;
	String anoMes;

	@FXML
	private TableView<ControleProcesso> tableViewControleProcesso;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnAnoMes;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnImportarFolha;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnSumarizarFolha;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnExportarFolha;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnImportarFuncionario;
	@FXML
	private TableColumn<ControleProcesso, Double> tableColumnSumarizarFuncionario;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnImportarErpMT;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnImportarErpCD;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnImportarErpDD;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnCriticarErp;
	@FXML
	private TableColumn<ControleProcesso, String> tableColumnExportarErp;
	@FXML
	private TableColumn<ControleProcesso, ControleProcesso> tableColumnEDIT;
	@FXML
	private TableColumn<ControleProcesso, ControleProcesso> tableColumnREMOVE;

	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btSair;

	private ObservableList<ControleProcesso> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/ControleProcessoForm.fxml";
		ControleProcesso entidade = new ControleProcesso();
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

	public void setControleProcessoServico(ControleProcessoService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		inicializarComponentes();
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		flagIncluir = (parametrosService.pesquisarPorChave("ControleProcesso", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("ControleProcesso", "FlagAlterar")).getValor().toUpperCase();
		flagExcluir = (parametrosService.pesquisarPorChave("ControleProcesso", "FlagExcluir")).getValor().toUpperCase();
		anoMes =  (parametrosService.pesquisarPorChave("AmbienteGeral", "AnoMes")).getValor();
	}

	private void inicializarComponentes() {
		tableColumnAnoMes.setCellValueFactory(new PropertyValueFactory<>("anoMes"));
		tableColumnImportarFolha.setCellValueFactory(new PropertyValueFactory<>("importarFolha"));
		tableColumnSumarizarFolha.setCellValueFactory(new PropertyValueFactory<>("sumarizarFolha"));
		tableColumnExportarFolha.setCellValueFactory(new PropertyValueFactory<>("exportarFolha"));
		tableColumnImportarFuncionario.setCellValueFactory(new PropertyValueFactory<>("importarFuncionario"));
		tableColumnSumarizarFuncionario.setCellValueFactory(new PropertyValueFactory<>("sumarizarFuncionario"));
		tableColumnImportarErpMT.setCellValueFactory(new PropertyValueFactory<>("importarErpMT"));
		tableColumnImportarErpCD.setCellValueFactory(new PropertyValueFactory<>("importarErpCD"));
		tableColumnImportarErpDD.setCellValueFactory(new PropertyValueFactory<>("importarErpDD"));
		tableColumnCriticarErp.setCellValueFactory(new PropertyValueFactory<>("criticarErp"));
		tableColumnExportarErp.setCellValueFactory(new PropertyValueFactory<>("exportarErp"));

//		tableColumnCodCentroCustos.setStyle("-fx-alignment: CENTER-RIGHT");
		btIncluir.setDisable((flagIncluir.equals("N") ? true : false));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<ControleProcesso> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewControleProcesso.setItems(obsLista);
		if (flagAlterar.equals("S")) {
			initEditButtons();
		}
		if (flagExcluir.equals("S")) {
			initRemoveButtons();
		}
	}

	private void criarDialogoForm(ControleProcesso entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();

			ControleProcessoFormController controller = loader.getController();
			controller.setControleProcesso(entidade);
			controller.setControleProcessoService(new ControleProcessoService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Controle dos Processos Mensais");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<ControleProcesso, ControleProcesso>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(ControleProcesso obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/ControleProcessoForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<ControleProcesso, ControleProcesso>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(ControleProcesso obj, boolean empty) {
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

	private void removeEntity(ControleProcesso objeto) {
		Optional<ButtonType> clicado = Alertas.mostrarConfirmacao("Confirmacao", null, "Tem certeza da delecao?", AlertType.CONFIRMATION );
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
