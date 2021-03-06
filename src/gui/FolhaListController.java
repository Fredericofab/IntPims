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
import model.entities.Folha;
import model.services.FolhaService;
import model.services.ParametrosService;

public class FolhaListController implements Initializable, DadosAlteradosListener {

	private FolhaService servico;
	
	private ParametrosService parametrosService = new ParametrosService();


//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagExcluir;
	String anoMes;

	@FXML
	private TableView<Folha> tableViewDadosFolha;
	@FXML
	private TableColumn<Folha, String> tableColumnAnoMes;
	@FXML
	private TableColumn<Folha, Double> tableColumnCodCentroCustos;
	@FXML
	private TableColumn<Folha, String> tableColumnDescCentroCustos;
	@FXML
	private TableColumn<Folha, Double> tableColumnCodVerba;
	@FXML
	private TableColumn<Folha, String> tableColumnDescVerba;
	@FXML
	private TableColumn<Folha, Double> tableColumnValorVerba;
	@FXML
	private TableColumn<Folha, Double> tableColumnReferenciaVerba;
	@FXML
	private TableColumn<Folha, String> tableColumnTipoVerba;
	@FXML
	private TableColumn<Folha, String> tableColumnImportar;
	@FXML
	private TableColumn<Folha, String> tableColumnConsiderarReferencia;
	@FXML
	private TableColumn<Folha, String> tableColumnObservacao;
	@FXML
	private TableColumn<Folha, Folha> tableColumnEDIT;
	@FXML
	private TableColumn<Folha, Folha> tableColumnREMOVE;

	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btSair;

	private ObservableList<Folha> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/FolhaForm.fxml";
		Folha entidade = new Folha();
		entidade.setAnoMes(anoMes);
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

	public void setFolhaServico(FolhaService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		inicializarComponentes();
	}

	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("DadosFolha", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("DadosFolha", "FlagAlterar")).getValor().toUpperCase();
		flagExcluir = (parametrosService.pesquisarPorChave("DadosFolha", "FlagExcluir")).getValor().toUpperCase();
		anoMes =  (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}

	private void inicializarComponentes() {
		tableColumnAnoMes.setCellValueFactory(new PropertyValueFactory<>("anoMes"));
		tableColumnCodCentroCustos.setCellValueFactory(new PropertyValueFactory<>("codCentroCustos"));
		tableColumnDescCentroCustos.setCellValueFactory(new PropertyValueFactory<>("descCentroCustos"));
		tableColumnCodVerba.setCellValueFactory(new PropertyValueFactory<>("codVerba"));
		tableColumnDescVerba.setCellValueFactory(new PropertyValueFactory<>("descVerba"));
		tableColumnValorVerba.setCellValueFactory(new PropertyValueFactory<>("valorVerba"));
		tableColumnReferenciaVerba.setCellValueFactory(new PropertyValueFactory<>("referenciaVerba"));
		tableColumnTipoVerba.setCellValueFactory(new PropertyValueFactory<>("tipoVerba"));
		tableColumnImportar.setCellValueFactory(new PropertyValueFactory<>("importar"));
		tableColumnConsiderarReferencia.setCellValueFactory(new PropertyValueFactory<>("considerarReferencia"));
		tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
		
		Utilitarios.formatarTableColumnDouble(tableColumnCodCentroCustos, 0);
		Utilitarios.formatarTableColumnDouble(tableColumnCodVerba, 0);
		Utilitarios.formatarTableColumnDouble(tableColumnValorVerba, 2);
		Utilitarios.formatarTableColumnDouble(tableColumnReferenciaVerba, 2);

		tableColumnCodCentroCustos.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnCodVerba.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnValorVerba.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnReferenciaVerba.setStyle("-fx-alignment: TOP-RIGHT");
		
		btIncluir.setDisable((flagIncluir.equals("N") ? true : false));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<Folha> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewDadosFolha.setItems(obsLista);
		initEditButtons();
		initRemoveButtons();
	}

	private void criarDialogoForm(Folha entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();

			FolhaFormController controller = loader.getController();
			controller.setDadosFolha(entidade);
			controller.setDadosFolhaService(new FolhaService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Dados da Folha");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Folha, Folha>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Folha obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setDisable((flagAlterar.equals("N") ? true : false));
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/FolhaForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Folha, Folha>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Folha obj, boolean empty) {
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

	private void removeEntity(Folha objeto) {
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
