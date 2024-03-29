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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Parametros;
import model.entities.ProcessoAtual;
import model.exceptions.ValidacaoException;
import model.services.ParametrosService;
import model.services.ProcessoAtualService;

public class ProcessoAtualViewController implements Initializable {

	private ProcessoAtual entidade = new ProcessoAtual();
	private ProcessoAtualService servico = new ProcessoAtualService();

	private ParametrosService parametrosService = new ParametrosService();

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
	private TextField txtImportarErpRM;
	@FXML
	private TextField txtImportarErpED;
	@FXML
	private TextField txtImportarErpDF;
	@FXML
	private TextField txtValidarErp;
	@FXML
	private TextField txtAplicarPoliticaErp;
	@FXML
	private TextField txtExportarErpVM;
	@FXML
	private TextField txtExportarErpCM;
	@FXML
	private TextField txtExportarErpDG;
	@FXML
	private TextField txtExportarErpOS;
	@FXML
	private TextField txtAtualizarCompo;

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
		valoresIniciais();
		txtAnoMes.setDisable(false);
		txtAnoMes.requestFocus();
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
		if (entidade == null) {
			valoresIniciais();
			txtAnoMes.setDisable(false);
			txtAnoMes.requestFocus();
		}
		else {
			txtAnoMes.setDisable(true);
		}
		inicializarComponentes();
		atualizarTela();
	}

	private ProcessoAtual carregarEntidade() {
		entidade = servico.pesquisarPorChave(anoMes);
		return entidade;
	}

	private void valoresIniciais() {
		entidade = new ProcessoAtual();
		entidade.setAnoMes(null);
		entidade.setImportarFolha("N");
		entidade.setSumarizarFolha("N");
		entidade.setExportarFolha("N");
		entidade.setImportarFuncionario("N");
		entidade.setSumarizarFuncionario("N");
		entidade.setImportarErpRM("N");
		entidade.setImportarErpED("N");
		entidade.setImportarErpDF("N");
		entidade.setValidarErp("N");
		entidade.setAplicarPoliticaErp("N");
		entidade.setExportarErpVM("N");
		entidade.setExportarErpCM("N");
		entidade.setExportarErpDG("N");
		entidade.setExportarErpOS("N");
		entidade.setAtualizarCompo("N");
	}

	private void atualizarTela() {
		txtAnoMes.setText(entidade.getAnoMes());
		txtImportarFolha.setText(entidade.getImportarFolha());
		txtSumarizarFolha.setText(entidade.getSumarizarFolha());
		txtExportarFolha.setText(entidade.getExportarFolha());
		txtImportarFuncionario.setText(entidade.getImportarFuncionario());
		txtSumarizarFuncionario.setText(entidade.getSumarizarFuncionario());
		txtImportarErpRM.setText(entidade.getImportarErpRM());
		txtImportarErpED.setText(entidade.getImportarErpED());
		txtImportarErpDF.setText(entidade.getImportarErpDF());
		txtValidarErp.setText(entidade.getValidarErp());
		txtAplicarPoliticaErp.setText(entidade.getAplicarPoliticaErp());
		txtExportarErpVM.setText(entidade.getExportarErpVM());
		txtExportarErpCM.setText(entidade.getExportarErpCM());
		txtExportarErpDG.setText(entidade.getExportarErpDG());
		txtExportarErpOS.setText(entidade.getExportarErpOS());
		txtAtualizarCompo.setText(entidade.getAtualizarCompo());
	}

	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtAnoMes);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAnoMes, 6);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarFolha, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSumarizarFolha, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtExportarFolha, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarFuncionario, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSumarizarFuncionario, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarErpRM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarErpED, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportarErpDF, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtValidarErp, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAplicarPoliticaErp, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtExportarErpVM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtExportarErpCM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtExportarErpDG, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtExportarErpOS, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAtualizarCompo, 1);
	}

	private ProcessoAtual getDadosDoForm() {
		ProcessoAtual objeto = new ProcessoAtual();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");

		objeto.setAnoMes(txtAnoMes.getText());

		objeto.setImportarFolha(txtImportarFolha.getText());
		objeto.setSumarizarFolha(txtSumarizarFolha.getText());
		objeto.setExportarFolha(txtExportarFolha.getText());
		objeto.setImportarFuncionario(txtImportarFuncionario.getText());
		objeto.setSumarizarFuncionario(txtSumarizarFuncionario.getText());
		objeto.setImportarErpRM(txtImportarErpRM.getText());
		objeto.setImportarErpED(txtImportarErpED.getText());
		objeto.setImportarErpDF(txtImportarErpDF.getText());
		objeto.setValidarErp(txtValidarErp.getText());
		objeto.setAplicarPoliticaErp(txtAplicarPoliticaErp.getText());
		objeto.setExportarErpVM(txtExportarErpVM.getText());
		objeto.setExportarErpCM(txtExportarErpCM.getText());
		objeto.setExportarErpDG(txtExportarErpDG.getText());
		objeto.setExportarErpOS(txtExportarErpOS.getText());
		objeto.setAtualizarCompo(txtAtualizarCompo.getText());

		objeto.setImportarFolha(Utilitarios.tentarConverterParaMaiusculo(txtImportarFolha.getText()));
		objeto.setSumarizarFolha(Utilitarios.tentarConverterParaMaiusculo(txtSumarizarFolha.getText()));
		objeto.setExportarFolha(Utilitarios.tentarConverterParaMaiusculo(txtExportarFolha.getText()));
		objeto.setImportarFuncionario(Utilitarios.tentarConverterParaMaiusculo(txtImportarFuncionario.getText()));
		objeto.setSumarizarFuncionario(Utilitarios.tentarConverterParaMaiusculo(txtSumarizarFuncionario.getText()));
		objeto.setImportarErpRM(Utilitarios.tentarConverterParaMaiusculo(txtImportarErpRM.getText()));
		objeto.setImportarErpED(Utilitarios.tentarConverterParaMaiusculo(txtImportarErpED.getText()));
		objeto.setImportarErpDF(Utilitarios.tentarConverterParaMaiusculo(txtImportarErpDF.getText()));
		objeto.setValidarErp(Utilitarios.tentarConverterParaMaiusculo(txtValidarErp.getText()));
		objeto.setAplicarPoliticaErp(Utilitarios.tentarConverterParaMaiusculo(txtAplicarPoliticaErp.getText()));
		objeto.setExportarErpVM(Utilitarios.tentarConverterParaMaiusculo(txtExportarErpVM.getText()));
		objeto.setExportarErpCM(Utilitarios.tentarConverterParaMaiusculo(txtExportarErpCM.getText()));
		objeto.setExportarErpDG(Utilitarios.tentarConverterParaMaiusculo(txtExportarErpDG.getText()));
		objeto.setExportarErpOS(Utilitarios.tentarConverterParaMaiusculo(txtExportarErpOS.getText()));
		objeto.setAtualizarCompo(Utilitarios.tentarConverterParaMaiusculo(txtAtualizarCompo.getText()));

		if (txtAnoMes.getText() == null || txtAnoMes.getText().trim().equals("")) {
			validacao.adicionarErro("txtAnoMes", "Informe o Ano e Mes de referencia");
		}
		if (txtAnoMes.getText() != null && txtAnoMes.getText().length() < 6) {
			validacao.adicionarErro("txtAnoMes", "Informe no formato AAAAMM");
		}

		if (txtAnoMes.getText() != null && txtAnoMes.getText().length() == 6) {
			Integer ano = Integer.parseInt(txtAnoMes.getText().substring(0, 4));
			if (ano < 2020 || ano > 2050) {
				validacao.adicionarErro("txtAnoMes", "Informe ano entre 2020 e 2050");
			}
			Integer mes = Integer.parseInt(txtAnoMes.getText().substring(4, 6));
			if (mes < 01 || mes > 12) {
				validacao.adicionarErro("txtAnoMes", "Informe mes entre 01 e 12");
			}
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
	
	private void lerParametros() {
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}

	
	private void atualizarParametro(String anoMesNovo) {
		ParametrosService parametrosService = new ParametrosService();
		Parametros parametros = parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes");
		parametros.setValor(anoMesNovo);
		parametrosService.salvarOuAtualizar(parametros);
	}
}
