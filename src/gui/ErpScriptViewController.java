package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DadosAlteradosListener;
import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import model.exceptions.ParametroInvalidoException;
import model.services.ErpScriptService;
import model.services.ParametrosService;

public class ErpScriptViewController implements Initializable {

	private ParametrosService parametrosService = new ParametrosService();
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();

//	prametros
	String arqScriptPasta;

	
	@FXML
	private Label labelMsgGeral;
	@FXML
	private Label labelMsgArqScript;

	@FXML
	private Button btProcessar;
	@FXML
	private Button btSair;
	@FXML
	private TextField txtNomeScript;
	@FXML
	private TextField txtRegLidos;
	@FXML
	private TextField txtRegProcessados;

	@FXML
	public void onBtProcessarAction(ActionEvent evento) {
		ErpScriptService servico = new ErpScriptService();
		String arqScript = arqScriptPasta + txtNomeScript.getText();
		servico.processarScript(arqScript);
		atualizarTela(servico);
		notificarDadosAlteradosListeners();
	}
	
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			lerParametros();
			labelMsgArqScript.setText(arqScriptPasta);
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. Aplicar Script no Erp", e.getMessage(),AlertType.ERROR);
		}
	}

	private void atualizarTela(ErpScriptService servico) {
		Integer qtdeLidas = servico.getQtdeLidas();
		Integer qtdeProcessadas = servico.getQtdeProcessadas();
		
		txtRegLidos.setText(qtdeLidas.toString());
		txtRegProcessados.setText(qtdeProcessadas.toString());

		if ( ( qtdeLidas > 0 ) && ( qtdeLidas - qtdeProcessadas == 0 ))  {
			labelMsgGeral.setText("Processo Concluido com Sucesso");
			labelMsgGeral.setTextFill(Color.BLUE);
		}
		else {
			labelMsgGeral.setText("Processo Terminado com Pendencias.");
			labelMsgGeral.setTextFill(Color.RED);
		}
	}

	public void serOuvinteDeDadosAlteradosListener(DadosAlteradosListener ouvinte) {
		ouvintes.add(ouvinte);
	}
	
	private void notificarDadosAlteradosListeners() {
		for (DadosAlteradosListener ouvinte : ouvintes) {
			ouvinte.onDadosAlterados();
		}
	}
	
	private void lerParametros() {
		arqScriptPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqScriptPasta")).getValor();
	}
}
