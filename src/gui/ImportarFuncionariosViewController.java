package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import model.services.ImportarFuncionariosService;

public class ImportarFuncionariosViewController implements Initializable {

	@FXML
	private Label labelMsgGeral;
	@FXML
	private Label labelMsgArqEntrada;
	@FXML
	private Label labelMsgRegLidos;
	@FXML
	private Label labelMsgRegCorrompidos;
	@FXML
	private Label labelMsgRegDeletados;
	@FXML
	private Label labelMsgRegIncluidos;
	@FXML
	private Button btImportar;
	@FXML
	private Button btSair;
	@FXML
	private TextField txtRegLidos;
	@FXML
	private TextField txtRegCorrompidos;
	@FXML
	private TextField txtRegDeletados;
	@FXML
	private TextField txtRegIncluidos;
	
	@FXML
	public void onBtImportarAction(ActionEvent evento) {
		ImportarFuncionariosService servico = new ImportarFuncionariosService();
		servico.processarTXT();
		atualizarTela(servico);
	}
	
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private void atualizarTela(ImportarFuncionariosService servico) {
		Integer qtdeLidas = servico.getQtdeLidas();
		Integer qtdeCorrompidas = servico.getQtdeCorrompidas();
		Integer qtdeDeletadas = servico.getQtdeDeletadas();
		Integer qtdeIncluidas = servico.getQtdeIncluidas();
		String arqEntrada = servico.getEntrada();
		
		txtRegLidos.setText(qtdeLidas.toString());
		txtRegCorrompidos.setText(qtdeCorrompidas.toString());
		txtRegDeletados.setText(qtdeDeletadas.toString());
		txtRegIncluidos.setText(qtdeIncluidas.toString());

		if ( (qtdeLidas > 0) && (qtdeLidas - qtdeIncluidas) == 0 ) {
			labelMsgGeral.setText("Processo Concluido com Sucesso");
			labelMsgGeral.setTextFill(Color.BLUE);
		}
		else {
			labelMsgGeral.setText("Processo Terminado com Pendencias.");
			labelMsgGeral.setTextFill(Color.RED);
		}
		
		labelMsgArqEntrada.setText(arqEntrada);
		
		labelMsgRegLidos.setText(null);
		labelMsgRegCorrompidos.setText(null);
		labelMsgRegDeletados.setText(null);
		labelMsgRegIncluidos.setText(null);

		if (qtdeLidas > 0) {
			labelMsgRegLidos.setText("Informação: Qtde de linhas lidas do Arquivo TXT");
		}
		if (qtdeCorrompidas > 0) {
			labelMsgRegCorrompidos.setText("PENDENCIA: Fazer correções no arquivo TXT gerado pela Folha");
		}
		if (qtdeDeletadas > 0) {
			labelMsgRegDeletados.setText("Informação: Registros deletados de uma importação Anterior");
		}
		if (qtdeIncluidas > 0) {
			labelMsgRegIncluidos.setText("Informação: Registros incluidos na importação Atual");
		}
	}
}
