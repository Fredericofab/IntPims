package model.entities;

import java.io.Serializable;

public class CriticasErp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tipoCritica;
	private Integer codigoCritica;
	private String descCritica;
	private String flagAtiva;
	private String anoMesAnalisado;
	private Integer registrosAnalisados;
	private Integer registrosAtualizados;
	private Integer registrosPendentes;
	private String ClausulaWhere;
	private String ClausulaSet;
	public CriticasErp() {
		super();
	}
	public CriticasErp(String tipoCritica, Integer codigoCritica, String descCritica, String flagAtiva,
			String anoMesAnalisado, Integer registrosAnalisados, Integer registrosAtualizados, Integer registrosPendentes,
			String clausulaWhere, String clausulaSet) {
		super();
		this.tipoCritica = tipoCritica;
		this.codigoCritica = codigoCritica;
		this.descCritica = descCritica;
		this.flagAtiva = flagAtiva;
		this.anoMesAnalisado = anoMesAnalisado;
		this.registrosAnalisados = registrosAnalisados;
		this.registrosAtualizados = registrosAtualizados;
		this.registrosPendentes = registrosPendentes;
		ClausulaWhere = clausulaWhere;
		ClausulaSet = clausulaSet;
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
	public String getDescCritica() {
		return descCritica;
	}
	public void setDescCritica(String descCritica) {
		this.descCritica = descCritica;
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
	public Integer getRegistrosAnalisados() {
		return registrosAnalisados;
	}
	public void setRegistrosAnalisados(Integer registrosAnalisados) {
		this.registrosAnalisados = registrosAnalisados;
	}
	public Integer getRegistrosAtualizados() {
		return registrosAtualizados;
	}
	public void setRegistrosAtualizados(Integer registrosAtualizados) {
		this.registrosAtualizados = registrosAtualizados;
	}
	public Integer getRegistrosPendentes() {
		return registrosPendentes;
	}
	public void setRegistrosPendentes(Integer registrosPendentes) {
		this.registrosPendentes = registrosPendentes;
	}
	public String getClausulaWhere() {
		return ClausulaWhere;
	}
	public void setClausulaWhere(String clausulaWhere) {
		ClausulaWhere = clausulaWhere;
	}
	public String getClausulaSet() {
		return ClausulaSet;
	}
	public void setClausulaSet(String clausulaSet) {
		ClausulaSet = clausulaSet;
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
		CriticasErp other = (CriticasErp) obj;
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
		return "CriticasErp [tipoCritica=" + tipoCritica + ", codigoCrtica=" + codigoCritica + ", descCritica="
				+ descCritica + ", flagAtiva=" + flagAtiva + ", anoMesAnalisado=" + anoMesAnalisado
				+ ", registrosAnalisados=" + registrosAnalisados + ", registrosAtualizados=" + registrosAtualizados
				+ ", registrosPendentes=" + registrosPendentes + ", ClausulaWhere=" + ClausulaWhere + ", ClausulaSet="
				+ ClausulaSet + "]";
	}

	
}