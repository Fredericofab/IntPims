
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.PoliticasErp;
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
	private TableColumn<PoliticasErp, String> tableColumnIMP;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnVM;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnCM;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnDG;
	@FXML
	private TableColumn<PoliticasErp, String> tableColumnOS;

	@FXML
	private TextField txtTotalRegistros;
	@FXML
	private TextField txtTotalNaoCalculados;
	@FXML
	private TextField txtTotalIndefinidos;
	@FXML
	private TextField txtImpPendentes;
	@FXML
	private TextField txtImpIgnorados;
	@FXML
	private TextField txtImpLiberados;
	@FXML
	private TextField txtImpLiberadosOS;
	@FXML
	private TextField txtImpLiberadosCM;
	@FXML
	private TextField txtImpLiberadosDG;
	@FXML
	private TextField txtVmPendentes;
	@FXML
	private TextField txtVmIgnorados;
	@FXML
	private TextField txtVmlLiberados;
	@FXML
	private Button btAplicarTodas;
	@FXML
	private Button btCalcular;
	@FXML
	private Button btSair;
	@FXML
	private Label labelEvolucao;
	
	private ObservableList<PoliticasErp> obsLista;

	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@FXML
	public void onBtAplicarTodasAction(ActionEvent evento) {
		try {
			labelEvolucao.setText("Aplicando Politicas. Aguarde...");
			servico.aplicarTodas();
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro na aplicacao do Filtro", e.getMessage(), AlertType.ERROR);
		} finally {
			labelEvolucao.setText("Atualizando as Estatisticas. Aguarde...");
			atualizarEstatisticaGeral();
			labelEvolucao.setText("Atualizando as Estatisticas Por Politica. Aguarde...");
			atualizarEstatisticasPorPolitica();
			atualizarTableView();
			labelEvolucao.setText("Processo Concluido com Sucesso");
		}
	}

	@FXML
	public void onBtCalcularAction(ActionEvent evento) {
		labelEvolucao.setText("Calculando as Estatisticas. Aguarde...");
		atualizarEstatisticaGeral();
		atualizarEstatisticasPorPolitica();
		atualizarTableView();
		labelEvolucao.setText("Processo Concluido com Sucesso");
	}

	public void setAplicarPoliticaErpServico(AplicarPoliticasErpService servico) {
		this.servico = servico;
	}

	private void atualizarEstatisticaGeral() {
		servico.atualizarEstatisticaGeral();
		Integer qtdeTotalRegistros = servico.getTotalRegistros();
		Integer qtdeTotalNaoCalculados = servico.getQtdeNaoCalculados();
		Integer qtdeTotalIndefinidos = servico.getQtdeIndefinidos();
		Integer qtdeImpPendentes = servico.getQtdeImportarI();
		Integer qtdeImpIgnorados = servico.getQtdeImportarN();
		Integer qtdeImpLiberados = servico.getQtdeImportarS();
		Integer qtdeImpLiberadosOS = servico.getQtdeImpLiberadosOS();
		Integer qtdeImpLiberadosCM = servico.getQtdeImpLiberadosCM();
		Integer qtdeImpLiberadosDG = servico.getQtdeImpLiberadosDG();
		Integer qtdeVmPendentes = servico.getQtdeValorMaterialI();
		Integer qtdeVmIgnorados = servico.getQtdeValorMaterialN();
		Integer qtdeVmLiberados = servico.getQtdeValorMaterialS();

		txtTotalRegistros.setText(qtdeTotalRegistros.toString());
		txtTotalNaoCalculados.setText(qtdeTotalNaoCalculados.toString());
		txtTotalIndefinidos.setText(qtdeTotalIndefinidos.toString());
		txtImpPendentes.setText(qtdeImpPendentes.toString());
		txtImpIgnorados.setText(qtdeImpIgnorados.toString());
		txtImpLiberados.setText(qtdeImpLiberados.toString());
		txtImpLiberadosOS.setText(qtdeImpLiberadosOS.toString());
		txtImpLiberadosCM.setText(qtdeImpLiberadosCM.toString());
		txtImpLiberadosDG.setText(qtdeImpLiberadosDG.toString());
		txtVmPendentes.setText(qtdeVmPendentes.toString());
		txtVmIgnorados.setText(qtdeVmIgnorados.toString());
		txtVmlLiberados.setText(qtdeVmLiberados.toString());

		txtTotalRegistros.setStyle("  -fx-alignment: CENTER-RIGHT");
		txtTotalNaoCalculados.setStyle("  -fx-alignment: CENTER-RIGHT");
		txtTotalIndefinidos.setStyle("-fx-alignment: CENTER-RIGHT; -fx-text-inner-color: red;");
		txtImpPendentes.setStyle("  -fx-alignment: CENTER-RIGHT; -fx-text-inner-color: red;");
		txtImpIgnorados.setStyle("  -fx-alignment: CENTER-RIGHT");
		txtImpLiberados.setStyle("  -fx-alignment: CENTER-RIGHT");
		txtImpLiberadosOS.setStyle("-fx-alignment: CENTER-RIGHT");
		txtImpLiberadosCM.setStyle("-fx-alignment: CENTER-RIGHT");
		txtImpLiberadosDG.setStyle("-fx-alignment: CENTER-RIGHT");
		txtVmPendentes.setStyle("  -fx-alignment: CENTER-RIGHT; -fx-text-inner-color: red;");
		txtVmIgnorados.setStyle("  -fx-alignment: CENTER-RIGHT");
		txtVmlLiberados.setStyle("-fx-alignment: CENTER-RIGHT");
	}

	private void atualizarEstatisticasPorPolitica() {
		servico.atualizarEstatisticasPorPolitica();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		tableColumnCodPolitica.setCellValueFactory(new PropertyValueFactory<>("codPolitica"));
		tableColumnNomePolitica.setCellValueFactory(new PropertyValueFactory<>("nomePolitica"));
		tableColumnFlagAtiva.setCellValueFactory(new PropertyValueFactory<>("flagAtiva"));
		tableColumnAnoMesAnalisado.setCellValueFactory(new PropertyValueFactory<>("anoMesAnalisado"));
		tableColumnIMP.setCellValueFactory(new PropertyValueFactory<>("importar"));
		tableColumnCM.setCellValueFactory(new PropertyValueFactory<>("salvarCstg_IntCM"));
		tableColumnDG.setCellValueFactory(new PropertyValueFactory<>("salvarCstg_IntDG"));
		tableColumnOS.setCellValueFactory(new PropertyValueFactory<>("salvarOS_Material"));
		tableColumnVM.setCellValueFactory(new PropertyValueFactory<>("salvarCstg_IntVM"));
		tableColumnQtdeAplicados.setCellValueFactory(new PropertyValueFactory<>("registrosAplicados"));
		

		tableColumnCodPolitica.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnQtdeAplicados.setStyle("-fx-alignment: TOP-RIGHT");
		tableColumnFlagAtiva.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnVM.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnIMP.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnOS.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnCM.setStyle("-fx-alignment: TOP-CENTER");
		tableColumnDG.setStyle("-fx-alignment: TOP-CENTER");
	}

	public void atualizarTableView() {
		PoliticasErpService politicasErpService = new PoliticasErpService();
		List<PoliticasErp> lista = politicasErpService.pesquisarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewPoliticasErp.setItems(obsLista);
		tableViewPoliticasErp.refresh();
	}

}
