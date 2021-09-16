package gui;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.util.Alertas;
import gui.util.RestricoesDeDigitacao;
import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.ControleProcesso;
import model.entities.Parametros;
import model.exceptions.ValidacaoException;
import model.services.ControleProcessoService;
import model.services.ParametrosService;

public class ProcessoAtualViewController implements Initializable {

	private ControleProcesso entidade = new ControleProcesso();

	private ControleProcessoService servico = new ControleProcessoService();

//	Parametros
	String anoMes;

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
	private Button btNovo;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	@FXML
	private Button btSair;

	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		entidade = new ControleProcesso();
		txtAnoMes.setDisable(false);
		atualizarTela();
	}

	@FXML
	public void onBtSalvarAction(ActionEvent evento) {
		try {
			servico.removerTodos();
			entidade = getDadosDoForm();
			servico.salvarOuAtualizar(entidade);
			atualizarParametro(entidade.getAnoMes());
			Utilitarios.atualStage(evento).close();
		} catch (ValidacaoException e) {
			mostrarErrosDeDigitacao(e.getErros());
		} catch (DbException e) {
			Alertas.mostrarAlertas("erro Salvando Controle dos Processos", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lerParametros();
		entidade = carregarEntidade();
		inicializarComponentes();
		txtAnoMes.setDisable(true);
		atualizarTela();
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("AmbienteGeral", "AnoMes")).getValor();
	}

	private ControleProcesso carregarEntidade() {
		entidade.setAnoMes(anoMes);
		entidade = servico.pesquisarPorChave(entidade);
		return entidade;
	}

	private void atualizarTela() {
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
		if (txtAnoMes.getText() != null && txtAnoMes.getText().length() < 6) {
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

	private void atualizarParametro(String anoMesNovo) {
		ParametrosService parametrosService = new ParametrosService();
		Parametros parametros = parametrosService.pesquisarPorChave("AmbienteGeral", "AnoMes");
		parametros.setValor(anoMesNovo);
		parametrosService.salvarOuAtualizar(parametros);
	}
}
