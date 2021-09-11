package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import model.entities.VerbaFolha;
import model.exceptions.ValidacaoException;
import model.services.ParametrosService;
import model.services.VerbaFolhaService;

public class VerbaFolhaFormController implements Initializable {

	private VerbaFolha entidade;
	
	private VerbaFolhaService servico;
	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	
	@FXML
	private TextField txtCodVerba;
	@FXML
	private TextField txtDescVerba;
	@FXML
	private TextField txtImportar;
	@FXML
	private Label labelErroCodVerba;	
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
			Alertas.mostrarAlertas("erro Salvando VerbaFolha", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
	
	public void setVerbaFolha(VerbaFolha entidade) {
		this.entidade = entidade;
	}
	
	public void setVerbaFolhaService(VerbaFolhaService servico) {
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
		ParametrosService parametrosService = new ParametrosService();
		flagIncluir = (parametrosService.pesquisarPorChave("VerbaFolha", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("VerbaFolha", "FlagAlterar")).getValor().toUpperCase();
	}
	
	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodVerba);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodVerba, 5);	
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtImportar);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportar, 1);
		if (flagIncluir.equals("S")) {
			txtCodVerba.setDisable(false);
			txtDescVerba.setDisable(false);
			txtImportar.setDisable(false);
		}
		else {
			if (flagAlterar.equals("S")) {
				txtCodVerba.setDisable(true);
				txtDescVerba.setDisable(true);
				txtImportar.setDisable(false);
			}
			else {
				txtCodVerba.setDisable(true);
				txtDescVerba.setDisable(true);
				txtImportar.setDisable(true);
			}
		}
	}
	
	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		txtCodVerba.setText(entidade.getCodVerba());
		txtDescVerba.setText(entidade.getDescVerba());
		txtImportar.setText(entidade.getImportar());
		txtCodVerba.setStyle("-fx-alignment: CENTER-RIGHT");
		
	}
	
	private VerbaFolha getDadosDoForm() {
		VerbaFolha objeto = new VerbaFolha();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		
		objeto.setCodVerba(txtCodVerba.getText());
		objeto.setDescVerba(txtDescVerba.getText());
		objeto.setImportar(Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText()));

		if (txtCodVerba.getText() == null || txtCodVerba.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodVerba", "Informe a Verba da Folha");
		}

		
		if (validacao.getErros().size() > 0) {
			throw validacao;
		}

		return objeto;
	}
	
	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		if (campos.contains("txtCodVerba")) {
			labelErroCodVerba.setText(erros.get("txtCodVerba"));
		}
		else {
			labelErroCodVerba.setText("");
		}
			
	}
}
