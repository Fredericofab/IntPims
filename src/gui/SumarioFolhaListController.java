package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gui.util.Utilitarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.SumarioFolha;
import model.services.SumarioFolhaService;

public class SumarioFolhaListController implements Initializable {

	private SumarioFolhaService servico;

	@FXML
	private TableView<SumarioFolha> tableViewSumarioFolha;

	@FXML
	private TableColumn<SumarioFolha, String> tableColumnAnoMes;

	@FXML
	private TableColumn<SumarioFolha, String> tableColumnCodCentroCustos;

	@FXML
	private TableColumn<SumarioFolha, String> tableColumnDescCentroCustos;

	@FXML
	private TableColumn<SumarioFolha, Double> tableColumnQtdeImportarSim;

	@FXML
	private TableColumn<SumarioFolha, Double> tableColumnTotalImportarSim;


	@FXML
	private TableColumn<SumarioFolha, Double> tableColumnQtdeImportarNao;

	@FXML
	private TableColumn<SumarioFolha, Double> tableColumnTotalImportarNao;

	@FXML
	private Button btGerarTxt;

	@FXML
	private Button btSair;
	

	private ObservableList<SumarioFolha> obsLista;



	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	public void setSumarioFolhaServico(SumarioFolhaService servico) {
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
		tableColumnQtdeImportarSim.setCellValueFactory(new PropertyValueFactory<>("qdteImportarSim"));
		tableColumnTotalImportarSim.setCellValueFactory(new PropertyValueFactory<>("totalImportarSim"));
		tableColumnQtdeImportarNao.setCellValueFactory(new PropertyValueFactory<>("qdteImportarNao"));
		tableColumnTotalImportarNao.setCellValueFactory(new PropertyValueFactory<>("totalImportarNao"));
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<SumarioFolha> lista = servico.pesquisarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewSumarioFolha.setItems(obsLista);

	}


}
