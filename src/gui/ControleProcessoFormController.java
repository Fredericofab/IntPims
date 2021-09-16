package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DadosAlteradosListener;
import gui.util.Alertas;
import gui.util.RestricoesDeDigitacao;
import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.ControleProcesso;
import model.exceptions.ValidacaoException;
import model.services.ControleProcessoService;
import model.services.ParametrosService;

public class ControleProcessoFormController implements Initializable {

	private ControleProcesso entidade;
	
	private ControleProcessoService servico;
	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	
	@FXML
	private TextField txtAnoMes;
	@FXML
	private TextField txtImportarFolha;
	@FXML
	private TextField txtSumarizarFolha;
	@FXML
	private TextField txtExportarFolha;
	@FXML
	private TextField txtImportarFuncionario;
	@FXML
	private TextField txtSumarizarFuncionario;
	@FXML
	private TextField txtImportarErpMT;
	@FXML
	private TextField txtImportarErpCD;
	@FXML
	private TextField txtImportarErpDD;
	@FXML
	private TextField txtCriticarErp;
	@FXML
	private TextField txtExportarErp;
	
	@FXML
	private Label labelErroAnoMes;
	
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	
	@FXML
	public void onBtSalvarAction(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("o outro progrmador esqueceu de injetar a entuidade");
		}
		if (servico == null) {
			throw new IllegalStateException("o outro progrmador esqueceu de injetar o servico");
		}
		try {
			entidade = getDadosDoForm();
			servico.salvarOuAtualizar(entidade);
			notificarDadosAlteradosListeners();
			Utilitarios.atualStage(evento).close();
		}
		catch (ValidacaoException e) {
			mostrarErrosDeDigitacao(e.getErros());
		}
		catch (DbException e){
			Alertas.mostrarAlertas("erro Salvando Controle dos Processos", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
		
	public void setControleProcesso(ControleProcesso entidade) {
		this.entidade = entidade;
	}
	
	public void setControleProcessoService(ControleProcessoService servico) {
		this.servico = servico;
	}
	
	public void serOuvinteDeDadosAlteradosListener(DadosAlteradosListener ouvinte) {
		ouvintes.add(ouvinte);
	}
	
	private void notificarDadosAlteradosListeners() {
		for (DadosAlteradosListener ouvinte : ouvintes) {
			ouvinte.onDadosAlterados();
		}
	}

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		atualizarTela();
		inicializarComponentes();
	}
	

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		flagIncluir = (parametrosService.pesquisarPorChave("ControleProcesso", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("ControleProcesso", "FlagAlterar")).getValor().toUpperCase();
	}

	private void atualizarTela() {
		// TODO Auto-generated method stub
		
	}

	
	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtAnoMes);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAnoMes, 6);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarFolha, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSumarizarFolha, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtExportarFolha, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarFuncionario, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSumarizarFuncionario, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarErpMT, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarErpCD, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarErpDD, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCriticarErp, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtExportarErp, 1);
		if (flagIncluir.equals("S")) {
			txtAnoMes.setDisable(false);
			txtImportarFolha.setDisable(false);
			txtSumarizarFolha.setDisable(false);
			txtExportarFolha.setDisable(false);
			txtImportarFuncionario.setDisable(false);
			txtSumarizarFuncionario.setDisable(false);
			txtImportarErpMT.setDisable(false);
			txtImportarErpCD.setDisable(false);
			txtImportarErpDD.setDisable(false);
			txtCriticarErp.setDisable(false);
			txtExportarErp.setDisable(false);
		}
		else {
			if (flagAlterar.equals("S")) {
				txtAnoMes.setDisable(true);
				txtImportarFolha.setDisable(false);
				txtSumarizarFolha.setDisable(false);
				txtExportarFolha.setDisable(false);
				txtImportarFuncionario.setDisable(false);
				txtSumarizarFuncionario.setDisable(false);
				txtImportarErpMT.setDisable(false);
				txtImportarErpCD.setDisable(false);
				txtImportarErpDD.setDisable(false);
				txtCriticarErp.setDisable(false);
				txtExportarErp.setDisable(false);
			}
			else {
				txtAnoMes.setDisable(true);
				txtImportarFolha.setDisable(true);
				txtSumarizarFolha.setDisable(true);
				txtExportarFolha.setDisable(true);
				txtImportarFuncionario.setDisable(true);
				txtSumarizarFuncionario.setDisable(true);
				txtImportarErpMT.setDisable(true);
				txtImportarErpCD.setDisable(true);
				txtImportarErpDD.setDisable(true);
				txtCriticarErp.setDisable(true);
				txtExportarErp.setDisable(true);
			}
		}
	}
	
	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		txtAnoMes.setText(entidade.getAnoMes());
		txtImportarFolha.setText(entidade.getImportarFolha());
		txtSumarizarFolha.setText(entidade.getSumarizarFolha());
		txtExportarFolha.setText(entidade.getExportarFolha());
		txtImportarFuncionario.setText(entidade.getImportarFuncionario());
		txtSumarizarFuncionario.setText(entidade.getSumarizarFuncionario());
		txtImportarErpMT.setText(entidade.getImportarErpMT());
		txtImportarErpCD.setText(entidade.getImportarErpCD());
		txtImportarErpDD.setText(entidade.getImportarErpDD());
		txtCriticarErp.setText(entidade.getCriticarErp());
		txtExportarErp.setText(entidade.getExportarErp());
	}
	
	private ControleProcesso getDadosDoForm() {
		ControleProcesso objeto = new ControleProcesso();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		
		objeto.setAnoMes(txtAnoMes.getText());
		
		objeto.setImportarFolha(txtImportarFolha.getText());
		objeto.setSumarizarFolha(txtSumarizarFolha.getText());
		objeto.setExportarFolha(txtExportarFolha.getText());
		objeto.setImportarFuncionario(txtImportarFuncionario.getText());
		objeto.setSumarizarFuncionario(txtSumarizarFuncionario.getText());
		objeto.setImportarErpMT(txtImportarErpMT.getText());
		objeto.setImportarErpCD(txtImportarErpCD.getText());
		objeto.setImportarErpDD(txtImportarErpDD.getText());
		objeto.setCriticarErp(txtCriticarErp.getText());
		objeto.setExportarErp(txtExportarErp.getText());

		objeto.setImportarFolha(Utilitarios.tentarConverterParaMaiusculo(txtImportarFolha.getText()));
		objeto.setSumarizarFolha(Utilitarios.tentarConverterParaMaiusculo(txtSumarizarFolha.getText()));
		objeto.setExportarFolha(Utilitarios.tentarConverterParaMaiusculo(txtExportarFolha.getText()));
		objeto.setImportarFuncionario(Utilitarios.tentarConverterParaMaiusculo(txtImportarFuncionario.getText()));
		objeto.setSumarizarFuncionario(Utilitarios.tentarConverterParaMaiusculo(txtSumarizarFuncionario.getText()));
		objeto.setImportarErpMT(Utilitarios.tentarConverterParaMaiusculo(txtImportarErpMT.getText()));
		objeto.setImportarErpCD(Utilitarios.tentarConverterParaMaiusculo(txtImportarErpCD.getText()));
		objeto.setImportarErpDD(Utilitarios.tentarConverterParaMaiusculo(txtImportarErpDD.getText()));
		objeto.setCriticarErp(Utilitarios.tentarConverterParaMaiusculo(txtCriticarErp.getText()));
		objeto.setExportarErp(Utilitarios.tentarConverterParaMaiusculo(txtExportarErp.getText()));

		if (txtAnoMes.getText() == null || txtAnoMes.getText().trim().equals("")) {
			validacao.adicionarErro("txtAnoMes", "Informe o Ano e Mes de referencia");
		}
		if (txtAnoMes.getText() != null && txtAnoMes.getText().length() < 6 ) {
			validacao.adicionarErro("txtAnoMes", "Informe no formato AAAAMM");
		}

		
		if (validacao.getErros().size() > 0) {
			throw validacao;
		}

		return objeto;
	}
	
	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		labelErroAnoMes.setText((campos.contains("txtAnoMes") ? erros.get("txtAnoMes") : ""));
	}
}
