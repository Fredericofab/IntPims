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
	private Double referenciaVerba;
	private String tipoVerba;
	private String importar;
	private String considerarReferencia;
	private String observacao;

	public Folha() {
	}
	public Folha(String anoMes, Double codCentroCustos, String descCentroCustos, Double codVerba, String descVerba,
			Double valorVerba, Double referenciaVerba, String tipoVerba, String importar, String considerarReferencia,
			String observacao) {
		super();
		this.anoMes = anoMes;
		this.codCentroCustos = codCentroCustos;
		this.descCentroCustos = descCentroCustos;
		this.codVerba = codVerba;
		this.descVerba = descVerba;
		this.valorVerba = valorVerba;
		this.referenciaVerba = referenciaVerba;
		this.tipoVerba = tipoVerba;
		this.importar = importar;
		this.considerarReferencia = considerarReferencia;
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
	public Double getReferenciaVerba() {
		return referenciaVerba;
	}
	public void setReferenciaVerba(Double referenciaVerba) {
		this.referenciaVerba = referenciaVerba;
	}
	public String getTipoVerba() {
		return tipoVerba;
	}
	public void setTipoVerba(String tipoVerba) {
		this.tipoVerba = tipoVerba;
	}
	public String getImportar() {
		return importar;
	}
	public void setImportar(String importar) {
		this.importar = importar;
	}
	public String getConsiderarReferencia() {
		return considerarReferencia;
	}
	public void setConsiderarReferencia(String considerarReferencia) {
		this.considerarReferencia = considerarReferencia;
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
		return "Folha [anoMes=" + anoMes + ", codCentroCustos=" + codCentroCustos + ", descCentroCustos="
				+ descCentroCustos + ", codVerba=" + codVerba + ", descVerba=" + descVerba + ", valorVerba="
				+ valorVerba + ", referenciaVerba=" + referenciaVerba + ", tipoVerba=" + tipoVerba + ", importar="
				+ importar + ", considerarReferencia=" + considerarReferencia + ", observacao=" + observacao + "]";
	}
}