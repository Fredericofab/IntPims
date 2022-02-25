package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.PoliticasErpDao;
import model.dao.FabricaDeDao;
import model.entities.PoliticasErp;

public class PoliticasErpService {

	private PoliticasErpDao dao = FabricaDeDao.criarPoliticasErpDao();
	
	private ParametrosService parametrosService = new ParametrosService();

//	parametros
	String saida;
	String saidaRelatorio;

	public List<PoliticasErp> pesquisarTodos() {
		return dao.listarTodos();
	}

	public PoliticasErp pesquisarPorChave(Integer codigo) {
		return dao.pesquisarPorChave(codigo);
	}

	public void salvarOuAtualizar(PoliticasErp objeto) {
		if (dao.pesquisarPorChave(objeto.getCodPolitica()) == null) {
			dao.inserir(objeto);
		} 
		else {
			dao.atualizar(objeto);
		}
	}
	
	public void remover(PoliticasErp objeto) {
		dao.deletarPorChave(objeto.getCodPolitica());
	}
	
	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<PoliticasErp> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("codPolitica,nomePolitica,descPolitica,flagAtiva,"
					+ "imp,OS,VM,CM,DG,clausulaWhere");
			bw.newLine();
			for (PoliticasErp politicasErp : lista) {
				String linha = politicasErp.getCodPolitica() + "," + 
							   politicasErp.getNomePolitica() + "," + 
						       politicasErp.getDescPolitica() + "," + 
							   politicasErp.getFlagAtiva() + "," + 
							   politicasErp.getImportar() + "," +
							   politicasErp.getSalvarOS_Material() + "," + 
							   politicasErp.getSalvarCstg_IntVM() + "," +
							   politicasErp.getSalvarCstg_IntCM() + "," +
							   politicasErp.getSalvarCstg_IntDG() + "," +
							   politicasErp.getClausulaWhere() ;
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
	
	public void relatorioTxt(Boolean oficial) {
		lerParametros(oficial);
		List<PoliticasErp> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saidaRelatorio))) {
			for (PoliticasErp politicasErp : lista) {
				bw.write("Politica.: " + politicasErp.getCodPolitica()   +
						 "                   Ativa: " + politicasErp.getFlagAtiva());
				bw.newLine();
				bw.write("Nome.....: " + politicasErp.getNomePolitica());
				bw.newLine();
				bw.newLine();
				bw.write("Descricao: ");
				bw.newLine();
				bw.write(politicasErp.getDescPolitica());
				bw.newLine();
				bw.newLine();
				bw.write("Clausula WHERE:");
				bw.newLine();
				bw.write(politicasErp.getClausulaWhere());
				bw.newLine();
				bw.newLine();
				bw.write("Ações: ");
				bw.newLine();
				bw.write("Salvar Valor Material.....(VM): " + (politicasErp.getSalvarCstg_IntVM() == null ? " " : politicasErp.getSalvarCstg_IntVM()));
				bw.newLine();
				bw.write("Importar......................: " + politicasErp.getImportar());
				bw.newLine();
				bw.write("  Salvar OS_Material......(OS): " + (politicasErp.getSalvarOS_Material() == null ? " " : politicasErp.getSalvarOS_Material()));
				bw.newLine();
				bw.write("  Salvar Consumo Material.(CM): " + (politicasErp.getSalvarCstg_IntCM() == null ? " " : politicasErp.getSalvarCstg_IntCM()));
				bw.newLine();
				bw.write("  Salvar Despesas Gerais..(DG): " + (politicasErp.getSalvarCstg_IntDG() == null ? " " : politicasErp.getSalvarCstg_IntDG()));
				bw.newLine();

				bw.newLine();
				bw.write("x==========================================================x");
				bw.newLine();
				bw.newLine();
			}

			if (! oficial) {
				Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saidaRelatorio, AlertType.INFORMATION);
			}
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Relatorio TXT", e.getMessage(), AlertType.ERROR);
		}
	}

	private void lerParametros(Boolean oficial) {
		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("PoliticasErp", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = (parametrosService.pesquisarPorChave("PoliticasErp", "ArqSaidaNome")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("PoliticasErp", "ArqSaidaTipo")).getValor();
		String relatorioPasta = (parametrosService.pesquisarPorChave("PoliticasErp", "RelatorioPasta")).getValor();
		String relatorioNome  = (parametrosService.pesquisarPorChave("PoliticasErp", "RelatorioNome")).getValor();
		String relatorioTipo  = (parametrosService.pesquisarPorChave("PoliticasErp", "RelatorioTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + "_oficial" + arqSaidaTipo ;
			saidaRelatorio = relatorioPasta + relatorioNome + anoMes + "_oficial" + relatorioTipo ;
		}
		else {
			saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
			saidaRelatorio = relatorioPasta + relatorioNome + anoMes + relatorioTipo ;
		}
	}
}
