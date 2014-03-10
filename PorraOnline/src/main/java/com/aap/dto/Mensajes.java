package com.aap.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "MENSAJES")
public class Mensajes implements Serializable {

	private static final long serialVersionUID = 8628993325128853215L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long me_id;
	
	private Date me_fecha;

	@Size(max=100)
	private String me_asunto;
	
	@Size(max=2500)
	private String me_texto;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "me_pa_id", referencedColumnName = "pa_id")
	private Partidas me_pa_id;
	
	@ManyToOne
	@JoinColumn(name = "me_usu_id", referencedColumnName = "usu_id")
	private Usuarios me_usu_id;

	public Long getMe_id() {
		return me_id;
	}

	public void setMe_id(Long me_id) {
		this.me_id = me_id;
	}

	public Date getMe_fecha() {
		return me_fecha;
	}

	public void setMe_fecha(Date me_fecha) {
		this.me_fecha = me_fecha;
	}

	public String getMe_asunto() {
		return me_asunto;
	}

	public void setMe_asunto(String me_asunto) {
		this.me_asunto = me_asunto;
	}

	public String getMe_texto() {
		return me_texto;
	}

	public void setMe_texto(String me_texto) {
		this.me_texto = me_texto;
	}

	public Partidas getMe_pa_id() {
		return me_pa_id;
	}

	public void setMe_pa_id(Partidas me_pa_id) {
		this.me_pa_id = me_pa_id;
	}

	public Usuarios getMe_usu_id() {
		return me_usu_id;
	}

	public void setMe_usu_id(Usuarios me_usu_id) {
		this.me_usu_id = me_usu_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((me_id == null) ? 0 : me_id.hashCode());
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
		Mensajes other = (Mensajes) obj;
		if (me_id == null) {
			if (other.me_id != null) {
				return false;
			}
		} else if (!me_id.equals(other.me_id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return me_asunto;
	}

}