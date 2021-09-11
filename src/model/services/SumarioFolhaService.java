package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.FabricaDeDao;
import model.dao.SumarioFolhaDao;
import model.entities.SumarioFolha;

public class SumarioFolhaService {
	
	private SumarioFolhaDao dao = FabricaDeDao.criarSumarioFolhaDao();

//	parametros
	String saida;
	
	public List<SumarioFolha> pesquisarTodos(){
		return dao.listarTodos();
			
	}

	public void salvarOuAtualizar(SumarioFolha objeto) {
		if (dao.pesquisarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos()) == null ) {
			dao.inserir(objeto);
		}
		else {
			dao.atualizar(objeto);
		}
	}
	
	public void remover(SumarioFolha objeto) {
		dao.deletarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos());
	}
	
	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<SumarioFolha> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("AnoMes,CodCCustos,DescCCustos,QtdeImportarSim,TotalImportarSim,QtdeImportarNao,TotalImportarNao");
			bw.newLine();
			for (SumarioFolha sumarioFolha : lista) {
				String linha = sumarioFolha.getAnoMes() + "," + 
							   sumarioFolha.getCodCentroCustos() + "," + sumarioFolha.getDescCentroCustos() + "," +
							   sumarioFolha.getQdteImportarSim() + "," + sumarioFolha.getTotalImportarSim() + "," +
							   sumarioFolha.getQdteImportarNao() + "," + sumarioFolha.getTotalImportarNao();
				bw.write(linha);
				bw.newLine();
			}
			Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida , AlertType.INFORMATION);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
		}
	}

	private void lerParametros(Boolean oficial) {
		ParametrosService parametrosService = new ParametrosService();
		String anoMes = (parametrosService.pesquisarPorChave("AmbienteGeral", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("SumarioFolha", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = (parametrosService.pesquisarPorChave("SumarioFolha", "ArqSaidaNome")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("SumarioFolha", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
		}
	}

}
