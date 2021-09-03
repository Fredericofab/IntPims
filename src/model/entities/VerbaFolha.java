package model.entities;

import java.io.Serializable;

public class VerbaFolha implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codVerba;
	private String descVerba;
	private String importar;

	public VerbaFolha() {
	}

	public VerbaFolha(String codVerba, String descVerba, String importar) {
		super();
		this.codVerba = codVerba;
		this.descVerba = descVerba;
		this.importar = importar;
	}


	public String getCodVerba() {
		return codVerba;
	}

	public void setCodVerba(String codVerba) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		VerbaFolha other = (VerbaFolha) obj;
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