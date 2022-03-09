package model.entities;

import java.util.Date;

public class Cstg_IntDG {

	private String cdEmpresa;
	private Date dtRefer;
	private Double cdCCusto;
	private String cdCtaCon;
	private Double qtValor;
	private String instancia;

	public Cstg_IntDG() {
		super();
	}
	public Cstg_IntDG(String cdEmpresa, Date dtRefer, Double cdCCusto, String cdCtaCon, Double qtValor,
			String instancia) {
		super();
		this.cdEmpresa = cdEmpresa;
		this.dtRefer = dtRefer;
		this.cdCCusto = cdCCusto;
		this.cdCtaCon = cdCtaCon;
		this.qtValor = qtValor;
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
	public Double getCdCCusto() {
		return cdCCusto;
	}
	public void setCdCCusto(Double cdCCusto) {
		this.cdCCusto = cdCCusto;
	}
	public String getCdCtaCon() {
		return cdCtaCon;
	}
	public void setCdCtaCon(String cdCtaCon) {
		this.cdCtaCon = cdCtaCon;
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
		result = prime * result + ((cdCCusto == null) ? 0 : cdCCusto.hashCode());
		result = prime * result + ((cdCtaCon == null) ? 0 : cdCtaCon.hashCode());
		result = prime * result + ((cdEmpresa == null) ? 0 : cdEmpresa.hashCode());
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
		Cstg_IntDG other = (Cstg_IntDG) obj;
		if (cdCCusto == null) {
			if (other.cdCCusto != null)
				return false;
		} else if (!cdCCusto.equals(other.cdCCusto))
			return false;
		if (cdCtaCon == null) {
			if (other.cdCtaCon != null)
				return false;
		} else if (!cdCtaCon.equals(other.cdCtaCon))
			return false;
		if (cdEmpresa == null) {
			if (other.cdEmpresa != null)
				return false;
		} else if (!cdEmpresa.equals(other.cdEmpresa))
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
		return "Cstg_intDG [cdEmpresa=" + cdEmpresa + ", dtRefer=" + dtRefer + ", cdCCusto=" + cdCCusto + ", cdCtaCon="
				+ cdCtaCon + ", qtValor=" + qtValor + ", instancia=" + instancia + "]";
	}
}
