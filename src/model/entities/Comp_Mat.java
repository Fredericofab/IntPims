package model.entities;

public class Comp_Mat {
	
	private Double cdCompo;
	private String cdMatIni;
	private String cdMatFim;
	private String instancia;
	private Double rowVersion;

	public Comp_Mat() {
		super();
	}
	public Comp_Mat(Double cdCompo, String cdMatIni, String cdMatFim, String instancia, Double rowVersion) {
		super();
		this.cdCompo = cdCompo;
		this.cdMatIni = cdMatIni;
		this.cdMatFim = cdMatFim;
		this.instancia = instancia;
		this.rowVersion = rowVersion;
	}
	
	public Double getCdCompo() {
		return cdCompo;
	}
	public void setCdCompo(Double cdCompo) {
		this.cdCompo = cdCompo;
	}
	public String getCdMatIni() {
		return cdMatIni;
	}
	public void setCdMatIni(String cdMatIni) {
		this.cdMatIni = cdMatIni;
	}
	public String getCdMatFim() {
		return cdMatFim;
	}
	public void setCdMatFim(String cdMatFim) {
		this.cdMatFim = cdMatFim;
	}
	public String getInstancia() {
		return instancia;
	}
	public void setInstancia(String instancia) {
		this.instancia = instancia;
	}
	public Double getRowVersion() {
		return rowVersion;
	}
	public void setRowVersion(Double rowVersion) {
		this.rowVersion = rowVersion;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdCompo == null) ? 0 : cdCompo.hashCode());
		result = prime * result + ((cdMatFim == null) ? 0 : cdMatFim.hashCode());
		result = prime * result + ((cdMatIni == null) ? 0 : cdMatIni.hashCode());
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
		Comp_Mat other = (Comp_Mat) obj;
		if (cdCompo == null) {
			if (other.cdCompo != null)
				return false;
		} else if (!cdCompo.equals(other.cdCompo))
			return false;
		if (cdMatFim == null) {
			if (other.cdMatFim != null)
				return false;
		} else if (!cdMatFim.equals(other.cdMatFim))
			return false;
		if (cdMatIni == null) {
			if (other.cdMatIni != null)
				return false;
		} else if (!cdMatIni.equals(other.cdMatIni))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Comp_Mat [cdCompo=" + cdCompo + ", cdMatIni=" + cdMatIni + ", cdMatFim=" + cdMatFim + ", instancia="
				+ instancia + ", rowVersion=" + rowVersion + "]";
	}
}
