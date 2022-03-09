package model.entities;

public class Cstg_IntFP {
	
	private String cdEmpresa;
	private String dtRefer;
	private Double cdFunc;
	private Double qtHoras;
	private Double qtValor;
	private String instancia;
	
	public Cstg_IntFP() {
	}
	public Cstg_IntFP(String cdEmpresa, String dtRefer, Double cdFunc, Double qtHoras, Double qtValor,
			String instancia) {
		super();
		this.cdEmpresa = cdEmpresa;
		this.dtRefer = dtRefer;
		this.cdFunc = cdFunc;
		this.qtHoras = qtHoras;
		this.qtValor = qtValor;
		this.instancia = instancia;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}
	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
	public String getDtRefer() {
		return dtRefer;
	}
	public void setDtRefer(String dtRefer) {
		this.dtRefer = dtRefer;
	}
	public Double getCdFunc() {
		return cdFunc;
	}
	public void setCdFunc(Double cdFunc) {
		this.cdFunc = cdFunc;
	}
	public Double getQtHoras() {
		return qtHoras;
	}
	public void setQtHoras(Double qtHoras) {
		this.qtHoras = qtHoras;
	}
	public Double getQtValor() {
		return qtValor;
	}
	public void setQtValor(Double qtValor) {
		this.qtValor = qtValor;
	}
	public String getInstancia() {
		return instancia;
	}
	public void setInstancia(String instancia) {
		this.instancia = instancia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdEmpresa == null) ? 0 : cdEmpresa.hashCode());
		result = prime * result + ((cdFunc == null) ? 0 : cdFunc.hashCode());
		result = prime * result + ((dtRefer == null) ? 0 : dtRefer.hashCode());
		result = prime * result + ((instancia == null) ? 0 : instancia.hashCode());
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
		Cstg_IntFP other = (Cstg_IntFP) obj;
		if (cdEmpresa == null) {
			if (other.cdEmpresa != null)
				return false;
		} else if (!cdEmpresa.equals(other.cdEmpresa))
			return false;
		if (cdFunc == null) {
			if (other.cdFunc != null)
				return false;
		} else if (!cdFunc.equals(other.cdFunc))
			return false;
		if (dtRefer == null) {
			if (other.dtRefer != null)
				return false;
		} else if (!dtRefer.equals(other.dtRefer))
			return false;
		if (instancia == null) {
			if (other.instancia != null)
				return false;
		} else if (!instancia.equals(other.instancia))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cstg_IntPF [cdEmpresa=" + cdEmpresa + ", dtRefer=" + dtRefer + ", cdFunc=" + cdFunc + ", qtHoras="
				+ qtHoras + ", qtValor=" + qtValor + ", instancia=" + instancia + "]";
	}
	
	
	

}
