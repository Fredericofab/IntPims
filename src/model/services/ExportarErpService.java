package model.services;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import db.DbException;
import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.scene.control.Alert.AlertType;
import model.entities.Comp_Mat;
import model.entities.Cstg_IntCM;
import model.entities.Cstg_IntDG;
import model.entities.Cstg_IntVM;
import model.entities.Erp;
import model.entities.FatorMedida;
import model.entities.OS_Material;
import model.exceptions.ParametroInvalidoException;

public class ExportarErpService {

	private ErpService erpService = new ErpService();
	private FatorMedidaService fatorMedidaService = new FatorMedidaService();
	private PoliticasErpService politicasErpService = new PoliticasErpService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
	private Cstg_IntVMService cstg_IntVMService = new Cstg_IntVMService();
	private Cstg_IntCMService cstg_IntCMService = new Cstg_IntCMService();
	private Cstg_IntDGService cstg_IntDGService = new Cstg_IntDGService();
	private OS_MaterialService os_MaterialService = new OS_MaterialService();
	private Comp_MatService comp_MatService = new Comp_MatService();

//	parametros
	String anoMes;
	String instancia;
	String usuarioPimsCS;
	String cdEmpresa;
	String cadastrarTipoV;
	String arqLogComponentes;

	char aspas = '"';
	
	List<Erp> listaErp;
	List<Double> listaComponente;
	List<Comp_Mat> listaComp_mat;
//	Set<Double> setComponente;
	String dataInicio;
	String dataFim;
	Integer qtdeDeletadaVM;
	Integer qtdeDeletadaCM;
	Integer qtdeDeletadaDG;
	Integer qtdeDeletadaOS;
	Integer qtdeDeletadaCompMat;
	Integer qtdeProcessadaVM;
	Integer qtdeProcessadaCM;
	Integer qtdeProcessadaDG;
	Integer qtdeProcessadaOS;
	Integer qtdeProcessadaCompo;
	Integer qtdeIncluidaVM;
	Integer qtdeIncluidaCM;
	Integer qtdeIncluidaDG;
	Integer qtdeIncluidaOS;
	Integer qtdeIncluidaCompo;
	Integer qtdeAtualizadaVM;
	Integer qtdeAtualizadaCM;
	Integer qtdeAtualizadaDG;
	Integer qtdeAtualizadaOS;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public Integer getQtdeDeletadaVM() {
		return qtdeDeletadaVM;
	}

	public Integer getQtdeDeletadaCM() {
		return qtdeDeletadaCM;
	}

	public Integer getQtdeDeletadaDG() {
		return qtdeDeletadaDG;
	}

	public Integer getQtdeDeletadaOS() {
		return qtdeDeletadaOS;
	}

	public Integer getQtdeDeletadaCompMat() {
		return qtdeDeletadaCompMat;
	}

	public Integer getQtdeProcessadaVM() {
		return qtdeProcessadaVM;
	}

	public Integer getQtdeProcessadaCM() {
		return qtdeProcessadaCM;
	}

	public Integer getQtdeProcessadaDG() {
		return qtdeProcessadaDG;
	}

	public Integer getQtdeProcessadaOS() {
		return qtdeProcessadaOS;
	}

	public Integer getQtdeProcessadaCompo() {
		return qtdeProcessadaCompo;
	}

	public Integer getQtdeIncluidaVM() {
		return qtdeIncluidaVM;
	}

	public Integer getQtdeIncluidaCM() {
		return qtdeIncluidaCM;
	}

	public Integer getQtdeIncluidaDG() {
		return qtdeIncluidaDG;
	}

	public Integer getQtdeIncluidaOS() {
		return qtdeIncluidaOS;
	}

	public Integer getQtdeIncluidaCompo() {
		return qtdeIncluidaCompo;
	}

	public Integer getQtdeAtualizadaVM() {
		return qtdeAtualizadaVM;
	}

	public Integer getQtdeAtualizadaCM() {
		return qtdeAtualizadaCM;
	}

	public Integer getQtdeAtualizadaDG() {
		return qtdeAtualizadaDG;
	}

	public Integer getQtdeAtualizadaOS() {
		return qtdeAtualizadaOS;
	}
	public void processar(String destino) throws DbException {
		try {
			lerParametros();
			defineDatas();
			listaErp = erpService.pesquisarTodos();

			if (destino == null || destino.equals("VM")) {
				deletarCstgIntVM();
				gravarCstgIntVM();
				processoAtualService.atualizarEtapa("ExportarErpVM", "S");
			}
			if (destino == null || destino.equals("CM")) {
				deletarCstgIntCM();
				gravarCstgIntCM();
				processoAtualService.atualizarEtapa("ExportarErpCM", "S");
			}
			if (destino == null || destino.equals("DG")) {
				deletarCstgIntDG();
				gravarCstgIntDG();
				processoAtualService.atualizarEtapa("ExportarErpDG", "S");
			}
			if (destino == null || destino.equals("OS")) {
				deletarOSMaterial();
				gravarOSMaterial();
				processoAtualService.atualizarEtapa("ExportarErpOS", "S");
			}
			if (destino == null || destino.equals("COMPO")) {
				deletarProdutoComponente();
				gravarProdutoComponente();
				processoAtualService.atualizarEtapa("AtualizarCompo", "S");
			}
			gerarTxt();
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado", e.getMessage(),
					AlertType.ERROR);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deletarCstgIntVM() {
		qtdeDeletadaVM = cstg_IntVMService.deletarPeriodo(dataInicio, dataFim, usuarioPimsCS);
	}

	private void deletarCstgIntCM() {
		qtdeDeletadaCM = cstg_IntCMService.deletarPeriodo(dataInicio, dataFim, usuarioPimsCS);
	}

	private void deletarCstgIntDG() {
		qtdeDeletadaDG = cstg_IntDGService.deletarPeriodo(dataInicio, dataFim, usuarioPimsCS);
	}

	private void deletarOSMaterial() {
		qtdeDeletadaOS = os_MaterialService.deletarPeriodo(dataInicio, dataFim, usuarioPimsCS);
	}

	private void deletarProdutoComponente() {
		Double anoMesReferencia = Utilitarios.tentarConverterParaDouble(anoMes.substring(2, 6) + "1");
		qtdeDeletadaCompMat = comp_MatService.deletarPeriodo(anoMesReferencia, usuarioPimsCS);
	}

	private void gravarCstgIntVM() {
		qtdeProcessadaVM = 0;
		cstg_IntVMService.setQtdeIncluida(0);
		cstg_IntVMService.setQtdeAtualizada(0);
		for (Erp erp : listaErp) {
			if (erp.getSalvarCstg_IntVM() != null && erp.getSalvarCstg_IntVM().equals("S")) {

				FatorMedida fatorMedida = fatorMedidaService.pesquisarPorChave(erp.getCodMaterial());
				Double fator = (fatorMedida == null ? 1 : fatorMedida.getFatorDivisao());

				Cstg_IntVM cstg_intVM = new Cstg_IntVM();
				cstg_intVM.setCdEmpresa(cdEmpresa);
				cstg_intVM.setCdMater(erp.getCodMaterial());
				cstg_intVM.setDtRefer(erp.getDataMovimento());
				cstg_intVM.setInstancia(instancia);
				cstg_intVM.setPrecoUnit(erp.getPrecoUnitario() / fator);
				cstg_IntVMService.salvarOuAtualizar(cstg_intVM, usuarioPimsCS);
				qtdeProcessadaVM += 1;
			}
		}
		qtdeIncluidaVM = cstg_IntVMService.getQtdeIncluida();
		qtdeAtualizadaVM = cstg_IntVMService.getQtdeAtualizada();
	}

	private void gravarCstgIntCM() {
		qtdeProcessadaCM = 0;
		cstg_IntCMService.setQtdeIncluida(0);
		cstg_IntCMService.setQtdeAtualizada(0);
		for (Erp erp : listaErp) {
			if (erp.getSalvarCstg_IntCM() != null && erp.getSalvarCstg_IntCM().equals("S")) {
				Cstg_IntCM cstg_intCM = new Cstg_IntCM();
				cstg_intCM.setCdEmpresa(cdEmpresa);
				cstg_intCM.setCdMater(erp.getCodMaterial());
				cstg_intCM.setDtRefer(erp.getDataMovimento());
				cstg_intCM.setInstancia(instancia);
				cstg_intCM.setPrecoUnit(erp.getPrecoUnitario());
				cstg_intCM.setCdCCusto(erp.getCodCentroCustos());
				cstg_intCM.setFgOrigem("N");
				cstg_intCM.setFgTipo("D");
				cstg_intCM.setQtMater(erp.getQuantidade());
				if (erp.getDescMovimento().toString().length() > 60) {
					cstg_intCM.setDeMater(erp.getDescMovimento().toString().substring(0, 60));
				} else {
					cstg_intCM.setDeMater(erp.getDescMovimento());
				}
				cstg_intCM.setCdEquipto("0000");
				cstg_IntCMService.salvarOuAtualizar(cstg_intCM, usuarioPimsCS);
				qtdeProcessadaCM += 1;
			}
		}
		qtdeIncluidaCM = cstg_IntCMService.getQtdeIncluida();
		qtdeAtualizadaCM = cstg_IntCMService.getQtdeAtualizada();
	}

	private void gravarCstgIntDG() {
		qtdeProcessadaDG = 0;
		cstg_IntDGService.setQtdeIncluida(0);
		cstg_IntDGService.setQtdeAtualizada(0);
		for (Erp erp : listaErp) {
			if (erp.getSalvarCstg_IntDG() != null && erp.getSalvarCstg_IntDG().equals("S")) {
				Cstg_IntDG cstg_intDG = new Cstg_IntDG();
				cstg_intDG.setCdEmpresa(cdEmpresa);
				cstg_intDG.setDtRefer(erp.getDataMovimento());
				cstg_intDG.setInstancia(instancia);
				cstg_intDG.setQtValor(erp.getValorMovimento());
				cstg_intDG.setCdCCusto(erp.getCodCentroCustos());
				cstg_intDG.setCdCtaCon(erp.getCodContaContabil());
				cstg_IntDGService.salvarOuAtualizar(cstg_intDG, usuarioPimsCS);
				qtdeProcessadaDG += 1;
			}
		}
		qtdeIncluidaDG = cstg_IntDGService.getQtdeIncluida();
		qtdeAtualizadaDG = cstg_IntDGService.getQtdeAtualizada();
	}

	private void gravarOSMaterial() {
		qtdeProcessadaOS = 0;
		os_MaterialService.setQtdeIncluida(0);
		os_MaterialService.setQtdeAtualizada(0);
		for (Erp erp : listaErp) {
			if (erp.getSalvarOS_Material() != null && erp.getSalvarOS_Material().equals("S")) {
				OS_Material os_Material = new OS_Material();

				String codMatS = erp.getCodMaterial();
				Double codmatD = Double.parseDouble(codMatS.replace(".", ""));

				os_Material.setCdMaterial(codmatD);
				os_Material.setCdMatCstg(codMatS);

				if (erp.getDescMovimento().toString().length() > 60) {
					os_Material.setDeMaterial(erp.getDescMovimento().toString().substring(0, 60));
				} else {
					os_Material.setDeMaterial(erp.getDescMovimento());
				}
				os_Material.setDtAplicacao(erp.getDataMovimento());
				os_Material.setFgCaptado("S");
				os_Material.setInstancia(instancia);
				os_Material.setNoBoletim(erp.getNumeroOS());
				os_Material.setNoReqExt(erp.getDocumentoErp());
				os_Material.setQtUsada(erp.getQuantidade());
				os_Material.setQtValor(erp.getPrecoUnitario());
				os_MaterialService.salvarOuAtualizar(os_Material, usuarioPimsCS);
				qtdeProcessadaOS += 1;
			}
		}
		qtdeIncluidaOS = os_MaterialService.getQtdeIncluida();
		qtdeAtualizadaOS = os_MaterialService.getQtdeAtualizada();
	}

	private void gravarProdutoComponente() throws IOException, FileNotFoundException{
		qtdeProcessadaCompo = 0;
		comp_MatService.setQtdeIncluida(0);
		criarArqLogComponentes();
		if (cadastrarTipoV.contentEquals("S")) {
			montarListas();
			for (Erp erp : listaErp) {
				if (    (erp.getSalvarCstg_IntCM()  != null && erp.getSalvarCstg_IntCM().equals("S")) 
					 ||	(erp.getSalvarOS_Material() != null && erp.getSalvarOS_Material().equals("S"))
					 || (erp.getSalvarCstg_IntVM() != null && erp.getSalvarCstg_IntVM().equals("S"))   ){
				Double componente = montarComponente(erp);
				if (listaComponente.contains(componente)) {
					qtdeProcessadaCompo += 1;
					Boolean cadastrar = true;
					for (Comp_Mat comp_Mat : listaComp_mat) {
						if (   (erp.getCodMaterial().compareTo(comp_Mat.getCdMatIni()) >= 0)
							&& (erp.getCodMaterial().compareTo(comp_Mat.getCdMatFim()) <= 0)) {
							cadastrar = false;
							break;
						}
					}
					if (cadastrar) {
						Comp_Mat comp_Mat = new Comp_Mat();
						comp_Mat.setCdCompo(componente);
						comp_Mat.setCdMatIni(erp.getCodMaterial());
						comp_Mat.setCdMatFim(erp.getCodMaterial());
						comp_Mat.setInstancia(instancia);
						comp_Mat.setRowVersion(Utilitarios.tentarConverterParaDouble(anoMes.substring(2, 6) + "1"));
						comp_MatService.inserir(comp_Mat, usuarioPimsCS);
						listaComp_mat.add(comp_Mat);
						
						String CodMaterialSemMascara = erp.getCodMaterial().substring(0, 2) +
													   erp.getCodMaterial().substring(3, 5) +
													   erp.getCodMaterial().substring(6, 10);
						comp_Mat.setCdMatIni(CodMaterialSemMascara);
						comp_Mat.setCdMatFim(CodMaterialSemMascara);
						comp_MatService.inserir(comp_Mat, usuarioPimsCS);
					}
				} else {
					gravaLogComponentes(erp);
				}
			}
			}
		}
		qtdeIncluidaCompo = comp_MatService.getQtdeIncluida();
	}

	private void montarListas() {
		String tipo = "V";
		listaComponente = comp_MatService.listarComponentes(tipo, usuarioPimsCS);
		listaComp_mat = comp_MatService.listarTodosDoTipo(tipo, usuarioPimsCS);
	}

	private Double montarComponente(Erp erp) {
		Double componente = null;
		if ((erp.getCodMaterial() != null) && (erp.getCodNatureza() != null)) {
			if (erp.getCodNatureza().length() == 8) {
				String montagem = "3000" + erp.getCodNatureza().substring(3, 5) + erp.getCodNatureza().substring(6, 8);
				componente = Utilitarios.tentarConverterParaDouble(montagem);
			}
		}
		return componente;
	}



	
	private void defineDatas() {
		String mes = anoMes.substring(4, 6);
		String ano = anoMes.substring(0, 4);
		dataInicio = "01/" + mes + "/" + ano;

		String diaFim = ("01 03 05 07 08 10 12".indexOf(mes) == -1 ? "30" : "31");
		if (mes.equals("02")) {
			Double resto = Double.parseDouble(ano) % 4.00;
			diaFim = (resto == 0.00 ? "29" : "28");
		}
		dataFim = diaFim + "/" + mes + "/" + ano;
	}

	private void gerarTxt() {
		Boolean oficial = true;
		erpService.gerarTxt(oficial);
		politicasErpService.gerarTxt(oficial);
		politicasErpService.relatorioTxt(oficial);
		fatorMedidaService.gerarTxt(oficial);
	}
	
	private void criarArqLogComponentes() throws IOException, FileNotFoundException{
		BufferedWriter bwComponentes = new BufferedWriter(new FileWriter(arqLogComponentes, false));
		bwComponentes.write("VALIDAÇÃO: Registros sem Componentes Associados.");
		bwComponentes.newLine();
		bwComponentes.write("AnoMes de Referencia: " + anoMes);
		bwComponentes.newLine();
		bwComponentes.write("TArq,TMov," +
				 "CCustos,DescCCustos,"   +
				 "CContabil,DescCContabil," +
				 "Material,DescMaterial,UN," +
				 "Qtde,PUnit,VTotal," +
				 "NumeroERP,DataMov");
		bwComponentes.newLine();
		bwComponentes.close();
	}
	private void gravaLogComponentes(Erp erp)  throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(arqLogComponentes, true));	
		String linha = erp.getOrigem() + "," + 
					   erp.getTipoMovimento() + "," + 
					   String.format("%.0f", erp.getCodCentroCustos()) + "," + 
					   erp.getDescCentroCustos() + "," + 
					   erp.getCodContaContabil() + "," + 
					   erp.getDescContaContabil().replace(",", " ") +  "," + 
					   aspas + erp.getCodMaterial() + aspas + "," + 
					   erp.getDescMovimento().replace(",", " ") +  "," + 
					   erp.getUnidadeMedida() + "," + 
					   Utilitarios.formatarNumeroDecimal4SemMilhar('.').format(erp.getQuantidade()) + "," + 
					   Utilitarios.formatarNumeroDecimal4SemMilhar('.').format(erp.getPrecoUnitario()) + "," + 
					   Utilitarios.formatarNumeroDecimal4SemMilhar('.').format(erp.getValorMovimento()) + "," + 
					   erp.getDocumentoErp() + "," + 
					   sdf.format(erp.getDataMovimento());
		bw.write(linha);
		bw.newLine();
		bw.close();
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		instancia = (parametrosService.pesquisarPorChave("AmbienteOracle", "InstanciaPimsCS")).getValor();
		usuarioPimsCS = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		cdEmpresa = (parametrosService.pesquisarPorChave("AmbienteOracle", "EmpresaPadrao")).getValor();
		cadastrarTipoV = (parametrosService.pesquisarPorChave("ExportarErp", "CadastrarTipoV")).getValor();
		String arqLogPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqLogPasta")).getValor();
		String arqLogTipo  = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqLogTipo")).getValor();
		arqLogComponentes = arqLogPasta + "LogComponentes" + anoMes + arqLogTipo ;

	}
}
