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
	private TextField txtTipoMovimento;
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
	private TextField txtCodNatureza;
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
	private TextField txtValidacoesOS;
	@FXML
	private TextField txtDocumentoErp;
	@FXML
	private DatePicker dpDataMovimento;
	@FXML
	private TextField txtSobreporPoliticas;
	@FXML
	private TextField txtPoliticas;
	@FXML
	private TextField txtImportar;
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
	private Label labelErroSobreporPoliticas;
	@FXML
	private Label labelErroDataMovimento;
	@FXML
	private Label labelErroImportar;
	@FXML
	private Label labelErroObservacao;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;

	@FXML
	public void onBtSalvarAction(ActionEvent evento) {
		try {
			entidade = getDadosDoForm();
			Boolean atualizarEtapaDoProcesso = true;
			servico.salvarOuAtualizar(entidade, atualizarEtapaDoProcesso);
			notificarDadosAlteradosListeners();
			Utilitarios.atualStage(evento).close();
		} catch (ValidacaoException e) {
			mostrarErrosDeDigitacao(e.getErros());
		} catch (DbException | IllegalArgumentException e) {
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
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtTipoMovimento, 6);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodCentroCustos, 20);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescCentroCustos, 50);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodContaContabil, 20);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescContaContabil, 50);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodNatureza, 9);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodMaterial, 10);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtUnidadeMedida, 5);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescMovimento, 255);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSobreporPoliticas, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportar, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtObservacao, 255);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarOS_Material, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntVM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntCM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntDG, 1);
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtSobreporPoliticas);
		RestricoesDeDigitacao.soPermiteTextFieldNullSNInterrogacao(txtImportar);
		RestricoesDeDigitacao.soPermiteTextFieldNullSNInterrogacao(txtSalvarOS_Material);
		RestricoesDeDigitacao.soPermiteTextFieldNullSNInterrogacao(txtSalvarCstg_IntVM);
		RestricoesDeDigitacao.soPermiteTextFieldNullSNInterrogacao(txtSalvarCstg_IntCM);
		RestricoesDeDigitacao.soPermiteTextFieldNullSNInterrogacao(txtSalvarCstg_IntDG);
		Utilitarios.formatarDatePicker(dpDataMovimento, "dd/MM/yyyy");

		if (flagIncluir.equals("S")) {
			desabilitarCompoenentes(false);
		} else {
			if (flagAlterar.equals("S")) {
				desabilitarCompoenentes(true);
				txtSobreporPoliticas.setDisable(false);
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
		txtTipoMovimento.setDisable(b);
		txtCodCentroCustos.setDisable(b);
		txtDescCentroCustos.setDisable(b);
		txtCodContaContabil.setDisable(b);
		txtDescContaContabil.setDisable(b);
		txtCodNatureza.setDisable(b);
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
		txtSobreporPoliticas.setDisable(b);
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
		txtTipoMovimento.setText(entidade.getTipoMovimento());
		txtOrigem.setText(entidade.getOrigem());
		txtCodCentroCustos.setText(String.format("%.0f", entidade.getCodCentroCustos()));
		txtDescCentroCustos.setText(entidade.getDescCentroCustos());
		txtCodContaContabil.setText(entidade.getCodContaContabil());
		txtDescContaContabil.setText(entidade.getDescContaContabil());
		txtCodNatureza.setText(entidade.getCodNatureza());
		txtCodMaterial.setText(entidade.getCodMaterial());
		txtDescMovimento.setText(entidade.getDescMovimento());
		txtUnidadeMedida.setText(entidade.getUnidadeMedida());
		txtQuantidade.setText(String.format("%.4f", entidade.getQuantidade()));
		txtPrecoUnitario.setText(String.format("%.4f", entidade.getPrecoUnitario()));
		txtValorMovimento.setText(String.format("%.4f", entidade.getValorMovimento()));
		txtNumeroOS.setText(entidade.getNumeroOS());
		txtFrotaOuCC.setText(entidade.getFrotaOuCC());
		txtValidacoesOS.setText(entidade.getValidacoesOS());
		txtDocumentoErp.setText(entidade.getDocumentoErp());

//		dpDataMovimento.setValue(entidade.getDataMovimento()); erro de compilação Date x LocalDate
		if (entidade.getDataMovimento() != null) {
			dpDataMovimento
					.setValue(LocalDate.ofInstant(entidade.getDataMovimento().toInstant(), ZoneId.systemDefault()));
		}

		txtSobreporPoliticas.setText(entidade.getSobreporPoliticas());
		txtPoliticas.setText(entidade.getPoliticas());
		txtImportar.setText(entidade.getImportar());
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
		objeto.setTipoMovimento(txtTipoMovimento.getText());
		objeto.setCodCentroCustos(Utilitarios.tentarConverterParaDouble(txtCodCentroCustos.getText()));
		objeto.setDescCentroCustos(txtDescCentroCustos.getText());
		objeto.setCodContaContabil(txtCodContaContabil.getText());
		objeto.setDescContaContabil(txtDescContaContabil.getText());
		objeto.setCodNatureza(txtCodNatureza.getText());
		objeto.setCodMaterial(txtCodMaterial.getText());
		objeto.setDescMovimento(txtDescMovimento.getText());
		objeto.setUnidadeMedida(txtUnidadeMedida.getText());
		objeto.setQuantidade(Utilitarios.tentarConverterParaDouble(txtQuantidade.getText()));
		objeto.setPrecoUnitario(Utilitarios.tentarConverterParaDouble(txtPrecoUnitario.getText()));
		objeto.setValorMovimento(Utilitarios.tentarConverterParaDouble(txtValorMovimento.getText()));
		objeto.setNumeroOS(txtNumeroOS.getText());
		objeto.setFrotaOuCC(txtFrotaOuCC.getText());
		objeto.setValidacoesOS(txtValidacoesOS.getText());
		objeto.setDocumentoErp(txtDocumentoErp.getText());
		if (dpDataMovimento.getValue() != null) {
			Instant instant = Instant.from(dpDataMovimento.getValue().atStartOfDay(ZoneId.systemDefault()));
			objeto.setDataMovimento(Date.from(instant));
		}
		objeto.setSobreporPoliticas(Utilitarios.tentarConverterParaMaiusculo(txtSobreporPoliticas.getText()));
		objeto.setPoliticas(txtPoliticas.getText());
		objeto.setImportar(Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText()));
		objeto.setSalvarOS_Material(Utilitarios.tentarConverterParaMaiusculo(txtSalvarOS_Material.getText()));
		objeto.setSalvarCstg_IntVM(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntVM.getText()));
		objeto.setSalvarCstg_IntCM(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntCM.getText()));
		objeto.setSalvarCstg_IntDG(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntDG.getText()));
		objeto.setObservacao(txtObservacao.getText());

		objeto = substituirNull(objeto);

		if (txtAnoMes.getText() == null || txtAnoMes.getText().trim().equals("")) {
			validacao.adicionarErro("txtAnoMes", "Informe o Ano e Mes de referencia");
		}
		if (txtAnoMes.getText() != null && txtAnoMes.getText().length() < 6) {
			validacao.adicionarErro("txtAnoMes", "Informe no formato AAAAMM");
		}
		if ((txtOrigem.getText() == null) || txtOrigem.getText().trim().equals("")
				|| ((!txtOrigem.getText().toUpperCase().equals("RM"))
						&& (!txtOrigem.getText().toUpperCase().equals("ED"))
						&& (!txtOrigem.getText().toUpperCase().equals("DF")))) {
			validacao.adicionarErro("txtOrigem", "Informe a Origem: RM, ED ou DF");
		}
		if (txtCodCentroCustos.getText() == null || txtCodCentroCustos.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodCentroCustos", "Informe o Centro de Custos");
		}
		if ((txtOrigem.getText() != null) && (txtOrigem.getText().toUpperCase().equals("DF"))
				&& (txtCodContaContabil.getText() == null || txtCodContaContabil.getText().trim().equals(""))) {
			validacao.adicionarErro("txtCodContaContabil", "Origem DF - Informe a Conta Contabil");
		}
		if ((txtOrigem.getText() != null) && (txtOrigem.getText().toUpperCase().equals("RM"))
				&& (txtCodMaterial.getText() == null || txtCodMaterial.getText().trim().equals(""))) {
			validacao.adicionarErro("txtCodMaterial", "Origem RM - Informe o Material");
		}
		if ((txtOrigem.getText() != null) && (txtOrigem.getText().toUpperCase().equals("ED"))
				&& (txtCodMaterial.getText() == null || txtCodMaterial.getText().trim().equals(""))) {
			validacao.adicionarErro("txtCodMaterial", "Origem ED - Informe o Material/Serviço");
		}

		if (txtSobreporPoliticas.getText() != null && txtSobreporPoliticas.getText().toUpperCase().equals("S")) {
			if ((txtImportar.getText() == null || txtImportar.getText().equals(""))
					&& (txtSalvarCstg_IntVM.getText() == null || txtSalvarCstg_IntVM.getText().equals(""))
					&& (txtSalvarCstg_IntCM.getText() == null || txtSalvarCstg_IntCM.getText().equals(""))
					&& (txtSalvarCstg_IntDG.getText() == null || txtSalvarCstg_IntDG.getText().equals(""))
					&& (txtSalvarOS_Material.getText() == null || txtSalvarOS_Material.getText().equals(""))) {
				validacao.adicionarErro("txtSobreporPoliticas", "Sobrepor Politica 'S' - Informe as politicas abaixo");
			}
		}

		int acoesSim = contarAcoes("S");
		int acoesInterrogacao = contarAcoes("?");
		if (txtImportar.getText() != null) {
			if (txtImportar.getText().toUpperCase().equals("S") && ((acoesSim + acoesInterrogacao) != 1)) {
				validacao.adicionarErro("txtImportar", "Importar SIM. Então infome um, e apenas um, Salvar");
			}
			if (txtImportar.getText().toUpperCase().equals("N") && (acoesSim + acoesInterrogacao) != 0) {
				validacao.adicionarErro("txtImportar", "Importar NAO. Então nao faz sentido ter algum Salvar");
			}
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
		if (objeto.getQuantidade() == null) {
			objeto.setQuantidade(0.00);
		}
		if (objeto.getPrecoUnitario() == null) {
			objeto.setPrecoUnitario(0.00);
		}
		if (objeto.getValorMovimento() == null) {
			objeto.setValorMovimento(0.00);
		}

		try {
			if (objeto.getDataMovimento() == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date dataMovimento;
				dataMovimento = sdf.parse(txtAnoMes.getText() + "01");
				objeto.setDataMovimento(dataMovimento);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objeto;
	}

	private int contarAcoes(String simNaoInterrogacao) {
		int contaOS = 0;
		int contaCM = 0;
		int contaDG = 0;
		if (txtSalvarOS_Material.getText() != null) {
			contaOS = (txtSalvarOS_Material.getText().toUpperCase().equals(simNaoInterrogacao) ? 1 : 0);
		}
		if (txtSalvarCstg_IntCM.getText() != null) {
			contaCM = (txtSalvarCstg_IntCM.getText().toUpperCase().equals(simNaoInterrogacao) ? 1 : 0);
		}
		if (txtSalvarCstg_IntDG.getText() != null) {
			contaDG = (txtSalvarCstg_IntDG.getText().toUpperCase().equals(simNaoInterrogacao) ? 1 : 0);
		}
		return contaOS + contaCM + contaDG;
	}

	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		labelErroAnoMes.setText((campos.contains("txtAnoMes") ? erros.get("txtAnoMes") : ""));
		labelErroOrigem.setText((campos.contains("txtOrigem") ? erros.get("txtOrigem") : ""));
		labelErroCodCentroCustos
				.setText((campos.contains("txtCodCentroCustos") ? erros.get("txtCodCentroCustos") : ""));
		labelErroCodContaContabil
				.setText((campos.contains("txtCodContaContabil") ? erros.get("txtCodContaContabil") : ""));
		labelErroCodMaterial.setText((campos.contains("txtCodMaterial") ? erros.get("txtCodMaterial") : ""));
		labelErroSobreporPoliticas
				.setText((campos.contains("txtSobreporPoliticas") ? erros.get("txtSobreporPoliticas") : ""));
		labelErroDataMovimento.setText((campos.contains("txtDataMovimento") ? erros.get("txtDataMovimento") : ""));
		labelErroImportar.setText((campos.contains("txtImportar") ? erros.get("txtImportar") : ""));
		labelErroObservacao.setText((campos.contains("txtObservacao") ? erros.get("txtObservacao") : ""));
	}
}
