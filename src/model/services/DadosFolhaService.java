package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.DadosFolhaDao;
import model.dao.FabricaDeDao;
import model.entities.DadosFolha;

public class DadosFolhaService {
	
	private DadosFolhaDao dao = FabricaDeDao.criarDadosFolhaDao();
	
	public List<DadosFolha> pesquisarTodos(){
		return dao.listarTodos();
			
	}

	public void salvarOuAtualizar(DadosFolha objeto) {
		if (dao.pesquisarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos(), objeto.getCodVerba()) == null ) {
			dao.inserir(objeto);
		}
		else {
			dao.atualizar(objeto);
		}
	}
	
	public void remover(DadosFolha objeto) {
		dao.deletarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos(), objeto.getCodVerba());
	}

	public void gerarTxt() {
		List<DadosFolha> lista = pesquisarTodos();
		String caminho = "C:\\Projeto Itapecuru Custag\\IGP TG\\saida\\";
		String arquivo = "DadosFolha";
		String anoMes = "202109";
		String extensao = ".txt";
		String saida = caminho + arquivo + anoMes + extensao;
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("AnoMes,CodCCustos,DescCCustos,Codverba,DescVerba,ValorVerba,FlagImportar,Observacao");
			bw.newLine();
			for (DadosFolha dadosFolha : lista) {
				String linha = dadosFolha.getAnoMes() + "," + 
							   dadosFolha.getCodCentroCustos() + "," + dadosFolha.getDescCentroCustos() + "," +
						       dadosFolha.getCodVerba() + "," + dadosFolha.getDescVerba() + "," +
							   dadosFolha.getValorVerba() + "," + dadosFolha.getImportar() + "," + dadosFolha.getObservacao(); 					; 			; 
				bw.write(linha);
				bw.newLine();
			}
			Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida , AlertType.ERROR);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
		}
	}}
