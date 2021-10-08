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
	private String criticarErp;
	private String exportarErp;
	private String verbaAlterada;
	private String folhaAlterada;
	
	public ProcessoAtual() {
	}
	
	public ProcessoAtual(String anoMes, String importarFolha, String sumarizarFolha, String exportarFolha,
			String importarFuncionario, String sumarizarFuncionario, String importarErpMT, String importarErpCD,
			String importarErpDG, String criticarErp, String exportarErp, String verbaAlterada, String folhaAlterada) {
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
		this.criticarErp = criticarErp;
		this.exportarErp = exportarErp;
		this.verbaAlterada = verbaAlterada;
		this.folhaAlterada = folhaAlterada;
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
	public String getCriticarErp() {
		return criticarErp;
	}
	public void setCriticarErp(String criticarErp) {
		this.criticarErp = criticarErp;
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
				+ ", importarErpCD=" + importarErpCD + ", importarErpDG=" + importarErpDG + ", criticarErp="
				+ criticarErp + ", exportarErp=" + exportarErp + ", verbaAlterada=" + verbaAlterada + ", folhaAlterada="
				+ folhaAlterada + "]";
	}
}
