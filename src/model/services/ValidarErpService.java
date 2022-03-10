package model.services;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.scene.control.Alert.AlertType;
import model.entities.Erp;
import model.entities.FatorMedida;

public class ValidarErpService {

	private ErpService erpService = new ErpService();
	private PoliticasErpService politicasErpService = new PoliticasErpService();
	private PimsGeralService pimsGeralService = new PimsGeralService();
	private FatorMedidaService fatorMedidaService = new FatorMedidaService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
	
//	parametros
	String usuarioPimsCS;
	String anoMes;
	String qtdeDiasOS;
	String matPossiveisFator;
	String matOficinaSemOS;
	String arqLogCC;
	String arqLogOS;
	
	char aspas = '"';
	
	Integer qtdeProcessados;
	Integer qtdeCCInexistentes;
	Integer qtdeCCInexistentesDistintos;
	Integer qtdeMatSemConversaoDistintos;
	Integer qtdeMatSemConvencao;
	Integer qtdeMatSemOS;
	Integer qtdeFaltaOSouFrotaCC;;
	Integer qtdeOSValidas;
	Integer qtdeOSValidasDistintas;
	Integer qtdeOSInexistentes;
	Integer qtdeOSInexistentesDistintas;
	Integer qtdeOSIncoerentes;
	Integer qtdeOSIncoerentesDistintas;
	Integer qtdeOSAntigas;
	Integer qtdeOSAntigasDistintas;

	List<Erp> lista;
	List<Erp> listaFiltrada;
	Set<FatorMedida> setFatorMedida;
	Map<Double, Integer> mapCCInexistentes = new HashMap<>();
	Map<String, Integer> mapOSInexistentes = new HashMap<>();
	Map<String, Integer> mapOSIncoerentes = new HashMap<>();
	Map<String, Integer> mapOSAntigas = new HashMap<>();
	Map<String, Integer> mapOSValidas = new HashMap<>();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public Integer getQtdeProcessados() {
		return qtdeProcessados;
	}
	public Integer getQtdeCCInexistentes() {
		return qtdeCCInexistentes;
	}
	public Integer getQtdeCCInexistentesDistintos() {
		return qtdeCCInexistentesDistintos;
	}
	public Integer getQtdeMatSemConversao() {
		return qtdeMatSemConvencao;
	}
	public Integer getQtdeMatSemConversaoDistintos() {
		return qtdeMatSemConversaoDistintos;
	}
	public Integer getQtdeMatSemOS() {
		return qtdeMatSemOS;
	}
	public Integer getQtdeFaltaOSouFrotaCC() {
		return qtdeFaltaOSouFrotaCC;
	}
	public Integer getQtdeOSValidas() {
		return qtdeOSValidas;
	}
	public Integer getQtdeOSValidasDistintas() {
		return qtdeOSValidasDistintas;
	}
	public Integer getQtdeOSInexistentes() {
		return qtdeOSInexistentes;
	}
	public Integer getQtdeOSInexistentesDistintas() {
		return qtdeOSInexistentesDistintas;
	}
	public Integer getQtdeOSIncoerentes() {
		return qtdeOSIncoerentes;
	}
	public Integer getQtdeOSIncoerentesDistintas() {
		return qtdeOSIncoerentesDistintas;
	}
	public Integer getQtdeOSAntigas() {
		return qtdeOSAntigas;
	}
	public Integer getQtdeOSAntigasDistintas() {
		return qtdeOSAntigasDistintas;
	}

	public void validarERP() throws IOException, FileNotFoundException {
		lerParametros(false);
		criarArqLog();
		erpService.limparValidacoesOS();
		erpService.limparPoliticas();
		politicasErpService.limparEstatisticas();
		lista = erpService.pesquisarTodos();
		qtdeProcessados = lista.size();
		validarFaltaOSouFrotaCC();
		validarOS();
		validarMatSemOS();
		validarCCustos();
		validarMatSemConversao();
		Integer totaLPendente = qtdeCCInexistentes + 
								qtdeFaltaOSouFrotaCC + 
								qtdeMatSemConvencao +
								qtdeOSInexistentes +  qtdeOSIncoerentes + qtdeOSAntigas;
		if (totaLPendente == 0) {
			processoAtualService.atualizarEtapa("ValidarErp", "S");
		} else {
			processoAtualService.atualizarEtapa("ValidarErp", "N");
			processoAtualService.atualizarEtapa("AplicarPoliticaErp", "N");
			processoAtualService.atualizarEtapa("ExportarErpVM", "N");
			processoAtualService.atualizarEtapa("ExportarErpCM", "N");
			processoAtualService.atualizarEtapa("ExportarErpDG", "N");
			processoAtualService.atualizarEtapa("ExportarErpOS", "N");
		}
	}

//9 Rotinas Auxiliares de Validacao (Inicio)	
	
	private void validarFaltaOSouFrotaCC() throws IOException {
		qtdeFaltaOSouFrotaCC = 0;
		for (Erp erp : lista) {
			if (( erp.getNumeroOS() == null && erp.getFrotaOuCC() != null ) ||
			    ( erp.getNumeroOS() != null && erp.getFrotaOuCC() == null )    ){
				qtdeFaltaOSouFrotaCC += 1;
				erp.setValidacoesOS("Falta OS ou FrotaCC.");
				erpService.salvarOuAtualizar(erp);
				gravaLogOSDetalhe("Falta OS ou FrotaCC", erp);
			}
		}
	}

	private void validarOS() throws IOException {
		qtdeOSValidas = 0;
		qtdeOSInexistentes = 0;
		qtdeOSIncoerentes = 0;
		qtdeOSAntigas = 0;
		for (Erp erp : lista) {
			if (erp.getNumeroOS() != null) {
				if (pimsGeralService.existeApt_os_he(erp.getNumeroOS(), usuarioPimsCS) == false ) {
					osInexistentes(erp);
				}
				else {
					Boolean flagOSIncoerentes = osIncoerentes(erp);
					Boolean flagOSAntigas = osAntigas(erp);
					if ((flagOSIncoerentes == false) && (flagOSAntigas == false) ) {
						osValidas(erp);
					}
				}
			}
		}
		qtdeOSInexistentesDistintas = mapOSInexistentes.size();
		qtdeOSIncoerentesDistintas = mapOSIncoerentes.size();
		qtdeOSAntigasDistintas = mapOSAntigas.size();
		qtdeOSValidasDistintas = mapOSValidas.size();
		
		for (String numeroOS : mapOSInexistentes.keySet() ) {
			for (Erp erp : lista) {
				if ( numeroOS.equals(erp.getNumeroOS()) && erp.getValidacoesOS() != null ) {
					gravaLogOSDetalhe("OS Inexistente.", erp);
				}
			}
		}
		for (String numeroOS : mapOSIncoerentes.keySet() ) {
			for (Erp erp : lista) {
				if ( numeroOS.equals(erp.getNumeroOS()) && erp.getValidacoesOS() != null ) {
					gravaLogOSDetalhe("OS Incoerente.", erp);
				}
			}
		}
		for (String numeroOS : mapOSAntigas.keySet() ) {
			for (Erp erp : lista) {
				if ( numeroOS.equals(erp.getNumeroOS()) && erp.getValidacoesOS() != null ) {
					gravaLogOSDetalhe("OS Antiga.", erp);
				}
			}
		}
	}

	private void osInexistentes(Erp erp) {
		qtdeOSInexistentes += 1;
		erp.setValidacoesOS("OS Inexistente.");
		erpService.salvarOuAtualizar(erp);
		Integer qtde;
		if (mapOSInexistentes.get(erp.getNumeroOS()) == null) {
			qtde = 1;
		}
		else {
			qtde = mapOSInexistentes.get(erp.getNumeroOS()) + 1;
		}
		mapOSInexistentes.put(erp.getNumeroOS(), qtde);
	}

	private Boolean osIncoerentes(Erp erp) {
		Boolean flagOSIncoerentes = false;
		Double codEquipto = pimsGeralService.codEquiptoApt_os_he(erp.getNumeroOS(), usuarioPimsCS);					
		Double codCCusto  = pimsGeralService.codCCustoApt_os_he(erp.getNumeroOS(), usuarioPimsCS);					
		Double codObjeto = ((codEquipto != 0) ? codEquipto : codCCusto);
		Double frotaOuCC = Utilitarios.tentarConverterParaDouble(erp.getFrotaOuCC());
		if (( frotaOuCC == null ) || ((frotaOuCC - codObjeto) != 0.00) ) {
			qtdeOSIncoerentes += 1;
			erp.setValidacoesOS("OS Incoerente.");
			erpService.salvarOuAtualizar(erp);
			Integer qtde;
			if (mapOSIncoerentes.get(erp.getNumeroOS()) == null) {
				qtde = 1;
			}
			else {
				qtde = mapOSIncoerentes.get(erp.getNumeroOS()) + 1;
			}
			mapOSIncoerentes.put(erp.getNumeroOS(), qtde);
			flagOSIncoerentes = true;
		}
		return flagOSIncoerentes;
	}

	private Boolean osAntigas(Erp erp) {
		Boolean flagOSAntigas = false;
		Date dataSaida    = pimsGeralService.dataSaidaApt_os_he(erp.getNumeroOS(), usuarioPimsCS);					
		Long maxDiasOS = Utilitarios.tentarConverterParaLong(qtdeDiasOS);
		if (maxDiasOS != null) {
			if ((dataSaida != null) && (diferencaDias(dataSaida) > maxDiasOS)) {
				qtdeOSAntigas += 1;
				erp.setValidacoesOS("OS Antiga.");
				erpService.salvarOuAtualizar(erp);
				Integer qtde;
				if (mapOSAntigas.get(erp.getNumeroOS()) == null) {
					qtde = 1;
				}
				else {
					qtde = mapOSAntigas.get(erp.getNumeroOS()) + 1;
				}
				mapOSAntigas.put(erp.getNumeroOS(), qtde);
				flagOSAntigas = true;
			}
		}
		else {
			Alertas.mostrarAlertas("Erro de Integridade no Parametro",
					"Parametro: Secao = ValidarErp, Entrada =  QtdeDiasOS",
					"O Parametro n�o � um numero v�lido. \n \n" + 
					"Informe um n�mero Inteiro", AlertType.ERROR);
		}
		return flagOSAntigas;	
	}

	private void osValidas(Erp erp) {
		qtdeOSValidas += 1;
		Integer qtde;
		if (mapOSValidas.get(erp.getNumeroOS()) == null) {
			qtde = 1;
		}
		else {
			qtde = mapOSValidas.get(erp.getNumeroOS()) + 1;
		}
		mapOSValidas.put(erp.getNumeroOS(), qtde);
	}

	private void validarMatSemOS() throws IOException {
		qtdeMatSemOS = 0;
		listaFiltrada = erpService.pesquisarTodosFiltrado(matOficinaSemOS);
		for (Erp erp : listaFiltrada) {
			if ( erp.getNumeroOS() == null && erp.getFrotaOuCC() == null )  {
				qtdeMatSemOS += 1;
				erp.setValidacoesOS("Material sem OS.");
				erpService.salvarOuAtualizar(erp);
				gravaLogOSDetalhe("Material sem OS.", erp);
			}
		}
	}
	
	private void validarCCustos() throws IOException {
		qtdeCCInexistentes = 0;
		Integer qtde;
		for (Erp erp : lista) {
			if (pimsGeralService.existeCCustos(erp.getCodCentroCustos(), usuarioPimsCS) == false ) {
				qtdeCCInexistentes += 1;
				if (mapCCInexistentes.get(erp.getCodCentroCustos()) == null) {
					qtde = 1;
				}
				else {
					qtde = mapCCInexistentes.get(erp.getCodCentroCustos()) + 1;
				}
				mapCCInexistentes.put(erp.getCodCentroCustos(), qtde);
				}
		}
		qtdeCCInexistentesDistintos = mapCCInexistentes.size();
		for (Double codCentroCustos :  mapCCInexistentes.keySet()) {
			qtde = mapCCInexistentes.get(codCentroCustos);
			for (Erp erp : lista) {
				if ( codCentroCustos == erp.getCodCentroCustos()) {
					gravaLogCCDetalhe("CC Inexistentes", qtde, erp);
					break;
				}
			}
		}
	}

	private void validarMatSemConversao() {
		listaFiltrada = erpService.pesquisarTodosFiltrado(matPossiveisFator);
		setFatorMedida  = new HashSet<>();
		for (Erp erp : listaFiltrada) {
			FatorMedida fatorMedida = new FatorMedida(erp.getCodMaterial(), 
								erp.getDescMovimento(),	erp.getUnidadeMedida(), 0.00);
			setFatorMedida.add(fatorMedida);
		}
		if (setFatorMedida.size() > 0) {
			gravarFatorMedida();
		}
		qtdeMatSemConvencao = 0;
		qtdeMatSemConversaoDistintos = 0;
		List<FatorMedida> listaFatorMedida = fatorMedidaService.pesquisarTodos();
		for (FatorMedida fatorMedida : listaFatorMedida) {
			if (fatorMedida.getFatorDivisao() == 0.00) {
				qtdeMatSemConversaoDistintos += 1;
				for (Erp erp : listaFiltrada) {
					if (fatorMedida.getCodMaterial().equals(erp.getCodMaterial()) ) {
						qtdeMatSemConvencao += 1;
					}
				}
			}
		}
	}

//9 Rotinas Auxiliares de Validacao (Termino)	
	
//3 Rotinas do Arquivo de Log (Inicio)
	
	private void criarArqLog() throws IOException, FileNotFoundException {
		BufferedWriter bwCC = new BufferedWriter(new FileWriter(arqLogCC, false));
		bwCC.write("VALIDA��O: Centros de Custos Inexistentes no PimsCS.");
		bwCC.newLine();
		bwCC.write("AnoMes de Referencia: " + anoMes);
		bwCC.newLine();
		bwCC.write("CCustos,DescCCustos,qtde" );
		bwCC.newLine();
		bwCC.close();

		BufferedWriter bwOS = new BufferedWriter(new FileWriter(arqLogOS, false));
		bwOS.write("VALIDA��O: Das Ordens de Servi�os.");
		bwOS.newLine();
		bwOS.write("AnoMes de Referencia: " + anoMes);
		bwOS.newLine();
		bwOS.write("Validacoes,TArq,TMov," +
				 "CCustos,DescCCustos,"   +
				 "CContabil,DescCContabil," +
				 "Material,DescMaterial,UN," +
				 "Qtde,PUnit,VTotal," +
				 "NumeroOS,FrotaOuCC,NumeroERP,DataMov");
		bwOS.newLine();
		bwOS.close();
	}

	private void gravaLogOSDetalhe(String secao, Erp erp) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(arqLogOS, true));	
		String linha = erp.getValidacoesOS() + "," + 
					   erp.getOrigem() + "," + 
					   erp.getTipoMovimento() + "," + 
					   String.format("%.0f", erp.getCodCentroCustos()) + "," + 
					   erp.getDescCentroCustos() + "," + 
					   erp.getCodContaContabil() + "," + 
					   erp.getDescContaContabil() + "," + 
					   aspas + erp.getCodMaterial() + aspas + "," + 
					   erp.getDescMovimento().replace(",",".") + "," + 
					   erp.getUnidadeMedida() + "," + 
					   Utilitarios.formatarNumeroDecimalSemMilhar('.').format(erp.getQuantidade()) + "," + 
					   Utilitarios.formatarNumeroDecimalSemMilhar('.').format(erp.getPrecoUnitario()) + "," + 
					   Utilitarios.formatarNumeroDecimalSemMilhar('.').format(erp.getValorMovimento()) + "," + 
					   erp.getNumeroOS() + "," + 
					   erp.getFrotaOuCC() + "," + 
					   erp.getDocumentoErp() + "," + 
					   sdf.format(erp.getDataMovimento());
		bw.write(linha);
		bw.newLine();
		bw.close();
	}

	private void gravaLogCCDetalhe(String secao, Integer qtde, Erp erp) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(arqLogCC, true));	
		String linha = String.format("%.0f", erp.getCodCentroCustos()) + "," + 
					   erp.getDescCentroCustos() + "," + 
					   qtde;
		bw.write(linha);
		bw.newLine();
		bw.close();
	}

//	3 Rotinas do Arquivo de Log (Termino)
	
//	3 Rotinas de Apoio (Inicio)

	private void gravarFatorMedida() {
		for (FatorMedida fatorMedida : setFatorMedida) {
			if (fatorMedidaService.pesquisarPorChave(fatorMedida.getCodMaterial()) == null) {
				fatorMedidaService.salvarOuAtualizar(fatorMedida);
			}
		}
	}

	private Long diferencaDias(Date dataSaida) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date dataProcessamento = sdf.parse(anoMes + "01");
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(dataSaida);
			cal2.setTime(dataProcessamento);
			Long x1 = cal1.getTimeInMillis();
			Long x2 = cal2.getTimeInMillis();
			Long dias = (x2 - x1) / 86400000;
			return dias;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void lerParametros(Boolean oficial) {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS  = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		qtdeDiasOS = (parametrosService.pesquisarPorChave("ValidarErp", "QtdeDiasOS")).getValor();	
		matPossiveisFator = (parametrosService.pesquisarPorChave("ValidarErp", "MatPossiveisFator")).getValor();	
		matOficinaSemOS = (parametrosService.pesquisarPorChave("ValidarErp", "MatOficinaSemOS")).getValor();

		String arqLogPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqLogPasta")).getValor();
		String arqLogTipo  = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqLogTipo")).getValor();
		arqLogCC = arqLogPasta + "LogCC" + anoMes + arqLogTipo ;
		arqLogOS = arqLogPasta + "LogOS" + anoMes + arqLogTipo ;
	}

//	3 Rotinas de Apoio (termino)	
}
