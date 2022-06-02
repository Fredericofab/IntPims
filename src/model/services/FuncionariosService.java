package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.FuncionariosDao;
import model.dao.FabricaDeDao;
import model.entities.Funcionarios;
import model.exceptions.ParametroInvalidoException;

public class FuncionariosService {

	private FuncionariosDao dao = FabricaDeDao.criarFuncionariosDao();

	private ParametrosService parametrosService = new ParametrosService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	parametros
	String saida;

	public List<Funcionarios> pesquisarTodos() {
		return dao.listarTodos();
	}

	public void salvarOuAtualizar(Funcionarios objeto, Boolean atualizarEtapaDoProcesso) {
		if (dao.pesquisarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos(), objeto.getCodFuncionario()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
		if ( atualizarEtapaDoProcesso ) reatualizarEtapaDoProcesso();
	}

	public void remover(Funcionarios objeto) {
		dao.deletarPorChave(objeto.getAnoMes(), objeto.getCodCentroCustos(), objeto.getCodFuncionario());
		reatualizarEtapaDoProcesso();
	}

	public Integer deletarTodos() {
		return dao.deletarTodos();
	}
	
	public void gerarTxt(Boolean oficial) {
		try {
			lerParametros(oficial);
			List<Funcionarios> lista = pesquisarTodos();
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
				bw.write("AnoMes,CodCCustos,DescCCustos,CodFuncionario,DescFuncionario");
				bw.newLine();
				for (Funcionarios funcionarios : lista) {
					String linha = funcionarios.getAnoMes() + "," + 
								   String.format("%.0f", funcionarios.getCodCentroCustos()) + "," +
								   funcionarios.getDescCentroCustos() + "," + 
								   String.format("%.0f", funcionarios.getCodFuncionario()) + "," +
								   funcionarios.getDescFuncionario(); 
					bw.write(linha);
					bw.newLine();
				}
				if (! oficial) {
					Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida, AlertType.INFORMATION);
				}
			} catch (IOException e) {
				Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
			}
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. Gerando Funcionario TXT", e.getMessage(),AlertType.ERROR);
		}
	}

	private void lerParametros(Boolean oficial) {
		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaPasta")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + "Funcionarios" + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + "Funcionarios" + anoMes + arqSaidaTipo ;
		}
	}
	
	private void reatualizarEtapaDoProcesso() {
		processoAtualService.atualizarEtapa("SumarizarFuncionario","N");
	}
}
