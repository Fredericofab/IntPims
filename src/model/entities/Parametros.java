package model.entities;

import java.io.Serializable;

public class Parametros implements Serializable {

	private static final long serialVersionUID = 1L;

	private String secao;
	private String entrada;
	private String valor;
	private String descricao;

	public Parametros() {
	}
	public Parametros(String secao, String entrada, String valor, String descricao) {
		super();
		this.secao = secao;
		this.entrada = entrada;
		this.valor = valor;
		this.descricao = descricao;
	}
	
	public String getSecao() {
		return secao;
	}
	public void setSecao(String secao) {
		this.secao = secao;
	}
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entrada == null) ? 0 : entrada.hashCode());
		result = prime * result + ((secao == null) ? 0 : secao.hashCode());
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
		Parametros other = (Parametros) obj;
		if (entrada == null) {
			if (other.entrada != null)
				return false;
		} else if (!entrada.equals(other.entrada))
			return false;
		if (secao == null) {
			if (other.secao != null)
				return false;
		} else if (!secao.equals(other.secao))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Parametros [secao=" + secao + ", entrada=" + entrada + ", valor=" + valor + ", descricao=" + descricao
				+ "]";
	}
	
}
	