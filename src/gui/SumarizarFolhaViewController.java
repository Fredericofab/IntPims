package gui;

import java.net.URL;
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
	private TextField txQtdeCCustos;
	@FXML
	private TextField txtValorExportarSim;
	@FXML
	private TextField txtValorExportarNao;


	@Override
	public void initialize(URL url, ResourceBundle rb) {
//		inicializarComponentes();
	}

//	private void inicializarComponentes() {
//		txtPastaOrigem.setText("C:\\Projeto Itapecuru Custag\\IGP TG\\entrada\\");
//		txtArquivoOrigem.setText("Folha202109.TXT");
//		txtAnoMes.setText("202109");
//	}
	
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
	
	@FXML
	public void onBtSumarizarAction(ActionEvent evento) {
		SumarizarFolhaService servico = new SumarizarFolhaService();
		servico.processar();
		atualizarTela(servico);
	}

	private void atualizarTela(SumarizarFolhaService servico) {
		Integer qtdeLidas = servico.getQtdeLidas();
		
		txtRegLidos.setText(qtdeLidas.toString());
	}
}
