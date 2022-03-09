package model.entities;

import java.util.Date;

public class OS_Material {
	
	private String noBoletim;
	private Date dtAplicacao;
	private Double cdMaterial;
	private String deMaterial;
	private String noReqExt;
	private Double qtUsada;
	private Double qtValor;
	private String fgCaptado;
	private String cdMatCstg;
	private String instancia;

	public OS_Material() {
		super();
	}
	public OS_Material(String noBoletim, Date dtAplicacao, Double cdMaterial, String deMaterial, String noReqExt,
			Double qtUsada, Double qtValor, String fgCaptado, String cdMatCstg, String instancia) {
		super();
		this.noBoletim = noBoletim;
		this.dtAplicacao = dtAplicacao;
		this.cdMaterial = cdMaterial;
		this.deMaterial = deMaterial;
		this.noReqExt = noReqExt;
		this.qtUsada = qtUsada;
		this.qtValor = qtValor;
		this.fgCaptado = fgCaptado;
		this.cdMatCstg = cdMatCstg;
		this.instancia = instancia;
	}
	
	public String getNoBoletim() {
		return noBoletim;
	}
	public void setNoBoletim(String noBoletim) {
		this.noBoletim = noBoletim;
	}
	public Date getDtAplicacao() {
		return dtAplicacao;
	}
	public void setDtAplicacao(Date dtAplicacao) {
		this.dtAplicacao = dtAplicacao;
	}
	public Double getCdMaterial() {
		return cdMaterial;
	}
	public void setCdMaterial(Double cdMaterial) {
		this.cdMaterial = cdMaterial;
	}
	public String getDeMaterial() {
		return deMaterial;
	}
	public void setDeMaterial(String deMaterial) {
		this.deMaterial = deMaterial;
	}
	public String getNoReqExt() {
		return noReqExt;
	}
	public void setNoReqExt(String noReqExt) {
		this.noReqExt = noReqExt;
	}
	public Double getQtUsada() {
		return qtUsada;
	}
	public void setQtUsada(Double qtUsada) {
		this.qtUsada = qtUsada;
	}
	public Double getQtValor() {
		return qtValor;
	}
	public void setQtValor(Double qtValor) {
		this.qtValor = qtValor;
	}
	public String getFgCaptado() {
		return fgCaptado;
	}
	public void setFgCaptado(String fgCaptado) {
		this.fgCaptado = fgCaptado;
	}
	public String getCdMatCstg() {
		return cdMatCstg;
	}
	public void setCdMatCstg(String cdMatCstg) {
		this.cdMatCstg = cdMatCstg;
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
		result = prime * result + ((cdMaterial == null) ? 0 : cdMaterial.hashCode());
		result = prime * result + ((dtAplicacao == null) ? 0 : dtAplicacao.hashCode());
		result = prime * result + ((instancia == null) ? 0 : instancia.hashCode());
		result = prime * result + ((noBoletim == null) ? 0 : noBoletim.hashCode());
		result = prime * result + ((noReqExt == null) ? 0 : noReqExt.hashCode());
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
		OS_Material other = (OS_Material) obj;
		if (cdMaterial == null) {
			if (other.cdMaterial != null)
				return false;
		} else if (!cdMaterial.equals(other.cdMaterial))
			return false;
		if (dtAplicacao == null) {
			if (other.dtAplicacao != null)
				return false;
		} else if (!dtAplicacao.equals(other.dtAplicacao))
			return false;
		if (instancia == null) {
			if (other.instancia != null)
				return false;
		} else if (!instancia.equals(other.instancia))
			return false;
		if (noBoletim == null) {
			if (other.noBoletim != null)
				return false;
		} else if (!noBoletim.equals(other.noBoletim))
			return false;
		if (noReqExt == null) {
			if (other.noReqExt != null)
				return false;
		} else if (!noReqExt.equals(other.noReqExt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OS_Material [noBoletim=" + noBoletim + ", dtAplicacao=" + dtAplicacao + ", cdMaterial=" + cdMaterial
				+ ", deMaterial=" + deMaterial + ", noReqExt=" + noReqExt + ", qtUsada=" + qtUsada + ", qtValor="
				+ qtValor + ", fgCaptado=" + fgCaptado + ", cdMatCstg=" + cdMatCstg + ", instancia=" + instancia + "]";
	}
}
