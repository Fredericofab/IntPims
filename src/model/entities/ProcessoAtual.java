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
	private String importarErpMT;
	private String importarErpCD;
	private String importarErpDG;
	private String validarErp;
	private String aplicarPoliticaErp;
	private String exportarErp;
	private String verbaAlterada;
	private String folhaAlterada;
	private String funcionarioAlterado;
	private String filtroErp;
	
	public ProcessoAtual() {
	}
	public ProcessoAtual(String anoMes, String importarFolha, String sumarizarFolha, String exportarFolha,
			String importarFuncionario, String sumarizarFuncionario, String importarErpMT, String importarErpCD,
			String importarErpDG, String validarErp, String aplicarPoliticaErp, String exportarErp, String verbaAlterada,
			String folhaAlterada, String funcionarioAlterado, String filtroErp) {
		super();
		this.anoMes = anoMes;
		this.importarFolha = importarFolha;
		this.sumarizarFolha = sumarizarFolha;
		this.exportarFolha = exportarFolha;
		this.importarFuncionario = importarFuncionario;
		this.sumarizarFuncionario = sumarizarFuncionario;
		this.importarErpMT = importarErpMT;
		this.importarErpCD = importarErpCD;
		this.importarErpDG = importarErpDG;
		this.validarErp = validarErp;
		this.aplicarPoliticaErp = aplicarPoliticaErp;
		this.exportarErp = exportarErp;
		this.verbaAlterada = verbaAlterada;
		this.folhaAlterada = folhaAlterada;
		this.funcionarioAlterado = funcionarioAlterado;
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
	public String getImportarErpMT() {
		return importarErpMT;
	}
	public void setImportarErpMT(String importarErpMT) {
		this.importarErpMT = importarErpMT;
	}
	public String getImportarErpCD() {
		return importarErpCD;
	}
	public void setImportarErpCD(String importarErpCD) {
		this.importarErpCD = importarErpCD;
	}
	public String getImportarErpDG() {
		return importarErpDG;
	}
	public void setImportarErpDG(String importarErpDG) {
		this.importarErpDG = importarErpDG;
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
	public String getExportarErp() {
		return exportarErp;
	}
	public void setExportarErp(String exportarErp) {
		this.exportarErp = exportarErp;
	}
	public String getVerbaAlterada() {
		return verbaAlterada;
	}
	public void setVerbaAlterada(String verbaAlterada) {
		this.verbaAlterada = verbaAlterada;
	}
	public String getFolhaAlterada() {
		return folhaAlterada;
	}
	public void setFolhaAlterada(String folhaAlterada) {
		this.folhaAlterada = folhaAlterada;
	}
	public String getFuncionarioAlterado() {
		return funcionarioAlterado;
	}
	public void setFuncionarioAlterado(String funcionarioAlterado) {
		this.funcionarioAlterado = funcionarioAlterado;
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
				+ ", sumarizarFuncionario=" + sumarizarFuncionario + ", importarErpMT=" + importarErpMT
				+ ", importarErpCD=" + importarErpCD + ", importarErpDG=" + importarErpDG + ", validarErp="
				+ validarErp + ", aplicarPoliticaErp=" + aplicarPoliticaErp + ", exportarErp=" + exportarErp + ", verbaAlterada="
				+ verbaAlterada + ", folhaAlterada=" + folhaAlterada + ", funcionarioAlterado=" + funcionarioAlterado
				+ ", filtroErp=" + filtroErp + "]";
	}

}
