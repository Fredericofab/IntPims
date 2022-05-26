package model.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import db.DbException;
import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.entities.PlcPrim;
import model.entities.PlcRat;

public class ContabilizarService {

	private PlcPrimService plcPrimService = new PlcPrimService();
	private PlcRatService plcRatService = new PlcRatService();
	
//	parametros
	String usuarioPimsCS;
	String anoMes;

	Integer qtdePlcPrim = 0;
	Integer qtdePlcRat = 0;
	Integer qtdeCCustos = 0;
	List<PlcPrim> listaPlcPrim;
	List<PlcRat> listaPlcRat;
	Set<Double> setCCusto;

	public Integer getQtdePlcPrim() {
		return qtdePlcPrim;
	}
	public Integer getQtdePlcRat() {
		return qtdePlcRat;
	}
	public Integer getQtdeCCustos() {
		return qtdeCCustos;
	}

	public void processar() {
		try {
			lerParametros();

			listaPlcPrim = plcPrimService.pesquisarTodos(anoMes, usuarioPimsCS);
			listaPlcRat = plcRatService.pesquisarTodos(anoMes, usuarioPimsCS);

			setCCusto = new HashSet<>();
			for (PlcPrim plcPrim : listaPlcPrim) { setCCusto.add(plcPrim.getCdCCusto()); }
			for (PlcRat plcRat : listaPlcRat) { setCCusto.add(plcRat.getCdCCPara()); }

			qtdePlcPrim = listaPlcPrim.size();
			qtdePlcRat = listaPlcRat.size();
			qtdeCCustos = setCCusto.size();

			calculaCompras();
		} catch (DbException e) {
			Alertas.mostrarAlertas("Erro Banco Oracle", "Processo Cancelado", e.getMessage(),
					AlertType.ERROR);
		}
	}

	private void calculaCompras() {
//		for (Double ccusto : setCCusto) {
//			if (ccusto != 10030101 ) {
//				continue;
//			}
//			System.out.println("tirar o if acima FRED");
//			Double compradorFinal = ccusto;
//			Double comprador = ccusto;
//			Double taxa = 100.00;
//		
//			procuraPrimario(ccusto, taxa);
//			
//			Double ccPara = compradorFinal;
//			Set<Double> setCCDe;
//
//			Boolean continua = true;
//			while (continua) {
//				for (PlcRat plcRat : listaPlcRat) {
//					if (plcRat.getCdCCPara().doubleValue() == ccPara) {
//						Double ccDe = plcRat.getCcCCDe();
//						Double taxaAcumulada = plcRat.getVlTaxa();
//						procuraPrimario(ccDe, taxaAcumulada);
//					}
//				}
//			}
//		}
	}

//	private void procuraPrimario(Double ccusto, Double taxa) {
//		for (PlcPrim plcPrim : listaPlcPrim) {
//			if (plcPrim.getCdCCusto().doubleValue() == ccusto) {
//				String conta = plcPrim.getCdConta();
//				String tipo = plcPrim.getFgTpCta();
//				Double valor = plcPrim.getVlDire();
////				gravaCompra(compradorFinal,conta,valor);
//			}
//		}
//	}
	
	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS  = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}
	
//	private void primario(PlcPrim plcPrim) { 
//	   System.out.println(String.format("%.0f", plcPrim.getCdCCusto()) + " " + plcPrim.getCdConta() + " " + plcPrim.getVlDire());
//	}
//	private void compra(PlcRat plcRat) { 
//	   System.out.println(String.format("%.0f", plcRat.getCdCCPara()) + " " + String.format("%.0f",plcRat.getCcCCDe()) + " " + plcRat.getFgFxVr() + " " + plcRat.getVlTaxa() + " " + plcRat.getVlConsum());
//	}

}
