package model.entities;

public class PlcPrim {

	private Double cdCCusto;
	private String cdConta;
	private String fgTpCta;
	private Double vlDire;

	public PlcPrim() {
		super();
	}
	public PlcPrim(Double cdCCusto, String cdConta, String fgTpCta, Double vlDire) {
		super();
		this.cdCCusto = cdCCusto;
		this.cdConta = cdConta;
		this.fgTpCta = fgTpCta;
		this.vlDire = vlDire;
	}
	
	public Double getCdCCusto() {
		return cdCCusto;
	}
	public void setCdCCusto(Double cdCCusto) {
		this.cdCCusto = cdCCusto;
	}
	public String getCdConta() {
		return cdConta;
	}
	public void setCdConta(String cdConta) {
		this.cdConta = cdConta;
	}
	public String getFgTpCta() {
		return fgTpCta;
	}
	public void setFgTpCta(String fgTpCta) {
		this.fgTpCta = fgTpCta;
	}
	public Double getVlDire() {
		return vlDire;
	}
	public void setVlDire(Double vlDire) {
		this.vlDire = vlDire;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdCCusto == null) ? 0 : cdCCusto.hashCode());
		result = prime * result + ((cdConta == null) ? 0 : cdConta.hashCode());
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
		PlcPrim other = (PlcPrim) obj;
		if (cdCCusto == null) {
			if (other.cdCCusto != null)
				return false;
		} else if (!cdCCusto.equals(other.cdCCusto))
			return false;
		if (cdConta == null) {
			if (other.cdConta != null)
				return false;
		} else if (!cdConta.equals(other.cdConta))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PlcPRIM [cdCCusto=" + cdCCusto + ", cdConta=" + cdConta + ", fgTpCta=" + fgTpCta + ", vlDire=" + vlDire
				+ "]";
	}
}
