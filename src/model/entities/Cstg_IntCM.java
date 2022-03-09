package model.entities;

import java.util.Date;

public class Cstg_IntCM {

	private String fgOrigem;
	private String cdEmpresa;
	private Date dtRefer;
	private String cdMater;
	private Double cdCCusto;
	private String fgTipo;
	private Double qtMater;
	private Double precoUnit;
	private String deMater;
	private String cdEquipto;
	private String instancia;

	public Cstg_IntCM() {
		super();
	}
	public Cstg_IntCM(String fgOrigem, String cdEmpresa, Date dtRefer, String cdMater, Double cdCCusto, String fgTipo,
			Double qtMater, Double precoUnit, String deMater, String cdEquipto, String instancia) {
		super();
		this.fgOrigem = fgOrigem;
		this.cdEmpresa = cdEmpresa;
		this.dtRefer = dtRefer;
		this.cdMater = cdMater;
		this.cdCCusto = cdCCusto;
		this.fgTipo = fgTipo;
		this.qtMater = qtMater;
		this.precoUnit = precoUnit;
		this.deMater = deMater;
		this.cdEquipto = cdEquipto;
		this.instancia = instancia;
	}

	public String getFgOrigem() {
		return fgOrigem;
	}
	public void setFgOrigem(String fgOrigem) {
		this.fgOrigem = fgOrigem;
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
	public Double getCdCCusto() {
		return cdCCusto;
	}
	public void setCdCCusto(Double cdCCusto) {
		this.cdCCusto = cdCCusto;
	}
	public String getFgTipo() {
		return fgTipo;
	}
	public void setFgTipo(String fgTipo) {
		this.fgTipo = fgTipo;
	}
	public Double getQtMater() {
		return qtMater;
	}
	public void setQtMater(Double qtMater) {
		this.qtMater = qtMater;
	}
	public Double getPrecoUnit() {
		return precoUnit;
	}
	public void setPrecoUnit(Double precoUnit) {
		this.precoUnit = precoUnit;
	}
	public String getDeMater() {
		return deMater;
	}
	public void setDeMater(String deMater) {
		this.deMater = deMater;
	}
	public String getCdEquipto() {
		return cdEquipto;
	}
	public void setCdEquipto(String cdEquipto) {
		this.cdEquipto = cdEquipto;
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
		result = prime * result + ((cdEmpresa == null) ? 0 : cdEmpresa.hashCode());
		result = prime * result + ((cdMater == null) ? 0 : cdMater.hashCode());
		result = prime * result + ((dtRefer == null) ? 0 : dtRefer.hashCode());
		result = prime * result + ((fgOrigem == null) ? 0 : fgOrigem.hashCode());
		result = prime * result + ((fgTipo == null) ? 0 : fgTipo.hashCode());
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
		Cstg_IntCM other = (Cstg_IntCM) obj;
		if (cdCCusto == null) {
			if (other.cdCCusto != null)
				return false;
		} else if (!cdCCusto.equals(other.cdCCusto))
			return false;
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
		if (fgOrigem == null) {
			if (other.fgOrigem != null)
				return false;
		} else if (!fgOrigem.equals(other.fgOrigem))
			return false;
		if (fgTipo == null) {
			if (other.fgTipo != null)
				return false;
		} else if (!fgTipo.equals(other.fgTipo))
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
		return "Cstg_IntCM [fgOrigem=" + fgOrigem + ", cdEmpresa=" + cdEmpresa + ", dtRefer=" + dtRefer + ", cdMater="
				+ cdMater + ", cdCCusto=" + cdCCusto + ", fgTipo=" + fgTipo + ", qtMater=" + qtMater + ", precoUnit="
				+ precoUnit + ", deMater=" + deMater + ", cdEquipto=" + cdEquipto + ", instancia=" + instancia + "]";
	}
}
