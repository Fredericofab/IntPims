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
import model.entities.Erp;
import model.exceptions.ValidacaoException;
import model.services.ErpService;
import model.services.ParametrosService;

public class ErpFormController implements Initializable {

	private Erp entidade;
	
	private ErpService servico;
	private ParametrosService parametrosService = new ParametrosService();

	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagObservacao;

	@FXML
	private TextField txtSequencial;
	@FXML
	private TextField txtAnoMes;
	@FXML
	private TextField txtOrigem;
	@FXML
	private TextField txtCodCentroCustos; 
	@FXML
	private TextField txtDescCentroCustos;
	@FXML
	private TextField txtCodContaContabil;
	@FXML
	private TextField txtDescContaContabil;
	@FXML
	private TextField txtFamiliaMaterial;
	@FXML
	private TextField txtImportar;
	@FXML
	private TextField txtObservacao;
	@FXML
	private Label labelErroAnoMes;	
	@FXML
	private Label labelErroCodCentroCustos;	
	@FXML
	private Label labelErroCodContaContabil;	
	@FXML
	private Label labelErroFamiliaMaterial;
	@FXML
	private Label labelErroObservacao;
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
			Alertas.mostrarAlertas("erro Salvando DadosErp", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
		
	public void setDadosErp(Erp entidade) {
		this.entidade = entidade;
	}
	
	public void setDadosErpService(ErpService servico) {
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
	
	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("DadosErp", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("DadosErp", "FlagAlterar")).getValor().toUpperCase();
		flagObservacao =  (parametrosService.pesquisarPorChave("DadosErp", "FlagObservacao")).getValor().toUpperCase();
	}

	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtAnoMes);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAnoMes, 6);
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodCentroCustos);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodCentroCustos, 20);	
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodContaContabil);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodContaContabil, 5);	
		RestricoesDeDigitacao.soPermiteTextFieldDouble(txtFamiliaMaterial);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtFamiliaMaterial, 14);			
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtImportar);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportar, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtObservacao, 255);
		if (flagIncluir.equals("S")) {
			desabilitarCompoenentes(false);
		}
		else {
			if (flagAlterar.equals("S")) {
				desabilitarCompoenentes(true);
				txtImportar.setDisable(false);
				txtObservacao.setDisable(false);
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
		txtCodContaContabil.setDisable(b);
		txtDescContaContabil.setDisable(b);
		txtFamiliaMaterial.setDisable(b);
		txtImportar.setDisable(b);
		txtObservacao.setDisable(b);
	}

	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		Locale.setDefault(Locale.US);

		txtSequencial.setText(String.valueOf(entidade.getSequencial()));
		txtAnoMes.setText(entidade.getAnoMes());
		txtOrigem.setText(entidade.getOrigem());
		txtCodCentroCustos.setText(String.format("%.0f", entidade.getCodCentroCustos()));
		txtDescCentroCustos.setText(entidade.getDescCentroCustos());
		txtCodContaContabil.setText(String.format("%.0f",entidade.getCodContaContabil()));
		txtDescContaContabil.setText(entidade.getDescContaContabil());
		txtFamiliaMaterial.setText(String.format("%.0f", entidade.getFamiliaMaterial()));
		txtImportar.setText(entidade.getImportar());
		txtObservacao.setText(entidade.getObservacao());
		
		txtCodContaContabil.setStyle("-fx-alignment: CENTER-RIGHT");
		txtCodCentroCustos.setStyle("-fx-alignment: CENTER-RIGHT");
		txtFamiliaMaterial.setStyle("-fx-alignment: CENTER-RIGHT");
	}
	
	private Erp getDadosDoForm() {
		Erp objeto = new Erp();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");

		objeto.setSequencial(Utilitarios.tentarConverterParaInt(txtSequencial.getText()));
		objeto.setAnoMes(txtAnoMes.getText());
		objeto.setOrigem(Utilitarios.tentarConverterParaMaiusculo(txtOrigem.getText()));
		objeto.setCodCentroCustos(Utilitarios.tentarConverterParaDouble(txtCodCentroCustos.getText()));
		objeto.setCodContaContabil(Utilitarios.tentarConverterParaDouble(txtCodContaContabil.getText()));
		objeto.setDescCentroCustos(txtDescCentroCustos.getText());
		objeto.setDescContaContabil(txtDescContaContabil.getText());
		objeto.setImportar(Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText()));
		objeto.setObservacao(txtObservacao.getText());
		objeto.setFamiliaMaterial(Utilitarios.tentarConverterParaDouble(txtFamiliaMaterial.getText()));

		if (txtAnoMes.getText() == null || txtAnoMes.getText().trim().equals("")) {
			validacao.adicionarErro("txtAnoMes", "Informe o Ano e Mes de referencia");
		}
		if (txtAnoMes.getText() != null && txtAnoMes.getText().length() < 6 ) {
			validacao.adicionarErro("txtAnoMes", "Informe no formato AAAAMM");
		}
		if (txtCodCentroCustos.getText() == null || txtCodCentroCustos.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodCentroCustos", "Informe o Centro de Custos");
		}
		if (txtCodContaContabil.getText() == null || txtCodContaContabil.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodContaContabil", "Informe a ContaContabil da Erp");
		}
		if (txtFamiliaMaterial.getText() == null || txtFamiliaMaterial.getText().trim().equals("")) {
			validacao.adicionarErro("txtFamiliaMaterial", "Informe a Familia do Material");
		}
		if (txtObservacao.getText() == null || txtObservacao.getText().trim().equals("")) {
			if (flagObservacao.equals("S")) {
			    validacao.adicionarErro("txtObservacao", "Informe uma observacao com justificativa da inclusao/alteracao");
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
		labelErroCodCentroCustos.setText((campos.contains("txtCodCentroCustos") ? erros.get("txtCodCentroCustos") : ""));
		labelErroCodContaContabil.setText((campos.contains("txtCodContaContabil") ? erros.get("txtCodContaContabil") : ""));
		labelErroFamiliaMaterial.setText((campos.contains("txtFamiliaMaterial") ? erros.get("txtFamiliaMaterial") : ""));
		labelErroObservacao.setText((campos.contains("txtObservacao") ? erros.get("txtObservacao") : ""));
	}
}
