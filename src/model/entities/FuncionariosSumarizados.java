package model.entities;

import java.io.Serializable;

public class FuncionariosSumarizados implements Serializable {

	private static final long serialVersionUID = 1L;

	private String anoMes;
	private Double codCentroCustos;
	private String descCentroCustos;
	private Integer qdteFuncionarios;
	
	public FuncionariosSumarizados() {
	}

	public FuncionariosSumarizados(String anoMes, Double codCentroCustos, String descCentroCustos,
			Integer qdteFuncionarios) {
		super();
		this.anoMes = anoMes;
		this.codCentroCustos = codCentroCustos;
		this.descCentroCustos = descCentroCustos;
		this.qdteFuncionarios = qdteFuncionarios;
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
	public Integer getQdteFuncionarios() {
		return qdteFuncionarios;
	}
	public void setQdteFuncionarios(Integer qtdeFuncionarios) {
		this.qdteFuncionarios = qtdeFuncionarios;
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
		FuncionariosSumarizados other = (FuncionariosSumarizados) obj;
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
		return "SumarioFuncionarios [anoMes=" + anoMes + ", codCentroCustos=" + codCentroCustos + ", descCentroCustos="
				+ descCentroCustos + ", qdteFuncionarios=" + qdteFuncionarios + "]";
	}


}	