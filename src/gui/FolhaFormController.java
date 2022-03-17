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
import model.entities.Folha;
import model.exceptions.ValidacaoException;
import model.services.FolhaService;
import model.services.ParametrosService;

public class FolhaFormController implements Initializable {

	private Folha entidade;
	
	private FolhaService servico;
	private ParametrosService parametrosService = new ParametrosService();

	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	String flagObservacao;
	
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
	private TextField txtReferenciaVerba;
	@FXML
	private TextField txtTipoVerba;
	@FXML
	private TextField txtImportar;
	@FXML
	private TextField txtConsiderarReferencia;
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
	private Label labelErroReferenciaVerba;
	@FXML
	private Label labelErroTipoVerba;
	@FXML
	private Label labelErroImportar;
	@FXML
	private Label labelErroConsiderarReferencia;
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
			Boolean atualizarEtapaDoProcesso = true;
			servico.salvarOuAtualizar(entidade, atualizarEtapaDoProcesso );
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
		
	public void setDadosFolha(Folha entidade) {
		this.entidade = entidade;
	}
	
	public void setDadosFolhaService(FolhaService servico) {
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
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodVerba);
		RestricoesDeDigitacao.soPermiteTextFieldDouble(txtValorVerba);
		RestricoesDeDigitacao.soPermiteTextFieldDouble(txtReferenciaVerba);
		RestricoesDeDigitacao.soPermiteTextFieldPDB(txtTipoVerba);
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtConsiderarReferencia);
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtImportar);

		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAnoMes, 6);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodCentroCustos, 20);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescCentroCustos, 50);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodVerba, 5);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescVerba, 50);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtValorVerba, 14);			
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtReferenciaVerba, 8);			
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtTipoVerba, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtConsiderarReferencia, 1);
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
		txtCodVerba.setDisable(b);
		txtDescVerba.setDisable(b);
		txtValorVerba.setDisable(b);
		txtReferenciaVerba.setDisable(b);
		txtTipoVerba.setDisable(b);
		txtImportar.setDisable(b);
		txtConsiderarReferencia.setDisable(b);
		txtObservacao.setDisable(b);
	}

	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		Locale.setDefault(Locale.US);

		txtAnoMes.setText(entidade.getAnoMes());
		txtCodCentroCustos.setText(String.format("%.0f", entidade.getCodCentroCustos()));
		txtDescCentroCustos.setText(entidade.getDescCentroCustos());
		txtCodVerba.setText(String.format("%.0f",entidade.getCodVerba()));
		txtDescVerba.setText(entidade.getDescVerba());
		txtValorVerba.setText(String.format("%.2f", entidade.getValorVerba()));
		txtReferenciaVerba.setText(String.format("%.2f", entidade.getReferenciaVerba()));
		txtTipoVerba.setText(entidade.getTipoVerba());
		txtImportar.setText(entidade.getImportar());
		txtConsiderarReferencia.setText(entidade.getConsiderarReferencia());
		txtObservacao.setText(entidade.getObservacao());
		
		txtCodVerba.setStyle("-fx-alignment: CENTER-RIGHT");
		txtCodCentroCustos.setStyle("-fx-alignment: CENTER-RIGHT");
		txtValorVerba.setStyle("-fx-alignment: CENTER-RIGHT");
		txtReferenciaVerba.setStyle("-fx-alignment: CENTER-RIGHT");
	}
	
	private Folha getDadosDoForm() {
		Folha objeto = new Folha();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		
		objeto.setAnoMes(txtAnoMes.getText());
		objeto.setCodCentroCustos(Utilitarios.tentarConverterParaDouble(txtCodCentroCustos.getText()));
		objeto.setCodVerba(Utilitarios.tentarConverterParaDouble(txtCodVerba.getText()));
		objeto.setDescCentroCustos(txtDescCentroCustos.getText());
		objeto.setDescVerba(txtDescVerba.getText());
		objeto.setValorVerba(Utilitarios.tentarConverterParaDouble(txtValorVerba.getText()));
		objeto.setReferenciaVerba(Utilitarios.tentarConverterParaDouble(txtReferenciaVerba.getText()));
		objeto.setTipoVerba(Utilitarios.tentarConverterParaMaiusculo(txtTipoVerba.getText()));
		objeto.setImportar(Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText()));
		objeto.setConsiderarReferencia(Utilitarios.tentarConverterParaMaiusculo(txtConsiderarReferencia.getText()));
		objeto.setObservacao(txtObservacao.getText());

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
		if (txtReferenciaVerba.getText() == null || txtReferenciaVerba.getText().trim().equals("")) {
			validacao.adicionarErro("txtReferenciaVerba", "Informe a Referencia da Verba");
		}
		if (txtTipoVerba.getText() == null || txtTipoVerba.getText().trim().equals("")) {
			validacao.adicionarErro("txtTipoVerba", "Informe P-Provento, D-Desconto ou B-Base");
		}
		if (txtImportar.getText() == null || txtImportar.getText().trim().equals("")) {
			validacao.adicionarErro("txtImportar", "Informe S ou N para ser importado pelo Pimscs");
		}
		if (txtConsiderarReferencia.getText() == null || txtConsiderarReferencia.getText().trim().equals("")) {
			validacao.adicionarErro("txtConsiderarReferencia", "Informe S ou N ser considerar essas referencias no total de horas");
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
		labelErroCodVerba.setText((campos.contains("txtCodVerba") ? erros.get("txtCodVerba") : ""));
		labelErroValorVerba.setText((campos.contains("txtValorVerba") ? erros.get("txtValorVerba") : ""));
		labelErroReferenciaVerba.setText((campos.contains("txtReferenciaVerba") ? erros.get("txtReferenciaVerba") : ""));
		labelErroTipoVerba.setText((campos.contains("txtTipoVerba") ? erros.get("txtTipoVerba") : ""));
		labelErroImportar.setText((campos.contains("txtImportar") ? erros.get("txtImportar") : ""));
		labelErroConsiderarReferencia.setText((campos.contains("txtConsiderarReferencia") ? erros.get("txtConsiderarReferencia") : ""));
		labelErroObservacao.setText((campos.contains("txtObservacao") ? erros.get("txtObservacao") : ""));
	}
	
	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("DadosFolha", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("DadosFolha", "FlagAlterar")).getValor().toUpperCase();
		flagObservacao =  (parametrosService.pesquisarPorChave("DadosFolha", "FlagObservacao")).getValor().toUpperCase();
	}
}
