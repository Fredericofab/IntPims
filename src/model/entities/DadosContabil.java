package model.entities;

import java.util.Arrays;

public class DadosContabil {

	private Double ccComprador;
	private String cdConta;
	private String fgTpCta;
	private Double[] ccDe = new Double[11];
	private Double[] taxa = new Double[11];
	private Double taxaAcum;
	private Double valorBase;
	private Double valorAdquirido;

	public DadosContabil() {
		super();
	}
	public DadosContabil(Double ccComprador, String cdConta, String fgTpCta, Double[] ccDe, Double[] taxa,
			Double taxaAcum, Double valorBase, Double valorAdquirido) {
		super();
		this.ccComprador = ccComprador;
		this.cdConta = cdConta;
		this.fgTpCta = fgTpCta;
		this.ccDe = ccDe;
		this.taxa = taxa;
		this.taxaAcum = taxaAcum;
		this.valorBase = valorBase;
		this.valorAdquirido = valorAdquirido;
	}
	public Double getCcComprador() {
		return ccComprador;
	}
	public void setCcComprador(Double ccComprador) {
		this.ccComprador = ccComprador;
	}
	public String getCdConta() {
		return cdConta;
	}
	public void setCdConta(String cdConta) {
		this.cdConta = cdConta;
	}
	public String getFgTpCta() {
		return fgTpCta;
	}
	public void setFgTpCta(String fgTpCta) {
		this.fgTpCta = fgTpCta;
	}
	public Double[] getCcDe() {
		return ccDe;
	}
	public void setCcDe(Double[] ccDe) {
		this.ccDe = ccDe;
	}
	public Double[] getTaxa() {
		return taxa;
	}
	public void setTaxa(Double[] taxa) {
		this.taxa = taxa;
	}
	public Double getTaxaAcum() {
		return taxaAcum;
	}
	public void setTaxaAcum(Double taxaAcum) {
		this.taxaAcum = taxaAcum;
	}
	public Double getValorBase() {
		return valorBase;
	}
	public void setValorBase(Double valorBase) {
		this.valorBase = valorBase;
	}
	public Double getValorAdquirido() {
		return valorAdquirido;
	}
	public void setValorAdquirido(Double valorAdquirido) {
		this.valorAdquirido = valorAdquirido;
	}
	@Override
	public String toString() {
		return "DadosContabil [ccComprador=" + String.format("%.0f",ccComprador) + ", cdConta=" + cdConta + ", fgTpCta=" + fgTpCta + ", ccDe="
				+ Arrays.toString(ccDe) + ", taxa=" + Arrays.toString(taxa) + ", taxaAcum=" + taxaAcum + ", valorBase="
				+ valorBase + ", valorAdquirido=" + valorAdquirido + "]";
	}
}
