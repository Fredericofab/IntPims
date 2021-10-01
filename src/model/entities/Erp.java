package model.entities;

import java.io.Serializable;
import java.sql.Date;

public class Erp implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer sequencial;
	private String anoMes;
	private String origem;
	private Double codCentroCustos;
	private String descCentroCustos;
	private Double codContaContabil;
	private String descContaContabil;
	private Double familiaMaterial;
	private Double codMaterial;
	private String unidMedMaterial;
	private Double quantidade;
	private Double precoUnitario;
	private Double valorMovimento;
	private Date dataMovimento;
	private Double osManfro;
	private String numeroDocErp;
	private String descMovimento;
	private String descMaterial;
	private String importar;
	private String observacao;
	private String criticas;
	private String salvarOS_Material;
	private String salvarCstg_IntVM;
	private String salvarCstg_intCM;
	private String salvarCstg_intDG;

	public Erp() {
	}

	public Erp(Integer sequencial, String anoMes, String origem, Double codCentroCustos, String descCentroCustos,
			Double codContaContabil, String descContaContabil, Double familiaMaterial, Double codMaterial,
			String unidMedMaterial, Double quantidade, Double precoUnitario, Double valorMovimento, Date dataMovimento,
			Double osManfro, String numeroDocErp, String descMovimento, String descMaterial, String importar,
			String observacao, String criticas, String salvarOS_Material, String salvarCstg_IntVM,
			String salvarCstg_intCM, String salvarCstg_intDG) {
		super();
		this.sequencial = sequencial;
		this.anoMes = anoMes;
		this.origem = origem;
		this.codCentroCustos = codCentroCustos;
		this.descCentroCustos = descCentroCustos;
		this.codContaContabil = codContaContabil;
		this.descContaContabil = descContaContabil;
		this.familiaMaterial = familiaMaterial;
		this.codMaterial = codMaterial;
		this.unidMedMaterial = unidMedMaterial;
		this.quantidade = quantidade;
		this.precoUnitario = precoUnitario;
		this.valorMovimento = valorMovimento;
		this.dataMovimento = dataMovimento;
		this.osManfro = osManfro;
		this.numeroDocErp = numeroDocErp;
		this.descMovimento = descMovimento;
		this.descMaterial = descMaterial;
		this.importar = importar;
		this.observacao = observacao;
		this.criticas = criticas;
		this.salvarOS_Material = salvarOS_Material;
		this.salvarCstg_IntVM = salvarCstg_IntVM;
		this.salvarCstg_intCM = salvarCstg_intCM;
		this.salvarCstg_intDG = salvarCstg_intDG;
	}

	public Integer getSequencial() {
		return sequencial;
	}

	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}

	public String getAnoMes() {
		return anoMes;
	}

	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
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

	public Double getCodContaContabil() {
		return codContaContabil;
	}

	public void setCodContaContabil(Double codContaContabil) {
		this.codContaContabil = codContaContabil;
	}

	public String getDescContaContabil() {
		return descContaContabil;
	}

	public void setDescContaContabil(String descContaContabil) {
		this.descContaContabil = descContaContabil;
	}

	public Double getFamiliaMaterial() {
		return familiaMaterial;
	}

	public void setFamiliaMaterial(Double familiaMaterial) {
		this.familiaMaterial = familiaMaterial;
	}

	public Double getCodMaterial() {
		return codMaterial;
	}

	public void setCodMaterial(Double codMaterial) {
		this.codMaterial = codMaterial;
	}

	public String getUnidMedMaterial() {
		return unidMedMaterial;
	}

	public void setUnidMedMaterial(String unidMedMaterial) {
		this.unidMedMaterial = unidMedMaterial;
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

	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public Double getOsManfro() {
		return osManfro;
	}

	public void setOsManfro(Double osManfro) {
		this.osManfro = osManfro;
	}

	public String getNumeroDocErp() {
		return numeroDocErp;
	}

	public void setNumeroDocErp(String numeroDocErp) {
		this.numeroDocErp = numeroDocErp;
	}

	public String getDescMovimento() {
		return descMovimento;
	}

	public void setDescMovimento(String descMovimento) {
		this.descMovimento = descMovimento;
	}

	public String getDescMaterial() {
		return descMaterial;
	}

	public void setDescMaterial(String descMaterial) {
		this.descMaterial = descMaterial;
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

	public String getCriticas() {
		return criticas;
	}

	public void setCriticas(String criticas) {
		this.criticas = criticas;
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

	public String getSalvarCstg_intCM() {
		return salvarCstg_intCM;
	}

	public void setSalvarCstg_intCM(String salvarCstg_intCM) {
		this.salvarCstg_intCM = salvarCstg_intCM;
	}

	public String getSalvarCstg_intDG() {
		return salvarCstg_intDG;
	}

	public void setSalvarCstg_intDG(String salvarCstg_intDG) {
		this.salvarCstg_intDG = salvarCstg_intDG;
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
		return "Erp [id=" + sequencial + ", anoMes=" + anoMes + ", origem=" + origem + ", codCentroCustos="
				+ codCentroCustos + ", descCentroCustos=" + descCentroCustos + ", codContaContabil=" + codContaContabil
				+ ", descContaContabil=" + descContaContabil + ", familiaMaterial=" + familiaMaterial + ", codMaterial="
				+ codMaterial + ", unidMedMaterial=" + unidMedMaterial + ", quantidade=" + quantidade
				+ ", precoUnitario=" + precoUnitario + ", valorMovimento=" + valorMovimento + ", dataMovimento="
				+ dataMovimento + ", osManfro=" + osManfro + ", numeroDocErp=" + numeroDocErp + ", descMovimento="
				+ descMovimento + ", descMaterial=" + descMaterial + ", importar=" + importar + ", observacao="
				+ observacao + ", criticas=" + criticas + ", salvarOS_Material=" + salvarOS_Material
				+ ", salvarCstg_IntVM=" + salvarCstg_IntVM + ", salvarCstg_intCM=" + salvarCstg_intCM
				+ ", salvarCstg_intDG=" + salvarCstg_intDG + "]";
	}

}
