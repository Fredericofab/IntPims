package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.VerbaFolhaDao;
import model.dao.FabricaDeDao;
import model.entities.VerbaFolha;

public class VerbaFolhaService {

	private VerbaFolhaDao dao = FabricaDeDao.criarVerbaFolhaDao();

	public List<VerbaFolha> pesquisarTodos() {
		return dao.listarTodos();
	}

	public VerbaFolha pesquisarPorChave(String codVerba) {
		return dao.pesquisarPorChave(codVerba);
	};

	public void salvarOuAtualizar(VerbaFolha objeto) {
		if (dao.pesquisarPorChave(objeto.getCodVerba()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
	}

	public void remover(VerbaFolha objeto) {
		dao.deletarPorChave(objeto.getCodVerba());
	}

	public void gerarTxt() {
		List<VerbaFolha> lista = pesquisarTodos();
		String caminho = "C:\\Projeto Itapecuru Custag\\IGP TG\\saida\\";
		String arquivo = "VerbaFolha";
		String anoMes = "202109";
		String extensao = ".txt";
		String saida = caminho + arquivo + anoMes + extensao;
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("Codverba,DescricaoVerba,FlagImportar");
			bw.newLine();
			for (VerbaFolha verbaFolha : lista) {
				String linha = verbaFolha.getCodVerba() + "," + verbaFolha.getDescVerba() + "," + verbaFolha.getImportar(); 
				bw.write(linha);
				bw.newLine();
			}
			Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida , AlertType.INFORMATION);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
		}
	}
}
