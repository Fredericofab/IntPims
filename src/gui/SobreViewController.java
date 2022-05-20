package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class SobreViewController implements Initializable {
	
	@FXML
	private Label labelVersao;
	@FXML
	private Label labelDesenvolvido;
	@FXML
	private TextArea txtAreaSobre;
	@FXML
	private Button btSair;
	
	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		String texto = auxMontaTexto();
		txtAreaSobre.setText(texto);
		labelVersao.setText("Vers�o 1.65 : 20/05/2022");
		labelDesenvolvido.setText("Desenvolvido por Frederico Barros");
	}

	private String auxMontaTexto() {
		StringBuilder sb = new StringBuilder();
		sb.append("O sistema IntPims foi desenvolvido para a Itapecuru Bioenergia com a finalidade \n");
		sb.append("de permitir captar e tratar os arquivos de interface que alimentar�o o m�dulo \n");
		sb.append("de custos (CUSTAG) do PimsCS.");
		sb.append("\n \n");
		sb.append("Para maiores informa��es consultar: \n");
		sb.append("	   * IntPims - Manual do Usu�rio.pdf \n");
		sb.append("	   * IntPims - Manual T�cnico.pdf \n");
		return sb.toString();
	}
}
