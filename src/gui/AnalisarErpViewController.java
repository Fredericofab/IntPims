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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.CriticasErp;
import model.services.AnalisarErpService;
import model.services.CriticasErpService;

public class AnalisarErpViewController implements Initializable {

	private AnalisarErpService servico;
	
	@FXML
	private TableView<CriticasErp> tableViewCriticasErp;
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
	private TextField txtTipoCritica;
	@FXML
	private TextField txtCodigoCritica;
	@FXML
	private TextField txtTotalAnalises;
	@FXML
	private TextField txtTotalAtualizacoes;
	@FXML
	private TextField txtTotalPendencias;
	@FXML
	private Button btAnalisarUm;
	@FXML
	private Button btAnalisarTodos;
	@FXML
	private Button btSair;
	
	private ObservableList<CriticasErp> obsLista;

	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@FXML
	public void onBtAnalisarUmAction(ActionEvent evento) {
		String tipo = txtTipoCritica.getText().toUpperCase();
		Integer codigo = Utilitarios.tentarConverterParaInt(txtCodigoCritica.getText());
		servico.analisarUm(tipo, codigo);
		atualizarTela();
		atualizarTableView();
	}

	@FXML
	public void onBtAnalisarTodosAction(ActionEvent evento) {
		servico.analisarTodos();
	}

	public void setAnalisarErpServico(AnalisarErpService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarComponentes();
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
	}

	public void atualizarTableView() {
		CriticasErpService criticasErpService = new CriticasErpService();
		List<CriticasErp> lista = criticasErpService.pesquisarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewCriticasErp.setItems(obsLista);
		tableViewCriticasErp.refresh();
	}
	
	private void atualizarTela() {
		Integer qtdeAnalisadas = servico.getQtdeAnalisadas();
		Integer qtdeAtualizadas = servico.getQtdeAtualizadas();
		Integer qtdePendentes = servico.getQtdePendentes();
		txtTotalAnalises.setText(qtdeAnalisadas.toString());
		txtTotalAtualizacoes.setText(qtdeAtualizadas.toString());
		txtTotalPendencias.setText(qtdePendentes.toString());
	}
}
