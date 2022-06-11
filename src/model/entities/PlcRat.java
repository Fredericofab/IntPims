package model.entities;

public class PlcRat {

	private Double cdCCPara;
	private String fgFxVr;
	private Double cdCCDe;
	private Double vlTaxa;
	private Double vlConsum;
	
	public PlcRat() {
		super();
	}
	public PlcRat(Double cdCCPara, String fgFxVr, Double cdCCDe, Double vlTaxa, Double vlConsum) {
		super();
		this.cdCCPara = cdCCPara;
		this.fgFxVr = fgFxVr;
		this.cdCCDe = cdCCDe;
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
	public Double getCdCCDe() {
		return cdCCDe;
	}
	public void setCdCCDe(Double cdCCDe) {
		this.cdCCDe = cdCCDe;
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
		result = prime * result + ((cdCCDe == null) ? 0 : cdCCDe.hashCode());
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
		if (cdCCDe == null) {
			if (other.cdCCDe != null)
				return false;
		} else if (!cdCCDe.equals(other.cdCCDe))
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
		return "PlcRAT [cdCCPara=" + String.format("%.0f",cdCCPara)  + ", fgFxVr=" + fgFxVr 
				+ ", cdCCDe=" + String.format("%.0f",cdCCDe) + ", vlTaxa=" + vlTaxa
				+ ", vlConsum=" + vlConsum + "]";
	}
}
