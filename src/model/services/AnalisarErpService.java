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
import model.exceptions.IntegridadeException;

public class AnalisarErpService {

	private PimsGeralDao pimsGeralDao = FabricaDeDao.criarPimsGeralDao();
	private ErpService erpService = new ErpService();
	private CriticaErpService criticasErpService = new CriticaErpService();

//	parametros
	String usuarioPimsCS;
	String anoMes;
	String qtdeDiasOS;

	List<Erp> listaErp;
	Integer qtdeAnalisados;
	Integer qtdePendentes;
	Integer qtdeLiberados;
	Integer qtdeIgnorados;
	Integer qtdeLiberadosOS;
	Integer qtdeLiberadosCM;
	Integer qtdeLiberadosDG;

	public Integer getQtdeTotal(String importar) {
		return erpService.qtdeTotal(importar);
	}

	public Integer getQtdeLiberadosOS() {
		return erpService.qtdeLiberadosOS();
	}

	public Integer getQtdeLiberadosCM() {
		return erpService.qtdeLiberadosCM();
	}

	public Integer getQtdeLiberadosDG() {
		return erpService.qtdeLiberadosDG();
	}

	public Integer getQtdeLiberadosVM() {
		return erpService.qtdeLiberadosVM();
	}

	public void analisarUm(String tipo, Integer codigo) {
		CriticaErp criticaErp = criticasErpService.pesquisarPorChave(tipo, codigo);
		if (criticaErp == null) {
			throw new IntegridadeException("Critica Informada não Existe");
		}
		lerParametros();
		listaErp = erpService.pesquisarTodos();
		if (tipo.equals("S")) {
			if (codigo == 001) {
				s001NaoExisteOSnoManfro();
			}
			if (codigo == 002) {
				s002OSAntiga();
			}
			if (codigo == 003) {
				s003OSValida();
			}
		} else {
			criticaTipoU(criticaErp);
		}
	}

	public void analisarTodos() {
		lerParametros();
		listaErp = erpService.pesquisarTodos();
		s001NaoExisteOSnoManfro();
		s002OSAntiga();
		s003OSValida();

		for (CriticaErp criticaErp : criticasErpService.pesquisarTodos()) {
			if (criticaErp.getTipoCritica().toUpperCase().equals("U")
					&& criticaErp.getFlagAtiva().toUpperCase().equals("S")) {
				criticaTipoU(criticaErp);
			}
		}
	}

	private void s001NaoExisteOSnoManfro() {
		CriticaErp criticaErp = criticasErpService.pesquisarPorChave("S", 001);
		zerarContadores();
		for (Erp erp : listaErp) {
			if (erp.getReferenciaOS() != null && erp.getReferenciaOS().equals("P") && erp.getNumeroOS() != null) {
				qtdeAnalisados += 1;
				Boolean existeOS = pimsGeralDao.existeApt_os_he(erp.getNumeroOS(), usuarioPimsCS);
				if (!existeOS) {
					qtdePendentes += 1;
					erp = atualizarRegistroErp(criticaErp, erp);
					erpService.salvarOuAtualizar(erp);
				}
			}
		}
		criticaErp = atualizarRegistroCriticaErp(criticaErp);
		criticasErpService.salvarOuAtualizar(criticaErp);
	}

	private void s002OSAntiga() {
		CriticaErp criticaErp = criticasErpService.pesquisarPorChave("S", 002);
		zerarContadores();
		for (Erp erp : listaErp) {
			if (erp.getReferenciaOS() != null && erp.getReferenciaOS().equals("P") && erp.getNumeroOS() != null) {
				qtdeAnalisados += 1;
				Boolean existeOS = pimsGeralDao.existeApt_os_he(erp.getNumeroOS(), usuarioPimsCS);
				if (existeOS) {
					Date dataSaida = pimsGeralDao.dataSaidaApt_os_he(erp.getNumeroOS(), usuarioPimsCS);
					if ((dataSaida != null) && (diferencaDias(dataSaida) > Long.parseLong(qtdeDiasOS))) {
						qtdePendentes += 1;
						erp = atualizarRegistroErp(criticaErp, erp);
						erpService.salvarOuAtualizar(erp);
					}
				}
			}
		}
		criticaErp = atualizarRegistroCriticaErp(criticaErp);
		criticasErpService.salvarOuAtualizar(criticaErp);
	}

	private void s003OSValida() {
		CriticaErp criticaErp = criticasErpService.pesquisarPorChave("S", 003);
		zerarContadores();
		for (Erp erp : listaErp) {
			if (erp.getReferenciaOS() != null && erp.getReferenciaOS().equals("P") && erp.getNumeroOS() != null) {
				qtdeAnalisados += 1;
				Boolean existeOS = pimsGeralDao.existeApt_os_he(erp.getNumeroOS(), usuarioPimsCS);
				if (existeOS) {
					Date dataSaida = pimsGeralDao.dataSaidaApt_os_he(erp.getNumeroOS(), usuarioPimsCS);
					if ((dataSaida == null) || (diferencaDias(dataSaida) < Long.parseLong(qtdeDiasOS))) {
						qtdeLiberados += 1;
						erp = atualizarRegistroErp(criticaErp, erp);
						erpService.salvarOuAtualizar(erp);
					}
				}
			}
		}
		criticaErp = atualizarRegistroCriticaErp(criticaErp);
		criticasErpService.salvarOuAtualizar(criticaErp);
	}

	private void criticaTipoU(CriticaErp criticaErp) {
		String filtro = criticaErp.getClausulaWhere();
		Integer codigoCritica = criticaErp.getCodigoCritica();
		listaErp = erpService.pesquisarCriticaFiltrada("U", codigoCritica, filtro);
		zerarContadores();
		for (Erp erp : listaErp) {
			qtdeAnalisados += 1;
			if ((criticaErp.getImportar() != null) && (criticaErp.getImportar().equals("?"))) {
				qtdePendentes += 1;
			}
			if ((criticaErp.getImportar() != null) && (criticaErp.getImportar().equals("S"))) {
				qtdeLiberados += 1;
			}
			if ((criticaErp.getImportar() != null) && (criticaErp.getImportar().equals("N"))) {
				qtdeIgnorados += 1;
			}
			erp = atualizarRegistroErp(criticaErp, erp);
			erpService.salvarOuAtualizar(erp);
		}
		criticaErp = atualizarRegistroCriticaErp(criticaErp);
		criticasErpService.salvarOuAtualizar(criticaErp);
	}

	public void atualizarEstatisticaPorCritica() {
		for (CriticaErp criticaErp : criticasErpService.pesquisarTodos()) {
			if (criticaErp.getFlagAtiva().toUpperCase().equals("S")) {
				String essaCriticaTxt = criticaErp.getTipoCritica() + String.format("%03d", criticaErp.getCodigoCritica());
				zerarContadores();
				qtdeAnalisados = criticaErp.getRegistrosAnalisados();
				qtdePendentes = erpService.qtdeDessaCritica(essaCriticaTxt, "?");
				qtdeLiberados = erpService.qtdeDessaCritica(essaCriticaTxt, "S");
				qtdeIgnorados = erpService.qtdeDessaCritica(essaCriticaTxt, "N");
				atualizarRegistroCriticaErp(criticaErp);
				criticasErpService.salvarOuAtualizar(criticaErp);
			}
		}
	}

	private Erp atualizarRegistroErp(CriticaErp criticaErp, Erp erp) {

		if (criticaErp.getImportar() != null) {
			erp.setImportar(criticaErp.getImportar());
		}

		if ((criticaErp.getSalvarOS_Material() != null)
				&& (!criticaErp.getSalvarOS_Material().toUpperCase().equals(" "))) {
			erp.setSalvarOS_Material(criticaErp.getSalvarOS_Material());
		}
		if ((criticaErp.getSalvarCstg_IntVM() != null)
				&& (!criticaErp.getSalvarCstg_IntVM().toUpperCase().equals(" "))) {
			erp.setSalvarCstg_IntVM(criticaErp.getSalvarCstg_IntVM());
		}
		if ((criticaErp.getSalvarCstg_IntCM() != null)
				&& (!criticaErp.getSalvarCstg_IntCM().toUpperCase().equals(" "))) {
			erp.setSalvarCstg_IntCM(criticaErp.getSalvarCstg_IntCM());
		}
		if ((criticaErp.getSalvarCstg_IntDG() != null)
				&& (!criticaErp.getSalvarCstg_IntDG().toUpperCase().equals(" "))) {
			erp.setSalvarCstg_IntDG(criticaErp.getSalvarCstg_IntDG());
		}

		String essaCriticaTxt = criticaErp.getTipoCritica() + String.format("%03d", criticaErp.getCodigoCritica()) + " ";
		String criticasDesseRegistros = erp.getCriticas();
		if (criticasDesseRegistros == null) {
			erp.setCriticas(essaCriticaTxt);
		} else {
			if (criticasDesseRegistros.indexOf(essaCriticaTxt) == -1) {
				erp.setCriticas(criticasDesseRegistros + essaCriticaTxt);
			}
		}
		return erp;
	}

	private CriticaErp atualizarRegistroCriticaErp(CriticaErp criticaErp) {
		criticaErp.setAnoMesAnalisado(anoMes);
		criticaErp.setRegistrosAnalisados(qtdeAnalisados);
		criticaErp.setRegistrosPendentes(qtdePendentes);
		criticaErp.setRegistrosLiberados(qtdeLiberados);
		criticaErp.setRegistrosIgnorados(qtdeIgnorados);
		return criticaErp;
	}

	private void zerarContadores() {
		qtdeAnalisados = 0;
		qtdePendentes = 0;
		qtdeLiberados = 0;
		qtdeIgnorados = 0;
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
		usuarioPimsCS = (parametrosService.pesquisarPorChave("AmbienteOracle", "UsuarioPimsCS")).getValor();
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		qtdeDiasOS = (parametrosService.pesquisarPorChave("AnalisarErp", "QtdeDiasOS")).getValor();
	}
}
