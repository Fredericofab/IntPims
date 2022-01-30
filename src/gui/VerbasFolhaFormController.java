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
import model.entities.VerbasFolha;
import model.exceptions.ValidacaoException;
import model.services.ParametrosService;
import model.services.VerbasFolhaService;

public class VerbasFolhaFormController implements Initializable {

	private VerbasFolha entidade;
	
	private VerbasFolhaService servico;
	private ParametrosService parametrosService = new ParametrosService();
	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;
	
	@FXML
	private TextField txtCodVerba;
	@FXML
	private TextField txtDescVerba;
	@FXML
	private TextField txtTipoVerba;
	@FXML
	private TextField txtConsiderarReferencia;
	@FXML
	private TextField txtImportar;
	@FXML
	private Label labelErroCodVerba;	
	@FXML
	private Label labelErroTipoVerba;	
	@FXML
	private Label labelErroConsiderarReferencia;	
	@FXML
	private Label labelErroImportar;	
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
	
	public void setVerbaFolha(VerbasFolha entidade) {
		this.entidade = entidade;
	}
	
	public void setVerbaFolhaService(VerbasFolhaService servico) {
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
		flagIncluir = (parametrosService.pesquisarPorChave("VerbasDaFolha", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("VerbasDaFolha", "FlagAlterar")).getValor().toUpperCase();
	}
	
	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodVerba);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodVerba, 5);	
		RestricoesDeDigitacao.soPermiteTextFieldPDB(txtTipoVerba);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtTipoVerba, 1);
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtImportar);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportar, 1);
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtConsiderarReferencia);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtConsiderarReferencia, 1);
		if (flagIncluir.equals("S")) {
			desabilitarCompoenentes(false);
		}
		else {
			if (flagAlterar.equals("S")) {
				desabilitarCompoenentes(true);
				txtImportar.setDisable(false);
				txtConsiderarReferencia.setDisable(false);
			}
			else {
				desabilitarCompoenentes(true);
			}
		}
	}
	
	private void desabilitarCompoenentes(Boolean b) {
		txtCodVerba.setDisable(b);
		txtDescVerba.setDisable(b);
		txtTipoVerba.setDisable(b);
		txtConsiderarReferencia.setDisable(b);
		txtImportar.setDisable(b);
	}

	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		Locale.setDefault(Locale.US);

		txtCodVerba.setText(String.format("%.0f", entidade.getCodVerba()));
		txtDescVerba.setText(entidade.getDescVerba());
		txtTipoVerba.setText(entidade.getTipoVerba());
		txtConsiderarReferencia.setText(entidade.getConsiderarReferencia());
		txtImportar.setText(entidade.getImportar());
		
		txtCodVerba.setStyle("-fx-alignment: TOP-RIGHT");
	}
	
	private VerbasFolha getDadosDoForm() {
		VerbasFolha objeto = new VerbasFolha();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		
		objeto.setCodVerba(Utilitarios.tentarConverterParaDouble(txtCodVerba.getText()));
		objeto.setDescVerba(txtDescVerba.getText());
		objeto.setTipoVerba(Utilitarios.tentarConverterParaMaiusculo(txtTipoVerba.getText()));
		objeto.setConsiderarReferencia(Utilitarios.tentarConverterParaMaiusculo(txtConsiderarReferencia.getText()));
		objeto.setImportar(Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText()));

		if (txtCodVerba.getText() == null || txtCodVerba.getText().trim().equals("")) {
			validacao.adicionarErro("txtCodVerba", "Informe a Verba da Folha");
		}
		if (txtTipoVerba.getText() == null || txtTipoVerba.getText().trim().equals("")) {
			validacao.adicionarErro("txtTipoVerba", "Informe P-Provento, D-Desconto ou B-Base");
		}
		if (txtConsiderarReferencia.getText() == null || txtConsiderarReferencia.getText().trim().equals("")) {
			validacao.adicionarErro("txtConsiderarReferencia", "Informe S ou N ser considerar essas referencias no total de horas");
		}
		if (txtImportar.getText() == null || txtImportar.getText().trim().equals("")) {
			validacao.adicionarErro("txtImportar", "Informe S ou N para ser importado pelo Pimscs");
		}

		if (validacao.getErros().size() > 0) {
			throw validacao;
		}
		return objeto;
	}
	
	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		labelErroCodVerba.setText((campos.contains("txtCodVerba") ? erros.get("txtCodVerba") : ""));
		labelErroTipoVerba.setText((campos.contains("txtTipoVerba") ? erros.get("txtTipoVerba") : ""));
		labelErroConsiderarReferencia.setText((campos.contains("txtConsiderarReferencia") ? erros.get("txtConsiderarReferencia") : ""));
		labelErroImportar.setText((campos.contains("txtImportar") ? erros.get("txtImportar") : ""));
	}
}
