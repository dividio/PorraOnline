package com.aap.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name = "BONIFICACIONES")
public class Bonificaciones implements Serializable {

    private static final long serialVersionUID = -4323652698168608117L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bo_id;

	@Size(max=150)
	private String bo_nombre;
	
	private Long bo_orden;
	
	private Long bo_numero_posiciones;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "bo_pa_id", referencedColumnName = "pa_id")
	private Partidas bo_pa_id;
	
	@OneToMany(mappedBy="pp_bo_id", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<PuntosPosicion> listaPuntosPosicion;

	public Long getBo_id() {
		return bo_id;
	}

	public void setBo_id(Long bo_id) {
		this.bo_id = bo_id;
	}

	public String getBo_nombre() {
		return bo_nombre;
	}

	public void setBo_nombre(String bo_nombre) {
		this.bo_nombre = bo_nombre;
	}

	public Partidas getBo_pa_id() {
		return bo_pa_id;
	}

	public void setBo_pa_id(Partidas bo_pa_id) {
		this.bo_pa_id = bo_pa_id;
	}

	public List<PuntosPosicion> getListaPuntosPosicion() {
		return listaPuntosPosicion;
	}

	public void setListaPuntosPosicion(List<PuntosPosicion> listaPuntosPosicion) {
		this.listaPuntosPosicion = listaPuntosPosicion;
	}

	public Long getBo_numero_posiciones() {
		return bo_numero_posiciones;
	}

	public void setBo_numero_posiciones(Long bo_numero_posiciones) {
		this.bo_numero_posiciones = bo_numero_posiciones;
	}

	public Long getBo_orden() {
		return bo_orden;
	}

	public void setBo_orden(Long bo_orden) {
		this.bo_orden = bo_orden;
	}

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((bo_id == null) ? 0 : bo_id.hashCode());
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
	    Bonificaciones other = (Bonificaciones) obj;
	    if (bo_id == null) {
		    if (other.bo_id != null) {
			    return false;
		    }
	    } else if (!bo_id.equals(other.bo_id)) {
		    return false;
	    }
	    return true;
    }
	
}