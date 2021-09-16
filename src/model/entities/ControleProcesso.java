package model.entities;

import java.io.Serializable;

public class ControleProcesso implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String anoMes;
	private String importarFolha;
	private String sumarizarFolha;
	private String exportarFolha;
	private String importarFuncionario;
	private String sumarizarFuncionario;
	private String importarErpMT;
	private String importarErpCD;
	private String importarErpDD;
	private String criticarErp;
	private String exportarErp;
	
	public ControleProcesso() {
	}

	public ControleProcesso(String anoMes, String importarFolha, String sumarizarFolha, String exportarFolha,
			String importarFuncionario, String sumarizarFuncionario, String importarErpMT, String importarErpCD,
			String importarErpDD, String criticarErp, String exportarErp) {
		super();
		this.anoMes = anoMes;
		this.importarFolha = importarFolha;
		this.sumarizarFolha = sumarizarFolha;
		this.exportarFolha = exportarFolha;
		this.importarFuncionario = importarFuncionario;
		this.sumarizarFuncionario = sumarizarFuncionario;
		this.importarErpMT = importarErpMT;
		this.importarErpCD = importarErpCD;
		this.importarErpDD = importarErpDD;
		this.criticarErp = criticarErp;
		this.exportarErp = exportarErp;
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

	public String getImportarErpDD() {
		return importarErpDD;
	}

	public void setImportarErpDD(String importarErpDD) {
		this.importarErpDD = importarErpDD;
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
		ControleProcesso other = (ControleProcesso) obj;
		if (anoMes == null) {
			if (other.anoMes != null)
				return false;
		} else if (!anoMes.equals(other.anoMes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ControleProcesso [anoMes=" + anoMes + ", importarFolha=" + importarFolha + ", sumarizarFolha="
				+ sumarizarFolha + ", exportarFolha=" + exportarFolha + ", importarFuncionario=" + importarFuncionario
				+ ", sumarizarFuncionario=" + sumarizarFuncionario + ", importarErpMT=" + importarErpMT
				+ ", importarErpCD=" + importarErpCD + ", importarErpDD=" + importarErpDD + ", criticarErp="
				+ criticarErp + ", exportarErp=" + exportarErp + "]";
	}
	
}
