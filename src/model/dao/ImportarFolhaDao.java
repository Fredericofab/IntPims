package model.dao;

public interface ImportarFolhaDao {
	
	Integer contarVerbasSemDefinicao();
	Integer deletarDadosFolhaAnoMes(String anoMes);
	Integer deletarSumarioFolhaAnoMes(String anoMes);

}
