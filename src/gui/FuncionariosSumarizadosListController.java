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
import model.entities.FuncionariosSumarizados;
import model.services.FuncionariosSumarizadosService;

public class FuncionariosSumarizadosListController implements Initializable {

	private FuncionariosSumarizadosService servico;

	@FXML
	private TableView<FuncionariosSumarizados> tableViewSumarioFuncionarios;
	@FXML
	private TableColumn<FuncionariosSumarizados, String> tableColumnAnoMes;
	@FXML
	private TableColumn<FuncionariosSumarizados, Double> tableColumnCodCentroCustos;
	@FXML
	private TableColumn<FuncionariosSumarizados, String> tableColumnDescCentroCustos;
	@FXML
	private TableColumn<FuncionariosSumarizados, Integer> tableColumnQtdeFuncionarios;

	@FXML
	private Button btGerarTxt;
	@FXML
	private Button btSair;
	
	private ObservableList<FuncionariosSumarizados> obsLista;

	@FXML
	public void onGerarTxtAction(ActionEvent evento) {
		Boolean oficial = false;
		servico.gerarTxt(oficial);
	}
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	public void setFuncionariosSumarizadosServico(FuncionariosSumarizadosService servico) {
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
		tableColumnQtdeFuncionarios.setCellValueFactory(new PropertyValueFactory<>("qdteFuncionarios"));

		Utilitarios.formatarTableColumnDouble(tableColumnCodCentroCustos, 0);
		
		tableColumnCodCentroCustos.setStyle("-fx-alignment: CENTER-RIGHT");
		tableColumnQtdeFuncionarios.setStyle("-fx-alignment: CENTER-RIGHT");
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("o servico nao foi carregado pelo outro programador");
		}
		List<FuncionariosSumarizados> lista = servico.pesquisarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewSumarioFuncionarios.setItems(obsLista);
	}
}
