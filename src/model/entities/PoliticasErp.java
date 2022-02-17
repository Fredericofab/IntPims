package model.entities;

import java.io.Serializable;

public class PoliticasErp implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer codPolitica;
	private String nomePolitica;
	private String flagAtiva;
	private String descPolitica;
	private String clausulaWhere;
	private String importar;
	private String salvarOS_Material;
	private String salvarCstg_IntVM;
	private String salvarCstg_IntCM;
	private String salvarCstg_IntDG;
	private String anoMesAnalisado;
	private Integer registrosAplicados;
	
	public PoliticasErp() {
		super();
	}
	public PoliticasErp(Integer codPolitica, String nomePolitica, String flagAtiva, String descPolitica,
			String clausulaWhere, String importar, String salvarOS_Material, String salvarCstg_IntVM,
			String salvarCstg_IntCM, String salvarCstg_IntDG, String anoMesAnalisado, Integer registrosAplicados) {
		super();
		this.codPolitica = codPolitica;
		this.nomePolitica = nomePolitica;
		this.flagAtiva = flagAtiva;
		this.descPolitica = descPolitica;
		this.clausulaWhere = clausulaWhere;
		this.importar = importar;
		this.salvarOS_Material = salvarOS_Material;
		this.salvarCstg_IntVM = salvarCstg_IntVM;
		this.salvarCstg_IntCM = salvarCstg_IntCM;
		this.salvarCstg_IntDG = salvarCstg_IntDG;
		this.anoMesAnalisado = anoMesAnalisado;
		this.registrosAplicados = registrosAplicados;
	}
	public Integer getCodPolitica() {
		return codPolitica;
	}
	public void setCodPolitica(Integer codPolitica) {
		this.codPolitica = codPolitica;
	}
	public String getNomePolitica() {
		return nomePolitica;
	}
	public void setNomePolitica(String nomePolitica) {
		this.nomePolitica = nomePolitica;
	}
	public String getFlagAtiva() {
		return flagAtiva;
	}
	public void setFlagAtiva(String flagAtiva) {
		this.flagAtiva = flagAtiva;
	}
	public String getDescPolitica() {
		return descPolitica;
	}
	public void setDescPolitica(String descPolitica) {
		this.descPolitica = descPolitica;
	}
	public String getClausulaWhere() {
		return clausulaWhere;
	}
	public void setClausulaWhere(String clausulaWhere) {
		this.clausulaWhere = clausulaWhere;
	}
	public String getImportar() {
		return importar;
	}
	public void setImportar(String importar) {
		this.importar = importar;
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
	public String getAnoMesAnalisado() {
		return anoMesAnalisado;
	}
	public void setAnoMesAnalisado(String anoMesAnalisado) {
		this.anoMesAnalisado = anoMesAnalisado;
	}
	public Integer getRegistrosAplicados() {
		return registrosAplicados;
	}
	public void setRegistrosAplicados(Integer registrosAplicados) {
		this.registrosAplicados = registrosAplicados;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codPolitica == null) ? 0 : codPolitica.hashCode());
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
		PoliticasErp other = (PoliticasErp) obj;
		if (codPolitica == null) {
			if (other.codPolitica != null)
				return false;
		} else if (!codPolitica.equals(other.codPolitica))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PoliticasErp [codPolitica=" + codPolitica + ", nomePolitica=" + nomePolitica + ", flagAtiva="
				+ flagAtiva + ", descPolitica=" + descPolitica + ", clausulaWhere=" + clausulaWhere + ", importar="
				+ importar + ", salvarOS_Material=" + salvarOS_Material + ", salvarCstg_IntVM=" + salvarCstg_IntVM
				+ ", salvarCstg_IntCM=" + salvarCstg_IntCM + ", salvarCstg_IntDG=" + salvarCstg_IntDG
				+ ", anoMesAnalisado=" + anoMesAnalisado + ", registrosAplicados=" + registrosAplicados + "]";
	}
}