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
import model.entities.CriticaErp;
import model.services.CriticaErpService;
import model.services.ParametrosService;

public class CriticaErpListController implements Initializable, DadosAlteradosListener {

	private CriticaErpService servico;
	
	private ParametrosService parametrosService = new ParametrosService();


//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagExcluir;

	@FXML
	private TableView<CriticaErp> tableViewDadosCriticasErp;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnTipoCritica;
	@FXML
	private TableColumn<CriticaErp, Integer> tableColumnCodCritica;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnNomeCritica;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnDescCritica;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnFlagAtiva;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnClausulaWhere;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnImportar;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnSalvarOS_Material;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnSalvarCstg_IntVM;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnSalvarCstg_IntCM;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnSalvarCstg_IntDG;

	@FXML
	private TableColumn<CriticaErp, CriticaErp> tableColumnEDIT;
	@FXML
	private TableColumn<CriticaErp, CriticaErp> tableColumnREMOVE;

	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btRelatorioTxt;
	@FXML
	private Button btSair;

	private ObservableList<CriticaErp> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/CriticaErpForm.fxml";
		CriticaErp entidade = new CriticaErp();
		entidade.setTipoCritica("U");
		entidade.setRegistrosPendentes(0);
		Integer sequencial = servico.ultimoSequencial(entidade) + 1;
		entidade.setCodigoCritica(sequencial);
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

	public void setCriticasErpServico(CriticaErpService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		inicializarComponentes();
	}

	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("CriticasErp", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("CriticasErp", "FlagAlterar")).getValor().toUpperCase();
		flagExcluir = (parametrosService.pesquisarPorChave("CriticasErp", "FlagExcluir")).getValor().toUpperCase();
	}

	private void inicializarComponentes() {
		tableColumnTipoCritica.setCellValueFactory(new PropertyValueFactory<>("tipoCritica"));
		tableColumnCodCritica.setCellValueFactory(new PropertyValueFactory<>("codigoCritica"));
		tableColumnNomeCritica.setCellValueFactory(new PropertyValueFactory<>("nomeCritica"));
		tableColumnDescCritica.setCellValueFactory(new PropertyValueFactory<>("descCritica"));
		tableColumnFlagAtiva.setCellValueFactory(new PropertyValueFactory<>("flagAtiva"));
		tableColumnClausulaWhere.setCellValueFactory(new PropertyValueFactory<>("clausulaWhere"));
		tableColumnImportar.setCellValueFactory(new PropertyValueFactory<>("importar"));
		tableColumnSalvarOS_Material.setCellValueFactory(new PropertyValueFactory<>("salvarOS_Material"));
		tableColumnSalvarCstg_IntVM.setCellValueFactory(new PropertyValueFactory<>("salvarCstg_IntVM"));
		tableColumnSalvarCstg_IntCM.setCellValueFactory(new PropertyValueFactory<>("salvarCstg_IntCM"));
		tableColumnSalvarCstg_IntDG.setCellValueFactory(new PropertyValueFactory<>("salvarCstg_IntDG"));

		
		tableColumnTipoCritica.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnCodCritica.setStyle("-fx-alignment: TOP-RIGHT");
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
		List<CriticaErp> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewDadosCriticasErp.setItems(obsLista);
		if (flagAlterar.equals("S")) {
			initEditButtons();
		}
		if (flagExcluir.equals("S")) {
			initRemoveButtons();
		}
	}

	private void criarDialogoForm(CriticaErp entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();

			CriticaErpFormController controller = loader.getController();
			controller.setCriticasErp(entidade);
			controller.setCriticasErpService(new CriticaErpService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("CriticaErp");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<CriticaErp, CriticaErp>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(CriticaErp obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/CriticaErpForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<CriticaErp, CriticaErp>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(CriticaErp obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				// meu ajuste no código copiado do curso
				// se tipo 'S'istema, entao nao pode excluir.
//				if (obj.getTipoCritica().equals("S")) {
//					setGraphic(null);
//					return;
//				}
				//FRED normalmente NAO comentar as 4 linhas acima e comentar a de abaixo
				System.out.println("FRED Manutencao em CriticaErpListController - initRemoveButtons");
				
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(CriticaErp objeto) {
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
