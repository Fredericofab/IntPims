package model.entities;

import java.io.Serializable;

public class CriticaErp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tipoCritica;
	private Integer codigoCritica;
	private String nomeCritica;
	private String flagAtiva;
	private String anoMesAnalisado;
	private Integer registrosPendentes;
	private String descCritica;
	private String clausulaWhere;
	private String importar;
	private String salvarOS_Material;
	private String salvarCstg_IntVM;
	private String salvarCstg_IntCM;
	private String salvarCstg_IntDG;

	public CriticaErp() {
		super();
	}
		
	public CriticaErp(String tipoCritica, Integer codigoCritica, String nomeCritica, String flagAtiva,
			String anoMesAnalisado, Integer registrosPendentes, String descCritica, String clausulaWhere,
			String importar, String salvarOS_Material, String salvarCstg_IntVM, String salvarCstg_IntCM,
			String salvarCstg_IntDG) {
		super();
		this.tipoCritica = tipoCritica;
		this.codigoCritica = codigoCritica;
		this.nomeCritica = nomeCritica;
		this.flagAtiva = flagAtiva;
		this.anoMesAnalisado = anoMesAnalisado;
		this.registrosPendentes = registrosPendentes;
		this.descCritica = descCritica;
		this.clausulaWhere = clausulaWhere;
		this.importar = importar;
		this.salvarOS_Material = salvarOS_Material;
		this.salvarCstg_IntVM = salvarCstg_IntVM;
		this.salvarCstg_IntCM = salvarCstg_IntCM;
		this.salvarCstg_IntDG = salvarCstg_IntDG;
	}
	
	public String getTipoCritica() {
		return tipoCritica;
	}
	public void setTipoCritica(String tipoCritica) {
		this.tipoCritica = tipoCritica;
	}
	public Integer getCodigoCritica() {
		return codigoCritica;
	}
	public void setCodigoCritica(Integer codigoCritica) {
		this.codigoCritica = codigoCritica;
	}
	public String getNomeCritica() {
		return nomeCritica;
	}
	public void setNomeCritica(String nomeCritica) {
		this.nomeCritica = nomeCritica;
	}
	public String getFlagAtiva() {
		return flagAtiva;
	}
	public void setFlagAtiva(String flagAtiva) {
		this.flagAtiva = flagAtiva;
	}
	public String getAnoMesAnalisado() {
		return anoMesAnalisado;
	}
	public void setAnoMesAnalisado(String anoMesAnalisado) {
		this.anoMesAnalisado = anoMesAnalisado;
	}
	public Integer getRegistrosPendentes() {
		return registrosPendentes;
	}
	public void setRegistrosPendentes(Integer registrosPendentes) {
		this.registrosPendentes = registrosPendentes;
	}
	public String getDescCritica() {
		return descCritica;
	}
	public void setDescCritica(String descCritica) {
		this.descCritica = descCritica;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCritica == null) ? 0 : codigoCritica.hashCode());
		result = prime * result + ((tipoCritica == null) ? 0 : tipoCritica.hashCode());
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
		CriticaErp other = (CriticaErp) obj;
		if (codigoCritica == null) {
			if (other.codigoCritica != null)
				return false;
		} else if (!codigoCritica.equals(other.codigoCritica))
			return false;
		if (tipoCritica == null) {
			if (other.tipoCritica != null)
				return false;
		} else if (!tipoCritica.equals(other.tipoCritica))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CriticaErp [tipoCritica=" + tipoCritica + ", codigoCritica=" + codigoCritica + ", nomeCritica="
				+ nomeCritica + ", flagAtiva=" + flagAtiva + ", anoMesAnalisado=" + anoMesAnalisado
				+ ", registrosPendentes=" + registrosPendentes + ", descCritica=" + descCritica + ", clausulaWhere="
				+ clausulaWhere + ", importar=" + importar + ", salvarOS_Material=" + salvarOS_Material
				+ ", salvarCstg_IntVM=" + salvarCstg_IntVM + ", salvarCstg_IntCM=" + salvarCstg_IntCM
				+ ", salvarCstg_IntDG=" + salvarCstg_IntDG + "]";
	}
}