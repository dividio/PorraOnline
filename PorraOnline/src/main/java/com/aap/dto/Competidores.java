package com.aap.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "COMPETIDORES")
public class Competidores implements Serializable {

	private static final long serialVersionUID = 5704108135070942905L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long co_id;

	private String co_nombre;

	@ManyToOne
	@JoinColumn(name = "co_pa_id", referencedColumnName = "pa_id")
	private Partidas co_pa_id;

	public Long getCo_id() {
		return co_id;
	}

	public void setCo_id(Long co_id) {
		this.co_id = co_id;
	}

	public String getCo_nombre() {
		return co_nombre;
	}

	public void setCo_nombre(String co_nombre) {
		this.co_nombre = co_nombre;
	}

	public Partidas getCo_pa_id() {
		return co_pa_id;
	}

	public void setCo_pa_id(Partidas co_pa_id) {
		this.co_pa_id = co_pa_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((co_id == null) ? 0 : co_id.hashCode());
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
		Competidores other = (Competidores) obj;
		if (co_id == null) {
			if (other.co_id != null) {
				return false;
			}
		} else if (!co_id.equals(other.co_id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return co_nombre;
	}
	
}