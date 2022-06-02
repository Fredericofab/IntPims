package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.FabricaDeDao;
import model.dao.FolhaSumarizadaDao;
import model.entities.FolhaSumarizada;
import model.exceptions.ParametroInvalidoException;

public class FolhaSumarizadaService {
	
	private FolhaSumarizadaDao dao = FabricaDeDao.criarFolhaSumarizadaDao();
	
	private ParametrosService parametrosService = new ParametrosService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	parametros
	String saida;
	
	public List<FolhaSumarizada> pesquisarTodos(){
		return dao.listarTodos();
			
	}

	public void salvarOuAtualizar(FolhaSumarizada objeto, Boolean atualizarEtapaDoProcesso) {
		if (dao.pesquisarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos()) == null ) {
			dao.inserir(objeto);
		}
		else {
			dao.atualizar(objeto);
		}
		if (atualizarEtapaDoProcesso ) reatualizarEtapaDoProcesso();
	}
	
	public void remover(FolhaSumarizada objeto) {
		dao.deletarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos());
		reatualizarEtapaDoProcesso();
	}
	
	public Integer deletarTodos() {
		return dao.deletarTodos();
	}

	
	public void gerarTxt(Boolean oficial) {
		try {
			lerParametros(oficial);
			List<FolhaSumarizada> lista = pesquisarTodos();
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
				bw.write("AnoMes,CodCCustos,DescCCustos,QtdeImportarSim,TotalImportarSim,TotalReferenciaSim,QtdeImportarNao,TotalImportarNao");
				bw.newLine();
				for (FolhaSumarizada sumarioFolha : lista) {
					String linha = sumarioFolha.getAnoMes() + "," + 
								   String.format("%.0f", sumarioFolha.getCodCentroCustos()) + "," +
								   sumarioFolha.getDescCentroCustos() + "," +
								   sumarioFolha.getQdteImportarSim() + "," +
								   String.format("%.2f", sumarioFolha.getTotalImportarSim()) + "," +
								   String.format("%.2f", sumarioFolha.getTotalReferenciaSim()) + "," +
								   sumarioFolha.getQdteImportarNao() + "," +
								   String.format("%.2f", sumarioFolha.getTotalImportarNao());
					bw.write(linha);
					bw.newLine();
				}
				if (! oficial) {
					Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida , AlertType.INFORMATION);
				}
			} catch (IOException e) {
				Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
			}
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. Gerando FolhaSumarizada TXT", e.getMessage(),AlertType.ERROR);
		}
	}

	private void lerParametros(Boolean oficial) {
		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaPasta")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + "FolhaSumarizada" + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + "FolhaSumarizada" + anoMes + arqSaidaTipo ;
		}
	}
	
	private void reatualizarEtapaDoProcesso() {
		processoAtualService.atualizarEtapa("ExportarFolha","N");
	}

}
