package com.aap.dto;

import java.io.Serializable;

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
@Table(name = "PENALIZACIONES")
public class Penalizaciones implements Serializable {

    private static final long serialVersionUID = 3585384791128993793L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pe_id;

	@Size(max=150)
	private String pe_nombre;
	
	private Long pe_puntos;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "pe_pa_id", referencedColumnName = "pa_id")
	private Partidas pe_pa_id;

	public Long getPe_id() {
		return pe_id;
	}

	public void setPe_id(Long pe_id) {
		this.pe_id = pe_id;
	}

	public String getPe_nombre() {
		return pe_nombre;
	}

	public void setPe_nombre(String pe_nombre) {
		this.pe_nombre = pe_nombre;
	}

	public Partidas getPe_pa_id() {
		return pe_pa_id;
	}

	public void setPe_pa_id(Partidas pe_pa_id) {
		this.pe_pa_id = pe_pa_id;
	}

	public Long getPe_puntos() {
		return pe_puntos;
	}

	public void setPe_puntos(Long pe_puntos) {
		this.pe_puntos = pe_puntos;
	}

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((pe_id == null) ? 0 : pe_id.hashCode());
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
	    Penalizaciones other = (Penalizaciones) obj;
	    if (pe_id == null) {
		    if (other.pe_id != null) {
			    return false;
		    }
	    } else if (!pe_id.equals(other.pe_id)) {
		    return false;
	    }
	    return true;
    }

	
}