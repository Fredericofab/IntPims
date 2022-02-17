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
import model.entities.PoliticasErp;
import model.services.PoliticasErpService;
import model.services.ParametrosService;

public class PoliticasErpListController implements Initializable, DadosAlteradosListener {

	private PoliticasErpService servico;
	
	private ParametrosService parametrosService = new ParametrosService();


//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagExcluir;

	@FXML
	private TableView<PoliticasErp> tableViewDadosPoliticasErp;
	@FXML
	private TableColumn<PoliticasErp, Integer> tableColumnCodPolitica;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnNomePolitica;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnDescPolitica;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnFlagAtiva;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnClausulaWhere;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnImportar;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnSalvarOS_Material;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnSalvarCstg_IntVM;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnSalvarCstg_IntCM;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnSalvarCstg_IntDG;

	@FXML
	private TableColumn<PoliticasErp, PoliticasErp> tableColumnEDIT;
	@FXML
	private TableColumn<PoliticasErp, PoliticasErp> tableColumnREMOVE;

	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btRelatorioTxt;
	@FXML
	private Button btSair;

	private ObservableList<PoliticasErp> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/PoliticasErpForm.fxml";
		PoliticasErp entidade = new PoliticasErp();
		criarDialogoForm(entidade, caminhoDaView, parentStage);
	}
	@FXML
	public void onGerarTxtAction(ActionEvent evento) {
		Boolean oficial = false;
		servico.gerarTxt(oficial);
	}
	@FXML
	public void onRelatorioTxtAction(ActionEvent evento) {
		Boolean oficial = false;
		servico.relatorioTxt(oficial);
	}
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	public void setPoliticasErpServico(PoliticasErpService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		inicializarComponentes();
	}

	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("PoliticasErp", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("PoliticasErp", "FlagAlterar")).getValor().toUpperCase();
		flagExcluir = (parametrosService.pesquisarPorChave("PoliticasErp", "FlagExcluir")).getValor().toUpperCase();
	}

	private void inicializarComponentes() {
		tableColumnCodPolitica.setCellValueFactory(new PropertyValueFactory<>("codPolitica"));
		tableColumnNomePolitica.setCellValueFactory(new PropertyValueFactory<>("nomePolitica"));
		tableColumnDescPolitica.setCellValueFactory(new PropertyValueFactory<>("descPolitica"));
		tableColumnFlagAtiva.setCellValueFactory(new PropertyValueFactory<>("flagAtiva"));
		tableColumnClausulaWhere.setCellValueFactory(new PropertyValueFactory<>("clausulaWhere"));
		tableColumnImportar.setCellValueFactory(new PropertyValueFactory<>("importar"));
		tableColumnSalvarOS_Material.setCellValueFactory(new PropertyValueFactory<>("salvarOS_Material"));
		tableColumnSalvarCstg_IntVM.setCellValueFactory(new PropertyValueFactory<>("salvarCstg_IntVM"));
		tableColumnSalvarCstg_IntCM.setCellValueFactory(new PropertyValueFactory<>("salvarCstg_IntCM"));
		tableColumnSalvarCstg_IntDG.setCellValueFactory(new PropertyValueFactory<>("salvarCstg_IntDG"));

		
		tableColumnCodPolitica.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnFlagAtiva.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnImportar.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnSalvarOS_Material.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnSalvarCstg_IntVM.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnSalvarCstg_IntCM.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnSalvarCstg_IntDG.setStyle("-fx-alignment: TOP-CENTER");

		btIncluir.setDisable((flagIncluir.equals("N") ? true : false));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<PoliticasErp> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewDadosPoliticasErp.setItems(obsLista);
		initEditButtons();
		initRemoveButtons();
	}

	private void criarDialogoForm(PoliticasErp entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();

			PoliticasErpFormController controller = loader.getController();
			controller.setPoliticasErp(entidade);
			controller.setPoliticasErpService(new PoliticasErpService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("PoliticaErp");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<PoliticasErp, PoliticasErp>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(PoliticasErp obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setDisable((flagAlterar.equals("N") ? true : false));
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/PoliticasErpForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<PoliticasErp, PoliticasErp>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(PoliticasErp obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setDisable((flagExcluir.equals("N") ? true : false));
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(PoliticasErp objeto) {
		Optional<ButtonType> clicado = Alertas.mostrarConfirmacao("Confirmacao", null, "Tem certeza da delecao?", AlertType.CONFIRMATION );
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
