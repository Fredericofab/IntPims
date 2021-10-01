package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.FabricaDeDao;
import model.dao.FuncionariosSumarizadosDao;
import model.entities.FuncionariosSumarizados;

public class FuncionariosSumarizadosService {
	
	private FuncionariosSumarizadosDao dao = FabricaDeDao.criarFuncionariosSumarizadosDao();

	private ParametrosService parametrosService = new ParametrosService();

//	parametros
	String saida;
	
	public List<FuncionariosSumarizados> pesquisarTodos(){
		return dao.listarTodos();
			
	}

	public void salvarOuAtualizar(FuncionariosSumarizados objeto) {
		if (dao.pesquisarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos()) == null ) {
			dao.inserir(objeto);
		}
		else {
			dao.atualizar(objeto);
		}
	}
	
	public void remover(FuncionariosSumarizados objeto) {
		dao.deletarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos());
	}
	
	public Integer deletarTodos() {
		return dao.deletarTodos();
	}

	
	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<FuncionariosSumarizados> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("AnoMes,CodCCustos,DescCCustos,QtdeFuncionarios");
			bw.newLine();
			for (FuncionariosSumarizados sumarioFuncionarios : lista) {
				String linha = sumarioFuncionarios.getAnoMes() + "," + 
							   String.format("%.0f", sumarioFuncionarios.getCodCentroCustos()) + "," + 
							   sumarioFuncionarios.getDescCentroCustos() + "," +
							   sumarioFuncionarios.getQdteFuncionarios();
				bw.write(linha);
				bw.newLine();
			}
			if (! oficial) {
				Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida , AlertType.INFORMATION);
			}
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
		}
	}

	private void lerParametros(Boolean oficial) {
		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("FuncionariosSumarizados", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = (parametrosService.pesquisarPorChave("FuncionariosSumarizados", "ArqSaidaNome")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("FuncionariosSumarizados", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
		}
	}

}
