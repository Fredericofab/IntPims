
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
import model.entities.PoliticasErp;
import model.exceptions.IntegridadeException;
import model.services.AplicarPoliticasErpService;
import model.services.PoliticasErpService;

public class AplicarPoliticasErpViewController implements Initializable {

	private AplicarPoliticasErpService servico;

	@FXML
	private TableView<PoliticasErp> tableViewPoliticasErp;
	@FXML
	private TableColumn<PoliticasErp, Integer> tableColumnCodPolitica;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnNomePolitica;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnFlagAtiva;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnAnoMesAnalisado;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnQtdeAplicados;

	@FXML
	private TextField txtCodPolitica;
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
	private Button btAplicarUma;
	@FXML
	private Button btAplicarTodas;
	@FXML
	private Button btCalcular;
	@FXML
	private Button btSair;

	private ObservableList<PoliticasErp> obsLista;

	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@FXML
	public void onBtAplicarUmaAction(ActionEvent evento) {
		Integer codigo = Utilitarios.tentarConverterParaInt(txtCodPolitica.getText());
		try {
			servico.aplicarUma(codigo);
			atualizarEstatisticaGeral();
			atualizarTableView();
		} catch (IntegridadeException e) {
			Alertas.mostrarAlertas("IntegridadeException", "Erro de Digitação", e.getMessage(), AlertType.ERROR);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro na aplicacao do Filtro", e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtAplicarTodasAction(ActionEvent evento) {
		try {
			servico.aplicarTodas();
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
		atualizarEstatisticaPorPolitica();
		atualizarTableView();
	}

	public void setAplicarPoliticaErpServico(AplicarPoliticasErpService servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		tableColumnCodPolitica.setCellValueFactory(new PropertyValueFactory<>("codPolitica"));
		tableColumnNomePolitica.setCellValueFactory(new PropertyValueFactory<>("nomePolitica"));
		tableColumnFlagAtiva.setCellValueFactory(new PropertyValueFactory<>("flagAtiva"));

		tableColumnCodPolitica.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnFlagAtiva.setStyle("-fx-alignment: TOP-CENTER");

		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodPolitica);
	}

	public void atualizarTableView() {
		PoliticasErpService politicasErpService = new PoliticasErpService();
		List<PoliticasErp> lista = politicasErpService.pesquisarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewPoliticasErp.setItems(obsLista);
		tableViewPoliticasErp.refresh();
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
	
	private void atualizarEstatisticaPorPolitica() {
		servico.atualizarEstatisticaPorPolitica();
	}

}
