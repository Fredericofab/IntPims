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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.PoliticasErp;
import model.exceptions.ValidacaoException;
import model.services.PoliticasErpService;
import model.services.ParametrosService;

public class PoliticasErpFormController implements Initializable {

	private PoliticasErp entidade;
	
	private PoliticasErpService servico;
	private ParametrosService parametrosService = new ParametrosService();

	
	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();
	
//	Parametros
	String flagIncluir;
	String flagAlterar;

	@FXML
	private TextField txtCodPolitica; 
	@FXML
	private TextField txtFlagAtiva;
	
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
	private TextField txtNomePolitica;
	@FXML
	private TextArea txtAreaDescPolitica;
	@FXML
	private TextArea txtAreaClausulaWhere;
	@FXML
	private ComboBox<String> cboxCamposOracle;

	@FXML
	private Label labelErroPolitica;	
	@FXML
	private Label labelErroAtiva;	
	@FXML
	private Label labelErroImportar;

	@FXML
	private Label labelErroDescPolitica;
	@FXML
	private Label labelErroClausulaWhere;	

	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	
	private ObservableList<String> obsCamposOracle;

	@FXML
	public void onBtSalvarAction(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esqueceu de injetar a entuidade");
		}
		if (servico == null) {
			throw new IllegalStateException("o outro programador esqueceu de injetar o servico");
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
			Alertas.mostrarAlertas("erro Salvando DadosPoliticasErp", null, e.getMessage(), AlertType.ERROR);
		}
	}
	

	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
	
	public void setPoliticasErp(PoliticasErp entidade) {
		this.entidade = entidade;
	}
	
	public void setPoliticasErpService(PoliticasErpService servico) {
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
		flagIncluir = (parametrosService.pesquisarPorChave("PoliticasErp", "FlagIncluir")).getValor().toUpperCase();
		flagAlterar = (parametrosService.pesquisarPorChave("PoliticasErp", "FlagAlterar")).getValor().toUpperCase();
	}

	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtCodPolitica);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodPolitica, 4);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtNomePolitica, 250);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtFlagAtiva, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportar, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarOS_Material, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntVM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntCM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntDG, 1);
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtFlagAtiva);
		RestricoesDeDigitacao.soPermiteTextFieldSNinterrogacao(txtImportar);
		RestricoesDeDigitacao.soPermiteTextFieldBrancoSNinterrogacao(txtSalvarOS_Material);
		RestricoesDeDigitacao.soPermiteTextFieldBrancoSNinterrogacao(txtSalvarCstg_IntVM);
		RestricoesDeDigitacao.soPermiteTextFieldBrancoSNinterrogacao(txtSalvarCstg_IntCM);
		RestricoesDeDigitacao.soPermiteTextFieldBrancoSNinterrogacao(txtSalvarCstg_IntDG);

		List<String> listaCamposOracle = Utilitarios.camposErp();

		obsCamposOracle = FXCollections.observableArrayList(listaCamposOracle);
		cboxCamposOracle.setItems(obsCamposOracle);

		
		if (flagIncluir.equals("S")) {
			desabilitarCompoenentes(false);
		}
		else {
			if (flagAlterar.equals("S")) {
				desabilitarCompoenentes(true);
				txtFlagAtiva.setDisable(false);
				txtImportar.setDisable(false);
				txtSalvarOS_Material.setDisable(false);
				txtSalvarCstg_IntVM.setDisable(false);
				txtSalvarCstg_IntCM.setDisable(false);
				txtSalvarCstg_IntDG.setDisable(false);
				txtAreaClausulaWhere.setDisable(false);
			}
			else {
				desabilitarCompoenentes(true);
			}
		}
	}
	
	private void desabilitarCompoenentes(boolean b) {
		txtCodPolitica.setDisable(b);
		txtAreaDescPolitica.setDisable(b);
		txtFlagAtiva.setDisable(b);
		txtImportar.setDisable(b);
		txtSalvarOS_Material.setDisable(b);
		txtSalvarCstg_IntVM.setDisable(b);
		txtSalvarCstg_IntCM.setDisable(b);
		txtSalvarCstg_IntDG.setDisable(b);
		txtAreaClausulaWhere.setDisable(b);
	}

	public void atualizarFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("o outro programador esquecer de injetar a entidade");
		}
		Locale.setDefault(Locale.US);

		txtCodPolitica.setText(String.format("%d", entidade.getCodPolitica()));
		txtNomePolitica.setText(entidade.getNomePolitica());
		txtAreaDescPolitica.setText(entidade.getDescPolitica());
		txtFlagAtiva.setText(entidade.getFlagAtiva());
		txtImportar.setText(entidade.getImportar());
		txtSalvarOS_Material.setText(entidade.getSalvarOS_Material());
		txtSalvarCstg_IntVM.setText(entidade.getSalvarCstg_IntVM());
		txtSalvarCstg_IntCM.setText(entidade.getSalvarCstg_IntCM());
		txtSalvarCstg_IntDG.setText(entidade.getSalvarCstg_IntDG());
		txtAreaClausulaWhere.setText(entidade.getClausulaWhere());

	}
	
	private PoliticasErp getDadosDoForm() {
		PoliticasErp objeto = new PoliticasErp();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		objeto.setCodPolitica(Utilitarios.tentarConverterParaInt(txtCodPolitica.getText()));
		objeto.setNomePolitica(txtNomePolitica.getText());
		objeto.setDescPolitica(txtAreaDescPolitica.getText());
		objeto.setFlagAtiva(Utilitarios.tentarConverterParaMaiusculo(txtFlagAtiva.getText()));
		objeto.setImportar(Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText()));
		objeto.setSalvarOS_Material(Utilitarios.tentarConverterParaMaiusculo(txtSalvarOS_Material.getText()));
		objeto.setSalvarCstg_IntVM(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntVM.getText()));
		objeto.setSalvarCstg_IntCM(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntCM.getText()));
		objeto.setSalvarCstg_IntDG(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntDG.getText()));
		objeto.setClausulaWhere(txtAreaClausulaWhere.getText());

		if (txtFlagAtiva.getText() == null || txtFlagAtiva.getText().trim().equals("")) {
			validacao.adicionarErro("txtFlagAtiva", "Informe se essa Politica esta Ativa ou Nao");
		}
		
		if (txtImportar.getText() == null || txtImportar.getText().trim().equals("")) {
			validacao.adicionarErro("txtImportar", "Informe se é para Importar ou Nao");
		}

		if (txtImportar.getText() != null) {
			String as4acoes = ( (txtSalvarOS_Material.getText() == null) ? " " : txtSalvarOS_Material.getText().toUpperCase() )
							+ ( (txtSalvarCstg_IntVM.getText()  == null) ? " " : txtSalvarCstg_IntVM.getText().toUpperCase() )
							+ ( (txtSalvarCstg_IntCM.getText()  == null) ? " " : txtSalvarCstg_IntCM.getText().toUpperCase() )
							+ ( (txtSalvarCstg_IntDG.getText()  == null) ? " " : txtSalvarCstg_IntDG.getText().toUpperCase() ) ;
			if ( txtImportar.getText().toUpperCase().equals("S")  &&  as4acoes.indexOf("S") == -1 &&  as4acoes.indexOf("?") == -1  ) { 
				validacao.adicionarErro("txtImportar", "Importar SIM. Então infome pelo menos um Salvar");
			}
			if ( txtImportar.getText().toUpperCase().equals("N")  &&  ( as4acoes.indexOf("S") != -1  || as4acoes.indexOf("?") != -1)) {
				validacao.adicionarErro("txtImportar", "Importar NAO. Então nao faz sentido ter algum Salvar");
			}
		}

		if (txtCodPolitica.getText() == null || txtCodPolitica.getText().trim().equals("")) {
			validacao.adicionarErro("txtPolitica", "Informe o Codigo para essa Politica");
		}
		
		if (txtNomePolitica.getText() == null || txtNomePolitica.getText().trim().equals("")) {
			validacao.adicionarErro("txtPolitica", "Informe um Nome para essa Politica");
		}
		if (txtAreaDescPolitica.getText() == null || txtAreaDescPolitica.getText().trim().equals("")) {
			validacao.adicionarErro("txtDescPolitica", "Informe uma descricao para essa Politica");
		}
		if (txtAreaClausulaWhere.getText() == null || txtAreaClausulaWhere.getText().trim().equals("")) {
			validacao.adicionarErro("txtClausulaWhere", "Informe a Clausula Where");
		}
			
		if (validacao.getErros().size() > 0) {
			throw validacao;
		}
		return objeto;
	}

	private PoliticasErp substituirNull(PoliticasErp objeto) {
		if (objeto.getRegistrosAplicados() == null) objeto.setRegistrosAplicados(0);
		return objeto;
	}
	
	
	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		labelErroPolitica.setText((campos.contains("txtPolitica") ? erros.get("txtPolitica") : ""));
		labelErroClausulaWhere.setText((campos.contains("txtClausulaWhere") ? erros.get("txtClausulaWhere") : ""));
		labelErroAtiva.setText((campos.contains("txtFlagAtiva") ? erros.get("txtFlagAtiva") : ""));
		labelErroDescPolitica.setText((campos.contains("txtDescPolitica") ? erros.get("txtDescPolitica") : ""));
		labelErroImportar.setText((campos.contains("txtImportar") ? erros.get("txtImportar") : ""));
	}
}
