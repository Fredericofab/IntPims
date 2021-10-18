package model.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.dao.FabricaDeDao;
import model.dao.PimsGeralDao;
import model.entities.CriticasErp;
import model.entities.Erp;

public class AnalisarErpService {
	
	private PimsGeralDao pimsGeralDao = FabricaDeDao.criarPimsGeralDao();
	private ErpService erpService = new ErpService();

//	parametros
	String usuarioPimsCS;
	String anoMes;
	String qtdeDiasOS;
	
	List<Erp> listaErp;
	Integer qtdeAnalisadas;
	Integer qtdeAtualizadas;
	Integer qtdePendentes;
	
	public Integer getQtdeAnalisadas() {
		return qtdeAnalisadas;
	}
	public Integer getQtdeAtualizadas() {
		return qtdeAtualizadas;
	}
	public Integer getQtdePendentes() {
		return qtdePendentes;
	}

	
	public void analisarUm(String tipo, Integer codigo) {
		lerParametros();
		qtdeAnalisadas  = 0;
		qtdeAtualizadas = 0;
		qtdePendentes   = 0;
		listaErp = erpService.pesquisarTodos();
		if (tipo.equals("S")) { 
			if (codigo == 001) { s001OSInexistente(); }
			if (codigo == 002) { s002OSAntiga(); }
		}
	}
	public void analisarTodos() {
		lerParametros();
		listaErp = erpService.pesquisarTodos();
	}



	private void s001OSInexistente() {
		for (Erp erp : listaErp) {
			if (erp.getReferenciaOS() != null && erp.getReferenciaOS().equals("P") && erp.getNumeroOS() != null) {
				qtdeAnalisadas  += 1;
				Boolean existeOS = pimsGeralDao.existeApt_os_he(erp.getNumeroOS(),usuarioPimsCS);	
				if (existeOS) {
					qtdeAtualizadas += 1;
					erp.setSalvarOS_Material("S");
				}
				else {
					qtdePendentes   += 1;
					erp = gravarCritica(erp, "S001 ");
					erp.setSalvarOS_Material("N");
				}
				erpService.salvarOuAtualizar(erp);
			}
		}
		atualizarCriticasErp("S",001);
	}

	
	private void s002OSAntiga() {
		for (Erp erp : listaErp) {
			if (erp.getReferenciaOS() != null && erp.getReferenciaOS().equals("P") && erp.getNumeroOS() != null) {
				Boolean existeOS = pimsGeralDao.existeApt_os_he(erp.getNumeroOS(),usuarioPimsCS);	
				if ( existeOS ) {
					qtdeAnalisadas  += 1;
					Date dataSaida = pimsGeralDao.dataSaidaApt_os_he(erp.getNumeroOS(),usuarioPimsCS);	
					if ((dataSaida == null) || (diferencaDias(dataSaida) < Long.parseLong(qtdeDiasOS))) {
						qtdeAtualizadas += 1;
						erp.setSalvarOS_Material("S");
					}
					else {
						qtdePendentes   += 1;
						erp = gravarCritica(erp, "S002 ");
						erp.setSalvarOS_Material("N");
					}
					erpService.salvarOuAtualizar(erp);
				}
			}
		}
		atualizarCriticasErp("S",002);
	}
	
	private void atualizarCriticasErp(String tipo, Integer codigo) {
		CriticasErpService criticasErpService = new CriticasErpService();
		CriticasErp criticasErp = criticasErpService.pesquisarPorChave(tipo, codigo);
		criticasErp.setRegistrosAnalisados(qtdeAnalisadas);
		criticasErp.setRegistrosAtualizados(qtdeAtualizadas);
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
