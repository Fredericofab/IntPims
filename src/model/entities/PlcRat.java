package model.entities;

public class PlcRat {

	private Double cdCCPara;
	private String fgFxVr;
	private Double ccCCDe;
	private Double vlTaxa;
	private Double vlConsum;
	
	public PlcRat() {
		super();
	}
	public PlcRat(Double cdCCPara, String fgFxVr, Double ccCCDe, Double vlTaxa, Double vlConsum) {
		super();
		this.cdCCPara = cdCCPara;
		this.fgFxVr = fgFxVr;
		this.ccCCDe = ccCCDe;
		this.vlTaxa = vlTaxa;
		this.vlConsum = vlConsum;
	}
	
	public Double getCdCCPara() {
		return cdCCPara;
	}
	public void setCdCCPara(Double cdCCPara) {
		this.cdCCPara = cdCCPara;
	}
	public String getFgFxVr() {
		return fgFxVr;
	}
	public void setFgFxVr(String fgFxVr) {
		this.fgFxVr = fgFxVr;
	}
	public Double getCcCCDe() {
		return ccCCDe;
	}
	public void setCcCCDe(Double ccCCDe) {
		this.ccCCDe = ccCCDe;
	}
	public Double getVlTaxa() {
		return vlTaxa;
	}
	public void setVlTaxa(Double vlTaxa) {
		this.vlTaxa = vlTaxa;
	}
	public Double getVlConsum() {
		return vlConsum;
	}
	public void setVlConsum(Double vlConsum) {
		this.vlConsum = vlConsum;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccCCDe == null) ? 0 : ccCCDe.hashCode());
		result = prime * result + ((cdCCPara == null) ? 0 : cdCCPara.hashCode());
		result = prime * result + ((fgFxVr == null) ? 0 : fgFxVr.hashCode());
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
		PlcRat other = (PlcRat) obj;
		if (ccCCDe == null) {
			if (other.ccCCDe != null)
				return false;
		} else if (!ccCCDe.equals(other.ccCCDe))
			return false;
		if (cdCCPara == null) {
			if (other.cdCCPara != null)
				return false;
		} else if (!cdCCPara.equals(other.cdCCPara))
			return false;
		if (fgFxVr == null) {
			if (other.fgFxVr != null)
				return false;
		} else if (!fgFxVr.equals(other.fgFxVr))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PlcRAT [cdCCPara=" + cdCCPara + ", fgFxVr=" + fgFxVr + ", ccCCDe=" + ccCCDe + ", vlTaxa=" + vlTaxa
				+ ", vlConsum=" + vlConsum + "]";
	}
}
