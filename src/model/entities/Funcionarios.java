package model.entities;

import java.io.Serializable;

public class Funcionarios implements Serializable {

	private static final long serialVersionUID = 1L;

	private String anoMes;
	private Double codCentroCustos;
	private String descCentroCustos;
	private Double codFuncionario;
	private String descFuncionario;

	public Funcionarios() {
	}

	public Funcionarios(String anoMes, Double codCentroCustos, String descCentroCustos, Double codFuncionario,
			String descFuncionario) {
		super();
		this.anoMes = anoMes;
		this.codCentroCustos = codCentroCustos;
		this.descCentroCustos = descCentroCustos;
		this.codFuncionario = codFuncionario;
		this.descFuncionario = descFuncionario;
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
	public Double getCodFuncionario() {
		return codFuncionario;
	}
	public void setCodFuncionario(Double codFuncionario) {
		this.codFuncionario = codFuncionario;
	}
	public String getDescFuncionario() {
		return descFuncionario;
	}
	public void setDescFuncionario(String descFuncionario) {
		this.descFuncionario = descFuncionario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anoMes == null) ? 0 : anoMes.hashCode());
		result = prime * result + ((codCentroCustos == null) ? 0 : codCentroCustos.hashCode());
		result = prime * result + ((codFuncionario == null) ? 0 : codFuncionario.hashCode());
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
		Funcionarios other = (Funcionarios) obj;
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
		if (codFuncionario == null) {
			if (other.codFuncionario != null)
				return false;
		} else if (!codFuncionario.equals(other.codFuncionario))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Funcionarios [anoMes=" + anoMes + ", codCentroCustos=" + codCentroCustos + ", descCentroCustos="
				+ descCentroCustos + ", codFuncionario=" + codFuncionario + ", descFuncionario=" + descFuncionario
				+ "]";
	}
	
}