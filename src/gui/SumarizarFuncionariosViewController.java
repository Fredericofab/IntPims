package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.services.SumarizarFuncionariosService;

public class SumarizarFuncionariosViewController implements Initializable {

	@FXML
	private Button btSumarizar;
	@FXML
	private Button btSair;

	@FXML
	private TextField txtRegLidos;
	@FXML
	private TextField txtQtdeCCustos;

	@FXML
	public void onBtSumarizarAction(ActionEvent evento) {
		SumarizarFuncionariosService servico = new SumarizarFuncionariosService();
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
	
	private void atualizarTela(SumarizarFuncionariosService servico) {
		Integer qtdeLidas = servico.getQtdeLidas();
		Integer qtdeCCustos = servico.getQtdeCCustos();

		txtRegLidos.setText(qtdeLidas.toString());
		txtQtdeCCustos.setText(qtdeCCustos.toString());

		txtRegLidos.setStyle("-fx-alignment: CENTER-RIGHT");
		txtQtdeCCustos.setStyle("-fx-alignment: CENTER-RIGHT");
	}
}
