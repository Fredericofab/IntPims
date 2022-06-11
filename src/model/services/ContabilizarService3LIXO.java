package model.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import db.DbException;
import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.entities.DadosContabil;
import model.entities.PlcPrim;
import model.entities.PlcRat;
import model.exceptions.ParametroInvalidoException;

public class ContabilizarService3LIXO {

	private PlcPrimService plcPrimService = new PlcPrimService();
	private PlcRatService plcRatService = new PlcRatService();
	private PimsGeralService pimsGeralService = new PimsGeralService();
	private DadosContabilService dadosContabilService = new DadosContabilService();
	
//	parametros
	String usuarioPimsCS;
	String pasta = "C:\\Projeto Itapecuru Custag\\saidaContabil\\";

	List<PlcPrim> listaPlcPrim;
	List<PlcRat> listaPlcRat;
	Set<Double> setCCusto;
	Integer qtdePlcPrim = 0;
	Integer qtdePlcRat = 0;
	Integer qtdeCCustos = 0;
	
	Integer registro = 0;
	String arquivo;
	File arqTxt;
	
	Double ccustoPai;
	Double ccVendedorUltimo;
	Double ccVendedorPrimeiro;
	
	PlcRat[] plcRatNivel = new PlcRat[10];

	Double taxa;
	Double[] taxaAcumFX= new Double[10];
	Double[] taxaAcumVR= new Double[10];
	
	public Integer getQtdePlcPrim() {
		return qtdePlcPrim;
	}
	public Integer getQtdePlcRat() {
		return qtdePlcRat;
	}
	public Integer getQtdeCCustos() {
		return qtdeCCustos;
	}

	public void processar(String anoMes, Double ccustoInformado) {
		try {
			lerParametros();
			dadosContabilService.deletarTodos();
			carregaDados(anoMes);

			if (ccustoInformado == null) {
				for (Double ccusto : setCCusto) { 
					ccustoPai = ccusto;
					inicializarTaxas();
					deletarArquivoTxt();
					gravarPrimario(ccustoPai);
					montarArvore(ccustoPai);
				}
			}
			else {
				ccustoPai = ccustoInformado;
				inicializarTaxas();
				deletarArquivoTxt();
				gravarPrimario(ccustoPai);
				montarArvore(ccustoPai);
			}
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. Contabilizar", e.getMessage(),AlertType.ERROR);
		} catch (DbException e) {
			Alertas.mostrarAlertas("Erro Banco Oracle", "Processo Cancelado", e.getMessage(),
					AlertType.ERROR);
		}
	}

	private void carregaDados(String anoMes) {
		listaPlcPrim = plcPrimService.pesquisarTodos(anoMes, usuarioPimsCS);
		listaPlcRat = plcRatService.pesquisarTodos(anoMes, usuarioPimsCS);
		setCCusto = new HashSet<>();
		for (PlcPrim plcPrim : listaPlcPrim) { setCCusto.add(plcPrim.getCdCCusto()); }
		for (PlcRat plcRat : listaPlcRat) { setCCusto.add(plcRat.getCdCCPara()); }

		qtdePlcPrim = listaPlcPrim.size();
		qtdePlcRat = listaPlcRat.size();
		qtdeCCustos = setCCusto.size();
	}

	private void gravarPrimario(Double ccusto) {
		String nivel = "0";
		for (PlcPrim plcPrim : listaPlcPrim) {
			if ( plcPrim.getCdCCusto() - ccusto == 0) {
				salvarDadosContabil(plcPrim, nivel, ccVendedorPrimeiro, ccVendedorUltimo, taxa);
			}
		}
	}

	private void montarArvore(Double ccustoPai) {
		registro = 0;
		List<PlcRat> nivel1 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - ccustoPai == 0).collect(Collectors.toList());
		for (PlcRat plcRat1 : nivel1 ) {	
System.out.println("1 " + plcRat1);
			ccVendedorPrimeiro = plcRat1.getCdCCDe();
			plcRatNivel[1] = plcRat1;
			gravarCompras(1, plcRat1);
			List<PlcRat> nivel2 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat1.getCdCCDe() == 0).collect(Collectors.toList());
			for (PlcRat plcRat2 : nivel2 ) {
				System.out.println("2 " + plcRat2);
				plcRatNivel[2] = plcRat2;
				gravarCompras(2, plcRat2);
				List<PlcRat> nivel3 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat2.getCdCCDe() == 0).collect(Collectors.toList());
				for (PlcRat plcRat3 : nivel3 ) {
					System.out.println("3 " + plcRat3);
					plcRatNivel[3] = plcRat3;
					gravarCompras(3, plcRat3);
					List<PlcRat> nivel4 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat3.getCdCCDe() == 0).collect(Collectors.toList());
					for (PlcRat plcRat4 : nivel4 ) {
						plcRatNivel[4] = plcRat4;
//						gravarCompras(4, plcRat4, nivel4.size());
						List<PlcRat> nivel5 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat4.getCdCCDe() == 0).collect(Collectors.toList());
						for (PlcRat plcRat5 : nivel5) {
							plcRatNivel[5] = plcRat5;
//							gravarCompras(5, plcRat5, nivel5.size());
							List<PlcRat> nivel6 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat5.getCdCCDe() == 0).collect(Collectors.toList());
							for (PlcRat plcRat6 : nivel6) {
								plcRatNivel[6] = plcRat6;
//								gravarCompras(6, plcRat6, nivel6.size());
								List<PlcRat> nivel7 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat6.getCdCCDe() == 0).collect(Collectors.toList());
								for (PlcRat plcRat7 : nivel7) {
									plcRatNivel[7] = plcRat7;
//									gravarCompras(7, plcRat7, nivel7.size());
									List<PlcRat> nivel8 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat7.getCdCCDe() == 0).collect(Collectors.toList());
									for (PlcRat plcRat8 : nivel8) {
										plcRatNivel[8] = plcRat8;
//										gravarCompras(8, plcRat8, nivel8.size());
										List<PlcRat> nivel9 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat8.getCdCCDe() == 0).collect(Collectors.toList());
										for (PlcRat plcRat9 : nivel9) {
											plcRatNivel[9] = plcRat9;
//											gravarCompras(9, plcRat9, nivel9.size());
											List<PlcRat> nivel10 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat9.getCdCCDe() == 0).collect(Collectors.toList());
											for (PlcRat plcRat10 : nivel10) {
												plcRatNivel[10] = plcRat10;
//												gravarCompras(10, plcRat10, nivel10.size());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void gravarCompras(int nivel, PlcRat plcRat) {
		String taxaFouV = plcRat.getFgFxVr();
		taxa = ( taxaFouV.contentEquals("F") ? calcularTaxaFX(nivel, plcRat) : calcularTaxaVR(nivel, plcRat));
		for (PlcPrim plcPrim : listaPlcPrim) {
			if ( plcPrim.getCdCCusto() - plcRat.getCdCCDe() == 0) {
				String tipoConta = (plcPrim.getFgTpCta()).substring(1, 2);
				if (tipoConta.contentEquals(taxaFouV)) {
					Double ccVendedorUltimo  = plcRat.getCdCCDe();
					salvarDadosContabil(plcPrim, String.valueOf(nivel), ccVendedorPrimeiro, ccVendedorUltimo, taxa);
				}
			}
		}
		gravarArquivoTxt(nivel, plcRat);
	}

	private void salvarDadosContabil(PlcPrim plcPrim, String nivel, Double ccVendedorPrimeiro, Double ccVendedorUltimo, Double taxa2) {
		DadosContabil dadosContabil = new DadosContabil();
		dadosContabil.setCcComprador(ccustoPai);
		dadosContabil.setCdConta(plcPrim.getCdConta());
		dadosContabil.setFgTpCta(plcPrim.getFgTpCta());
		dadosContabil.setNivel(nivel);
		dadosContabil.setCcVendedorPrimeiro(ccVendedorPrimeiro);
		dadosContabil.setCcVendedorUltimo(ccVendedorUltimo);
		Double taxaTruncada = Math.round(taxa * 10000.0) / 10000.0;
		Double valorTruncado = Math.round( plcPrim.getVlDire() * taxa * 10000.0) / 10000.0;
		dadosContabil.setTaxa(taxaTruncada);
		dadosContabil.setValor(valorTruncado);
		dadosContabilService.salvar(dadosContabil);
	}	
	
	private void inicializarTaxas() {
		ccVendedorPrimeiro  = 0.00;
		ccVendedorUltimo  = 0.00;
		for (int i = 0 ; i < 10 ; i++ ) {
			taxaAcumFX[i] = null; 
			taxaAcumVR[i] = null;
		}
		taxaAcumFX[0] = 1.00; 
		taxaAcumVR[0] = 1.00;
		taxa = 1.00;
	}

	private Double calcularTaxaFX(int nivel, PlcRat plcRat) {
//		try {
			switch (nivel) {
			case 1:
				taxaAcumFX[1] = taxaAcumFX[0] * plcRat.getVlTaxa() / 100.0;
				taxa = taxaAcumFX[1];
				break;
			case 2:
				
				taxaAcumFX[2] = taxaAcumFX[1] * plcRat.getVlTaxa() / 100.0;
				taxa = taxaAcumFX[2];
				break;
			case 3:
				taxaAcumFX[3] = taxaAcumFX[2] * plcRat.getVlTaxa() / 100.0;
				taxa = taxaAcumFX[3];
				break;
			default:
				break;
			}
			return taxa;
//		}
//		catch (NullPointerException e) {
//			System.out.println(nivel);
//			System.out.println(plcRat);
//			System.out.println("FX");
//			return taxa;
//		}
	}
	private Double calcularTaxaVR(int nivel, PlcRat plcRat) {
//		try {
			switch (nivel) {
			case 1:
				taxaAcumVR[1] = taxaAcumVR[0] * plcRat.getVlTaxa() / 100.0;
				taxa = taxaAcumVR[1];
				break;
			case 2:
				taxaAcumVR[2] = taxaAcumVR[1] * plcRat.getVlTaxa() / 100.0;
				taxa = taxaAcumVR[2];
				break;
			case 3:
				taxaAcumVR[3] = taxaAcumVR[2] * plcRat.getVlTaxa() / 100.0;
				taxa = taxaAcumVR[3];
				break;
			default:
				break;
			}
			return taxa;
//		}
//		catch (NullPointerException e) {
//			System.out.println(nivel);
//			System.out.println(plcRat);
//			System.out.println("VR");
//			return taxa;
//		}
	}
	

	private void deletarArquivoTxt() {
		String nomePai = pimsGeralService.descricaoCCustos(ccustoPai, "PimsItaj");
		arquivo = String.format("%.0f",ccustoPai) + " " + nomePai + ".csv";
		arquivo = arquivo.replace("/", " ");
		arquivo = pasta + arquivo;
		arqTxt = new File(arquivo);
		arqTxt.delete();
	}
	private void gravarArquivoTxt(int nivel, PlcRat plcRat) {
		registro += 1;
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, true))) {
			String deNome = pimsGeralService.descricaoCCustos(plcRat.getCdCCDe(), "PimsItaj");
			String linha = registro + "," + nivel + "," + "," + plcRat + ",De=" + deNome;
			bw.write(linha);
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS  = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
	}
}
