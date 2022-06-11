package model.entities;

public class DadosContabil {

	private Double ccComprador;
	private String cdConta;
	private String fgTpCta;
	private String nivel;
	private Double ccVendedorPrimeiro;
	private Double ccVendedorUltimo;
	private Double taxa;
	private Double valor;

	public DadosContabil() {
		super();
	}
	public DadosContabil(Double ccComprador, String cdConta, String fgTpCta, String nivel, Double ccVendedorPrimeiro,
			Double ccVendedorUltimo, Double taxa, Double valor) {
		super();
		this.ccComprador = ccComprador;
		this.cdConta = cdConta;
		this.fgTpCta = fgTpCta;
		this.nivel = nivel;
		this.ccVendedorPrimeiro = ccVendedorPrimeiro;
		this.ccVendedorUltimo = ccVendedorUltimo;
		this.taxa = taxa;
		this.valor = valor;
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
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public Double getCcVendedorPrimeiro() {
		return ccVendedorPrimeiro;
	}
	public void setCcVendedorPrimeiro(Double ccVendedorPrimeiro) {
		this.ccVendedorPrimeiro = ccVendedorPrimeiro;
	}
	public Double getCcVendedorUltimo() {
		return ccVendedorUltimo;
	}
	public void setCcVendedorUltimo(Double ccVendedorUltimo) {
		this.ccVendedorUltimo = ccVendedorUltimo;
	}
	public Double getTaxa() {
		return taxa;
	}
	public void setTaxa(Double taxa) {
		this.taxa = taxa;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		return "DadosContabil [ccComprador=" + String.format("%.0f",ccComprador) 
				+ ", cdConta=" + cdConta + ", fgTpCta=" + fgTpCta + ", nivel=" + nivel
				+ ", ccVendedorPrimeiro=" + String.format("%.0f",ccVendedorPrimeiro) 
				+ ", ccVendedorUltimo="   + String.format("%.0f",ccVendedorUltimo) 
				+ ", taxa=" + taxa + ", valor=" + valor + "]";
	}
}
