package model.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.entities.Funcionarios;
import model.entities.FuncionariosSumarizados;

public class SumarizarFuncionariosService {
	
	private ParametrosService parametrosService = new ParametrosService();
	private FuncionariosSumarizadosService funcionariosSumarizadosService = new FuncionariosSumarizadosService();
	private FuncionariosService funcionariosService = new FuncionariosService();
	private ProcessoAtualService processoAtualService = new ProcessoAtualService();

//	Parametros
	String anoMes;

	List<Funcionarios> lista;
	Map<Double, FuncionariosSumarizados> map;
	Integer qtdeLidas = 0;
	Integer qtdeCCustos = 0;

	public List<Funcionarios> getLista() {
		return lista;
	}
	public Map<Double, FuncionariosSumarizados> getMap() {
		return map;
	}
	public Integer getQtdeLidas() {
		return qtdeLidas;
	}
	public Integer getQtdeCCustos() {
		return qtdeCCustos;
	}

	public void processar() {
		lerParametros();
		deletarTodosSumarioFuncionarios();
		sumarizarFuncionarios();
		gravarSumarioFuncionarios();
		processoAtualService.atualizarEtapa("SumarizarFuncionario","S");
		processoAtualService.atualizarEtapa("FuncionarioAlterado","N");
	}

	private void deletarTodosSumarioFuncionarios() {
		funcionariosSumarizadosService.deletarTodos();
	}
	
	private void sumarizarFuncionarios() {
		lista = funcionariosService.pesquisarTodos();
		Double codCCusto;
		map = new HashMap<Double, FuncionariosSumarizados>();
		for (Funcionarios funcionarios : lista) {
			qtdeLidas = qtdeLidas + 1;

			codCCusto = funcionarios.getCodCentroCustos();

			FuncionariosSumarizados sumarioFuncionarios = new FuncionariosSumarizados();
			if (map.containsKey(codCCusto)) {
				sumarioFuncionarios = map.get(codCCusto);
			} else {
				sumarioFuncionarios.setAnoMes(funcionarios.getAnoMes());
				sumarioFuncionarios.setCodCentroCustos(funcionarios.getCodCentroCustos());
				sumarioFuncionarios.setDescCentroCustos(funcionarios.getDescCentroCustos());
				sumarioFuncionarios.setQdteFuncionarios(0);
				qtdeCCustos = qtdeCCustos + 1;
			}

			sumarioFuncionarios.setQdteFuncionarios(sumarioFuncionarios.getQdteFuncionarios() + 1);
			map.put(codCCusto, sumarioFuncionarios);
		}
	}

	private void gravarSumarioFuncionarios() {
		for (Double chave : map.keySet()) {
			funcionariosSumarizadosService.salvarOuAtualizar(map.get(chave));
		}
	}

	private void lerParametros() {
		anoMes = (parametrosService.pesquisarPorChave("ControleProcesso", "AnoMes")).getValor();
	}
}
