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
import model.entities.CriticasErp;
import model.exceptions.ValidacaoException;
import model.services.CriticasErpService;
import model.services.ParametrosService;

public class CriticasErpFormController implements Initializable {

	private CriticasErp entidade;
	
	private CriticasErpService servico;
	private ParametrosService parametrosService = new ParametrosService();

	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;

	@FXML
	private TextField txtTipoCritica;
	@FXML
	private TextField txtCodigoCritica; 
	@FXML
	private TextField txtDescCritica;
	@FXML
	private TextField txtFlagAtiva;
	@FXML
	private TextField txtAnoMesAnalisado;
	@FXML
	private TextField txtRegistrosAnalisados;
	@FXML
	private TextField txtRegistrosAtualizados;
	@FXML
	private TextField txtRegistrosPendentes;
	@FXML
	private TextField txtClausulaWhere;
	@FXML
	private TextField txtClausulaSet;
	@FXML
	private Label labelErroTipoCritica;	
	@FXML
	private Label labelErroDescCritica;	
	@FXML
	private Label labelErroAtiva;	
	@FXML
	private Label labelErroClausulaWhere;	
	@FXML
	private Label labelErroClausulaSet;	
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
		}
		catch (ValidacaoException e) {
			mostrarErrosDeDigitacao(e.getErros());
		}
		catch (DbException e){
			Alertas.mostrarAlertas("erro Salvando DadosCriticasErp", null, e.getMessage(), AlertType.ERROR);
		}
	}
	

	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
	
	public void setCriticasErp(CriticasErp entidade) {
		this.entidade = entidade;
	}
	
	public void setCriticasErpService(CriticasErpService servico) {
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
		flagIncluir = (parametrosService.pesquisarPorChave("CriticasErp", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("CriticasErp", "FlagAlterar")).getValor().toUpperCase();
	}

	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodigoCritica);
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtRegistrosAnalisados);
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtRegistrosAtualizados);
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtRegistrosPendentes);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtTipoCritica, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodigoCritica, 3);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescCritica, 250);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtFlagAtiva, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAnoMesAnalisado, 6);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtRegistrosAnalisados, 5);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtRegistrosAtualizados, 5);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtRegistrosPendentes, 5);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtClausulaWhere, 250);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtClausulaSet, 250);
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtFlagAtiva);
		if (flagIncluir.equals("S")) {
			desabilitarCompoenentes(false);
		}
		else {
			if (flagAlterar.equals("S")) {
				desabilitarCompoenentes(true);
				txtFlagAtiva.setDisable(false);
				txtClausulaWhere.setDisable(false);
				txtClausulaSet.setDisable(false);
			}
			else {
				desabilitarCompoenentes(true);
			}
		}
	}
	
	private void desabilitarCompoenentes(boolean b) {
		txtTipoCritica.setDisable(b);
		txtCodigoCritica.setDisable(b);
		txtDescCritica.setDisable(b);
		txtFlagAtiva.setDisable(b);
		txtClausulaWhere.setDisable(b);
		txtClausulaSet.setDisable(b);
	}

	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		Locale.setDefault(Locale.US);

		txtTipoCritica.setText(entidade.getTipoCritica());
		txtCodigoCritica.setText(String.format("%d", entidade.getCodigoCritica()));
		txtDescCritica.setText(entidade.getDescCritica());
		txtFlagAtiva.setText(entidade.getFlagAtiva());
		txtAnoMesAnalisado.setText(entidade.getAnoMesAnalisado());
		txtRegistrosAnalisados.setText(String.format("%d", entidade.getRegistrosAnalisados()));
		txtRegistrosAtualizados.setText(String.format("%d", entidade.getRegistrosAtualizados()));
		txtRegistrosPendentes.setText(String.format("%d", entidade.getRegistrosPendentes()));
		txtClausulaWhere.setText(entidade.getClausulaWhere());
		txtClausulaSet.setText(entidade.getClausulaSet());
		
		txtRegistrosAnalisados.setStyle("-fx-alignment: CENTER-RIGHT");
		txtRegistrosAtualizados.setStyle("-fx-alignment: CENTER-RIGHT");
		txtRegistrosPendentes.setStyle("-fx-alignment: CENTER-RIGHT");
	}
	
	private CriticasErp getDadosDoForm() {
		CriticasErp objeto = new CriticasErp();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		objeto.setTipoCritica(Utilitarios.tentarConverterParaMaiusculo(txtTipoCritica.getText()));
		objeto.setCodigoCritica(Utilitarios.tentarConverterParaInt(txtCodigoCritica.getText()));
		objeto.setDescCritica(txtDescCritica.getText());
		objeto.setFlagAtiva(Utilitarios.tentarConverterParaMaiusculo(txtFlagAtiva.getText()));
		objeto.setAnoMesAnalisado(txtAnoMesAnalisado.getText());
		objeto.setRegistrosAnalisados(Utilitarios.tentarConverterParaInt(txtRegistrosAnalisados.getText()));
		objeto.setRegistrosAtualizados(Utilitarios.tentarConverterParaInt(txtRegistrosAtualizados.getText()));
		objeto.setRegistrosPendentes(Utilitarios.tentarConverterParaInt(txtRegistrosPendentes.getText()));
		objeto.setClausulaWhere(txtClausulaWhere.getText());
		objeto.setClausulaSet(txtClausulaSet.getText());

		if ((txtTipoCritica.getText() != null) && (txtTipoCritica.getText().toUpperCase().equals("S")
				|| txtTipoCritica.getText().toUpperCase().equals("U") )) {
			// ok
		} else {
			validacao.adicionarErro("txtTipoCritica", "Informe o Tipo de Critica: S-Sistema ou U-Usuario");
		}

		if (txtFlagAtiva.getText() == null || txtFlagAtiva.getText().trim().equals("")) {
			validacao.adicionarErro("txtFlagAtiva", "Informe se essa Critica esta Ativa ou Nao");
		}

		if (txtDescCritica.getText() == null || txtDescCritica.getText().trim().equals("")) {
			validacao.adicionarErro("txtDescCritica", "Informe uma descricao para essa Critica");
		}
		
		if ((txtTipoCritica.getText() != null) && (txtTipoCritica.getText().toUpperCase().equals("U")) && 
			(txtClausulaWhere.getText() == null || txtClausulaWhere.getText().trim().equals(""))) {
			validacao.adicionarErro("txtClausulaWhere", "Critica do tipo U (Usuario). Informe a Clausula Where");
		}
	
		if ((txtTipoCritica.getText() != null) && (txtTipoCritica.getText().toUpperCase().equals("U")) && 
			(txtClausulaSet.getText() == null || txtClausulaSet.getText().trim().equals(""))) {
			validacao.adicionarErro("txtClausulaSet", "Critica do tipo U (Usuario). Informe a Clausula Set");
		}
		
		if ((txtTipoCritica.getText() != null) && (txtTipoCritica.getText().toUpperCase().equals("S")) && 
				(txtClausulaWhere.getText() != null && ! txtClausulaWhere.getText().trim().equals(""))) {
				validacao.adicionarErro("txtClausulaWhere", "Critica do tipo S (Sistema). Apague a Clausula Where");
			}
		
		if ((txtTipoCritica.getText() != null) && (txtTipoCritica.getText().toUpperCase().equals("S")) && 
			(txtClausulaSet.getText() != null &&  ! txtClausulaSet.getText().trim().equals(""))) {
			validacao.adicionarErro("txtClausulaSet", "Critica do tipo S (Sistema). Apague a Clausula Set");
		}

		if (validacao.getErros().size() > 0) {
			throw validacao;
		}
		return objeto;
	}
	
	private CriticasErp substituirNull(CriticasErp objeto) {
		if (objeto.getRegistrosAnalisados() == null) { objeto.setRegistrosAnalisados(0);	}
		if (objeto.getRegistrosAtualizados() == null) { objeto.setRegistrosAtualizados(0);	}
		if (objeto.getRegistrosPendentes() == null) { objeto.setRegistrosPendentes(0);	}
		return objeto;
	}

	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		labelErroTipoCritica.setText((campos.contains("txtTipoCritica") ? erros.get("txtTipoCritica") : ""));
		labelErroAtiva.setText((campos.contains("txtFlagAtiva") ? erros.get("txtFlagAtiva") : ""));
		labelErroDescCritica.setText((campos.contains("txtDescCritica") ? erros.get("txtDescCritica") : ""));
		labelErroClausulaWhere.setText((campos.contains("txtClausulaWhere") ? erros.get("txtClausulaWhere") : ""));
		labelErroClausulaSet.setText((campos.contains("txtClausulaSet") ? erros.get("txtClausulaSet") : ""));
	}
}
