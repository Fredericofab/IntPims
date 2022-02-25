package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import model.services.ValidarErpService;

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
	private Label labelMsgFaltaOSouFrotaCC;
	@FXML
	private Label labelMsgOSValidas;
	@FXML
	private Label labelMsgOSInexistentes;
	@FXML
	private Label labelMsgOSIncoerentes;
	@FXML
	private Label labelMsgOSAntigas;
	@FXML
	private Label labelMsgMatSemOS;
	@FXML
	private Button btValidar;
	@FXML
	private Button btSair;
	@FXML
	private TextField txtRegProcessados;
	@FXML
	private TextField txtCCInexistentes;
	@FXML
	private TextField txtMatSemConversao;
	@FXML
	private TextField txtMatSemOS;
	@FXML
	private TextField txtFaltaOSouFrotaCC;
	@FXML
	private TextField txtOSValidas;
	@FXML
	private TextField txtOSInexistentes;
	@FXML
	private TextField txtOSIncoerentes;
	@FXML
	private TextField txtOSAntigas;
	@FXML
	private TextField txtCCInexistentesDistintos;
	@FXML
	private TextField txtMatSemConversaoDistintos;
	@FXML
	private TextField txtOSValidasDistintas;
	@FXML
	private TextField txtOSInexistentesDistintas;
	@FXML
	private TextField txtOSIncoerentesDistintas;
	@FXML
	private TextField txtOSAntigasDistintas;

	@FXML
	public void onBtValidarAction(ActionEvent evento) {
		try {
			ValidarErpService servico = new ValidarErpService();
			servico.validarERP();
			atualizarTela(servico);
		}
		catch (DbException e) {
			e.printStackTrace();
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo",
					e.getMessage() + "\n \n",
					AlertType.ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			Alertas.mostrarAlertas("IOException", "Erro no Acesso aos Arquivos de Log \n \n",
					e.getMessage() + "\n \n",
					AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private void atualizarTela(ValidarErpService servico) {
		Integer qtdeProcessados = servico.getQtdeProcessados();
		Integer qtdeCCInexistentes = servico.getQtdeCCInexistentes();
		Integer qtdeCCInexistentesDistintos = servico.getQtdeCCInexistentesDistintos();
		Integer qtdeMatSemConversao = servico.getQtdeMatSemConversao();
		Integer qtdeMatSemConversaoDistintos = servico.getQtdeMatSemConversaoDistintos();
		Integer qtdeOSValidas = servico.getQtdeOSValidas();
		Integer qtdeOSValidasDistintas = servico.getQtdeOSValidasDistintas();
		Integer qtdeOSInexistentes = servico.getQtdeOSInexistentes();
		Integer qtdeOSInexistentesDistintas = servico.getQtdeOSInexistentesDistintas();
		Integer qtdeOSIncoerentes = servico.getQtdeOSIncoerentes();
		Integer qtdeOSIncoerentesDistintas = servico.getQtdeOSIncoerentesDistintas();
		Integer qtdeOSAntigas = servico.getQtdeOSAntigas();
		Integer qtdeOSAntigasDistintas = servico.getQtdeOSAntigasDistintas();
		Integer qtdeMatSemOS = servico.getQtdeMatSemOS();
		Integer qtdeFaltaOSouFrotaCC = servico.getQtdeFaltaOSouFrotaCC();
		Integer qtdeTotalErros = qtdeCCInexistentes + qtdeMatSemConversao + qtdeFaltaOSouFrotaCC +
				 qtdeOSInexistentes + qtdeOSIncoerentes + qtdeOSAntigas;

		txtRegProcessados.setText(qtdeProcessados.toString());
		txtCCInexistentes.setText(qtdeCCInexistentes.toString());
		txtCCInexistentesDistintos.setText(qtdeCCInexistentesDistintos.toString());
		txtMatSemConversao.setText(qtdeMatSemConversao.toString());
		txtMatSemConversaoDistintos.setText(qtdeMatSemConversaoDistintos.toString());
		txtOSValidas.setText(qtdeOSValidas.toString());
		txtOSValidasDistintas.setText(qtdeOSValidasDistintas.toString());
		txtOSInexistentes.setText(qtdeOSInexistentes.toString());
		txtOSInexistentesDistintas.setText(qtdeOSInexistentesDistintas.toString());
		txtOSIncoerentes.setText(qtdeOSIncoerentes.toString());
		txtOSIncoerentesDistintas.setText(qtdeOSIncoerentesDistintas.toString());
		txtOSAntigas.setText(qtdeOSAntigas.toString());
		txtOSAntigasDistintas.setText(qtdeOSAntigasDistintas.toString());
		txtFaltaOSouFrotaCC.setText(qtdeFaltaOSouFrotaCC.toString());
		txtMatSemOS.setText(qtdeMatSemOS.toString());
		
		if ( (qtdeProcessados > 0) && (qtdeTotalErros == 0 )) {
			labelMsgGeral.setText("Processo Concluido com Sucesso");
			labelMsgGeral.setTextFill(Color.BLUE);
		}
		else {
			labelMsgGeral.setText("Processo Terminado com Pendencias.");
			labelMsgGeral.setTextFill(Color.RED);
		}
		
		labelMsgRegProcessados.setText(null);
		labelMsgCCInexistentes.setText(null);
		labelMsgMatSemConversao.setText(null);
		labelMsgOSInexistentes.setText(null);
		labelMsgOSIncoerentes.setText(null);
		labelMsgOSAntigas.setText(null);
		labelMsgMatSemOS.setText(null);
		labelMsgFaltaOSouFrotaCC.setText(null);

		if (qtdeProcessados > 0) {
			labelMsgRegProcessados.setText("Informação: Qtde de Registros no Movimento Erp");
		}
		if (qtdeCCInexistentes > 0) {
			labelMsgCCInexistentes.setText("PENDENCIA: Centros de Custos não cadastrados no PimsCS. Ver LogCC");
		}
		if (qtdeOSValidas > 0) {
			labelMsgOSValidas.setText("Informação: Qtde de Registros com OS Validas no Movimento Erp");
		}
		if (qtdeOSInexistentes > 0) {
			labelMsgOSInexistentes.setText("PENDENCIA: Ordem de Serviço nao existe no Manfro. Ver LogOS.");
		}
		if (qtdeOSIncoerentes > 0) {
			labelMsgOSIncoerentes.setText("PENDENCIA: Ordem de Serviço não é para a Frota/CC informado. Ver LogOS.");
		}
		if (qtdeOSAntigas > 0) {
			labelMsgOSAntigas.setText("PENDENCIA: Ordem de Serviço fechada a muitos dias. Ver LogOS.");
		}

		if (qtdeMatSemConversao > 0) {
			labelMsgMatSemConversao.setText("PENDENCIA: Fazer Manutenção na Tabela Fator de Medidas.");
		}
		
		if (qtdeMatSemOS > 0) {
			labelMsgMatSemOS.setText("ALERTA: Existem Movimentos para Oficina que nao tem OS. Ver LogOS.");
		}
		if (qtdeFaltaOSouFrotaCC > 0) {
			labelMsgFaltaOSouFrotaCC.setText("PENDENCIA: Se preenchido, necessário os dois campos. Ver LogOS.");
		}
	}
}
