package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.services.ContabilizarService;

public class ContabilizarViewController implements Initializable {

	@FXML
	private Button btProcessar;
	@FXML
	private Button btSair;
	@FXML
	private TextField txtRegPlcPrim;
	@FXML
	private TextField txtRegPlcRat;
	@FXML
	private TextField txtRegCCustos;
	
	@FXML
	public void onBtProcessarAction(ActionEvent evento) {
		ContabilizarService servico = new ContabilizarService();
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

	private void atualizarTela(ContabilizarService servico) {
		Integer qtdePlcPrim = servico.getQtdePlcPrim();
		Integer qtdePlcRat = servico.getQtdePlcRat();
		Integer qtdeCCustos = servico.getQtdeCCustos();
		
		txtRegPlcPrim.setText(qtdePlcPrim.toString());
		txtRegPlcRat.setText(qtdePlcRat.toString());
		txtRegCCustos.setText(qtdeCCustos.toString());
	}
}
