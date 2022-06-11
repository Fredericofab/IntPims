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
import model.services.ParametrosService;

public class ContabilizarViewController implements Initializable {

	private ParametrosService parametrosService = new ParametrosService();

//	Parametros
	String anoMes;
	
	Double ccusto;

	@FXML
	private Button btProcessar;
	@FXML
	private Button btSair;
	@FXML
	private TextField txtAnoMes;
	@FXML
	private TextField txtCCusto;
	@FXML
	private TextField txtRegPlcPrim;
	@FXML
	private TextField txtRegPlcRat;
	@FXML
	private TextField txtRegCCustos;
	
	@FXML
	public void onBtProcessarAction(ActionEvent evento) {
		ContabilizarService servico = new ContabilizarService();
		getDadosDoForm();
		servico.processar(anoMes, ccusto);
		atualizarTela(servico);
	}
	

	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		txtAnoMes.setText(anoMes);
		txtAnoMes.setText("201701");
		txtCCusto.setText("10072302");
System.out.println("ContabilizarViewController - excluir 2 acima");		
	}
	
	private void atualizarTela(ContabilizarService servico) {
		Integer qtdePlcPrim = servico.getQtdePlcPrim();
		Integer qtdePlcRat = servico.getQtdePlcRat();
		Integer qtdeCCustos = servico.getQtdeCCustos();
		
		txtRegPlcPrim.setText(qtdePlcPrim.toString());
		txtRegPlcRat.setText(qtdePlcRat.toString());
		txtRegCCustos.setText(qtdeCCustos.toString());
	}

	private void getDadosDoForm() {
		anoMes = txtAnoMes.getText();
		ccusto = Utilitarios.tentarConverterParaDouble(txtCCusto.getText());
	}

	private void lerParametros() {
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}

}
