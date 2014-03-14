package com.aap.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "RESULTADOS")
public class Resultados implements Serializable {

	private static final long serialVersionUID = 4197034866343618531L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long re_id;
	
	private Long re_posicion;

	@ManyToOne
	@JoinColumn(name = "re_co_id", referencedColumnName = "co_id")
	private Competidores re_co_id;
	
	@ManyToOne
	@JoinColumn(name = "re_bo_id", referencedColumnName = "bo_id")
	private Bonificaciones re_bo_id;
	
	@ManyToOne
	@JoinColumn(name = "re_pe_id", referencedColumnName = "pe_id")
	private Penalizaciones re_pe_id;
	
	@JsonBackReference(value="re_ev_id")
	@ManyToOne
	@JoinColumn(name = "re_ev_id", referencedColumnName = "ev_id")
	private Eventos re_ev_id;

	public Long getRe_id() {
		return re_id;
	}

	public void setRe_id(Long re_id) {
		this.re_id = re_id;
	}

	public Long getRe_posicion() {
		return re_posicion;
	}

	public void setRe_posicion(Long re_posicion) {
		this.re_posicion = re_posicion;
	}

	public Competidores getRe_co_id() {
		return re_co_id;
	}

	public void setRe_co_id(Competidores re_co_id) {
		this.re_co_id = re_co_id;
	}

	public Eventos getRe_ev_id() {
		return re_ev_id;
	}

	public void setRe_ev_id(Eventos re_ev_id) {
		this.re_ev_id = re_ev_id;
	}

	public Bonificaciones getRe_bo_id() {
		return re_bo_id;
	}

	public void setRe_bo_id(Bonificaciones re_bo_id) {
		this.re_bo_id = re_bo_id;
	}

	public Penalizaciones getRe_pe_id() {
		return re_pe_id;
	}

	public void setRe_pe_id(Penalizaciones re_pe_id) {
		this.re_pe_id = re_pe_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((re_id == null) ? 0 : re_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Resultados other = (Resultados) obj;
		if (re_id == null) {
			if (other.re_id != null) {
				return false;
			}
		} else if (!re_id.equals(other.re_id)) {
			return false;
		}
		return true;
	}

}