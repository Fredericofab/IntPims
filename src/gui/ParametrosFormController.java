package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import model.entities.Parametros;
import model.exceptions.ValidacaoException;
import model.services.ParametrosService;

public class ParametrosFormController implements Initializable {

	private Parametros entidade;
	
	private ParametrosService servico;
	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
	@FXML
	private TextField txtSecao;
	@FXML
	private TextField txtEntrada;
	@FXML
	private TextField txtValor;
	@FXML
	private TextField txtDescricao;

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
//		catch (ValidacaoException e) {
//			mostrarErrosDeDigitacao(e.getErros());
//		}
		catch (DbException e){
			Alertas.mostrarAlertas("erro Salvando Parametros", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
	
	public void setParametros(Parametros entidade) {
		this.entidade = entidade;
	}
	
	public void setParametrosService(ParametrosService servico) {
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
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSecao, 30);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtEntrada, 30);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtValor, 2555);	
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtDescricao, 255);	
	}
	
	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		txtSecao.setText(entidade.getSecao());
		txtEntrada.setText(entidade.getEntrada());
		txtValor.setText(entidade.getValor());
		txtDescricao.setText(entidade.getDescricao());
	}
	
	private Parametros getDadosDoForm() {
		Parametros objeto = new Parametros();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		
		objeto.setSecao(txtSecao.getText());
		objeto.setEntrada(txtEntrada.getText());
		objeto.setValor(txtValor.getText());
		objeto.setDescricao(txtDescricao.getText());
		
		objeto.setValor(Utilitarios.tentarConverterParaMaiusculo(txtValor.getText()));

//		if (txtCodVerba.getText() == null || txtCodVerba.getText().trim().equals("")) {
//			validacao.adicionarErro("txtCodVerba", "Informe a Verba da Folha");
//		}
//
//		
//		if (validacao.getErros().size() > 0) {
//			throw validacao;
//		}
//
		return objeto;
	}
	
//	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
//		Set<String> campos = erros.keySet();
//		if (campos.contains("txtCodVerba")) {
//			labelErroCodVerba.setText(erros.get("txtCodVerba"));
//		}
//		else {
//			labelErroCodVerba.setText("");
//		}
//			
//	}
}
