package com.aap.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PRONOSTICOS")
public class Pronosticos implements Serializable {

	private static final long serialVersionUID = 3636649404764798451L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pr_id;
	
	private Long pr_posicion;

	private Long pr_puntos_conseguidos;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "pr_co_id", referencedColumnName = "co_id")
	private Competidores pr_co_id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "pr_ev_id", referencedColumnName = "ev_id")
	private Eventos pr_ev_id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "pr_usu_id", referencedColumnName = "usu_id")
	private Usuarios pr_usu_id;

	public Long getPr_id() {
		return pr_id;
	}

	public void setPr_id(Long pr_id) {
		this.pr_id = pr_id;
	}

	public Long getPr_posicion() {
		return pr_posicion;
	}

	public void setPr_posicion(Long pr_posicion) {
		this.pr_posicion = pr_posicion;
	}

	public Long getPr_puntos_conseguidos() {
		return pr_puntos_conseguidos;
	}

	public void setPr_puntos_conseguidos(Long pr_puntos_conseguidos) {
		this.pr_puntos_conseguidos = pr_puntos_conseguidos;
	}

	public Competidores getPr_co_id() {
		return pr_co_id;
	}

	public void setPr_co_id(Competidores pr_co_id) {
		this.pr_co_id = pr_co_id;
	}

	public Eventos getPr_ev_id() {
		return pr_ev_id;
	}

	public void setPr_ev_id(Eventos pr_ev_id) {
		this.pr_ev_id = pr_ev_id;
	}

	public Usuarios getPr_usu_id() {
		return pr_usu_id;
	}

	public void setPr_usu_id(Usuarios pr_usu_id) {
		this.pr_usu_id = pr_usu_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pr_id == null) ? 0 : pr_id.hashCode());
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
		Pronosticos other = (Pronosticos) obj;
		if (pr_id == null) {
			if (other.pr_id != null) {
				return false;
			}
		} else if (!pr_id.equals(other.pr_id)) {
			return false;
		}
		return true;
	}

}