package model.entities;

import java.io.Serializable;

public class VerbasFolha implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double codVerba;
	private String descVerba;
	private String importar;

	public VerbasFolha() {
	}

	public VerbasFolha(Double codVerba, String descVerba, String importar) {
		super();
		this.codVerba = codVerba;
		this.descVerba = descVerba;
		this.importar = importar;
	}


	public Double getCodVerba() {
		return codVerba;
	}

	public void setCodVerba(Double codVerba) {
		this.codVerba = codVerba;
	}

	public String getDescVerba() {
		return descVerba;
	}

	public void setDescVerba(String descVerba) {
		this.descVerba = descVerba;
	}

	public String getImportar() {
		return importar;
	}

	public void setImportar(String importar) {
		this.importar = importar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codVerba == null) ? 0 : codVerba.hashCode());
		result = prime * result + ((descVerba == null) ? 0 : descVerba.hashCode());
		result = prime * result + ((importar == null) ? 0 : importar.hashCode());
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
		VerbasFolha other = (VerbasFolha) obj;
		if (codVerba == null) {
			if (other.codVerba != null)
				return false;
		} else if (!codVerba.equals(other.codVerba))
			return false;
		if (descVerba == null) {
			if (other.descVerba != null)
				return false;
		} else if (!descVerba.equals(other.descVerba))
			return false;
		if (importar == null) {
			if (other.importar != null)
				return false;
		} else if (!importar.equals(other.importar))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VerbaFolha [codVerba=" + codVerba + ", descVerba=" + descVerba + ", importar=" + importar + "]";
	}


}