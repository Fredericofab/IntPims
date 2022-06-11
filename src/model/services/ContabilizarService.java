package model.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
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

public class ContabilizarService {

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

	Double taxa;
	PlcRat[] plcRatNivel = new PlcRat[11];
	Double[] taxaFX= new Double[11];
	Double[] taxaVR= new Double[11];
	
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
			System.out.println(LocalDateTime.now());
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
			System.out.println(LocalDateTime.now());
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
		for (PlcRat plcRat1 : nivel1) {
			ccVendedorPrimeiro = plcRat1.getCdCCDe();
			plcRatNivel[1] = plcRat1;
			gravarCompras(1, plcRat1);
			List<PlcRat> nivel2 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat1.getCdCCDe() == 0).collect(Collectors.toList());
			for (PlcRat plcRat2 : nivel2) {
				plcRatNivel[2] = plcRat2;
				gravarCompras(2, plcRat2);
				List<PlcRat> nivel3 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat2.getCdCCDe() == 0).collect(Collectors.toList());
				for (PlcRat plcRat3 : nivel3) {
					plcRatNivel[3] = plcRat3;
					gravarCompras(3, plcRat3);
					List<PlcRat> nivel4 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat3.getCdCCDe() == 0).collect(Collectors.toList());
					for (PlcRat plcRat4 : nivel4 ) {
						plcRatNivel[4] = plcRat4;
						gravarCompras(4, plcRat4);
						List<PlcRat> nivel5 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat4.getCdCCDe() == 0).collect(Collectors.toList());
						for (PlcRat plcRat5 : nivel5) {
							plcRatNivel[5] = plcRat5;
							gravarCompras(5, plcRat5);
							List<PlcRat> nivel6 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat5.getCdCCDe() == 0).collect(Collectors.toList());
							for (PlcRat plcRat6 : nivel6) {
								plcRatNivel[6] = plcRat6;
								gravarCompras(6, plcRat6);
								List<PlcRat> nivel7 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat6.getCdCCDe() == 0).collect(Collectors.toList());
								for (PlcRat plcRat7 : nivel7) {
									plcRatNivel[7] = plcRat7;
									gravarCompras(7, plcRat7);
									List<PlcRat> nivel8 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat7.getCdCCDe() == 0).collect(Collectors.toList());
									for (PlcRat plcRat8 : nivel8) {
										plcRatNivel[8] = plcRat8;
										gravarCompras(8, plcRat8);
										List<PlcRat> nivel9 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat8.getCdCCDe() == 0).collect(Collectors.toList());
										for (PlcRat plcRat9 : nivel9) {
											plcRatNivel[9] = plcRat9;
											gravarCompras(9, plcRat9);
											List<PlcRat> nivel10 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat9.getCdCCDe() == 0).collect(Collectors.toList());
											for (PlcRat plcRat10 : nivel10) {
												plcRatNivel[10] = plcRat10;
												gravarCompras(10, plcRat10);
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
		if ( taxa != 0.00 ) {
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
	}

	private void salvarDadosContabil(PlcPrim plcPrim, String nivel, Double ccVendedorPrimeiro, Double ccVendedorUltimo, Double taxa2) {
//		if (ccVendedorPrimeiro == 1007300404  && ccVendedorUltimo == 10073004 && nivel.contentEquals("2") ) {
//			for (int x = 0 ; x <= 2 ; x++) {
//				System.out.println(x);
//				System.out.println(taxaFX[x]);
//				System.out.println(taxaVR[x]);
//				System.out.println(plcRatNivel[x]);
//			}
//			System.out.println(taxa);
//			System.out.println("zzzzzzzzzzzz");
//		}

		
		
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
		taxaFX[0] = 1.00; 
		taxaVR[0] = 1.00;
		taxa = 1.00;
	}

	private Double calcularTaxaFX(int nivel, PlcRat plcRat) {
		taxaFX[0] = 100.00;
		plcRatNivel[0] = new PlcRat(0.00 , "F", 0.00, 100.00, 0.00);

		for (int i = 0 ; i <= nivel ; i++ ) {
			taxaFX[i] = plcRatNivel[i].getVlTaxa() ;
		}
		taxa = 1.00;
		for (int i = 0 ; i <= nivel ; i++) {
			taxa = taxa * (( taxaFX[i] * ( plcRatNivel[i].getFgFxVr().contentEquals("F") ? 1.00 : 0.00) ) / 100.00 ) ; 
		}
		return taxa;
	}
	
	private Double calcularTaxaVR(int nivel, PlcRat plcRat) {
		taxaVR[0] = 100.00;
		plcRatNivel[0] = new PlcRat(0.00 , "V", 0.00, 100.00, 0.00);
		for (int i = 0 ; i <= nivel ; i++ ) {
			taxaVR[i] = plcRatNivel[i].getVlTaxa() ;
		}
		taxa = 1.00;
		for (int i = 0 ; i <= nivel ; i++) {
			taxa = taxa * (( taxaVR[i] *  ( plcRatNivel[i].getFgFxVr().contentEquals("V") ? 1.00 : 0.00) ) / 100.00 ) ; 
		}
		return taxa;
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
