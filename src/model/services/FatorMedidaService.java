package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import gui.util.Utilitarios;
import javafx.scene.control.Alert.AlertType;
import model.dao.FatorMedidaDao;
import model.dao.FabricaDeDao;
import model.entities.FatorMedida;

public class FatorMedidaService {

	private FatorMedidaDao dao = FabricaDeDao.criarFatorMedidaDao();

	private ParametrosService parametrosService = new ParametrosService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	parametros
	String saida;
	String defaultFatorDivisao;

	public List<FatorMedida> pesquisarTodos() {
		return dao.listarTodos();
	}

	public FatorMedida pesquisarPorChave(String codMaterial) {
		return dao.pesquisarPorChave(codMaterial);
	};

	public void salvarOuAtualizar(FatorMedida objeto) {
		if (dao.pesquisarPorChave(objeto.getCodMaterial()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
		reatualizarEtapaDoProcesso();
	}

	public void remover(FatorMedida objeto) {
		dao.deletarPorChave(objeto.getCodMaterial());
		reatualizarEtapaDoProcesso();
	}

	public void atualizarNovos() {
		lerParametros(false);
		Double fator = Utilitarios.tentarConverterParaDouble(defaultFatorDivisao);
		if (fator != null) {
			dao.atualizarNovos(fator);
		}
		else {
			Alertas.mostrarAlertas("Erro de Integridade no Parametro",
					"Parametro: Secao = FatorMedida, Entrada =  DefaultFatorDivisao",
					"O Parametro não é um numero válido. \n \n" + 
					"Informe SEM vírgula e com PONTO decimal", AlertType.ERROR);
		}	
	}

	public void gerarTxt(Boolean oficial) {
		lerParametros(oficial);
		List<FatorMedida> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("CodMaterial,DescricaoMaterial,UnidMed, FatorDivisao");
			bw.newLine();
			for (FatorMedida fatorMedida : lista) {
				String linha = fatorMedida.getCodMaterial() + "," + fatorMedida.getDescMaterial() + ","
						+ fatorMedida.getUnidadeMedida() + "," + String.format("%.4f", fatorMedida.getFatorDivisao());
				bw.write(linha);
				bw.newLine();
			}
			if (!oficial) {
				Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida, AlertType.INFORMATION);
			}
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
		}
	}

	private void lerParametros(Boolean oficial) {
		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaPasta")).getValor();
		String arqSaidaTipo = (parametrosService.pesquisarPorChave("ArquivosTextos", "ArqSaidaTipo")).getValor();
		if (oficial) {
			saida = arqSaidaPasta + "FatorMedida" + anoMes + "_oficial" + arqSaidaTipo;
		} else {
			saida = arqSaidaPasta + "FatorMedida" + anoMes + arqSaidaTipo;
		}
		defaultFatorDivisao = (parametrosService.pesquisarPorChave("FatorMedida", "DefaultFatorDivisao")).getValor().toUpperCase();
	}

	private void reatualizarEtapaDoProcesso() {
		processoAtualService.atualizarEtapa("ValidarErp", "N");
		processoAtualService.atualizarEtapa("AplicarPoliticaErp", "N");
		processoAtualService.atualizarEtapa("ExportarErp", "N");
	}
}
