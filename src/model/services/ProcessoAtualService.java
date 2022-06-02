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
import model.exceptions.ParametroInvalidoException;

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

	public ProcessoAtual pesquisarPorChave(String anoMes) {
		return dao.pesquisarPorChave(anoMes);
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
					 "importarErpMT,importarErpCD,importarErpDG," +
					 "validarErp,aplicarPoliticaErp,exportarErpVM," +
					 "exportarErpCM, exportarErpDG, exportarErpOS");
						bw.newLine();
			for (ProcessoAtual processoAtual : lista) {
				String linha = processoAtual.getAnoMes() + "," +
						processoAtual.getImportarFolha() + "," +
						processoAtual.getSumarizarFolha() + "," +
						processoAtual.getExportarFolha() + "," +
						processoAtual.getImportarFuncionario() + "," +
						processoAtual.getSumarizarFuncionario() + "," +
						processoAtual.getImportarErpRM() + "," +
						processoAtual.getImportarErpED() + "," + 
						processoAtual.getImportarErpDF() + "," +
						processoAtual.getValidarErp()	+ "," +
						processoAtual.getAplicarPoliticaErp()	+ "," +
						processoAtual.getExportarErpVM() + "," +
						processoAtual.getExportarErpCM() + "," +
						processoAtual.getExportarErpDG() + "," +
						processoAtual.getExportarErpOS();
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
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = "ControleProcesso";
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
		}
	}
	
	public void atualizarEtapa(String campo, String valor) {
		try {
			lerParametros(true);
			ProcessoAtual processoAtual = pesquisarPorChave(anoMes);
			if (campo.equals("ImportarFolha")) processoAtual.setImportarFolha(valor); 
			if (campo.equals("SumarizarFolha")) processoAtual.setSumarizarFolha(valor); 
			if (campo.equals("ExportarFolha")) processoAtual.setExportarFolha(valor); 
			if (campo.equals("ImportarFuncionario")) processoAtual.setImportarFuncionario(valor); 
			if (campo.equals("SumarizarFuncionario")) processoAtual.setSumarizarFuncionario(valor); 
			if (campo.equals("ImportarErpRM")) processoAtual.setImportarErpRM(valor); 
			if (campo.equals("ImportarErpED")) processoAtual.setImportarErpED(valor); 
			if (campo.equals("ImportarErpDF")) processoAtual.setImportarErpDF(valor); 
			if (campo.equals("ValidarErp")) processoAtual.setValidarErp(valor); 
			if (campo.equals("AplicarPoliticaErp")) processoAtual.setAplicarPoliticaErp(valor); 
			if (campo.equals("ExportarErpVM")) processoAtual.setExportarErpVM(valor); 
			if (campo.equals("ExportarErpCM")) processoAtual.setExportarErpCM(valor); 
			if (campo.equals("ExportarErpDG")) processoAtual.setExportarErpDG(valor); 
			if (campo.equals("ExportarErpOS")) processoAtual.setExportarErpOS(valor); 
			salvarOuAtualizar(processoAtual);
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. Atualizando Etapas Atuais", e.getMessage(),AlertType.ERROR);
		}

	}

}
