package model.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.dao.FabricaDeDao;
import model.dao.PimsGeralDao;
import model.entities.CriticaErp;
import model.entities.Erp;

public class AnalisarErpService {
	
	private PimsGeralDao pimsGeralDao = FabricaDeDao.criarPimsGeralDao();
	private ErpService erpService = new ErpService();
	private CriticaErpService criticasErpService = new CriticaErpService();

//	parametros
	String usuarioPimsCS;
	String anoMes;
	String qtdeDiasOS;
	
	List<Erp> listaErp;
	Integer qtdePendentes;

	
	public Integer getQtdeTotalDeRegistros() {
		return erpService.qtdeTotal();
	}
	public Integer getQtdeTotalLiberados() {
		return erpService.qtdeImportarS();
	}
	public Integer getQtdeTotalDesconsiderados() {
		return erpService.qtdeImportarN();
	}
	public Integer getQtdeTotalPendentes() {
		return erpService.qtdeImportarIndefinido();
	}

	
	public void analisarUm(String tipo, Integer codigo) {
		lerParametros();
		listaErp = erpService.pesquisarTodos();
		if (tipo.equals("S")) { 
			if (codigo == 001) { s001ExisteOSnoManfro(); }
			if (codigo == 002) { s002OSRecente(); }
		}
		else {
			if (tipo.equals("U") && codigo != null) {
				criticaTipoU(criticasErpService.pesquisarPorChave("U", codigo));
			}
		}
			
	}
	public void analisarTodos() {
		lerParametros();
		listaErp = erpService.pesquisarTodos();
		s001ExisteOSnoManfro();
		s002OSRecente();

		for (CriticaErp criticaErp : criticasErpService.pesquisarTodos() ) {
			if (criticaErp.getTipoCritica().toUpperCase().equals("U") && 
				criticaErp.getFlagAtiva().toUpperCase().equals("S")) {
				criticaTipoU(criticaErp);
			}
		}
	}



	private void s001ExisteOSnoManfro() {
		qtdePendentes   = 0;
		for (Erp erp : listaErp) {
			if (erp.getReferenciaOS() != null && erp.getReferenciaOS().equals("P") && erp.getNumeroOS() != null) {
				Boolean existeOS = pimsGeralDao.existeApt_os_he(erp.getNumeroOS(),usuarioPimsCS);	
				if (existeOS) {
					erp.setSalvarOS_Material("S");
				}
				else {
					qtdePendentes   += 1;
					erp = gravarCritica(erp, "S001 ");
					erp.setImportar("?");
					erp.setSalvarOS_Material("?");
				}
				erpService.salvarOuAtualizar(erp);
			}
		}
		atualizarCriticasErp("S",001);
	}
		
	private void s002OSRecente() {
		qtdePendentes   = 0;
		for (Erp erp : listaErp) {
			if (erp.getReferenciaOS() != null && erp.getReferenciaOS().equals("P") && erp.getNumeroOS() != null) {
				Boolean existeOS = pimsGeralDao.existeApt_os_he(erp.getNumeroOS(),usuarioPimsCS);	
				if ( existeOS ) {
					Date dataSaida = pimsGeralDao.dataSaidaApt_os_he(erp.getNumeroOS(),usuarioPimsCS);	
					if ((dataSaida == null) || (diferencaDias(dataSaida) < Long.parseLong(qtdeDiasOS))) {
						erp.setSalvarOS_Material("S");
					}
					else {
						qtdePendentes   += 1;
						erp = gravarCritica(erp, "S002 ");
						erp.setImportar("?");
						erp.setSalvarOS_Material("?");
					}
					erpService.salvarOuAtualizar(erp);
				}
			}
		}
		atualizarCriticasErp("S",002);
	}

	private void criticaTipoU(CriticaErp criticasErp) {
		String clausulaWhere = criticasErp.getClausulaWhere();
//		erpService.atualizarCriticaTipoU(clausulaWhere, clausulaSet);
	}
	
	private void atualizarCriticasErp(String tipo, Integer codigo) {
		CriticaErp criticasErp = criticasErpService.pesquisarPorChave(tipo, codigo);
		criticasErp.setRegistrosPendentes(qtdePendentes);
		criticasErpService.salvarOuAtualizar(criticasErp);
	}
	
	private Erp gravarCritica(Erp erp, String critica) {
		String criticas = erp.getCriticas();
		if (criticas == null) { 
			erp.setCriticas(critica);
		}
		else {
			if (criticas.indexOf(critica) == -1) {erp.setCriticas(criticas + critica); }
		}
		return erp;
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

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();
		usuarioPimsCS  = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		anoMes  = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		qtdeDiasOS = (parametrosService.pesquisarPorChave("AnalisarErp", "QtdeDiasOS")).getValor();
	}

}
