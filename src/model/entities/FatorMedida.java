package model.entities;

import java.io.Serializable;

public class FatorMedida implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codMaterial;
	private String descMaterial;
	private String unidadeMedida;
	private Double fatorDivisao;
	
	public FatorMedida() {
	}
	public FatorMedida(String codMaterial, String descMaterial, String unidadeMedida, Double fatorDivisao) {
		super();
		this.codMaterial = codMaterial;
		this.descMaterial = descMaterial;
		this.unidadeMedida = unidadeMedida;
		this.fatorDivisao = fatorDivisao;
	}
	public String getCodMaterial() {
		return codMaterial;
	}
	public void setCodMaterial(String codMaterial) {
		this.codMaterial = codMaterial;
	}
	public String getDescMaterial() {
		return descMaterial;
	}
	public void setDescMaterial(String descMaterial) {
		this.descMaterial = descMaterial;
	}
	public String getUnidadeMedida() {
		return unidadeMedida;
	}
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	public Double getFatorDivisao() {
		return fatorDivisao;
	}
	public void setFatorDivisao(Double fatorDivisao) {
		this.fatorDivisao = fatorDivisao;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codMaterial == null) ? 0 : codMaterial.hashCode());
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
		FatorMedida other = (FatorMedida) obj;
		if (codMaterial == null) {
			if (other.codMaterial != null)
				return false;
		} else if (!codMaterial.equals(other.codMaterial))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FatorMedida [codMaterial=" + codMaterial + ", descMaterial=" + descMaterial + ", unidadeMedida="
				+ unidadeMedida + ", fatorDivisao=" + fatorDivisao + "]";
	}
}