package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
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
	private TextField txtCodMaterial;
	@FXML
	private TextField txtDescMovimento;
	@FXML
	private TextField txtUnidadeMedida;
	@FXML
	private TextField txtQuantidade;
	@FXML
	private TextField txtPrecoUnitario;
	@FXML
	private TextField txtValorMovimento;
	@FXML
	private TextField txtNumeroOS;
	@FXML
	private TextField txtFrotaOuCC;
	@FXML
	private TextField txtDocumentoErp;
	@FXML
	private DatePicker dpDataMovimento;
	@FXML
	private TextField txtImportar;
	@FXML
	private TextField txtPoliticas;
	@FXML
	private TextField txtSalvarOS_Material;
	@FXML
	private TextField txtSalvarCstg_IntVM;
	@FXML
	private TextField txtSalvarCstg_IntCM;
	@FXML
	private TextField txtSalvarCstg_IntDG;
	@FXML
	private TextField txtObservacao;
	@FXML
	private Label labelErroAnoMes;
	@FXML
	private Label labelErroOrigem;
	@FXML
	private Label labelErroCodCentroCustos;
	@FXML
	private Label labelErroCodContaContabil;
	@FXML
	private Label labelErroCodMaterial;
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
			entidade = substituirNull(entidade);
			servico.salvarOuAtualizar(entidade);
			notificarDadosAlteradosListeners();
			Utilitarios.atualStage(evento).close();
		} catch (ValidacaoException e) {
			mostrarErrosDeDigitacao(e.getErros());
		} catch (DbException e) {
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
		flagObservacao = (parametrosService.pesquisarPorChave("DadosErp", "FlagObservacao")).getValor().toUpperCase();
	}

	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtAnoMes);
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodCentroCustos);
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtNumeroOS);
		RestricoesDeDigitacao.soPermiteTextFieldDouble(txtQuantidade);
		RestricoesDeDigitacao.soPermiteTextFieldDouble(txtPrecoUnitario);
		RestricoesDeDigitacao.soPermiteTextFieldDouble(txtValorMovimento);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAnoMes, 6);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtOrigem, 2);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodCentroCustos, 20);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodContaContabil,20);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtUnidadeMedida,5);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescMovimento,35);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportar, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtObservacao, 255);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarOS_Material,1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntVM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntCM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntDG, 1);
		RestricoesDeDigitacao.soPermiteTextFieldSNinterrogacao(txtImportar);
		RestricoesDeDigitacao.soPermiteTextFieldSNinterrogacao(txtSalvarOS_Material);
		RestricoesDeDigitacao.soPermiteTextFieldSNinterrogacao(txtSalvarCstg_IntVM);
		RestricoesDeDigitacao.soPermiteTextFieldSNinterrogacao(txtSalvarCstg_IntCM);
		RestricoesDeDigitacao.soPermiteTextFieldSNinterrogacao(txtSalvarCstg_IntDG);
		Utilitarios.formatarDatePicker(dpDataMovimento, "dd/MM/yyyy");
	
		if (flagIncluir.equals("S")) {
			desabilitarCompoenentes(false);
		} else {
			if (flagAlterar.equals("S")) {
				desabilitarCompoenentes(true);
				txtImportar.setDisable(false);
				txtObservacao.setDisable(false);
				txtSalvarOS_Material.setDisable(false);
				txtSalvarCstg_IntVM.setDisable(false);
				txtSalvarCstg_IntCM.setDisable(false);
				txtSalvarCstg_IntDG.setDisable(false);
			} else {
				desabilitarCompoenentes(true);
			}
		}
	}

	private void desabilitarCompoenentes(boolean b) {
		txtAnoMes.setDisable(b);
		txtOrigem.setDisable(b);
		txtCodCentroCustos.setDisable(b);
		txtDescCentroCustos.setDisable(b);
		txtCodContaContabil.setDisable(b);
		txtDescContaContabil.setDisable(b);
		txtCodMaterial.setDisable(b);
		txtDescMovimento.setDisable(b);
		txtUnidadeMedida.setDisable(b);
		txtQuantidade.setDisable(b);
		txtPrecoUnitario.setDisable(b);
		txtValorMovimento.setDisable(b);
		txtNumeroOS.setDisable(b);
		txtFrotaOuCC.setDisable(b);
		txtDocumentoErp.setDisable(b);
		dpDataMovimento.setDisable(b);
		txtImportar.setDisable(b);
		txtObservacao.setDisable(b);
		txtSalvarOS_Material.setDisable(b);
		txtSalvarCstg_IntVM.setDisable(b);
		txtSalvarCstg_IntCM.setDisable(b);
		txtSalvarCstg_IntDG.setDisable(b);
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
		txtCodContaContabil.setText(entidade.getCodContaContabil());
		txtDescContaContabil.setText(entidade.getDescContaContabil());
		txtCodMaterial.setText(entidade.getCodMaterial());
		txtDescMovimento.setText(entidade.getDescMovimento());
		txtUnidadeMedida.setText(entidade.getUnidadeMedida());
		txtQuantidade.setText(String.format("%.2f", entidade.getQuantidade()));
		txtPrecoUnitario.setText(String.format("%.2f", entidade.getPrecoUnitario()));
		txtValorMovimento.setText(String.format("%.2f", entidade.getValorMovimento()));
		txtNumeroOS.setText(entidade.getNumeroOS());
		txtFrotaOuCC.setText(entidade.getFrotaOuCC());
		txtDocumentoErp.setText(entidade.getDocumentoErp());

//		dpDataMovimento.setValue(entidade.getDataMovimento()); erro de compilação Date x LocalDate
		if (entidade.getDataMovimento() != null) {
			dpDataMovimento.setValue(LocalDate.ofInstant(entidade.getDataMovimento().toInstant(), ZoneId.systemDefault()));
		}
		
		txtImportar.setText(entidade.getImportar());
		txtPoliticas.setText(entidade.getPoliticas());
		txtSalvarOS_Material.setText(entidade.getSalvarOS_Material());
		txtSalvarCstg_IntVM.setText(entidade.getSalvarCstg_IntVM());
		txtSalvarCstg_IntCM.setText(entidade.getSalvarCstg_IntCM());
		txtSalvarCstg_IntDG.setText(entidade.getSalvarCstg_IntDG());
		txtObservacao.setText(entidade.getObservacao());

		txtCodContaContabil.setStyle("-fx-alignment: CENTER-RIGHT");
		txtCodCentroCustos.setStyle("-fx-alignment: CENTER-RIGHT");
		txtCodMaterial.setStyle("-fx-alignment: CENTER-RIGHT");
		txtQuantidade.setStyle("-fx-alignment: CENTER-RIGHT");
		txtPrecoUnitario.setStyle("-fx-alignment: CENTER-RIGHT");
		txtValorMovimento.setStyle("-fx-alignment: CENTER-RIGHT");
		txtNumeroOS.setStyle("-fx-alignment: CENTER-RIGHT");
	}

	private Erp getDadosDoForm() {
		Erp objeto = new Erp();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");

		objeto.setSequencial(Utilitarios.tentarConverterParaInt(txtSequencial.getText()));
		objeto.setAnoMes(txtAnoMes.getText());
		objeto.setOrigem(Utilitarios.tentarConverterParaMaiusculo(txtOrigem.getText()));
		objeto.setCodCentroCustos(Utilitarios.tentarConverterParaDouble(txtCodCentroCustos.getText()));
		objeto.setDescCentroCustos(txtDescCentroCustos.getText());
		objeto.setCodContaContabil(txtCodContaContabil.getText());
		objeto.setDescContaContabil(txtDescContaContabil.getText());
		objeto.setCodMaterial(txtCodMaterial.getText());
		objeto.setDescMovimento(txtDescMovimento.getText());
		objeto.setUnidadeMedida(txtUnidadeMedida.getText());
		objeto.setQuantidade(Utilitarios.tentarConverterParaDouble(txtQuantidade.getText()));
		objeto.setPrecoUnitario(Utilitarios.tentarConverterParaDouble(txtPrecoUnitario.getText()));
		objeto.setValorMovimento(Utilitarios.tentarConverterParaDouble(txtValorMovimento.getText()));
		objeto.setNumeroOS(txtNumeroOS.getText());
		objeto.setFrotaOuCC(txtFrotaOuCC.getText());
		objeto.setDocumentoErp(txtDocumentoErp.getText());
		if(dpDataMovimento.getValue() != null) {
			Instant instant = Instant.from(dpDataMovimento.getValue().atStartOfDay(ZoneId.systemDefault()));
			objeto.setDataMovimento(Date.from(instant));
		}
		objeto.setImportar(Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText()));
		objeto.setPoliticas(txtPoliticas.getText());
		objeto.setSalvarOS_Material(Utilitarios.tentarConverterParaMaiusculo(txtSalvarOS_Material.getText()));
		objeto.setSalvarCstg_IntVM(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntVM.getText()));
		objeto.setSalvarCstg_IntCM(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntCM.getText()));
		objeto.setSalvarCstg_IntDG(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntDG.getText()));
		objeto.setObservacao(txtObservacao.getText());

		if (txtAnoMes.getText() == null || txtAnoMes.getText().trim().equals("")) {
			validacao.adicionarErro("txtAnoMes", "Informe o Ano e Mes de referencia");
		}
		if (txtAnoMes.getText() != null && txtAnoMes.getText().length() < 6) {
			validacao.adicionarErro("txtAnoMes", "Informe no formato AAAAMM");
		}
		if ((txtOrigem.getText() != null) && (txtOrigem.getText().toUpperCase().equals("MT")
				|| txtOrigem.getText().toUpperCase().equals("CD") || txtOrigem.getText().toUpperCase().equals("DG"))) {
			// ok
		} else {
			validacao.adicionarErro("txtOrigem", "Informe a Origem: MT, CD ou DG");
		}
		if (txtCodCentroCustos.getText() == null || txtCodCentroCustos.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodCentroCustos", "Informe o Centro de Custos");
		}
		if ((txtOrigem.getText() != null) && (txtOrigem.getText().toUpperCase().equals("DG")) && 
			(txtCodContaContabil.getText() == null || txtCodContaContabil.getText().trim().equals(""))) {
			validacao.adicionarErro("txtCodContaContabil", "Origem DG - Informe a Conta Contabil");
		}
		if ((txtOrigem.getText() != null) && (txtOrigem.getText().toUpperCase().equals("MT")) && 
				(txtCodMaterial.getText() == null || txtCodMaterial.getText().trim().equals(""))) {
				validacao.adicionarErro("txtCodMaterial", "Origem MT - Informe o Material");
			}

		if (txtObservacao.getText() == null || txtObservacao.getText().trim().equals("")) {
			if (flagObservacao.equals("S")) {
				validacao.adicionarErro("txtObservacao",
						"Informe uma observacao com justificativa da inclusao/alteracao");
			}
		}
		if (validacao.getErros().size() > 0) {
			throw validacao;
		}
		return objeto;
	}

	private Erp substituirNull(Erp objeto) {
		if (objeto.getQuantidade() == null) { objeto.setQuantidade(0.00);	}
		if (objeto.getPrecoUnitario() == null) { objeto.setPrecoUnitario(0.00);	}
		if (objeto.getValorMovimento() == null) { objeto.setValorMovimento(0.00);	}
		if (objeto.getDataMovimento() == null) { 
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date dataMovimento;
				dataMovimento = sdf.parse(txtAnoMes.getText() + "01");
				objeto.setDataMovimento(dataMovimento);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return objeto;
	}


	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		labelErroAnoMes.setText((campos.contains("txtAnoMes") ? erros.get("txtAnoMes") : ""));
		labelErroOrigem.setText((campos.contains("txtOrigem") ? erros.get("txtOrigem") : ""));
		labelErroCodCentroCustos.setText((campos.contains("txtCodCentroCustos") ? erros.get("txtCodCentroCustos") : ""));
		labelErroCodContaContabil.setText((campos.contains("txtCodContaContabil") ? erros.get("txtCodContaContabil") : ""));
		labelErroCodMaterial.setText((campos.contains("txtCodMaterial") ? erros.get("txtCodMaterial") : ""));
		labelErroObservacao.setText((campos.contains("txtObservacao") ? erros.get("txtObservacao") : ""));
	}
}
