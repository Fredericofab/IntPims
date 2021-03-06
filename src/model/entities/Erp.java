package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Erp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String origem;
	private String tipoMovimento;
	private String anoMes;
	private Double codCentroCustos;
	private String descCentroCustos;
	private String codContaContabil;
	private String descContaContabil;
	private String codNatureza;
	private String codMaterial;
	private String descMovimento;
	private String unidadeMedida;
	private Double quantidade;
	private Double precoUnitario;
	private Double valorMovimento;
	private String numeroOS;
	private String frotaOuCC;
	private String documentoErp;
	private Date dataMovimento;
	private String sobreporPoliticas;
	private String importar;
	private String observacao;
	private String validacoesOS;
	private String politicas;
	private String salvarOS_Material;
	private String salvarCstg_IntVM;
	private String salvarCstg_IntCM;
	private String salvarCstg_IntDG;
	private Integer sequencial;

	public Erp() {
	}
	public Erp(String origem, String tipoMovimento, String anoMes, Double codCentroCustos, String descCentroCustos,
			String codContaContabil, String descContaContabil, String codNatureza, String codMaterial,
			String descMovimento, String unidadeMedida, Double quantidade, Double precoUnitario, Double valorMovimento,
			String numeroOS, String frotaOuCC, String documentoErp, Date dataMovimento, String sobreporPoliticas,
			String importar, String observacao, String validacoesOS, String politicas, String salvarOS_Material,
			String salvarCstg_IntVM, String salvarCstg_IntCM, String salvarCstg_IntDG, Integer sequencial) {
		super();
		this.origem = origem;
		this.tipoMovimento = tipoMovimento;
		this.anoMes = anoMes;
		this.codCentroCustos = codCentroCustos;
		this.descCentroCustos = descCentroCustos;
		this.codContaContabil = codContaContabil;
		this.descContaContabil = descContaContabil;
		this.codNatureza = codNatureza;
		this.codMaterial = codMaterial;
		this.descMovimento = descMovimento;
		this.unidadeMedida = unidadeMedida;
		this.quantidade = quantidade;
		this.precoUnitario = precoUnitario;
		this.valorMovimento = valorMovimento;
		this.numeroOS = numeroOS;
		this.frotaOuCC = frotaOuCC;
		this.documentoErp = documentoErp;
		this.dataMovimento = dataMovimento;
		this.sobreporPoliticas = sobreporPoliticas;
		this.importar = importar;
		this.observacao = observacao;
		this.validacoesOS = validacoesOS;
		this.politicas = politicas;
		this.salvarOS_Material = salvarOS_Material;
		this.salvarCstg_IntVM = salvarCstg_IntVM;
		this.salvarCstg_IntCM = salvarCstg_IntCM;
		this.salvarCstg_IntDG = salvarCstg_IntDG;
		this.sequencial = sequencial;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getTipoMovimento() {
		return tipoMovimento;
	}
	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
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
	public String getCodContaContabil() {
		return codContaContabil;
	}
	public void setCodContaContabil(String codContaContabil) {
		this.codContaContabil = codContaContabil;
	}
	public String getDescContaContabil() {
		return descContaContabil;
	}
	public void setDescContaContabil(String descContaContabil) {
		this.descContaContabil = descContaContabil;
	}
	public String getCodNatureza() {
		return codNatureza;
	}
	public void setCodNatureza(String codNatureza) {
		this.codNatureza = codNatureza;
	}
	public String getCodMaterial() {
		return codMaterial;
	}
	public void setCodMaterial(String codMaterial) {
		this.codMaterial = codMaterial;
	}
	public String getDescMovimento() {
		return descMovimento;
	}
	public void setDescMovimento(String descMovimento) {
		this.descMovimento = descMovimento;
	}
	public String getUnidadeMedida() {
		return unidadeMedida;
	}
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	public Double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}
	public Double getPrecoUnitario() {
		return precoUnitario;
	}
	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}
	public Double getValorMovimento() {
		return valorMovimento;
	}
	public void setValorMovimento(Double valorMovimento) {
		this.valorMovimento = valorMovimento;
	}
	public String getNumeroOS() {
		return numeroOS;
	}
	public void setNumeroOS(String numeroOS) {
		this.numeroOS = numeroOS;
	}
	public String getFrotaOuCC() {
		return frotaOuCC;
	}
	public void setFrotaOuCC(String frotaOuCC) {
		this.frotaOuCC = frotaOuCC;
	}
	public String getDocumentoErp() {
		return documentoErp;
	}
	public void setDocumentoErp(String documentoErp) {
		this.documentoErp = documentoErp;
	}
	public Date getDataMovimento() {
		return dataMovimento;
	}
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	public String getSobreporPoliticas() {
		return sobreporPoliticas;
	}
	public void setSobreporPoliticas(String sobreporPoliticas) {
		this.sobreporPoliticas = sobreporPoliticas;
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
	public String getValidacoesOS() {
		return validacoesOS;
	}
	public void setValidacoesOS(String validacoesOS) {
		this.validacoesOS = validacoesOS;
	}
	public String getPoliticas() {
		return politicas;
	}
	public void setPoliticas(String politicas) {
		this.politicas = politicas;
	}
	public String getSalvarOS_Material() {
		return salvarOS_Material;
	}
	public void setSalvarOS_Material(String salvarOS_Material) {
		this.salvarOS_Material = salvarOS_Material;
	}
	public String getSalvarCstg_IntVM() {
		return salvarCstg_IntVM;
	}
	public void setSalvarCstg_IntVM(String salvarCstg_IntVM) {
		this.salvarCstg_IntVM = salvarCstg_IntVM;
	}
	public String getSalvarCstg_IntCM() {
		return salvarCstg_IntCM;
	}
	public void setSalvarCstg_IntCM(String salvarCstg_IntCM) {
		this.salvarCstg_IntCM = salvarCstg_IntCM;
	}
	public String getSalvarCstg_IntDG() {
		return salvarCstg_IntDG;
	}
	public void setSalvarCstg_IntDG(String salvarCstg_IntDG) {
		this.salvarCstg_IntDG = salvarCstg_IntDG;
	}
	public Integer getSequencial() {
		return sequencial;
	}
	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sequencial == null) ? 0 : sequencial.hashCode());
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
		Erp other = (Erp) obj;
		if (sequencial == null) {
			if (other.sequencial != null)
				return false;
		} else if (!sequencial.equals(other.sequencial))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Erp [origem=" + origem + ", tipoMovimento=" + tipoMovimento + ", anoMes=" + anoMes
				+ ", codCentroCustos=" + codCentroCustos + ", descCentroCustos=" + descCentroCustos
				+ ", codContaContabil=" + codContaContabil + ", descContaContabil=" + descContaContabil
				+ ", codNatureza=" + codNatureza + ", codMaterial=" + codMaterial + ", descMovimento=" + descMovimento
				+ ", unidadeMedida=" + unidadeMedida + ", quantidade=" + quantidade + ", precoUnitario=" + precoUnitario
				+ ", valorMovimento=" + valorMovimento + ", numeroOS=" + numeroOS + ", frotaOuCC=" + frotaOuCC
				+ ", documentoErp=" + documentoErp + ", dataMovimento=" + dataMovimento + ", sobreporPoliticas="
				+ sobreporPoliticas + ", importar=" + importar + ", observacao=" + observacao + ", validacoesOS="
				+ validacoesOS + ", politicas=" + politicas + ", salvarOS_Material=" + salvarOS_Material
				+ ", salvarCstg_IntVM=" + salvarCstg_IntVM + ", salvarCstg_IntCM=" + salvarCstg_IntCM
				+ ", salvarCstg_IntDG=" + salvarCstg_IntDG + ", sequencial=" + sequencial + "]";
	}
}