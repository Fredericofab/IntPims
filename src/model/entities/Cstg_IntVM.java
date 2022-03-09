package model.entities;

import java.util.Date;

public class Cstg_IntVM {

	private String cdEmpresa;
	private Date dtRefer;
	private String cdMater;
	private Double precoUnit;
	private String instancia;

	public Cstg_IntVM() {
		super();
	}
	public Cstg_IntVM(String cdEmpresa, Date dtRefer, String cdMater, Double precoUnit, String instancia) {
		super();
		this.cdEmpresa = cdEmpresa;
		this.dtRefer = dtRefer;
		this.cdMater = cdMater;
		this.precoUnit = precoUnit;
		this.instancia = instancia;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}
	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
	public Date getDtRefer() {
		return dtRefer;
	}
	public void setDtRefer(Date dtRefer) {
		this.dtRefer = dtRefer;
	}
	public String getCdMater() {
		return cdMater;
	}
	public void setCdMater(String cdMater) {
		this.cdMater = cdMater;
	}
	public Double getPrecoUnit() {
		return precoUnit;
	}
	public void setPrecoUnit(Double precoUnit) {
		this.precoUnit = precoUnit;
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
		result = prime * result + ((cdMater == null) ? 0 : cdMater.hashCode());
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
		Cstg_IntVM other = (Cstg_IntVM) obj;
		if (cdEmpresa == null) {
			if (other.cdEmpresa != null)
				return false;
		} else if (!cdEmpresa.equals(other.cdEmpresa))
			return false;
		if (cdMater == null) {
			if (other.cdMater != null)
				return false;
		} else if (!cdMater.equals(other.cdMater))
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
		return "Cstg_IntVM [cdEmpresa=" + cdEmpresa + ", dtRefer=" + dtRefer + ", cdMater=" + cdMater + ", precoUnit="
				+ precoUnit + ", instancia=" + instancia + "]";
	}
}
