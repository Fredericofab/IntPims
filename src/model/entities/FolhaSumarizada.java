package model.entities;

import java.io.Serializable;

public class FolhaSumarizada implements Serializable {

	private static final long serialVersionUID = 1L;

	private String anoMes;
	private Double codCentroCustos;
	private String descCentroCustos;
	private Integer qdteImportarSim;
	private Integer qdteImportarNao;
	private Double totalImportarSim;
	private Double totalImportarNao;
	private Double totalReferenciaSim;

	public FolhaSumarizada() {
	}
	public FolhaSumarizada(String anoMes, Double codCentroCustos, String descCentroCustos, Integer qdteImportarSim,
			Integer qdteImportarNao, Double totalImportarSim, Double totalImportarNao, Double totalReferenciaSim) {
		super();
		this.anoMes = anoMes;
		this.codCentroCustos = codCentroCustos;
		this.descCentroCustos = descCentroCustos;
		this.qdteImportarSim = qdteImportarSim;
		this.qdteImportarNao = qdteImportarNao;
		this.totalImportarSim = totalImportarSim;
		this.totalImportarNao = totalImportarNao;
		this.totalReferenciaSim = totalReferenciaSim;
	}

	public String getAnoMes() {
		return anoMes;
	}
	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
	}
	public Double getCodCentroCustos() {
		return codCentroCustos;
	}
	public void setCodCentroCustos(Double codCentroCustos) {
		this.codCentroCustos = codCentroCustos;
	}
	public String getDescCentroCustos() {
		return descCentroCustos;
	}
	public void setDescCentroCustos(String descCentroCustos) {
		this.descCentroCustos = descCentroCustos;
	}
	public Integer getQdteImportarSim() {
		return qdteImportarSim;
	}
	public void setQdteImportarSim(Integer qdteImportarSim) {
		this.qdteImportarSim = qdteImportarSim;
	}
	public Integer getQdteImportarNao() {
		return qdteImportarNao;
	}
	public void setQdteImportarNao(Integer qdteImportarNao) {
		this.qdteImportarNao = qdteImportarNao;
	}
	public Double getTotalImportarSim() {
		return totalImportarSim;
	}
	public void setTotalImportarSim(Double totalImportarSim) {
		this.totalImportarSim = totalImportarSim;
	}
	public Double getTotalImportarNao() {
		return totalImportarNao;
	}
	public void setTotalImportarNao(Double totalImportarNao) {
		this.totalImportarNao = totalImportarNao;
	}
	public Double getTotalReferenciaSim() {
		return totalReferenciaSim;
	}
	public void setTotalReferenciaSim(Double totalReferenciaSim) {
		this.totalReferenciaSim = totalReferenciaSim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anoMes == null) ? 0 : anoMes.hashCode());
		result = prime * result + ((codCentroCustos == null) ? 0 : codCentroCustos.hashCode());
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
		FolhaSumarizada other = (FolhaSumarizada) obj;
		if (anoMes == null) {
			if (other.anoMes != null)
				return false;
		} else if (!anoMes.equals(other.anoMes))
			return false;
		if (codCentroCustos == null) {
			if (other.codCentroCustos != null)
				return false;
		} else if (!codCentroCustos.equals(other.codCentroCustos))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FolhaSumarizada [anoMes=" + anoMes + ", codCentroCustos=" + codCentroCustos + ", descCentroCustos="
				+ descCentroCustos + ", qdteImportarSim=" + qdteImportarSim + ", qdteImportarNao=" + qdteImportarNao
				+ ", totalImportarSim=" + totalImportarSim + ", totalImportarNao=" + totalImportarNao
				+ ", totalReferenciaSim=" + totalReferenciaSim + "]";
	}

}	