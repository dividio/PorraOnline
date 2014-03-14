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
@Table(name = "PUNTOS_POSICION")
public class PuntosPosicion implements Serializable {

    private static final long serialVersionUID = -6051990040039061956L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pp_id;
	
	private Long pp_posicion;

	private Long pp_puntos;
	
	private Long pp_puntos_aproximacion;
	
	private Long pp_nivel_aproximacion;
	
	@JsonBackReference(value="pp_bo_id")
	@ManyToOne
	@JoinColumn(name = "pp_bo_id", referencedColumnName = "bo_id")
	private Bonificaciones pp_bo_id;

	public Long getPp_id() {
		return pp_id;
	}

	public void setPp_id(Long pp_id) {
		this.pp_id = pp_id;
	}

	public Long getPp_posicion() {
		return pp_posicion;
	}

	public void setPp_posicion(Long pp_posicion) {
		this.pp_posicion = pp_posicion;
	}

	public Long getPp_puntos() {
		return pp_puntos;
	}

	public void setPp_puntos(Long pp_puntos) {
		this.pp_puntos = pp_puntos;
	}

	public Long getPp_puntos_aproximacion() {
		return pp_puntos_aproximacion;
	}

	public void setPp_puntos_aproximacion(Long pp_puntos_aproximacion) {
		this.pp_puntos_aproximacion = pp_puntos_aproximacion;
	}

	public Long getPp_nivel_aproximacion() {
		return pp_nivel_aproximacion;
	}

	public void setPp_nivel_aproximacion(Long pp_nivel_aproximacion) {
		this.pp_nivel_aproximacion = pp_nivel_aproximacion;
	}
	
	public Bonificaciones getPp_bo_id() {
		return pp_bo_id;
	}

	public void setPp_bo_id(Bonificaciones pp_bo_id) {
		this.pp_bo_id = pp_bo_id;
	}

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((pp_id == null) ? 0 : pp_id.hashCode());
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
	    PuntosPosicion other = (PuntosPosicion) obj;
	    if (pp_id == null) {
		    if (other.pp_id != null) {
			    return false;
		    }
	    } else if (!pp_id.equals(other.pp_id)) {
		    return false;
	    }
	    return true;
    }
	

}