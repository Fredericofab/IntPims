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
import model.entities.FatorMedida;
import model.services.ParametrosService;
import model.services.FatorMedidaService;

public class FatorMedidaListController implements Initializable, DadosAlteradosListener {

	private FatorMedidaService servico;
	private ParametrosService parametrosService = new ParametrosService();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagExcluir;
	String atualizarNovos;

	@FXML
	private TableView<FatorMedida> tableViewFatorMedida;
	@FXML
	private TableColumn<FatorMedida, String> tableColumnCodMaterial;
	@FXML
	private TableColumn<FatorMedida, String> tableColumnDescMaterial;
	@FXML
	private TableColumn<FatorMedida, String> tableColumnUnidadeMedida;
	@FXML
	private TableColumn<FatorMedida, Double> tableColumnFatorDivisao;
	@FXML
	private TableColumn<FatorMedida, FatorMedida> tableColumnEDIT;
	@FXML
	private TableColumn<FatorMedida, FatorMedida> tableColumnREMOVE;
	
	@FXML
	private Button btIncluir;
	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btAtualizarNovos;
	@FXML
	private Button btSair;

	private ObservableList<FatorMedida> obsLista;

	@FXML
	public void onBtIncluirAction(ActionEvent evento) {
		Stage parentStage = Utilitarios.atualStage(evento);
		String caminhoDaView = "/gui/FatorMedidaForm.fxml";
		FatorMedida entidade = new FatorMedida();
		criarDialogoForm(entidade, caminhoDaView, parentStage);
	}
	@FXML
	public void onGerarTxtAction(ActionEvent evento) {
		Boolean oficial = false;
		servico.gerarTxt(oficial);
	}
	@FXML
	public void onAtualizarNovosAction(ActionEvent evento) {
		try {
		servico.atualizarNovos();
		atualizarTableView();
		}
		catch (DbException e){
			Alertas.mostrarAlertas("erro Salvando FatorMedida", null, e.getMessage(), AlertType.ERROR);
		}
	}
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	public void setFatorMedidaServico(FatorMedidaService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		inicializarComponentes();
	}
	
	private void inicializarComponentes() {
		tableColumnCodMaterial.setCellValueFactory(new PropertyValueFactory<>("codMaterial"));
		tableColumnDescMaterial.setCellValueFactory(new PropertyValueFactory<>("descMaterial"));
		tableColumnUnidadeMedida.setCellValueFactory(new PropertyValueFactory<>("unidadeMedida"));
		tableColumnFatorDivisao.setCellValueFactory(new PropertyValueFactory<>("fatorDivisao"));
		
		Utilitarios.formatarTableColumnDouble(tableColumnFatorDivisao, 4);

		tableColumnCodMaterial.setStyle("-fx-alignment: TOP-RIGHT");

		btIncluir.setDisable((flagIncluir.equals("N") ? true : false));
		btAtualizarNovos.setDisable((atualizarNovos.equals("N") ? true : false));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<FatorMedida> lista = servico.pesquisarTodos();

		obsLista = FXCollections.observableArrayList(lista);
		tableViewFatorMedida.setItems(obsLista);

		initEditButtons();
		initRemoveButtons();
	}

	private void criarDialogoForm(FatorMedida entidade, String caminhoDaView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			ScrollPane pane = loader.load();

			FatorMedidaFormController controller = loader.getController();
			controller.setFatorMedida(entidade);
			controller.setFatorMedidaService(new FatorMedidaService());
			controller.serOuvinteDeDadosAlteradosListener(this);
			controller.atualizarFormulario();

			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Fator de Conversão de Medidas");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<FatorMedida, FatorMedida>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(FatorMedida obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setDisable((flagAlterar.equals("N") ? true : false));
				button.setOnAction(
						event -> criarDialogoForm(obj, "/gui/FatorMedidaForm.fxml", Utilitarios.atualStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<FatorMedida, FatorMedida>() {
			private final Button button = new Button("Excluir");


			@Override
			protected void updateItem(FatorMedida obj, boolean empty) {
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

	private void removeEntity(FatorMedida objeto) {
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
	
	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("FatorMedida", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("FatorMedida", "FlagAlterar")).getValor().toUpperCase();
		flagExcluir = (parametrosService.pesquisarPorChave("FatorMedida", "FlagExcluir")).getValor().toUpperCase();
		atualizarNovos = (parametrosService.pesquisarPorChave("FatorMedida", "AtualizarNovos")).getValor().toUpperCase();
	}

}
