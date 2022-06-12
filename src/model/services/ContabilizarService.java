package model.services;

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
	private DadosContabilService dadosContabilService = new DadosContabilService();
	
//	parametros
	String usuarioPimsCS;

	List<PlcPrim> listaPlcPrim;
	List<PlcRat> listaPlcRat;
	Set<Double> setCCusto;
	Integer qtdePlcPrim = 0;
	Integer qtdePlcRat = 0;
	Integer qtdeCCustos = 0;
	
	Double ccustoPai;
	Double taxa;
	PlcRat[] plcRatNivel = new PlcRat[11];
	Double[] ccDe = new Double[11];
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
			lerParametros();
			dadosContabilService.deletarTodos();
			carregaDados(anoMes);

			if (ccustoInformado == null) {
				for (Double ccusto : setCCusto) { 
					ccustoPai = ccusto;
					inicializarTaxas();
					gravarPrimario(ccustoPai);
					montarArvore(ccustoPai);
				}
			}
			else {
				ccustoPai = ccustoInformado;
				inicializarTaxas();
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
		for (PlcPrim plcPrim : listaPlcPrim) {
			if ( plcPrim.getCdCCusto() - ccusto == 0) {
				salvarDadosContabil(plcPrim);
			}
		}
	}

	private void montarArvore(Double ccustoPai) {
		List<PlcRat> nivel1 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - ccustoPai == 0).collect(Collectors.toList());
		for (PlcRat plcRat1 : nivel1) {
			gravarCompras(1, plcRat1);
			List<PlcRat> nivel2 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat1.getCdCCDe() == 0).collect(Collectors.toList());
			for (PlcRat plcRat2 : nivel2) {
				gravarCompras(2, plcRat2);
				List<PlcRat> nivel3 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat2.getCdCCDe() == 0).collect(Collectors.toList());
				for (PlcRat plcRat3 : nivel3) {
					gravarCompras(3, plcRat3);
					List<PlcRat> nivel4 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat3.getCdCCDe() == 0).collect(Collectors.toList());
					for (PlcRat plcRat4 : nivel4 ) {
						gravarCompras(4, plcRat4);
						List<PlcRat> nivel5 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat4.getCdCCDe() == 0).collect(Collectors.toList());
						for (PlcRat plcRat5 : nivel5) {
							gravarCompras(5, plcRat5);
							List<PlcRat> nivel6 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat5.getCdCCDe() == 0).collect(Collectors.toList());
							for (PlcRat plcRat6 : nivel6) {
								gravarCompras(6, plcRat6);
								List<PlcRat> nivel7 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat6.getCdCCDe() == 0).collect(Collectors.toList());
								for (PlcRat plcRat7 : nivel7) {
									gravarCompras(7, plcRat7);
									List<PlcRat> nivel8 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat7.getCdCCDe() == 0).collect(Collectors.toList());
									for (PlcRat plcRat8 : nivel8) {
										gravarCompras(8, plcRat8);
										List<PlcRat> nivel9 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat8.getCdCCDe() == 0).collect(Collectors.toList());
										for (PlcRat plcRat9 : nivel9) {
											gravarCompras(9, plcRat9);
											List<PlcRat> nivel10 = listaPlcRat.stream().filter(x -> x.getCdCCPara() - plcRat9.getCdCCDe() == 0).collect(Collectors.toList());
											for (PlcRat plcRat10 : nivel10) {
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
		plcRatNivel[nivel] = plcRat;
		ccDe[nivel] = plcRat.getCdCCDe();
		for (int i = nivel +1 ; i<= 10 ; i++) { ccDe[i] = 0.00; }
		
		String taxaFouV = plcRat.getFgFxVr();
		taxa = ( taxaFouV.contentEquals("F") ? calcularTaxaFX(nivel, plcRat) : calcularTaxaVR(nivel, plcRat));
		if ( taxa != 0.00 ) {
			for (PlcPrim plcPrim : listaPlcPrim) {
				if ( plcPrim.getCdCCusto() - plcRat.getCdCCDe() == 0) {
					String tipoConta = (plcPrim.getFgTpCta()).substring(1, 2);
					if (tipoConta.contentEquals(taxaFouV)) {
						salvarDadosContabil(plcPrim);
					}
				}
			}
		}
	}

	private void salvarDadosContabil(PlcPrim plcPrim) {
		DadosContabil dadosContabil = new DadosContabil();
		dadosContabil.setCcComprador(ccustoPai);
		dadosContabil.setCdConta(plcPrim.getCdConta());
		dadosContabil.setFgTpCta(plcPrim.getFgTpCta());
		dadosContabil.setCcDe(ccDe);
		dadosContabil.setTaxa((plcPrim.getFgTpCta().contentEquals("F")) ? taxaFX : taxaVR);
		Double taxaTruncada = Math.round(taxa * 10000.0) / 10000.0;
		Double valorTruncado = Math.round( plcPrim.getVlDire() * taxa * 10000.0) / 10000.0;
		dadosContabil.setTaxaAcum(taxaTruncada);
		dadosContabil.setValorBase(plcPrim.getVlDire());
		dadosContabil.setValorAdquirido(valorTruncado);
		dadosContabilService.salvar(dadosContabil);
	}	
	
	private void inicializarTaxas() {
		for (int i = 0 ; i <= 10 ; i++) {
			ccDe[i] = 0.00;
			taxaFX[i] = 0.00;
			taxaVR[i] = 0.00;
		}
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

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS  = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
	}
}
