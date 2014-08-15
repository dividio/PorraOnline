package com.aap.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "EVENTOS")
public class Eventos implements Serializable {

	private static final long serialVersionUID = 2473935125202719477L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ev_id;

	@Size(max=100)
	private String ev_nombre;

	private Date ev_fecha_evento;

	private Date ev_fecha_inicio_pronosticos;
	
	private Date ev_fecha_limite_pronosticos;
	
	@Size(max=100)
	private String ev_lugar;
	
	@Size(max=250)
	private String ev_url_referencia;
	
	private Boolean ev_anulado;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ev_pa_id", referencedColumnName = "pa_id")
	private Partidas ev_pa_id;

	@JsonIgnore
	@OneToMany(mappedBy="re_ev_id", cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Resultados> listaResultados;
	
	public Long getEv_id() {
		return ev_id;
	}

	public void setEv_id(Long ev_id) {
		this.ev_id = ev_id;
	}

	public String getEv_nombre() {
		return ev_nombre;
	}

	public void setEv_nombre(String ev_nombre) {
		this.ev_nombre = ev_nombre;
	}

	public Date getEv_fecha_evento() {
		return ev_fecha_evento;
	}

	public void setEv_fecha_evento(Date ev_fecha_evento) {
		this.ev_fecha_evento = ev_fecha_evento;
	}

	public Date getEv_fecha_limite_pronosticos() {
		return ev_fecha_limite_pronosticos;
	}

	public void setEv_fecha_limite_pronosticos(Date ev_fecha_limite_pronosticos) {
		this.ev_fecha_limite_pronosticos = ev_fecha_limite_pronosticos;
	}

	public Partidas getEv_pa_id() {
		return ev_pa_id;
	}

	public void setEv_pa_id(Partidas ev_pa_id) {
		this.ev_pa_id = ev_pa_id;
	}

	public List<Resultados> getListaResultados() {
		return listaResultados;
	}

	public void setListaResultados(List<Resultados> listaResultados) {
		this.listaResultados = listaResultados;
	}

	public Date getEv_fecha_inicio_pronosticos() {
		return ev_fecha_inicio_pronosticos;
	}

	public void setEv_fecha_inicio_pronosticos(Date ev_fecha_inicio_pronosticos) {
		this.ev_fecha_inicio_pronosticos = ev_fecha_inicio_pronosticos;
	}

	public String getEv_lugar() {
		return ev_lugar;
	}

	public void setEv_lugar(String ev_lugar) {
		this.ev_lugar = ev_lugar;
	}

	public String getEv_url_referencia() {
		return ev_url_referencia;
	}

	public void setEv_url_referencia(String ev_url_referencia) {
		this.ev_url_referencia = ev_url_referencia;
	}

	public Boolean getEv_anulado() {
		return ev_anulado;
	}

	public void setEv_anulado(Boolean ev_anulado) {
		this.ev_anulado = ev_anulado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ev_id == null) ? 0 : ev_id.hashCode());
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
		Eventos other = (Eventos) obj;
		if (ev_id == null) {
			if (other.ev_id != null) {
				return false;
			}
		} else if (!ev_id.equals(other.ev_id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return ev_nombre;
	}

}