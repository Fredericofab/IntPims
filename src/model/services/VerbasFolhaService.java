package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.VerbasFolhaDao;
import model.dao.FabricaDeDao;
import model.entities.VerbasFolha;
import model.exceptions.ParametroInvalidoException;

public class VerbasFolhaService {

	private VerbasFolhaDao dao = FabricaDeDao.criarVerbasFolhaDao();
	
	private ParametrosService parametrosService = new ParametrosService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

	
//	parametros
	String saida;
	String defaultImportar;
	String defaultConsiderarReferencia;


	public List<VerbasFolha> pesquisarTodos() {
		return dao.listarTodos();
	}

	public VerbasFolha pesquisarPorChave(Double codVerba) {
		return dao.pesquisarPorChave(codVerba);
	};

	public void salvarOuAtualizar(VerbasFolha objeto) {
		if (dao.pesquisarPorChave(objeto.getCodVerba()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
		reatualizarEtapaDoProcesso();
	}

	public void remover(VerbasFolha objeto) {
		dao.deletarPorChave(objeto.getCodVerba());
		reatualizarEtapaDoProcesso();
	}
	

	public Integer contarVerbasSemDefinicao() {
		return dao.contarVerbasSemDefinicao();
	}
	public void atualizarNovos() {
		try {
			lerParametros(false);
			dao.atualizarNovos(defaultImportar, defaultConsiderarReferencia);
		} catch (ParametroInvalidoException e) {
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. Atualizar VerbaFolha", e.getMessage(),AlertType.ERROR);
		}
	}

	public void gerarTxt(Boolean oficial) {
		try {
			lerParametros(oficial);
			List<VerbasFolha> lista = pesquisarTodos();
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
				bw.write("Codverba,DescricaoVerba,TipoVerba, ConsiderarReferencia, FlagImportar");
				bw.newLine();
				for (VerbasFolha verbaFolha : lista) {
					String linha = String.format("%.0f", verbaFolha.getCodVerba()) + "," + 
								   verbaFolha.getDescVerba() + "," + 
								   verbaFolha.getTipoVerba() + "," +
								   verbaFolha.getConsiderarReferencia() + "," +
								   verbaFolha.getImportar(); 
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
			Alertas.mostrarAlertas("Erro no Cadastro de Parametros", "Processo Cancelado. VerbasFolha", e.getMessage(),AlertType.ERROR);
		}
	}

	private void lerParametros(Boolean oficial) {
		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaPasta")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + "VerbasDaFolha" + anoMes + "_oficial" + arqSaidaTipo ;
		}
		else {
			saida = arqSaidaPasta + "VerbasDaFolha" + anoMes + arqSaidaTipo ;
		}
		defaultImportar = (parametrosService.pesquisarPorChave("VerbasDaFolha", "DefaultImportar")).getValor().toUpperCase();
		defaultConsiderarReferencia = (parametrosService.pesquisarPorChave("VerbasDaFolha", "DefaultConsiderarReferencia")).getValor().toUpperCase();
	}
	
	private void reatualizarEtapaDoProcesso() {
		processoAtualService.atualizarEtapa("ImportarFolha","N");
		processoAtualService.atualizarEtapa("SumarizarFolha","N");
		processoAtualService.atualizarEtapa("ExportarFolha","N");
	}
}
