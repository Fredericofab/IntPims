package gui;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.services.SumarizarFolhaService;

public class SumarizarFolhaViewController implements Initializable {

	@FXML
	private Button btSumarizar;
	@FXML
	private Button btSair;

	@FXML
	private TextField txtRegLidos;
	@FXML
	private TextField txtQtdeCCustos;
	@FXML
	private TextField txtValortotal;
	@FXML
	private TextField txtValorExportarSim;
	@FXML
	private TextField txtValorExportarNao;

	@FXML
	public void onBtSumarizarAction(ActionEvent evento) {
		SumarizarFolhaService servico = new SumarizarFolhaService();
		servico.processar();
		atualizarTela(servico);
	}
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	
	private void atualizarTela(SumarizarFolhaService servico) {
		Integer qtdeLidas = servico.getQtdeLidas();
		Integer qtdeCCustos = servico.getQtdeCCustos();
		Double valorTotal = servico.getValorTotal();

		Double valorExportarSim = servico.getValorExportarSim();
		Double valorExportarNao = servico.getValorExportarNao();
		
		txtRegLidos.setText(qtdeLidas.toString());
		txtQtdeCCustos.setText(qtdeCCustos.toString());

		Locale.setDefault(Locale.US);
		txtValortotal.setText(Utilitarios.formatarNumeroDecimalComMilhar('.', ' ').format(valorTotal)) ; 
		txtValorExportarSim.setText(Utilitarios.formatarNumeroDecimalComMilhar('.', ' ').format(valorExportarSim));
		txtValorExportarNao.setText(Utilitarios.formatarNumeroDecimalComMilhar('.', ' ').format(valorExportarNao));
		
		txtRegLidos.setStyle("-fx-alignment: CENTER-RIGHT");
		txtQtdeCCustos.setStyle("-fx-alignment: CENTER-RIGHT");
		txtValortotal.setStyle("-fx-alignment: CENTER-RIGHT");
		txtValorExportarSim.setStyle("-fx-alignment: CENTER-RIGHT");
		txtValorExportarNao.setStyle("-fx-alignment: CENTER-RIGHT");
	}
}
