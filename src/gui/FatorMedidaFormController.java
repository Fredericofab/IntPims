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
import model.entities.FatorMedida;
import model.exceptions.ValidacaoException;
import model.services.ParametrosService;
import model.services.FatorMedidaService;

public class FatorMedidaFormController implements Initializable {

	private FatorMedida entidade;
	
	private FatorMedidaService servico;
	private ParametrosService parametrosService = new ParametrosService();
	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	
	@FXML
	private TextField txtCodMaterial;
	@FXML
	private TextField txtDescMaterial;
	@FXML
	private TextField txtUnidadeMedida;
	@FXML
	private TextField txtFatorDivisao;
	@FXML
	private Label labelErroCodMaterial;	
	@FXML
	private Label labelErroDescMaterial;	
	@FXML
	private Label labelErroUnidadeMedida;	
	@FXML
	private Label labelErroFatorDivisao;	
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
			Alertas.mostrarAlertas("erro Salvando FatorMedida", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
	
	public void setFatorMedida(FatorMedida entidade) {
		this.entidade = entidade;
	}
	
	public void setFatorMedidaService(FatorMedidaService servico) {
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
		RestricoesDeDigitacao.soPermiteTextFieldDouble(txtFatorDivisao);

		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodMaterial, 10);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescMaterial, 255);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtUnidadeMedida,5);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtFatorDivisao, 14);

		
		if (flagIncluir.equals("S")) {
			desabilitarCompoenentes(false);
		}
		else {
			if (flagAlterar.equals("S")) {
				desabilitarCompoenentes(true);
				txtFatorDivisao.setDisable(false);
			}
			else {
				desabilitarCompoenentes(true);
			}
		}
	}
	
	private void desabilitarCompoenentes(Boolean b) {
		txtCodMaterial.setDisable(b);
		txtDescMaterial.setDisable(b);
		txtUnidadeMedida.setDisable(b);
		txtFatorDivisao.setDisable(b);
	}

	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		Locale.setDefault(Locale.US);

		txtCodMaterial.setText(entidade.getCodMaterial());
		txtDescMaterial.setText(entidade.getDescMaterial());
		txtUnidadeMedida.setText(entidade.getUnidadeMedida());
		txtFatorDivisao.setText(String.format("%.0f", entidade.getFatorDivisao()));
		
		txtFatorDivisao.setStyle("-fx-alignment: TOP-RIGHT");
	}
	
	private FatorMedida getDadosDoForm() {
		FatorMedida objeto = new FatorMedida();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		
		objeto.setCodMaterial(txtCodMaterial.getText());
		objeto.setDescMaterial(txtDescMaterial.getText());
		objeto.setUnidadeMedida(Utilitarios.tentarConverterParaMaiusculo(txtUnidadeMedida.getText()));
		objeto.setFatorDivisao(Utilitarios.tentarConverterParaDouble(txtFatorDivisao.getText()));

		if (txtCodMaterial.getText() == null || txtCodMaterial.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodMaterial", "Informe o Codigo do Material");
		}
		if (txtDescMaterial.getText() == null || txtDescMaterial.getText().trim().equals("")) {
			validacao.adicionarErro("txtDescMaterial", "Informe a Descricao do Material");
		}
		if (txtUnidadeMedida.getText() == null || txtUnidadeMedida.getText().trim().equals("")) {
			validacao.adicionarErro("txtUnidadeMedida", "Informe a Unidade de Medida registrada no ERP");
		}
		if (txtFatorDivisao.getText() == null || txtFatorDivisao.getText().trim().equals("")) {
			validacao.adicionarErro("txtFatorDivisao", "Informe o Fator de Divisao para a Unidade de Aplicação no PimsCS");
		}

		if (validacao.getErros().size() > 0) {
			throw validacao;
		}
		return objeto;
	}
	
	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		labelErroCodMaterial.setText((campos.contains("txtCodMaterial") ? erros.get("txtCodMaterial") : ""));
		labelErroDescMaterial.setText((campos.contains("txtDescMaterial") ? erros.get("txtDescMaterial") : ""));
		labelErroUnidadeMedida.setText((campos.contains("txtUnidadeMedida") ? erros.get("txtUnidadeMedida") : ""));
		labelErroFatorDivisao.setText((campos.contains("txtFatorDivisao") ? erros.get("txtFatorDivisao") : ""));
	}
	
	private void lerParametros() {
		flagIncluir = (parametrosService.pesquisarPorChave("FatorMedida", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("FatorMedida", "FlagAlterar")).getValor().toUpperCase();
	}
}
