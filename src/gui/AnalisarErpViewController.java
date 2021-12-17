
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gui.util.RestricoesDeDigitacao;
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
import model.entities.CriticaErp;
import model.services.AnalisarErpService;
import model.services.CriticaErpService;

public class AnalisarErpViewController implements Initializable {

	private AnalisarErpService servico;
	
	@FXML
	private TableView<CriticaErp> tableViewCriticasErp;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnTipoCritica;
	@FXML
	private TableColumn<CriticaErp, Integer> tableColumnCodCritica;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnDescCritica;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnFlagAtiva;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnAnoMesAnalisado;
	@FXML
	private TableColumn<CriticaErp, Integer> tableColumnRegistrosAnalisados;
	@FXML
	private TableColumn<CriticaErp, Integer> tableColumnRegistrosAtualizados;
	@FXML
	private TableColumn<CriticaErp, Integer> tableColumnRegistrosPendentes;

	@FXML
	private TextField txtTipoCritica;
	@FXML
	private TextField txtCodigoCritica;
	@FXML
	private TextField txtTotalRegistros;
	@FXML
	private TextField txtTotalLiberados;
	@FXML
	private TextField txtTotalDesconsiderados;
	@FXML
	private TextField txtTotalPendentes;
	@FXML
	private Button btAnalisarUm;
	@FXML
	private Button btAnalisarTodos;
	@FXML
	private Button btSair;
	
	private ObservableList<CriticaErp> obsLista;

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
		atualizarTela();
		atualizarTableView();
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
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodigoCritica);
	}

	public void atualizarTableView() {
		CriticaErpService criticasErpService = new CriticaErpService();
		List<CriticaErp> lista = criticasErpService.pesquisarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewCriticasErp.setItems(obsLista);
		tableViewCriticasErp.refresh();
	}
	
	private void atualizarTela() {
		Integer qtdeTotalRegistros = servico.getQtdeTotalDeRegistros();
		Integer qtdeTotalLiberados = servico.getQtdeTotalLiberados();
		Integer qtdeTotalDesconsiderados = servico.getQtdeTotalDesconsiderados();
		Integer qtdeTotalPendentes = servico.getQtdeTotalPendentes();
		txtTotalRegistros.setText(qtdeTotalRegistros.toString());
		txtTotalLiberados.setText(qtdeTotalLiberados.toString());
		txtTotalDesconsiderados.setText(qtdeTotalDesconsiderados.toString());
		txtTotalPendentes.setText(qtdeTotalPendentes.toString());
	}
}
