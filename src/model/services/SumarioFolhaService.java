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
	
	public void gerarTxt() {
		List<SumarioFolha> lista = pesquisarTodos();
		String caminho = "C:\\Projeto Itapecuru Custag\\IGP TG\\saida\\";
		String arquivo = "SumarioFolha";
		String anoMes = "202109";
		String extensao = ".txt";
		String saida = caminho + arquivo + anoMes + extensao;
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
			Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida , AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
		}
	}

}
