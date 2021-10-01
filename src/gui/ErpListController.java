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
import model.entities.Erp;
import model.services.ErpService;
import model.services.ParametrosService;

public class ErpListController implements Initializable, DadosAlteradosListener {

	private ErpService servico;
	
	private ParametrosService parametrosService = new ParametrosService();


//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagExcluir;
	String anoMes;

	@FXML
	private TableView<Erp> tableViewDadosErp;
	@FXML
	private TableColumn<Erp, String> tableColumnSequencial;
	@FXML
	private TableColumn<Erp, String> tableColumnAnoMes;
	@FXML
	private TableColumn<Erp, String> tableColumnOrigem;
	@FXML
	private TableColumn<Erp, Double> tableColumnCodCentroCustos;
	@FXML
	private TableColumn<Erp, String> tableColumnDescCentroCustos;
	@FXML
	private TableColumn<Erp, Double> tableColumnCodContaContabil;
	@FXML
	private TableColumn<Erp, String> tableColumnDescContaContabil;
	@FXML
	private TableColumn<Erp, Double> tableColumnFamiliaMaterial;
	@FXML
	private TableColumn<Erp, String> tableColumnImportar;
	@FXML
	private TableColumn<Erp, String> tableColumnObservacao;
	@FXML
	private TableColumn<Erp, Erp> tableColumnEDIT;
	@FXML
	private TableColumn<Erp, Erp> tableColumnREMOVE;

	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btSair;

	private ObservableList<Erp> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/ErpForm.fxml";
		Integer sequencial = servico.ultimoSequencial() + 1;
		Erp entidade = new Erp();
		entidade.setAnoMes(anoMes);
		entidade.setSequencial(sequencial);
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

	public void setErpServico(ErpService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		inicializarComponentes();
	}

	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("DadosErp", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("DadosErp", "FlagAlterar")).getValor().toUpperCase();
		flagExcluir = (parametrosService.pesquisarPorChave("DadosErp", "FlagExcluir")).getValor().toUpperCase();
		anoMes =  (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}

	private void inicializarComponentes() {
		tableColumnSequencial.setCellValueFactory(new PropertyValueFactory<>("sequencial"));
		tableColumnAnoMes.setCellValueFactory(new PropertyValueFactory<>("anoMes"));
		tableColumnOrigem.setCellValueFactory(new PropertyValueFactory<>("origem"));
		tableColumnCodCentroCustos.setCellValueFactory(new PropertyValueFactory<>("codCentroCustos"));
		tableColumnDescCentroCustos.setCellValueFactory(new PropertyValueFactory<>("descCentroCustos"));
		tableColumnCodContaContabil.setCellValueFactory(new PropertyValueFactory<>("codContaContabil"));
		tableColumnDescContaContabil.setCellValueFactory(new PropertyValueFactory<>("descContaContabil"));
		tableColumnFamiliaMaterial.setCellValueFactory(new PropertyValueFactory<>("familiaMaterial"));
		tableColumnImportar.setCellValueFactory(new PropertyValueFactory<>("importar"));
		tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
		
		Utilitarios.formatarTableColumnDouble(tableColumnCodCentroCustos, 0);
		Utilitarios.formatarTableColumnDouble(tableColumnCodContaContabil, 0);
		Utilitarios.formatarTableColumnDouble(tableColumnFamiliaMaterial, 2);

		tableColumnCodCentroCustos.setStyle("-fx-alignment: CENTER-RIGHT");
		tableColumnCodContaContabil.setStyle("-fx-alignment: CENTER-RIGHT");
		tableColumnFamiliaMaterial.setStyle("-fx-alignment: CENTER-RIGHT");
		
		btIncluir.setDisable((flagIncluir.equals("N") ? true : false));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<Erp> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewDadosErp.setItems(obsLista);
		if (flagAlterar.equals("S")) {
			initEditButtons();
		}
		if (flagExcluir.equals("S")) {
			initRemoveButtons();
		}
	}

	private void criarDialogoForm(Erp entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();

			ErpFormController controller = loader.getController();
			controller.setDadosErp(entidade);
			controller.setDadosErpService(new ErpService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Dados da Erp");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Erp, Erp>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Erp obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/ErpForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Erp, Erp>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Erp obj, boolean empty) {
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

	private void removeEntity(Erp objeto) {
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
