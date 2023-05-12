package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.services.ExportarErpService;

public class ExportarErpViewController implements Initializable {

	ExportarErpService servico = new ExportarErpService();

	@FXML
	private TextField txtQtdeDeletadaVM;
	@FXML
	private TextField txtQtdeDeletadaCM;
	@FXML
	private TextField txtQtdeDeletadaDG;
	@FXML
	private TextField txtQtdeDeletadaOS;
	@FXML
	private TextField txtQtdeDeletadaCompMat;
	@FXML
	private TextField txtQtdeProcessadaVM;
	@FXML
	private TextField txtQtdeProcessadaCM;
	@FXML
	private TextField txtQtdeProcessadaDG;
	@FXML
	private TextField txtQtdeProcessadaOS;
	@FXML
	private TextField txtQtdeProcessadaCompo;
	@FXML
	private TextField txtQtdeIncluidaVM;
	@FXML
	private TextField txtQtdeIncluidaCM;
	@FXML
	private TextField txtQtdeIncluidaDG;
	@FXML
	private TextField txtQtdeIncluidaOS;
	@FXML
	private TextField txtQtdeIncluidaCompo;
	@FXML
	private TextField txtQtdeAtualizadaVM;
	@FXML
	private TextField txtQtdeAtualizadaCM;
	@FXML
	private TextField txtQtdeAtualizadaDG;
	@FXML
	private TextField txtQtdeAtualizadaOS;

	@FXML
	private Label labelVM;
	@FXML
	private Label labelCM;
	@FXML
	private Label labelDG;
	@FXML
	private Label labelOS;
	@FXML
	private Label labelCompo;

	@FXML
	private Button btExportar;
	@FXML
	private Button btExportarVM;
	@FXML
	private Button btExportarOS;
	@FXML
	private Button btExportarCM;
	@FXML
	private Button btExportarDG;
	@FXML
	private Button btAtualizarCompo;
	@FXML
	private Button btSair;

	@FXML
	public void onBtExportarAction(ActionEvent evento) {
		try {
			servico.processar(null);
			atualizarTela(servico);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo", e.getMessage(),
					AlertType.ERROR);
		} catch (RuntimeException e) {
			Utilitarios.mostrarErroGenerico(e);
		}
	}
	
	@FXML
	public void onBtExportarVMAction(ActionEvent evento) {
		try {
			servico.processar("VM");
			atualizarTela(servico);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo", e.getMessage(),
					AlertType.ERROR);
		} catch (RuntimeException e) {
			Utilitarios.mostrarErroGenerico(e);
		}
	}
	@FXML
	public void onBtExportarCMAction(ActionEvent evento) {
		try {
			servico.processar("CM");
			atualizarTela(servico);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo", e.getMessage(),
					AlertType.ERROR);
		} catch (RuntimeException e) {
			Utilitarios.mostrarErroGenerico(e);
		}

	}
	@FXML
	public void onBtExportarDGAction(ActionEvent evento) {
		try {
			servico.processar("DG");
			atualizarTela(servico);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo", e.getMessage(),
					AlertType.ERROR);
		} catch (RuntimeException e) {
			Utilitarios.mostrarErroGenerico(e);
		}
	}
	@FXML
	public void onBtExportarOSAction(ActionEvent evento) {
		try {
			servico.processar("OS");
			atualizarTela(servico);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo", e.getMessage(),
					AlertType.ERROR);
		} catch (RuntimeException e) {
			Utilitarios.mostrarErroGenerico(e);
		}
	}
	@FXML
	public void onBtAtualizarCompoAction(ActionEvent evento) {
		try {
			servico.processar("COMPO");
			atualizarTela(servico);
		} catch (DbException e) {
			Alertas.mostrarAlertas("DbException", "Erro no Processamento do Arquivo", e.getMessage(),
					AlertType.ERROR);
		} catch (RuntimeException e) {
			Utilitarios.mostrarErroGenerico(e);
		}
	}

	private void atualizarTela(ExportarErpService servico) {
		Integer qtdeDeletadaVM = servico.getQtdeDeletadaVM();
		Integer qtdeDeletadaCM = servico.getQtdeDeletadaCM();
		Integer qtdeDeletadaDG = servico.getQtdeDeletadaDG();
		Integer qtdeDeletadaOS = servico.getQtdeDeletadaOS();
		Integer qtdeDeletadaCompMat = servico.getQtdeDeletadaCompMat();
		Integer qtdeProcessadaVM = servico.getQtdeProcessadaVM();
		Integer qtdeProcessadaCM = servico.getQtdeProcessadaCM();
		Integer qtdeProcessadaDG = servico.getQtdeProcessadaDG();
		Integer qtdeProcessadaOS = servico.getQtdeProcessadaOS();
		Integer qtdeProcessadaCompo = servico.getQtdeProcessadaCompo();
		Integer qtdeIncluidaVM = servico.getQtdeIncluidaVM();
		Integer qtdeIncluidaCM = servico.getQtdeIncluidaCM();
		Integer qtdeIncluidaDG = servico.getQtdeIncluidaDG();
		Integer qtdeIncluidaOS = servico.getQtdeIncluidaOS();
		Integer qtdeIncluidaCompo = servico.getQtdeIncluidaCompo();
		Integer qtdeAtualizadaVM = servico.getQtdeAtualizadaVM();
		Integer qtdeAtualizadaCM = servico.getQtdeAtualizadaCM();
		Integer qtdeAtualizadaDG = servico.getQtdeAtualizadaDG();
		Integer qtdeAtualizadaOS = servico.getQtdeAtualizadaOS();
	
		if (qtdeDeletadaVM != null) txtQtdeDeletadaVM.setText(qtdeDeletadaVM.toString()); 
		if (qtdeDeletadaCM != null) txtQtdeDeletadaCM.setText(qtdeDeletadaCM.toString());
		if (qtdeDeletadaDG != null) txtQtdeDeletadaDG.setText(qtdeDeletadaDG.toString());
		if (qtdeDeletadaOS != null) txtQtdeDeletadaOS.setText(qtdeDeletadaOS.toString());
		if (qtdeDeletadaCompMat != null) txtQtdeDeletadaCompMat.setText(qtdeDeletadaCompMat.toString());
		if (qtdeProcessadaVM != null) txtQtdeProcessadaVM.setText(qtdeProcessadaVM.toString()); 
		if (qtdeProcessadaCM != null) txtQtdeProcessadaCM.setText(qtdeProcessadaCM.toString());
		if (qtdeProcessadaDG != null) txtQtdeProcessadaDG.setText(qtdeProcessadaDG.toString());
		if (qtdeProcessadaOS != null) txtQtdeProcessadaOS.setText(qtdeProcessadaOS.toString());
		if (qtdeProcessadaCompo != null) txtQtdeProcessadaCompo.setText(qtdeProcessadaCompo.toString());
		if (qtdeIncluidaVM != null) txtQtdeIncluidaVM.setText(qtdeIncluidaVM.toString()); 
		if (qtdeIncluidaCM != null) txtQtdeIncluidaCM.setText(qtdeIncluidaCM.toString()); 
		if (qtdeIncluidaDG != null) txtQtdeIncluidaDG.setText(qtdeIncluidaDG.toString()); 
		if (qtdeIncluidaOS != null) txtQtdeIncluidaOS.setText(qtdeIncluidaOS.toString()); 
		if (qtdeIncluidaCompo != null) txtQtdeIncluidaCompo.setText(qtdeIncluidaCompo.toString()); 
		if (qtdeAtualizadaVM != null) txtQtdeAtualizadaVM.setText(qtdeAtualizadaVM.toString()); 
		if (qtdeAtualizadaCM != null) txtQtdeAtualizadaCM.setText(qtdeAtualizadaCM.toString()); 
		if (qtdeAtualizadaDG != null) txtQtdeAtualizadaDG.setText(qtdeAtualizadaDG.toString()); 
		if (qtdeAtualizadaOS != null) txtQtdeAtualizadaOS.setText(qtdeAtualizadaOS.toString()); 

		Integer qtPro; 
		Integer qtInc; 
		Integer qtAtu; 

		qtPro  = ( qtdeProcessadaVM == null ? 0 : qtdeProcessadaVM) ;
		qtInc  = ( qtdeIncluidaVM == null ? 0 : qtdeIncluidaVM) ;
		qtAtu  = ( qtdeAtualizadaVM == null ? 0 : qtdeAtualizadaVM) ;
		if (qtPro > 0) labelVM.setText(( qtPro == qtInc + qtAtu ) ? "Processado com Sucesso" : "" );
	
		qtPro  = ( qtdeProcessadaCM == null ? 0 : qtdeProcessadaCM) ;
		qtInc  = ( qtdeIncluidaCM == null ? 0 : qtdeIncluidaCM) ;
		qtAtu  = ( qtdeAtualizadaCM == null ? 0 : qtdeAtualizadaCM) ;
		if (qtPro > 0) labelCM.setText(( qtPro == qtInc + qtAtu ) ? "Processado com Sucesso" : "" );

		qtPro  = ( qtdeProcessadaDG == null ? 0 : qtdeProcessadaDG) ;
		qtInc  = ( qtdeIncluidaDG == null ? 0 : qtdeIncluidaDG) ;
		qtAtu  = ( qtdeAtualizadaDG == null ? 0 : qtdeAtualizadaDG) ;
		if (qtPro > 0) labelDG.setText(( qtPro == qtInc + qtAtu ) ? "Processado com Sucesso" : "" );

		qtPro  = ( qtdeProcessadaOS == null ? 0 : qtdeProcessadaOS) ;
		qtInc  = ( qtdeIncluidaOS == null ? 0 : qtdeIncluidaOS) ;
		qtAtu  = ( qtdeAtualizadaOS == null ? 0 : qtdeAtualizadaOS) ;
		if (qtPro > 0) labelOS.setText(( qtPro == qtInc + qtAtu ) ? "Processado com Sucesso" : "" );

		qtPro  = ( qtdeProcessadaCompo == null ? 0 : qtdeProcessadaCompo) ;
		qtInc  = ( qtdeIncluidaCompo == null ? 0 : qtdeIncluidaCompo) ;
//		qtAtu  = ( qtdeAtualizadaOS == null ? 0 : qtdeAtualizadaOS) ;
		if (qtPro > 0) labelCompo.setText("Processado com Sucesso");
//		if (qtPro > 0) labelCompo.setText(( qtPro == qtInc + qtAtu ) ? "Processado com Sucesso" : "" );
}

	@FXML
	public void onBtSairAction(ActionEvent evento) {
		Utilitarios.atualStage(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

}
