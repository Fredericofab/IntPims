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
import model.entities.CriticasErp;
import model.services.CriticasErpService;
import model.services.ParametrosService;

public class CriticasErpListController implements Initializable, DadosAlteradosListener {

	private CriticasErpService servico;
	
	private ParametrosService parametrosService = new ParametrosService();


//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagExcluir;
	String anoMes;

	@FXML
	private TableView<CriticasErp> tableViewDadosCriticasErp;
	@FXML
	private TableColumn<CriticasErp, String> tableColumnTipoCritica;
	@FXML
	private TableColumn<CriticasErp, Integer> tableColumnCodCritica;
	@FXML
	private TableColumn<CriticasErp, String> tableColumnDescCritica;
	@FXML
	private TableColumn<CriticasErp, String> tableColumnFlagAtiva;
	@FXML
	private TableColumn<CriticasErp, String> tableColumnAnoMesAnalisado;
	@FXML
	private TableColumn<CriticasErp, Integer> tableColumnRegistrosAnalisados;
	@FXML
	private TableColumn<CriticasErp, Integer> tableColumnRegistrosAtualizados;
	@FXML
	private TableColumn<CriticasErp, Integer> tableColumnRegistrosPendentes;
	@FXML
	private TableColumn<CriticasErp, String> tableColumnClausulaWhere;
	@FXML
	private TableColumn<CriticasErp, String> tableColumnClausulaSet;
	@FXML
	private TableColumn<CriticasErp, CriticasErp> tableColumnEDIT;
	@FXML
	private TableColumn<CriticasErp, CriticasErp> tableColumnREMOVE;

	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btSair;

	private ObservableList<CriticasErp> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/CriticasErpForm.fxml";
		CriticasErp entidade = new CriticasErp();
		entidade.setTipoCritica("U");
		entidade.setRegistrosAnalisados(0);
		entidade.setRegistrosAtualizados(0);
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
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	public void setCriticasErpServico(CriticasErpService servico) {
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
		tableColumnDescCritica.setCellValueFactory(new PropertyValueFactory<>("descCritica"));
		tableColumnFlagAtiva.setCellValueFactory(new PropertyValueFactory<>("flagAtiva"));
		tableColumnAnoMesAnalisado.setCellValueFactory(new PropertyValueFactory<>("anoMesAnalisado"));
		tableColumnRegistrosAnalisados.setCellValueFactory(new PropertyValueFactory<>("registrosAnalisados"));
		tableColumnRegistrosAtualizados.setCellValueFactory(new PropertyValueFactory<>("registrosAtualizados"));
		tableColumnRegistrosPendentes.setCellValueFactory(new PropertyValueFactory<>("registrosPendentes"));
		tableColumnClausulaWhere.setCellValueFactory(new PropertyValueFactory<>("clausulaWhere"));
		tableColumnClausulaSet.setCellValueFactory(new PropertyValueFactory<>("clausulaSet"));

		btIncluir.setDisable((flagIncluir.equals("N") ? true : false));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<CriticasErp> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewDadosCriticasErp.setItems(obsLista);
		if (flagAlterar.equals("S")) {
			initEditButtons();
		}
		if (flagExcluir.equals("S")) {
			initRemoveButtons();
		}
	}

	private void criarDialogoForm(CriticasErp entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();

			CriticasErpFormController controller = loader.getController();
			controller.setCriticasErp(entidade);
			controller.setCriticasErpService(new CriticasErpService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("CriticasErp");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<CriticasErp, CriticasErp>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(CriticasErp obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/CriticasErpForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<CriticasErp, CriticasErp>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(CriticasErp obj, boolean empty) {
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

	private void removeEntity(CriticasErp objeto) {
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
