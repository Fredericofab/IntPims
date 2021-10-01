package model.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gui.util.Alertas;
import javafx.scene.control.Alert.AlertType;
import model.dao.ParametrosDao;
import model.dao.FabricaDeDao;
import model.entities.Parametros;

public class ParametrosService {

	private ParametrosDao dao = FabricaDeDao.criarParametrosDao();


//  parametros
	String saida;
	
	public List<Parametros> pesquisarTodos() {
		return dao.listarTodos();
	}

	public Parametros pesquisarPorChave(String secao, String entrada) {
		return dao.pesquisarPorChave(secao,entrada);
	};

	public void salvarOuAtualizar(Parametros objeto) {
		if (dao.pesquisarPorChave(objeto.getSecao(),objeto.getEntrada()) == null) {
			dao.inserir(objeto);
		} else {
			dao.atualizar(objeto);
		}
	}

	public void remover(Parametros objeto) {
		dao.deletarPorChave(objeto.getSecao(),objeto.getEntrada());
	}

	public void gerarTxt() {
		lerParametros();
		List<Parametros> lista = pesquisarTodos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(saida))) {
			bw.write("Secao,Entada,Valor,Descricao");
			bw.newLine();
			for (Parametros parametros : lista) {
				String linha = parametros.getSecao() + "," + parametros.getEntrada() + "," + parametros.getValor() + "," + parametros.getDescricao(); 
				bw.write(linha);
				bw.newLine();
			}
			Alertas.mostrarAlertas(null, "Arquivo Gravado com Sucesso", saida , AlertType.INFORMATION);
		} catch (IOException e) {
			Alertas.mostrarAlertas("IOException", "Erro na Gravacao do Arquivo TXT", e.getMessage(), AlertType.ERROR);
		}
	}

	private void lerParametros() {
		ParametrosService parametrosService = new ParametrosService();

		String anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
		String arqSaidaPasta = (parametrosService.pesquisarPorChave("Parametros", "ArqSaidaPasta")).getValor();
		String arqSaidaNome  = (parametrosService.pesquisarPorChave("Parametros", "ArqSaidaNome")).getValor();
		String arqSaidaTipo  = (parametrosService.pesquisarPorChave("Parametros", "ArqSaidaTipo")).getValor();
		saida = arqSaidaPasta + arqSaidaNome + anoMes + arqSaidaTipo ;
	}
}
