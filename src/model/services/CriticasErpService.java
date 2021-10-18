package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.CriticasErpDao;
import model.dao.FabricaDeDao;
import model.entities.CriticasErp;

public class CriticasErpService {

	private CriticasErpDao dao = FabricaDeDao.criarCriticasErpDao();
	
	private ParametrosService parametrosService = new ParametrosService();

//	parametros
	String saida;

	public List<CriticasErp> pesquisarTodos() {
		return dao.listarTodos();
	}

	public CriticasErp pesquisarPorChave(String tipo, Integer codigo) {
		return dao.pesquisarPorChave(tipo, codigo);
	}

	public void salvarOuAtualizar(CriticasErp objeto) {
		if (dao.pesquisarPorChave(objeto.getTipoCritica(), objeto.getCodigoCritica()) == null) {
			dao.inserir(objeto);
		} 
		else {
			dao.atualizar(objeto);
		}
	}
	
	public void remover(CriticasErp objeto) {
		dao.deletarPorChave(objeto.getTipoCritica(), objeto.getCodigoCritica());
	}
	
	public Integer ultimoSequencial(CriticasErp objeto) {
		return dao.ultimoSequencial(objeto.getTipoCritica());
	}

	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<CriticasErp> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("tipoCritica,codigoCritica,descCritica,flagAtiva,"
					+ "anoMesAnalisado,registrosAnalisados,"
					+ "registrosAtualizados,registrosPendentes,"
					+ "clausulaWhere,clausulaSet");
			bw.newLine();
			for (CriticasErp criticasErp : lista) {
				String linha = criticasErp.getTipoCritica() + "," + 
							   criticasErp.getCodigoCritica() + "," + 
							   criticasErp.getDescCritica() + "," + 
							   criticasErp.getFlagAtiva() + "," + 
							   criticasErp.getAnoMesAnalisado() + "," + 
							   criticasErp.getRegistrosAnalisados() + "," + 
							   criticasErp.getRegistrosAtualizados() + "," + 
							   criticasErp.getRegistrosPendentes() + "," + 
							   criticasErp.getClausulaWhere() + "," + 
							   criticasErp.getClausulaSet();
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

	private void lerParametros(Boolean oficial) {
		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("CriticasErp", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = (parametrosService.pesquisarPorChave("CriticasErp", "ArqSaidaNome")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("CriticasErp", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
		}
	}
}
