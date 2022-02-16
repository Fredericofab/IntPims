package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import model.entities.Funcionarios;
import model.exceptions.ValidacaoException;
import model.services.FuncionariosService;
import model.services.ParametrosService;

public class FuncionariosFormController implements Initializable {

	private Funcionarios entidade;
	
	private FuncionariosService servico;
	private ParametrosService parametrosService = new ParametrosService();

	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	
	@FXML
	private TextField txtAnoMes;
	@FXML
	private TextField txtCodCentroCustos; 
	@FXML
	private TextField txtDescCentroCustos;
	@FXML
	private TextField txtCodFuncionario;
	@FXML
	private TextField txtDescFuncionario;
	@FXML
	private Label labelErroAnoMes;	
	@FXML
	private Label labelErroCodCentroCustos;	
	@FXML
	private Label labelErroCodFuncionario;	
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
			Alertas.mostrarAlertas("erro Salvando Funcionarios", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
		
	public void setFuncionarios(Funcionarios entidade) {
		this.entidade = entidade;
	}
	
	public void setFuncionariosService(FuncionariosService servico) {
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
		inicializarComponentes();
	}
	
	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtAnoMes);
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodCentroCustos);
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodFuncionario);

		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAnoMes, 6);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodCentroCustos, 20);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescCentroCustos, 50);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodFuncionario, 10);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescFuncionario, 50);	

		if (flagIncluir.equals("S")) {
			desabilitarCompoenentes(false);
		}
		else {
			if (flagAlterar.equals("S")) {
				desabilitarCompoenentes(true);
				txtDescCentroCustos.setDisable(false);
				txtDescFuncionario.setDisable(false);

			}
			else {
				desabilitarCompoenentes(true);
			}
		}
	}
	
	private void desabilitarCompoenentes(boolean b) {
		txtAnoMes.setDisable(b);
		txtCodCentroCustos.setDisable(b);
		txtDescCentroCustos.setDisable(b);
		txtCodFuncionario.setDisable(b);
		txtDescFuncionario.setDisable(b);
	}

	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		Locale.setDefault(Locale.US);

		txtAnoMes.setText(entidade.getAnoMes());
		txtCodCentroCustos.setText(String.format("%.0f", entidade.getCodCentroCustos()));
		txtDescCentroCustos.setText(entidade.getDescCentroCustos());
		txtCodFuncionario.setText(String.format("%.0f", entidade.getCodFuncionario()));
		txtDescFuncionario.setText(entidade.getDescFuncionario());
		
		txtCodFuncionario.setStyle("-fx-alignment: CENTER-RIGHT");
		txtCodCentroCustos.setStyle("-fx-alignment: CENTER-RIGHT");
	}
	
	private Funcionarios getDadosDoForm() {
		Funcionarios objeto = new Funcionarios();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		
		objeto.setAnoMes(txtAnoMes.getText());
		objeto.setCodCentroCustos(Utilitarios.tentarConverterParaDouble(txtCodCentroCustos.getText()));
		objeto.setCodFuncionario(Utilitarios.tentarConverterParaDouble(txtCodFuncionario.getText()));
		objeto.setDescCentroCustos(txtDescCentroCustos.getText());
		objeto.setDescFuncionario(txtDescFuncionario.getText());

		if (txtAnoMes.getText() == null || txtAnoMes.getText().trim().equals("")) {
			validacao.adicionarErro("txtAnoMes", "Informe o Ano e Mes de referencia");
		}
		if (txtAnoMes.getText() != null && txtAnoMes.getText().length() < 6 ) {
			validacao.adicionarErro("txtAnoMes", "Informe no formato AAAAMM");
		}
		if (txtCodCentroCustos.getText() == null || txtCodCentroCustos.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodCentroCustos", "Informe o Centro de Custos");
		}
		if (txtCodFuncionario.getText() == null || txtCodFuncionario.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodFuncionario", "Informe o Codigo do Funcionario");
		}
		if (validacao.getErros().size() > 0) {
			throw validacao;
		}
		return objeto;
	}
	
	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		labelErroAnoMes.setText((campos.contains("txtAnoMes") ? erros.get("txtAnoMes") : ""));
		labelErroCodCentroCustos.setText((campos.contains("txtCodCentroCustos") ? erros.get("txtCodCentroCustos") : ""));
		labelErroCodFuncionario.setText((campos.contains("txtCodFuncionario") ? erros.get("txtCodFuncionario") : ""));
	}
	
	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("Funcionarios", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("Funcionarios", "FlagAlterar")).getValor().toUpperCase();
	}

}
