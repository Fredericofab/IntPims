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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.CriticaErp;
import model.exceptions.ValidacaoException;
import model.services.CriticaErpService;
import model.services.ParametrosService;

public class CriticaErpFormController implements Initializable {

	private CriticaErp entidade;
	
	private CriticaErpService servico;
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
	private TextField txtAnoMesAnalisado;
	@FXML
	private TextField txtRegistrosPendentes;
	@FXML
	private TextField txtNomeCritica;
	@FXML
	private TextArea txtAreaDescCritica;
	@FXML
	private TextArea txtAreaClausulaWhere;
	@FXML
	private Label labelErroTipoCritica;	
	@FXML
	private Label labelErroDescCritica;	
	@FXML
	private Label labelErroAtiva;	
	@FXML
	private Label labelErroImportar;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	
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
			Alertas.mostrarAlertas("erro Salvando DadosCriticasErp", null, e.getMessage(), AlertType.ERROR);
		}
	}
	

	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}
	
	public void setCriticasErp(CriticaErp entidade) {
		this.entidade = entidade;
	}
	
	public void setCriticasErpService(CriticaErpService servico) {
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
		RestricoesDeDigitacao.soPermiteTextFieldInteiro(txtRegistrosPendentes);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtTipoCritica, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtCodigoCritica, 3);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtNomeCritica, 250);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtFlagAtiva, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportar, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarOS_Material, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntVM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntCM, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtSalvarCstg_IntDG, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtAnoMesAnalisado, 6);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtRegistrosPendentes, 5);
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtFlagAtiva);
		RestricoesDeDigitacao.soPermiteTextFieldSN(txtImportar);
		RestricoesDeDigitacao.soPermiteTextFieldBrancoSNinterrogacao(txtSalvarOS_Material);
		RestricoesDeDigitacao.soPermiteTextFieldBrancoSNinterrogacao(txtSalvarCstg_IntVM);
		RestricoesDeDigitacao.soPermiteTextFieldBrancoSNinterrogacao(txtSalvarCstg_IntCM);
		RestricoesDeDigitacao.soPermiteTextFieldBrancoSNinterrogacao(txtSalvarCstg_IntDG);

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
		txtTipoCritica.setDisable(b);
		txtCodigoCritica.setDisable(b);
		txtAreaDescCritica.setDisable(b);
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

		txtTipoCritica.setText(entidade.getTipoCritica());
		txtCodigoCritica.setText(String.format("%d", entidade.getCodigoCritica()));
		txtNomeCritica.setText(entidade.getNomeCritica());
		txtAreaDescCritica.setText(entidade.getDescCritica());
		txtFlagAtiva.setText(entidade.getFlagAtiva());
		txtImportar.setText(entidade.getImportar());
		txtSalvarOS_Material.setText(entidade.getSalvarOS_Material());
		txtSalvarCstg_IntVM.setText(entidade.getSalvarCstg_IntVM());
		txtSalvarCstg_IntCM.setText(entidade.getSalvarCstg_IntCM());
		txtSalvarCstg_IntDG.setText(entidade.getSalvarCstg_IntDG());
		txtAnoMesAnalisado.setText(entidade.getAnoMesAnalisado());
		txtRegistrosPendentes.setText(String.format("%d", entidade.getRegistrosPendentes()));
		txtAreaClausulaWhere.setText(entidade.getClausulaWhere());
		
		txtRegistrosPendentes.setStyle("-fx-alignment: CENTER-RIGHT");
	}
	
	private CriticaErp getDadosDoForm() {
		CriticaErp objeto = new CriticaErp();
		ValidacaoException validacao = new ValidacaoException("Erros Na Digitacao do Form");
		objeto.setTipoCritica(Utilitarios.tentarConverterParaMaiusculo(txtTipoCritica.getText()));
		objeto.setCodigoCritica(Utilitarios.tentarConverterParaInt(txtCodigoCritica.getText()));
		objeto.setNomeCritica(txtNomeCritica.getText());
		objeto.setDescCritica(txtAreaDescCritica.getText());
		objeto.setFlagAtiva(Utilitarios.tentarConverterParaMaiusculo(txtFlagAtiva.getText()));
		objeto.setImportar(Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText()));
		objeto.setSalvarOS_Material(Utilitarios.tentarConverterParaMaiusculo(txtSalvarOS_Material.getText()));
		objeto.setSalvarCstg_IntVM(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntVM.getText()));
		objeto.setSalvarCstg_IntCM(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntCM.getText()));
		objeto.setSalvarCstg_IntDG(Utilitarios.tentarConverterParaMaiusculo(txtSalvarCstg_IntDG.getText()));
		objeto.setAnoMesAnalisado(txtAnoMesAnalisado.getText());
		objeto.setRegistrosPendentes(Utilitarios.tentarConverterParaInt(txtRegistrosPendentes.getText()));
		objeto.setClausulaWhere(txtAreaClausulaWhere.getText());

		if (txtFlagAtiva.getText() == null || txtFlagAtiva.getText().trim().equals("")) {
			validacao.adicionarErro("txtFlagAtiva", "Informe se essa Critica esta Ativa ou Nao");
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


		if (txtNomeCritica.getText() == null || txtNomeCritica.getText().trim().equals("")) {
			validacao.adicionarErro("txtDescCritica", "Informe um Nome para essa Critica");
		}

		if (txtAreaDescCritica.getText() == null || txtAreaDescCritica.getText().trim().equals("")) {
			validacao.adicionarErro("txtDescCritica", "Informe uma descricao para essa Critica");
		}
		
		if ((txtTipoCritica.getText() != null) && (txtTipoCritica.getText().toUpperCase().equals("U")) && 
			(txtAreaClausulaWhere.getText() == null || txtAreaClausulaWhere.getText().trim().equals(""))) {
			validacao.adicionarErro("txtTipoCritica", "Critica do tipo U (Usuario). Informe a Clausula Where");
		}
	
		if ((txtTipoCritica.getText() != null) && (txtTipoCritica.getText().toUpperCase().equals("S")) && 
				(txtAreaClausulaWhere.getText() != null && ! txtAreaClausulaWhere.getText().trim().equals(""))) {
				validacao.adicionarErro("txtTipoCritica", "Critica do tipo S (Sistema). Apague a Clausula Where");
			}
		
		if (validacao.getErros().size() > 0) {
			throw validacao;
		}
		return objeto;
	}
	
	private CriticaErp substituirNull(CriticaErp objeto) {
		if (objeto.getRegistrosPendentes() == null) { objeto.setRegistrosPendentes(0);	}
		return objeto;
	}

	private void mostrarErrosDeDigitacao(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		labelErroTipoCritica.setText((campos.contains("txtTipoCritica") ? erros.get("txtTipoCritica") : ""));
		labelErroAtiva.setText((campos.contains("txtFlagAtiva") ? erros.get("txtFlagAtiva") : ""));
		labelErroDescCritica.setText((campos.contains("txtDescCritica") ? erros.get("txtDescCritica") : ""));
		labelErroImportar.setText((campos.contains("txtImportar") ? erros.get("txtImportar") : ""));
	}
}
