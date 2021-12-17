package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.CriticaErpDao;
import model.dao.FabricaDeDao;
import model.entities.CriticaErp;

public class CriticaErpService {

	private CriticaErpDao dao = FabricaDeDao.criarCriticasErpDao();
	
	private ParametrosService parametrosService = new ParametrosService();

//	parametros
	String saida;
	String saidaRelatorio;

	public List<CriticaErp> pesquisarTodos() {
		return dao.listarTodos();
	}

	public CriticaErp pesquisarPorChave(String tipo, Integer codigo) {
		return dao.pesquisarPorChave(tipo, codigo);
	}

	public void salvarOuAtualizar(CriticaErp objeto) {
		if (dao.pesquisarPorChave(objeto.getTipoCritica(), objeto.getCodigoCritica()) == null) {
			dao.inserir(objeto);
		} 
		else {
			dao.atualizar(objeto);
		}
	}
	
	public void remover(CriticaErp objeto) {
		dao.deletarPorChave(objeto.getTipoCritica(), objeto.getCodigoCritica());
	}
	
	public Integer ultimoSequencial(CriticaErp objeto) {
		return dao.ultimoSequencial(objeto.getTipoCritica());
	}

	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<CriticaErp> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("tipoCritica,codigoCritica,descCritica,flagAtiva,"
					+ "anoMesAnalisado,registrosAnalisados,"
					+ "registrosAtualizados,registrosPendentes,"
					+ "clausulaWhere,clausulaSet");
			bw.newLine();
			for (CriticaErp criticasErp : lista) {
				String linha = criticasErp.getTipoCritica() + "," + 
							   criticasErp.getCodigoCritica() + "," + 
							   criticasErp.getDescCritica() + "," + 
							   criticasErp.getFlagAtiva() + "," + 
							   criticasErp.getAnoMesAnalisado() + "," + 
							   criticasErp.getRegistrosPendentes() + "," + 
							   criticasErp.getClausulaWhere();
				bw.write(linha);
				bw.newLine();
			}
			if (! oficial) {
				Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida, AlertType.INFORMATION);
			}
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
		}
	}
	
	public void relatorioTxt(Boolean oficial) {
		lerParametros(oficial);
		List<CriticaErp> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saidaRelatorio))) {
			for (CriticaErp criticasErp : lista) {
				bw.write("Critica: " + criticasErp.getTipoCritica()   +
									   criticasErp.getCodigoCritica() +
						 "  Ativa: " + criticasErp.getFlagAtiva());
				bw.newLine();
				bw.write("Nome: " + criticasErp.getNomeCritica());
				bw.newLine();
				bw.write("Descricao: ");
				bw.newLine();
				bw.write(criticasErp.getDescCritica());
				bw.newLine();
				if (criticasErp.getTipoCritica().toUpperCase().equals("U")) {
					bw.newLine();
					bw.write("Clausula WHERE:");
					bw.newLine();
					bw.write(criticasErp.getClausulaWhere());
					bw.newLine();
				}
				bw.newLine();
				bw.write("==========================================================");
				bw.newLine();
				bw.newLine();
			}

			if (! oficial) {
				Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saidaRelatorio, AlertType.INFORMATION);
			}
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Relatorio TXT", e.getMessage(), AlertType.ERROR);
		}
	}

	private void lerParametros(Boolean oficial) {
		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("CriticasErp", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = (parametrosService.pesquisarPorChave("CriticasErp", "ArqSaidaNome")).getValor();
		String arqSaidaRelatorio  = (parametrosService.pesquisarPorChave("CriticasErp", "ArqSaidaRelatorio")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("CriticasErp", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + "_oficial" + arqSaidaTipo ;
			saidaRelatorio = arqSaidaPasta + arqSaidaRelatorio + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
			saidaRelatorio = arqSaidaPasta + arqSaidaRelatorio + anoMes + arqSaidaTipo ;
		}
	}
}
