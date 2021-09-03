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
import model.services.ImportarFolhaService;

public class ImportarFolhaViewController implements Initializable {

	@FXML
	private TextField txtPastaOrigem;

	@FXML
	private TextField txtArquivoOrigem;

	@FXML
	private TextField txtAnoMes;

	@FXML
	private Label labelMsgGeral;

	@FXML
	private Label labelMsgRegLidos;

	@FXML
	private Label labelMsgRegCorrompidos;

	@FXML
	private Label labelMsgVerbasDistintas;

	@FXML
	private Label labelMsgVerbasSemDefinicao;

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
	private TextField txtVerbasDistintas;

	@FXML
	private TextField txtVerbasSemDefinicao;

	@FXML
	private TextField txtRegDeletados;
	
	@FXML
	private TextField txtRegIncluidos;


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		txtPastaOrigem.setText("C:\\Projeto Itapecuru Custag\\IGP TG\\entrada\\");
		txtArquivoOrigem.setText("FolhaAAAAMM.TXT");
		txtAnoMes.setText("202109");
	}
	
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
	
	@FXML
	public void onBtImportarAction(ActionEvent evento) {
		ImportarFolhaService servico = new ImportarFolhaService();
		String entrada = txtPastaOrigem.getText() + txtArquivoOrigem.getText();
		String anoMes = txtAnoMes.getText();
		servico.processarTXT(entrada, anoMes);
		atualizarTela(servico);
	}

	private void atualizarTela(ImportarFolhaService servico) {
		Integer qtdeLidas = servico.getQtdeLidas();
		Integer qtdeCorrompidas = servico.getQtdeCorrompidas();
		Integer qtdeVerbasDistintas = servico.getqtdeVerbasDistintas();
		Integer qtdeVerbasSemDefinicao = servico.getQtdeVerbasSemDefinicao();
		Integer qtdeDeletadas = servico.getQtdeDeletadas();
		Integer qtdeIncluidas = servico.getQtdeIncluidas();
		
		txtRegLidos.setText(qtdeLidas.toString());
		txtRegCorrompidos.setText(qtdeCorrompidas.toString());
		txtVerbasDistintas.setText(qtdeVerbasDistintas.toString());
		txtVerbasSemDefinicao.setText(qtdeVerbasSemDefinicao.toString());
		txtRegDeletados.setText(qtdeDeletadas.toString());
		txtRegIncluidos.setText(qtdeIncluidas.toString());
		
		if (qtdeLidas ==  qtdeIncluidas) {
			labelMsgGeral.setText("Processo Concluido com Sucesso");
			labelMsgGeral.setTextFill(Color.BLUE);
		}
		else {
			labelMsgGeral.setText("Processo Terminado com Pendencias.");
			labelMsgGeral.setTextFill(Color.RED);
		}
		
		labelMsgRegLidos.setText(null);
		labelMsgRegCorrompidos.setText(null);
		labelMsgVerbasDistintas.setText(null);
		labelMsgVerbasSemDefinicao.setText(null);
		labelMsgRegDeletados.setText(null);
		labelMsgRegIncluidos.setText(null);

		if (qtdeLidas > 0) {
			labelMsgRegLidos.setText("Informa��o: Qtde de linhas no Arquivo TXT");
		}
		if (qtdeCorrompidas > 0) {
			labelMsgRegCorrompidos.setText("PENDENCIA: Fazer corre��es no arquivo TXT gerado pela Folha");
		}
		if (qtdeVerbasDistintas > 0) {
			labelMsgVerbasDistintas.setText("Informa��o: Qtde de Verbas distintas no Arquivo TXT");
		}
		if (qtdeVerbasSemDefinicao > 0) {
			labelMsgVerbasSemDefinicao.setText("PENDENCIA: Fazer Manuten��o no Cadastro de Verbas");
		}
		if (qtdeDeletadas > 0) {
			labelMsgRegDeletados.setText("Informa��o: Registros deletados de uma importa��o Anterior");
		}
		if (qtdeIncluidas > 0) {
			labelMsgRegIncluidos.setText("Informa��o: Registros incluidos na importa��o Atual");
		}
	}
}