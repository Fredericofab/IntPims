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

public class ValidarErpViewController implements Initializable {

	@FXML
	private Label labelMsgGeral;
	@FXML
	private Label labelMsgRegProcessados;
	@FXML
	private Label labelMsgCCInexistentes;
	@FXML
	private Label labelMsgMatSemConversao;
	@FXML
	private Label labelMsgOSInexistentes;
	@FXML
	private Label labelMsgOSIncoerentes;
	@FXML
	private Label labelMsgOSAntigas;
	@FXML
	private Button btValidar;
	@FXML
	private Button btSair;
	@FXML
	private TextField txtRegProcessados;
	@FXML
	private TextField txtRegCCInexistentes;
	@FXML
	private TextField txtMatSemConversao;
	@FXML
	private TextField txtOSInexistentes;
	@FXML
	private TextField txtOSIncoerentes;
	@FXML
	private TextField txtOSAntigas;
	
	@FXML
	public void onBtValidarAction(ActionEvent evento) {
//		ValidarErpService servico = new ValidarErpService();
//		servico.validarERP();
//		atualizarTela(servico);
	}
	
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

//	private void atualizarTela(ValidarErpService servico) {
//		Integer qtdeProcessados = servico.getQtdeProcessados();
//		Integer qtdeCCInexistentes = servico.getQtdeCCInexistentes();
//		Integer qtdeMatSemConversao = servico.getQtdeMatSemConversao();
//		Integer qtdeOSIncoerentes = servico.getQtdeOSIncoerentes();
//		Integer qtdeOSInexistentes = servico.getQtdeOSInexistente();
//		Integer qtdeOSAntigas = servico.getQtdeOSAntigas();
//		Integer qtdeTotalErros = qtdeCCInexistentes + qtdeMatSemConversao +
//				 qtdeOSInexistentes + qtdeOSIncoerentes + qtdeOSAntigas;
//
//		txtRegProcessados.setText(qtdeProcessados.toString());
//		txtRegCCInexistentes.setText(qtdeCCInexistentes.toString());
//		txtMatSemConversao.setText(qtdeMatSemConversao.toString());
//		txtOSIncoerentes.setText(qtdeOSIncoerentes.toString());
//		txtOSInexistentes.setText(qtdeOSInexistentes.toString());
//		txtOSAntigas.setText(qtdeOSAntigas.toString());
//		
//		if ( (qtdeProcessados > 0) && (qtdeTotalErros == 0 )) {
//			labelMsgGeral.setText("Processo Concluido com Sucesso");
//			labelMsgGeral.setTextFill(Color.BLUE);
//		}
//		else {
//			labelMsgGeral.setText("Processo Terminado com Pendencias.");
//			labelMsgGeral.setTextFill(Color.RED);
//		}
//		
//		labelMsgRegProcessados.setText(null);
//		labelMsgCCInexistentes.setText(null);
//		labelMsgMatSemConversao.setText(null);
//		labelMsgOSInexistentes.setText(null);
//		labelMsgOSIncoerentes.setText(null);
//		labelMsgOSAntigas.setText(null);
//
//		if (qtdeProcessados > 0) {
//			labelMsgRegProcessados.setText("Informação: Qtde de Registros no Movimento Erp");
//		}
//		if (qtdeCCInexistentes > 0) {
//			labelMsgCCInexistentes.setText("PENDENCIA: Verificar no Movimento a Politica CCInexistentes");
//		}
//		if (qtdeMatSemConversao > 0) {
//			labelMsgMatSemConversao.setText("PENDENCIA: Fazer Manutenção no Cadastro de Conversao de UN");
//		}
//		if (qtdeOSIncoerentes > 0) {
//			labelMsgOSInexistentes.setText("PENDENCIA: Verificar no Movimento a Politica OSIncoerentes");
//		}
//		if (qtdeOSInexistentes > 0) {
//			labelMsgOSIncoerentes.setText("PENDENCIA: Verificar no Movimento a Politica OSInexistentes");
//		}
//	}
}
