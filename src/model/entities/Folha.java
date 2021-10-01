package model.entities;

import java.io.Serializable;

public class Folha implements Serializable {

	private static final long serialVersionUID = 1L;

	private String anoMes;
	private Double codCentroCustos;
	private String descCentroCustos;
	private Double codVerba;
	private String descVerba;
	private Double valorVerba;
	private String importar;
	private String observacao;

	public Folha() {
	}

	public Folha(String anoMes, Double codCentroCustos, String descCentroCustos, Double codVerba, String descVerba,
			Double valorVerba, String importar, String observacao) {
		super();
		this.anoMes = anoMes;
		this.codCentroCustos = codCentroCustos;
		this.descCentroCustos = descCentroCustos;
		this.codVerba = codVerba;
		this.descVerba = descVerba;
		this.valorVerba = valorVerba;
		this.importar = importar;
		this.observacao = observacao;
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

	public Double getValorVerba() {
		return valorVerba;
	}

	public void setValorVerba(Double valorVerba) {
		this.valorVerba = valorVerba;
	}

	public String getImportar() {
		return importar;
	}

	public void setImportar(String importar) {
		this.importar = importar;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anoMes == null) ? 0 : anoMes.hashCode());
		result = prime * result + ((codCentroCustos == null) ? 0 : codCentroCustos.hashCode());
		result = prime * result + ((codVerba == null) ? 0 : codVerba.hashCode());
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
		Folha other = (Folha) obj;
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
		if (codVerba == null) {
			if (other.codVerba != null)
				return false;
		} else if (!codVerba.equals(other.codVerba))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DadosFolha [anoMes=" + anoMes + ", codCentroCusto=" + codCentroCustos + ", descCentroCusto="
				+ descCentroCustos + ", codVerba=" + codVerba + ", descVerba=" + descVerba + ", valorVerba="
				+ valorVerba + ", importar=" + importar + ",  observacao=" 	+ observacao + "]";
	}
}