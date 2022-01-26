
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alertas;
import gui.util.RestricoesDeDigitacao;
import gui.util.Utilitarios;
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.CriticaErp;
import model.exceptions.IntegridadeException;
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
	private TableColumn<CriticaErp, String> tableColumnNomeCritica;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnFlagAtiva;
	@FXML
	private TableColumn<CriticaErp, String> tableColumnAnoMesAnalisado;
	@FXML
	private TableColumn<CriticaErp, Integer> tableColumnRegistrosAnalisados;
	@FXML
	private TableColumn<CriticaErp, Integer> tableColumnRegistrosLiberados;
	@FXML
	private TableColumn<CriticaErp, Integer> tableColumnRegistrosIgnorados;
	@FXML
	private TableColumn<CriticaErp, Integer> tableColumnRegistrosPendentes;

	@FXML
	private TextField txtTipoCritica;
	@FXML
	private TextField txtCodigoCritica;
	@FXML
	private TextField txtTotalRegistros;
	@FXML
	private TextField txtTotalIndefinidos;
	@FXML
	private TextField txtTotalPendentes;
	@FXML
	private TextField txtTotalIgnorados;
	@FXML
	private TextField txtTotalLiberadosOS;
	@FXML
	private TextField txtTotalLiberadosCM;
	@FXML
	private TextField txtTotalLiberadosDG;
	@FXML
	private TextField txtTotalLiberadosVM;
	@FXML
	private TextField txtTotalLiberados;
	@FXML
	private Button btAnalisarUm;
	@FXML
	private Button btAnalisarTodos;
	@FXML
	private Button btCalcular;
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
		try {
			servico.analisarUm(tipo, codigo);
			atualizarEstatisticaGeral();
			atualizarTableView();
		} catch (IntegridadeException e) {
			Alertas.mostrarAlertas("IntegridadeException", "Erro de Digitação", e.getMessage(), AlertType.ERROR);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro na aplicacao do Filtro", e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtAnalisarTodosAction(ActionEvent evento) {
		try {
			servico.analisarTodos();
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro na aplicacao do Filtro", e.getMessage(), AlertType.ERROR);
		} finally {
			atualizarEstatisticaGeral();
			atualizarTableView();
		}
	}

	@FXML
	public void onBtCalcularAction(ActionEvent evento) {
		atualizarEstatisticaGeral();
		atualizarEstatisticaPorCritica();
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
		tableColumnNomeCritica.setCellValueFactory(new PropertyValueFactory<>("nomeCritica"));
		tableColumnFlagAtiva.setCellValueFactory(new PropertyValueFactory<>("flagAtiva"));
		tableColumnAnoMesAnalisado.setCellValueFactory(new PropertyValueFactory<>("anoMesAnalisado"));
		tableColumnRegistrosAnalisados.setCellValueFactory(new PropertyValueFactory<>("registrosAnalisados"));
		tableColumnRegistrosLiberados.setCellValueFactory(new PropertyValueFactory<>("registrosLiberados"));
		tableColumnRegistrosIgnorados.setCellValueFactory(new PropertyValueFactory<>("registrosIgnorados"));
		tableColumnRegistrosPendentes.setCellValueFactory(new PropertyValueFactory<>("registrosPendentes"));

		tableColumnTipoCritica.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnCodCritica.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnFlagAtiva.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnRegistrosAnalisados.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnRegistrosLiberados.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnRegistrosIgnorados.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnRegistrosPendentes.setStyle("-fx-alignment: TOP-RIGHT");

		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodigoCritica);
	}

	public void atualizarTableView() {
		CriticaErpService criticasErpService = new CriticaErpService();
		List<CriticaErp> lista = criticasErpService.pesquisarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewCriticasErp.setItems(obsLista);
		tableViewCriticasErp.refresh();
	}

	private void atualizarEstatisticaGeral() {
		Integer qtdeTotalIndefinidos = servico.getQtdeTotal(null);
		Integer qtdeTotalPendentes = servico.getQtdeTotal("?");
		Integer qtdeTotalIgnorados = servico.getQtdeTotal("N");
		Integer qtdeTotalLiberados = servico.getQtdeTotal("S");
		Integer qtdeTotalRegistros = qtdeTotalLiberados + qtdeTotalIgnorados + qtdeTotalPendentes
				+ qtdeTotalIndefinidos;
		Integer qtdeLiberadosOS = servico.getQtdeLiberadosOS();
		Integer qtdeLiberadosCM = servico.getQtdeLiberadosCM();
		Integer qtdeLiberadosDG = servico.getQtdeLiberadosDG();
		Integer qtdeLiberadosVM = servico.getQtdeLiberadosVM();

		txtTotalIndefinidos.setText(qtdeTotalIndefinidos.toString());
		txtTotalPendentes.setText(qtdeTotalPendentes.toString());
		txtTotalIgnorados.setText(qtdeTotalIgnorados.toString());
		txtTotalLiberados.setText(qtdeTotalLiberados.toString());
		txtTotalRegistros.setText(qtdeTotalRegistros.toString());
		txtTotalLiberadosOS.setText(qtdeLiberadosOS.toString());
		txtTotalLiberadosCM.setText(qtdeLiberadosCM.toString());
		txtTotalLiberadosDG.setText(qtdeLiberadosDG.toString());
		txtTotalLiberadosVM.setText(qtdeLiberadosVM.toString());
		
		txtTotalIndefinidos.setStyle("-fx-alignment: CENTER-RIGHT; -fx-text-inner-color: red;");
		txtTotalPendentes.setStyle("  -fx-alignment: CENTER-RIGHT; -fx-text-inner-color: red;");
		txtTotalIgnorados.setStyle("  -fx-alignment: CENTER-RIGHT");
		txtTotalLiberados.setStyle("  -fx-alignment: CENTER-RIGHT");
		txtTotalRegistros.setStyle("  -fx-alignment: CENTER-RIGHT");
		txtTotalLiberadosOS.setStyle("-fx-alignment: CENTER-RIGHT");
		txtTotalLiberadosCM.setStyle("-fx-alignment: CENTER-RIGHT");
		txtTotalLiberadosDG.setStyle("-fx-alignment: CENTER-RIGHT");
		txtTotalLiberadosVM.setStyle("-fx-alignment: CENTER-RIGHT");
				
	}
	
	private void atualizarEstatisticaPorCritica() {
		servico.atualizarEstatisticaPorCritica();
	}

}
