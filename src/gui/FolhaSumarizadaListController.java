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
import model.entities.FolhaSumarizada;
import model.services.FolhaSumarizadaService;

public class FolhaSumarizadaListController implements Initializable {

	private FolhaSumarizadaService servico;

	@FXML
	private TableView<FolhaSumarizada> tableViewSumarioFolha;
	@FXML
	private TableColumn<FolhaSumarizada, String> tableColumnAnoMes;
	@FXML
	private TableColumn<FolhaSumarizada, Double> tableColumnCodCentroCustos;
	@FXML
	private TableColumn<FolhaSumarizada, String> tableColumnDescCentroCustos;
	@FXML
	private TableColumn<FolhaSumarizada, Integer> tableColumnQtdeImportarSim;
	@FXML
	private TableColumn<FolhaSumarizada, Double> tableColumnTotalImportarSim;
	@FXML
	private TableColumn<FolhaSumarizada, Integer> tableColumnQtdeImportarNao;
	@FXML
	private TableColumn<FolhaSumarizada, Double> tableColumnTotalImportarNao;

	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btSair;
	
	private ObservableList<FolhaSumarizada> obsLista;

	@FXML
	public void onGerarTxtAction(ActionEvent evento) {
		Boolean oficial = false;
		servico.gerarTxt(oficial);
	}
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	public void setFolhaSumarizadaServico(FolhaSumarizadaService servico) {
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

		Utilitarios.formatarTableColumnDouble(tableColumnCodCentroCustos, 0);
		Utilitarios.formatarTableColumnDouble(tableColumnTotalImportarSim, 2);
		Utilitarios.formatarTableColumnDouble(tableColumnTotalImportarNao, 2);
		
		tableColumnCodCentroCustos.setStyle("-fx-alignment: CENTER-RIGHT");
		tableColumnQtdeImportarSim.setStyle("-fx-alignment: CENTER-RIGHT");
		tableColumnQtdeImportarNao.setStyle("-fx-alignment: CENTER-RIGHT");
		tableColumnTotalImportarSim.setStyle("-fx-alignment: CENTER-RIGHT");
		tableColumnTotalImportarNao.setStyle("-fx-alignment: CENTER-RIGHT");
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<FolhaSumarizada> lista = servico.pesquisarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewSumarioFolha.setItems(obsLista);
	}
}
