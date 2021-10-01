package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.FolhaDao;
import model.dao.FabricaDeDao;
import model.entities.Folha;

public class FolhaService {

	private FolhaDao dao = FabricaDeDao.criarFolhaDao();
	
	private ParametrosService parametrosService = new ParametrosService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	parametros
	String saida;

	public List<Folha> pesquisarTodos() {
		return dao.listarTodos();
	}

	public void salvarOuAtualizar(Folha objeto) {
		if (dao.pesquisarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos(), objeto.getCodVerba()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
		reatualizarEtapaDoProcesso();
	}

	public void remover(Folha objeto) {
		dao.deletarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos(), objeto.getCodVerba());
		reatualizarEtapaDoProcesso();
	}

	public Integer deletarTodos() {
		return dao.deletarTodos();
	}
	
	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<Folha> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("AnoMes,CodCCustos,DescCCustos,Codverba,DescVerba,ValorVerba,FlagImportar,Observacao");
			bw.newLine();
			for (Folha dadosFolha : lista) {
				String linha = dadosFolha.getAnoMes() + "," + 
							   String.format("%.0f", dadosFolha.getCodCentroCustos()) + "," + 
							   dadosFolha.getDescCentroCustos() + "," + 
							   String.format("%.0f",dadosFolha.getCodVerba()) + "," + 
							   dadosFolha.getDescVerba() + "," + 
							   String.format("%.2f",dadosFolha.getValorVerba()) + "," + 
							   dadosFolha.getImportar() + "," + 
							   dadosFolha.getObservacao();
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
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("DadosFolha", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = (parametrosService.pesquisarPorChave("DadosFolha", "ArqSaidaNome")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("DadosFolha", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
		}
	}
	
	private void reatualizarEtapaDoProcesso() {
		processoAtualService.atualizarEtapa("SumarizarFolha","N");
		processoAtualService.atualizarEtapa("ExportarFolha","N");
		processoAtualService.atualizarEtapa("FolhaAlterada","S");
	}

}
