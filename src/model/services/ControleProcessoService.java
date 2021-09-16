package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.ControleProcessoDao;
import model.dao.FabricaDeDao;
import model.entities.ControleProcesso;

public class ControleProcessoService {

	private ControleProcessoDao dao = FabricaDeDao.criarControleProcessoDao();

//	parametros
	String saida;

	public List<ControleProcesso> pesquisarTodos() {
		return dao.listarTodos();
	}

	public void salvarOuAtualizar(ControleProcesso objeto) {
		if (dao.pesquisarPorChave(objeto.getAnoMes()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
	}

	public void remover(ControleProcesso objeto) {
		dao.deletarPorChave(objeto.getAnoMes());
	}

	public ControleProcesso pesquisarPorChave(ControleProcesso objeto) {
		return dao.pesquisarPorChave(objeto.getAnoMes());
	}
	
	public void removerTodos() {
		dao.deletarTodos();
	}

	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<ControleProcesso> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("anoMes,importarFolha,sumarizarFolha,exportarFolha," +
					 "importarFuncionario,sumarizarFuncionario," +
					 "importarErpMT,importarErpCD,importarErpDD,criticarErp,exportarErp");
						bw.newLine();
			for (ControleProcesso controleProcesso : lista) {
				String linha = controleProcesso.getAnoMes() + "," +
						controleProcesso.getImportarFolha() + "," +
						controleProcesso.getSumarizarFolha() + "," +
						controleProcesso.getExportarFolha() + "," +
						controleProcesso.getImportarFuncionario() + "," +
						controleProcesso.getSumarizarFuncionario() + "," +
						controleProcesso.getImportarErpMT() + "," +
						controleProcesso.getImportarErpCD() + "," + 
						controleProcesso.getImportarErpDD() + "," +
						controleProcesso.getCriticarErp()	+ "," +
						controleProcesso.getExportarErp();
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
		ParametrosService parametrosService = new ParametrosService();
		String anoMes = (parametrosService.pesquisarPorChave("AmbienteGeral", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("ControleProcesso", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = (parametrosService.pesquisarPorChave("ControleProcesso", "ArqSaidaNome")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("ControleProcesso", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
		}
	}
}
