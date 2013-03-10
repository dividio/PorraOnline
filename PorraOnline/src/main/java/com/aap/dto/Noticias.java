package com.aap.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "NOTICIAS")
public class Noticias implements Serializable {

	private static final long serialVersionUID = 8628993325128853215L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no_id;
	
	private Date no_fecha;

	@Size(max=100)
	private String no_asunto;

	@Size(max=1500)
	private String no_texto;
	
	public Long getNo_id() {
		return no_id;
	}

	public void setNo_id(Long no_id) {
		this.no_id = no_id;
	}

	public Date getNo_fecha() {
		return no_fecha;
	}

	public void setNo_fecha(Date no_fecha) {
		this.no_fecha = no_fecha;
	}

	public String getNo_asunto() {
		return no_asunto;
	}

	public void setNo_asunto(String no_asunto) {
		this.no_asunto = no_asunto;
	}

	public String getNo_texto() {
		return no_texto;
	}

	public void setNo_texto(String no_texto) {
		this.no_texto = no_texto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((no_id == null) ? 0 : no_id.hashCode());
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
		Noticias other = (Noticias) obj;
		if (no_id == null) {
			if (other.no_id != null) {
				return false;
			}
		} else if (!no_id.equals(other.no_id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return no_asunto;
	}

}