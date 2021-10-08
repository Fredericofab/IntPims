package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.ProcessoAtualDao;
import model.dao.FabricaDeDao;
import model.entities.ProcessoAtual;

public class ProcessoAtualService {

	private ProcessoAtualDao dao = FabricaDeDao.criarProcessoAtualDao();

	private ParametrosService parametrosService = new ParametrosService();

//	parametros
	String saida;
	String anoMes;

	public List<ProcessoAtual> pesquisarTodos() {
		return dao.listarTodos();
	}

	public void salvarOuAtualizar(ProcessoAtual objeto) {
		if (dao.pesquisarPorChave(objeto.getAnoMes()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
	}

	public void remover(ProcessoAtual objeto) {
		dao.deletarPorChave(objeto.getAnoMes());
	}

	public ProcessoAtual pesquisarPorChave(ProcessoAtual objeto) {
		return dao.pesquisarPorChave(objeto.getAnoMes());
	}
	
	public void removerTodos() {
		dao.deletarTodos();
	}

	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<ProcessoAtual> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("anoMes,importarFolha,sumarizarFolha,exportarFolha," +
					 "importarFuncionario,sumarizarFuncionario," +
					 "importarErpMT,importarErpCD,importarErpDG,criticarErp,exportarErp," +
					 "verbaAlterada, folhaAlterada");
						bw.newLine();
			for (ProcessoAtual processoAtual : lista) {
				String linha = processoAtual.getAnoMes() + "," +
						processoAtual.getImportarFolha() + "," +
						processoAtual.getSumarizarFolha() + "," +
						processoAtual.getExportarFolha() + "," +
						processoAtual.getImportarFuncionario() + "," +
						processoAtual.getSumarizarFuncionario() + "," +
						processoAtual.getImportarErpMT() + "," +
						processoAtual.getImportarErpCD() + "," + 
						processoAtual.getImportarErpDG() + "," +
						processoAtual.getCriticarErp()	+ "," +
						processoAtual.getExportarErp()   + "," +
				processoAtual.getVerbaAlterada()  + "," +
				processoAtual.getFolhaAlterada();
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
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
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
	
	public void atualizarEtapa(String campo, String valor) {
		lerParametros(true);
		ProcessoAtual processoAtual = new ProcessoAtual();
		processoAtual.setAnoMes(anoMes);
		processoAtual = pesquisarPorChave(processoAtual);
		if (campo.equals("ImportarFolha")) processoAtual.setImportarFolha(valor); 
		if (campo.equals("SumarizarFolha")) processoAtual.setSumarizarFolha(valor); 
		if (campo.equals("ExportarFolha")) processoAtual.setExportarFolha(valor); 
		if (campo.equals("ImportarFuncionario")) processoAtual.setImportarFuncionario(valor); 
		if (campo.equals("SumarizarFuncionario")) processoAtual.setSumarizarFuncionario(valor); 
		if (campo.equals("ImportarErpMT")) processoAtual.setImportarErpMT(valor); 
		if (campo.equals("ImportarErpCD")) processoAtual.setImportarErpCD(valor); 
		if (campo.equals("ImportarErpDG")) processoAtual.setImportarErpDG(valor); 
		if (campo.equals("CriticarErp")) processoAtual.setCriticarErp(valor); 
		if (campo.equals("ExportarErp")) processoAtual.setExportarErp(valor); 
		
		if (campo.equals("VerbaAlterada")) processoAtual.setVerbaAlterada(valor); 
		if (campo.equals("FolhaAlterada")) processoAtual.setFolhaAlterada(valor); 

		salvarOuAtualizar(processoAtual);
	}

}
