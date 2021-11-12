package gui.util;

import javafx.scene.control.TextField;

public class RestricoesDeDigitacao {


	public static void soPermiteTextFieldInteiro(TextField txt) {
		txt.textProperty().addListener((obs, valorVelho, valorNovo) -> {
			if (valorNovo != null && !valorNovo.matches("\\d*")) {
				txt.setText(valorVelho);
			}
		});
	}
	
	public static void soPermiteTextFieldTamanhoMax(TextField txt, int max) {
		txt.textProperty().addListener((obs, valorVelho, valorNovo) -> {
			if (valorNovo != null && valorNovo.length() > max) {
				txt.setText(valorVelho);
			}
		});
	}

	public static void soPermiteTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, valorVelho, valorNovo) -> {
			if (valorNovo != null && !valorNovo.matches("\\d*([\\.]\\d*)?")) {
				txt.setText(valorVelho);
			}
		});
	}


	public static void soPermiteTextFieldSN(TextField txt) {
		txt.textProperty().addListener((obs, valorVelho, valorNovo) -> {
			if (valorNovo != null && !valorNovo.matches("[SNsn]")) {
				txt.setText(valorVelho);
			}
		});
	}
	
	public static void soPermiteTextFieldSNinterrogacao(TextField txt) {
		txt.textProperty().addListener((obs, valorVelho, valorNovo) -> {
			if (valorNovo != null && !valorNovo.toUpperCase().matches("[SN?]")) {
				txt.setText(valorVelho);
			}
		});
	}
}
