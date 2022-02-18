package model.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.util.Utilitarios;
import model.entities.Erp;

public class ValidarErpService {

	private ErpService erpService = new ErpService();
	private PimsGeralService pimsGeralService = new PimsGeralService();
//	private VerbasFolhaService verbasDaFolhaService = new VerbasFolhaService();
//	private ProcessoAtualService processoAtualService = new ProcessoAtualService();
	
//	parametros
	String usuarioPimsCS;
	String anoMes;
	String qtdeDiasOS;
	
	Integer qtdeProcessados;
	Integer qtdeCCInexistentes;
	Integer qtdeCCInexistentesDistintos;

	Integer qtdeMatSemConversao = 100;
	Integer qtdeMatSemOS = 100;
	Integer qtdeFaltaOSouFrotaCC = 100;

	Integer qtdeOSValidas;
	Integer qtdeOSInexistentes;
	Integer qtdeOSInexistentesDistintas;
	Integer qtdeOSIncoerentes;
	Integer qtdeOSIncoerentesDistintas;
	Integer qtdeOSAntigas;
	Integer qtdeOSAntigasDistintas;

	Map<Double, Integer> mapCCInexistentes = new HashMap<>();
	Map<String, Integer> mapOSInexistentes = new HashMap<>();
	Map<String, Integer> mapOSIncoerentes = new HashMap<>();
	Map<String, Integer> mapOSAntigas = new HashMap<>();

	
	List<Erp> lista;
//	Set<VerbasFolha> setVerbas;


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
		return qtdeMatSemConversao;
	}
	public Integer getQtdeOSValidas() {
		return qtdeOSValidas;
	}

	public Integer getQtdeOSInexistentes() {
		return qtdeOSInexistentes;
	}
	public Integer getQtdeOSInexistentesDistintas() {
		return qtdeOSInexistentesDistintas;
	}
//	public Map<String, Integer> getMapOSInexistentes() {
//		return mapOSInexistentes;
//	}

	public Integer getQtdeOSIncoerentes() {
		return qtdeOSIncoerentes;
	}
	public Integer getQtdeOSIncoerentesDistintas() {
		return qtdeOSIncoerentesDistintas;
	}
//	public Map<String, Integer> getMapOSIncoerentes() {
//		return mapOSIncoerentes;
//	}
	
	public Integer getQtdeOSAntigas() {
		return qtdeOSAntigas;
	}
	public Integer getQtdeOSAntigasDistintas() {
		return qtdeOSAntigasDistintas;
	}
//	public Map<String, Integer> getMapOSAntigas() {
//		return mapOSAntigas;
//	}

	public Integer getQtdeMatSemOS() {
		return qtdeMatSemOS;
	}
	public Integer getQtdeFaltaOSouFrotaCC() {
		return qtdeFaltaOSouFrotaCC;
	}

//	public List<Folha> getLista() {
//		return lista;
//	}
//	public Set<VerbasFolha> getSetVerbas() {
//		return setVerbas;
//	}
//

	public void validarERP() {
		lerParametros();
		lista = erpService.pesquisarTodos();
		qtdeProcessados = lista.size();
		validarCCustos();
		validarMateriais();
		validarFaltaOSouFrotaCC();
		validarOS();
	}
	
	private void validarMateriais() {
		// TODO Auto-generated method stub
	}

	private void validarFaltaOSouFrotaCC() {
		qtdeFaltaOSouFrotaCC = 0;
		for (Erp erp : lista) {
			if (( erp.getNumeroOS() == null && erp.getFrotaOuCC() != null ) ||
			    ( erp.getNumeroOS() != null && erp.getFrotaOuCC() == null )    ){
				qtdeFaltaOSouFrotaCC += 1;
			}
		}
	}

	private void validarCCustos() {
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
	}


	
	private void validarOS() {
		qtdeOSValidas = 0;
		qtdeOSInexistentes = 0;
		qtdeOSIncoerentes = 0;
		qtdeOSAntigas = 0;
		for (Erp erp : lista) {
			if (erp.getNumeroOS() != null) {
				if (pimsGeralService.existeApt_os_he(erp.getNumeroOS(), usuarioPimsCS) == false ) {
					osInexistentes(erp.getNumeroOS());
				}
				else {
					Boolean flagOSIncoerentes = false;
					Boolean flagOSAntigas = false;
					Double codEquipto = pimsGeralService.codEquiptoApt_os_he(erp.getNumeroOS(), usuarioPimsCS);					
					Double codCCusto  = pimsGeralService.codCCustoApt_os_he(erp.getNumeroOS(), usuarioPimsCS);					
					Date dataSaida    = pimsGeralService.dataSaidaApt_os_he(erp.getNumeroOS(), usuarioPimsCS);					

					Double codObjeto = ((codEquipto != 0) ? codEquipto : codCCusto);
					Double frotaOuCC = Utilitarios.tentarConverterParaDouble(erp.getFrotaOuCC());
					if (( frotaOuCC == null ) || ((frotaOuCC - codObjeto) != 0.00) ) {
						osIncoerentes(erp.getNumeroOS());
						flagOSIncoerentes = true;
					}

					if ((dataSaida != null) && (diferencaDias(dataSaida) > Long.parseLong(qtdeDiasOS))) {
						osAntigas(erp.getNumeroOS());
						flagOSAntigas = true;
					}
					if ((flagOSIncoerentes == false) && (flagOSAntigas == false) ) {
						qtdeOSValidas += 1;
					}
				}
			}
		}
		qtdeOSInexistentesDistintas = mapOSInexistentes.size();
		qtdeOSIncoerentesDistintas = mapOSIncoerentes.size();
		qtdeOSAntigasDistintas = mapOSAntigas.size();
	}



	private void osInexistentes(String numeroOS) {
		qtdeOSInexistentes += 1;
		Integer qtde;
		if (mapOSInexistentes.get(numeroOS) == null) {
			qtde = 1;
		}
		else {
			qtde = mapOSInexistentes.get(numeroOS) + 1;
		}
		mapOSInexistentes.put(numeroOS, qtde);
	}

	private void osIncoerentes(String numeroOS) {
		qtdeOSIncoerentes += 1;
		Integer qtde;
		if (mapOSIncoerentes.get(numeroOS) == null) {
			qtde = 1;
		}
		else {
			qtde = mapOSIncoerentes.get(numeroOS) + 1;
		}
		mapOSIncoerentes.put(numeroOS, qtde);
	}

	private void osAntigas(String numeroOS) {
		qtdeOSAntigas += 1;
		Integer qtde;
		if (mapOSAntigas.get(numeroOS) == null) {
			qtde = 1;
		}
		else {
			qtde = mapOSAntigas.get(numeroOS) + 1;
		}
		mapOSAntigas.put(numeroOS, qtde);
	}
	
	
//	public void processarTXT() {
//		try {
//			lerParametros();
//			deletarTodosFolha();
//			deletarTodosFolhaSumarizada();
//			lerFolhaTXT(entrada, anoMes);
//			if (setVerbas.size() > 0) {
//				qtdeVerbasDistintas = setVerbas.size();
//				gravarVerbasNovas();
//			}
//			contarVerbasSemDefinicao();
//			if (qtdeVerbasSemDefinicao == 0 && qtdeCorrompidas == 0) {
//				gravarDadosFolha();
//			}
//		} catch (DbException e) {
//			Alertas.mostrarAlertas("Erro Banco Oracle", "Processo Cancelado", e.getMessage(),
//					AlertType.ERROR);
//		}
//		
//		if ((qtdeIncluidas > 0) && (qtdeLidas - qtdeIncluidas - qtdeNaoImportadas) == 0) {
//			processoAtualService.atualizarEtapa("ImportarFolha", "S");
//		} else {
//			processoAtualService.atualizarEtapa("ImportarFolha", "N");
//		}
//		processoAtualService.atualizarEtapa("SumarizarFolha", "N");
//		processoAtualService.atualizarEtapa("ExportarFolha", "N");
//		processoAtualService.atualizarEtapa("VerbaAlterada", "N");
//		processoAtualService.atualizarEtapa("FolhaAlterada", "N");
//	}
//	
//	private void deletarTodosFolhaSumarizada() {
//		folhaSumarizadaService.deletarTodos();
//	}
//	private void deletarTodosFolha() {
//		qtdeDeletadas = folhaService.deletarTodos();
//	}
//
//	private void lerFolhaTXT(String entrada, String anoMesReferencia) {
//		String linha = null;
//		lista = new ArrayList<Folha>();
//		setVerbas = new HashSet<>();
//
//		try (BufferedReader br = new BufferedReader(new FileReader(entrada))) {
//			linha = br.readLine();
//			while (linha != null) {
//				qtdeLidas = qtdeLidas + 1;
//				Folha dadosFolha = null;
//				dadosFolha = converteRegistro(linha, anoMesReferencia, qtdeLidas);
//				if (dadosFolha != null) {
//					if (naoImportar.indexOf(dadosFolha.getTipoVerba()) >= 0) {
//						qtdeNaoImportadas = qtdeNaoImportadas + 1;
//					}
//					else {
//						lista.add(dadosFolha);
//						VerbasFolha verbaFolha = new VerbasFolha(dadosFolha.getCodVerba(), dadosFolha.getDescVerba(),
//								dadosFolha.getTipoVerba(), null, null);
//						setVerbas.add(verbaFolha);
//					}
//				}
//				linha = br.readLine();
//			}
//		} catch (TxtIntegridadeException e) {
//			Alertas.mostrarAlertas("Erro de Integridade no Arquivo", "Processo de Leitura do TXT interrompido",
//					e.getMessage(), AlertType.ERROR);
//		} catch (FileNotFoundException e) {
//			Alertas.mostrarAlertas("Arquivo não encontrado", "Processo Cancelado",
//					e.getMessage(), AlertType.ERROR);
//		} catch (IOException e) {
//			Alertas.mostrarAlertas("IOException", "Erro na Importacao Dados da Folha",
//					e.getMessage(), AlertType.ERROR);
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void gravarVerbasNovas() {
//		for (VerbasFolha verbaFolha : setVerbas) {
//			if (verbasDaFolhaService.pesquisarPorChave(verbaFolha.getCodVerba()) == null) {
//				verbasDaFolhaService.salvarOuAtualizar(verbaFolha);
//			}
//		}
//	}
//	private void contarVerbasSemDefinicao() {
//		qtdeVerbasSemDefinicao = verbasDaFolhaService.contarVerbasSemDefinicao();
//	}
//
//	private void gravarDadosFolha() {
//		VerbasFolha verbaFolha;
//		qtdeIncluidas = 0;
//		Double codVerba;
//		for (Folha dadosFolha : lista) {
//			codVerba = dadosFolha.getCodVerba();
//			verbaFolha = verbasDaFolhaService.pesquisarPorChave(codVerba);
//			if (verbaFolha != null) {
//				dadosFolha.setImportar(verbaFolha.getImportar());
//				dadosFolha.setConsiderarReferencia(verbaFolha.getConsiderarReferencia());
//				folhaService.salvarOuAtualizar(dadosFolha);
//				qtdeIncluidas = qtdeIncluidas + 1;
//			}
//		}
//	}
//
//	private Folha converteRegistro(String linha, String anoMesReferencia, Integer numeroLinha)
//			throws TxtIntegridadeException {
//		String[] campos = linha.split(arqEntradaDelimitador);
//		if ( numeroLinha == 1 ) {
//			campos[0] = Utilitarios.excluiCaracterNaoEditavel(campos[0], 6);
//		}
//		Optional<ButtonType> continuar = null;
//		try {
//			if (campos.length == 8) {
//				String anoMes = campos[0];
//				Double codCentroCustos = Double.parseDouble(campos[1]);
//				String descCentroCustos = campos[2];
//				Double codVerba = Double.parseDouble(campos[3]);
//				String descVerba = campos[4];
//				String tipoVerba = campos[5];
//				Double referenciaVerba = Double.parseDouble(campos[6]);
//				Double valorVerba = Double.parseDouble(campos[7]);
//				String importar = null;
//				String considerarReferencia = null;
//				String observacao = null;
//				if (!anoMes.equals(anoMesReferencia)) {
//					throw new TxtIntegridadeException("Registro nao Coerente com o Mês de Referencia");
//				}
//				Folha dadosFolha = new Folha(anoMes, codCentroCustos, descCentroCustos, codVerba, descVerba, valorVerba,
//						referenciaVerba, tipoVerba, importar, considerarReferencia, observacao);
//				return dadosFolha;
//			} else {
//				throw new TxtIntegridadeException("Quantidade de Campos Diferente do Esperado (8)");
//			}
//		} catch (TxtIntegridadeException e) {
//			qtdeCorrompidas = qtdeCorrompidas + 1;
//			continuar = Alertas.mostrarConfirmacao("Erro de Integridade no Arquivo",
//					"Continuar Processo de Leitura do TXT ?",
//					e.getMessage() + "\n \n" + "na linha No.: " + numeroLinha.toString() + "\n" + "\n" + linha,
//					AlertType.CONFIRMATION);
//		} catch (NumberFormatException e) {
//			qtdeCorrompidas = qtdeCorrompidas + 1;
//			continuar = Alertas.mostrarConfirmacao("Erro de Integridade no Arquivo",
//					"Continuar Processo de Leitura do TXT ?",
//					"Campo numerico esperado. \n \n" + e.getMessage() + "\n \n" +
//					"na linha No.: " + numeroLinha.toString() + "\n" + "\n" + linha,
//					AlertType.CONFIRMATION);
//		} catch (RuntimeException e) {
//			qtdeCorrompidas = qtdeCorrompidas + 1;
//			continuar = Alertas.mostrarConfirmacao("RuntimeException", "Erro na Importacao Dados da Folha",
//					e.getClass() + "\n" + e.getMessage(), AlertType.CONFIRMATION);
//		} finally {
//			if ((continuar != null) && (continuar.get() == ButtonType.CANCEL)) {
//				throw new TxtIntegridadeException("Processo Interrompido pelo usuário");
//			}
//		}
//		return null;
//	}
//

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
	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS  = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		qtdeDiasOS = (parametrosService.pesquisarPorChave("ValidarErp", "QtdeDiasOS")).getValor();	
	}



}
