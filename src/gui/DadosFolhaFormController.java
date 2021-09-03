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
import model.entities.DadosFolha;
import model.exceptions.ValidacaoException;
import model.services.DadosFolhaService;

public class DadosFolhaFormController implements Initializable {

	private DadosFolha entidade;
	
	private DadosFolhaService servico;
	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
	@FXML
	private TextField txtAnoMes;
	
	@FXML
	private TextField txtCodCentroCustos; 
	
	@FXML
	private TextField txtDescCentroCustos;
	
	@FXML
	private TextField txtCodVerba;
	
	@FXML
	private TextField txtDescVerba;
	
	@FXML
	private TextField txtValorVerba;
	
	@FXML
	private TextField txtImportar;
	
	@FXML
	private TextField txtObservacao;
	
	@FXML
	private Label labelErroAnoMes;	

	@FXML
	private Label labelErroCodCentroCustos;	

	@FXML
	private Label labelErroCodVerba;	

	@FXML
	private Label labelErroValorVerba;
	
	
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
			Alertas.mostrarAlertas("erro Salvando DadosFolha", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
		
	public void setDadosFolha(DadosFolha entidade) {
		this.entidade = entidade;
	}
	
	public void setDadosFolhaService(DadosFolhaService servico) {
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
		inicializarComponentes();
	}
	
	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtAnoMes);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAnoMes, 6);
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodCentroCustos);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodCentroCustos, 10);	
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodVerba);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodVerba, 5);	
		RestricoesDeDigitacao.soPermiteTextFieldDouble(txtValorVerba);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtValorVerba, 14);			
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtImportar);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportar, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtObservacao, 30);
	}
	
	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		txtAnoMes.setText(entidade.getAnoMes());
		txtCodCentroCustos.setText(entidade.getCodCentroCustos());
		txtDescCentroCustos.setText(entidade.getDescCentroCustos());
		txtCodVerba.setText(entidade.getCodVerba());
		txtDescVerba.setText(entidade.getDescVerba());
		Locale.setDefault(Locale.US);
		txtValorVerba.setText(String.format("%.2f", entidade.getValorVerba()));
		txtImportar.setText(entidade.getImportar());
		txtObservacao.setText(entidade.getObservacao());
	}
	
	private DadosFolha getDadosDoForm() {
		DadosFolha objeto = new DadosFolha();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		
		objeto.setAnoMes(txtAnoMes.getText());
		objeto.setCodCentroCustos(txtCodCentroCustos.getText());
		objeto.setCodVerba(txtCodVerba.getText());
		objeto.setDescCentroCustos(txtDescCentroCustos.getText());
		objeto.setDescVerba(txtDescVerba.getText());
		objeto.setImportar(Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText()));
		objeto.setObservacao(txtObservacao.getText());
		objeto.setValorVerba(Utilitarios.tentarConverterParaDouble(txtValorVerba.getText()));

		if (txtAnoMes.getText() == null || txtAnoMes.getText().trim().equals("")) {
			validacao.adicionarErro("txtAnoMes", "Informe o Ano e Mes de referencia");
		}
		if (txtAnoMes.getText() != null && txtAnoMes.getText().length() < 6 ) {
			validacao.adicionarErro("txtAnoMes", "Informe no formato AAAAMM");
		}
		if (txtCodCentroCustos.getText() == null || txtCodCentroCustos.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodCentroCustos", "Informe o Centro de Custos");
		}
		if (txtCodVerba.getText() == null || txtCodVerba.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodVerba", "Informe a Verba da Folha");
		}
		if (txtValorVerba.getText() == null || txtValorVerba.getText().trim().equals("")) {
			validacao.adicionarErro("txtValorVerba", "Informe o Valor da Verba");
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
		labelErroCodVerba.setText((campos.contains("txtCodVerba") ? erros.get("txtCodVerba") : ""));
		labelErroValorVerba.setText((campos.contains("txtValorVerba") ? erros.get("txtValorVerba") : ""));

	}
}
