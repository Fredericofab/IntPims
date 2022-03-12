package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.entities.ProcessoAtual;
import model.services.ErpFiltrosService;
import model.services.ParametrosService;
import model.services.ProcessoAtualService;

public class ErpFiltrosViewController implements Initializable {
	
	private ParametrosService parametrosService = new ParametrosService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
	private ErpFiltrosService erpFiltrosService = new ErpFiltrosService();
	private ProcessoAtual processoAtual;

	private List<DadosAlteradosListener> ouvintes = new ArrayList<DadosAlteradosListener>();

//	Parametros
	String anoMes;

	private String importar;
	private String valorMaterial;
	private String sobreporPolitica;
	private Boolean liberacaoDupla;
	private String politica;
	private String validacaoOS;
	private String filtro;
	
	@FXML
	private TextField txtImportar;
	@FXML
	private TextField txtValorMaterial;
	@FXML
	private TextField txtSobreporPolitica;
	@FXML
	private CheckBox checkLiberacaoDupla;
	@FXML
	private TextField txtPolitica;
	@FXML	
	private ToggleGroup validacoesOS;
	@FXML
	private RadioButton radioNenhum;
	@FXML
	private ComboBox<String> cboxCamposOracle;
	@FXML
	private TextArea txtAreaFiltro;
	
	@FXML
	private Button btLimpar;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btSair;

	private ObservableList<String> obsCamposOracle;

	@FXML
	public void onBtLimparAction(ActionEvent evento) {
		txtImportar.setText(null);
		txtValorMaterial.setText(null);
		txtSobreporPolitica.setText(null);
		txtPolitica.setText(null);
		txtAreaFiltro.setText(null);
		checkLiberacaoDupla.setSelected(false);
		radioNenhum.setSelected(true);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent evento) {
		getDadosDoForm();
		if (( importar != null ||  politica != null || validacaoOS != null || 
			  valorMaterial != null || sobreporPolitica != null ||  liberacaoDupla != null ) &&
			( filtro != null )){
			Alertas.mostrarAlertas(null, "Filtro Básico ou Filtro Personalizado", 
							"Limpar os filtros e \n" +
							"Informar filtro(s) Basico(s) OU o Filtro Personalizado.",
							AlertType.ERROR);
		}
		else {
			erpFiltrosService.salvarFiltro(importar, valorMaterial, sobreporPolitica, liberacaoDupla, politica, validacaoOS, filtro);
			notificarDadosAlteradosListeners();
			Utilitarios.atualStage(evento).close();
		}
	}

	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		lerParametros();
		inicializarComponentes();
		atualizarTela();
	}

	private void inicializarComponentes() {
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtImportar, 1);
		RestricoesDeDigitacao.soPermiteTextFieldTamanhoMax(txtPolitica, 4);
		
		List<String> listaCamposOracle = Utilitarios.camposErp();

		checkLiberacaoDupla.setSelected(false);
		
		obsCamposOracle = FXCollections.observableArrayList(listaCamposOracle);
		cboxCamposOracle.setItems(obsCamposOracle);
	}


	private void atualizarTela() {
		txtImportar.setText(null);
		txtValorMaterial.setText(null);
		txtSobreporPolitica.setText(null);
		txtPolitica.setText(null);
		processoAtual	= processoAtualService.pesquisarPorChave(anoMes);
		filtro = processoAtual.getFiltroErp();
		txtAreaFiltro.setText(filtro);
	}

	private void getDadosDoForm() {
		importar = Utilitarios.tentarConverterParaMaiusculo(txtImportar.getText());
		valorMaterial = Utilitarios.tentarConverterParaMaiusculo(txtValorMaterial.getText());
		sobreporPolitica = Utilitarios.tentarConverterParaMaiusculo(txtSobreporPolitica.getText());
		liberacaoDupla  = checkLiberacaoDupla.isSelected();
		politica  = Utilitarios.tentarConverterParaMaiusculo(txtPolitica.getText());
		if (validacoesOS.getSelectedToggle() == null ) {
			validacaoOS = null;
		}
		else {
			RadioButton radio = (RadioButton) validacoesOS.getSelectedToggle();
			validacaoOS = radio.getText();
			if (validacaoOS.equals("Nenhum")) {
				validacaoOS = null;
			}
		}
		filtro = txtAreaFiltro.getText();
	}

	private void lerParametros() {
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}

}
