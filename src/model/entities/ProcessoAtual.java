package model.entities;

import java.io.Serializable;

public class ProcessoAtual implements Serializable {

	private static final long serialVersionUID = 1L;

	private String anoMes;
	private String importarFolha;
	private String sumarizarFolha;
	private String exportarFolha;
	private String importarFuncionario;
	private String sumarizarFuncionario;
	private String importarErpRM;
	private String importarErpED;
	private String importarErpDF;
	private String validarErp;
	private String aplicarPoliticaErp;
	private String exportarErpVM;
	private String exportarErpCM;
	private String exportarErpDG;
	private String exportarErpOS;
	private String atualizarCompo;
	private String filtroErp;

	public ProcessoAtual() {
	}
	public ProcessoAtual(String anoMes, String importarFolha, String sumarizarFolha, String exportarFolha,
			String importarFuncionario, String sumarizarFuncionario, String importarErpRM, String importarErpED,
			String importarErpDF, String validarErp, String aplicarPoliticaErp, String exportarErpVM,
			String exportarErpCM, String exportarErpDG, String exportarErpOS, String atualizarCompo, String filtroErp) {
		super();
		this.anoMes = anoMes;
		this.importarFolha = importarFolha;
		this.sumarizarFolha = sumarizarFolha;
		this.exportarFolha = exportarFolha;
		this.importarFuncionario = importarFuncionario;
		this.sumarizarFuncionario = sumarizarFuncionario;
		this.importarErpRM = importarErpRM;
		this.importarErpED = importarErpED;
		this.importarErpDF = importarErpDF;
		this.validarErp = validarErp;
		this.aplicarPoliticaErp = aplicarPoliticaErp;
		this.exportarErpVM = exportarErpVM;
		this.exportarErpCM = exportarErpCM;
		this.exportarErpDG = exportarErpDG;
		this.exportarErpOS = exportarErpOS;
		this.atualizarCompo = atualizarCompo;
		this.filtroErp = filtroErp;
	}

	public String getAnoMes() {
		return anoMes;
	}
	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
	}
	public String getImportarFolha() {
		return importarFolha;
	}
	public void setImportarFolha(String importarFolha) {
		this.importarFolha = importarFolha;
	}
	public String getSumarizarFolha() {
		return sumarizarFolha;
	}
	public void setSumarizarFolha(String sumarizarFolha) {
		this.sumarizarFolha = sumarizarFolha;
	}
	public String getExportarFolha() {
		return exportarFolha;
	}
	public void setExportarFolha(String exportarFolha) {
		this.exportarFolha = exportarFolha;
	}
	public String getImportarFuncionario() {
		return importarFuncionario;
	}
	public void setImportarFuncionario(String importarFuncionario) {
		this.importarFuncionario = importarFuncionario;
	}
	public String getSumarizarFuncionario() {
		return sumarizarFuncionario;
	}
	public void setSumarizarFuncionario(String sumarizarFuncionario) {
		this.sumarizarFuncionario = sumarizarFuncionario;
	}
	public String getImportarErpRM() {
		return importarErpRM;
	}
	public void setImportarErpRM(String importarErpRM) {
		this.importarErpRM = importarErpRM;
	}
	public String getImportarErpED() {
		return importarErpED;
	}
	public void setImportarErpED(String importarErpED) {
		this.importarErpED = importarErpED;
	}
	public String getImportarErpDF() {
		return importarErpDF;
	}
	public void setImportarErpDF(String importarErpDF) {
		this.importarErpDF = importarErpDF;
	}
	public String getValidarErp() {
		return validarErp;
	}
	public void setValidarErp(String validarErp) {
		this.validarErp = validarErp;
	}
	public String getAplicarPoliticaErp() {
		return aplicarPoliticaErp;
	}
	public void setAplicarPoliticaErp(String aplicarPoliticaErp) {
		this.aplicarPoliticaErp = aplicarPoliticaErp;
	}
	public String getExportarErpVM() {
		return exportarErpVM;
	}
	public void setExportarErpVM(String exportarErpVM) {
		this.exportarErpVM = exportarErpVM;
	}
	public String getExportarErpCM() {
		return exportarErpCM;
	}
	public void setExportarErpCM(String exportarErpCM) {
		this.exportarErpCM = exportarErpCM;
	}
	public String getExportarErpDG() {
		return exportarErpDG;
	}
	public void setExportarErpDG(String exportarErpDG) {
		this.exportarErpDG = exportarErpDG;
	}
	public String getExportarErpOS() {
		return exportarErpOS;
	}
	public void setExportarErpOS(String exportarErpOS) {
		this.exportarErpOS = exportarErpOS;
	}
	public String getAtualizarCompo() {
		return atualizarCompo;
	}
	public void setAtualizarCompo(String atualizarCompo) {
		this.atualizarCompo = atualizarCompo;
	}
	public String getFiltroErp() {
		return filtroErp;
	}
	public void setFiltroErp(String filtroErp) {
		this.filtroErp = filtroErp;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anoMes == null) ? 0 : anoMes.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessoAtual other = (ProcessoAtual) obj;
		if (anoMes == null) {
			if (other.anoMes != null)
				return false;
		} else if (!anoMes.equals(other.anoMes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessoAtual [anoMes=" + anoMes + ", importarFolha=" + importarFolha + ", sumarizarFolha="
				+ sumarizarFolha + ", exportarFolha=" + exportarFolha + ", importarFuncionario=" + importarFuncionario
				+ ", sumarizarFuncionario=" + sumarizarFuncionario + ", importarErpRM=" + importarErpRM
				+ ", importarErpED=" + importarErpED + ", importarErpDF=" + importarErpDF + ", validarErp=" + validarErp
				+ ", aplicarPoliticaErp=" + aplicarPoliticaErp + ", exportarErpVM=" + exportarErpVM + ", exportarErpCM="
				+ exportarErpCM + ", exportarErpDG=" + exportarErpDG + ", exportarErpOS=" + exportarErpOS
				+ ", atualizarCompo=" + atualizarCompo + ", filtroErp=" + filtroErp + "]";
	}
}
