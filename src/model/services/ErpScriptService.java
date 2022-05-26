package model.services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import db.DbException;
import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;

public class ErpScriptService {

	private ErpService erpService = new ErpService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
	
	Integer qtdeLidas = 0;
	Integer qtdeProcessadas = 0;
	List <String> lista;
	
	public Integer getQtdeLidas() {
		return qtdeLidas;
	}
	public Integer getQtdeProcessadas() {
		return qtdeProcessadas;
	}

	public void processarScript(String arqScript) {
		try {
			lerScriptTXT(arqScript);
			executarScript();
			reatualizarEtapaDoProcesso();
		} catch (DbException e) {
			Alertas.mostrarAlertas("Erro Banco Oracle", "Processo Cancelado", e.getMessage(), AlertType.ERROR);
		}
	}

	private void lerScriptTXT(String arqScript) {
		String linha = null;
		lista = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arqScript),"UTF-8"))) {
			linha = br.readLine();
			while (linha != null) {
				qtdeLidas = qtdeLidas + 1;
				lista.add(linha);
				linha = br.readLine();
			}
		} catch (FileNotFoundException e) {
			Alertas.mostrarAlertas("Arquivo não encontrado", "Processo Cancelado", e.getMessage(), AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Importacao do Script", e.getMessage(),
					AlertType.ERROR);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private void executarScript() {
		for (String linha : lista) {
			String comando = linha.replace(";"," ");
			erpService.executarScript(comando);
			qtdeProcessadas = qtdeProcessadas + 1;
		}
	}
	
	private void reatualizarEtapaDoProcesso() {
		processoAtualService.atualizarEtapa("ValidarErp","N");
		processoAtualService.atualizarEtapa("AplicarPoliticaErp","N");
		processoAtualService.atualizarEtapa("ExportarErpVM","N");
		processoAtualService.atualizarEtapa("ExportarErpCM","N");
		processoAtualService.atualizarEtapa("ExportarErpDG","N");
		processoAtualService.atualizarEtapa("ExportarErpOS","N");
	}

}
