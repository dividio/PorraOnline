package com.aap.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PARTIDAS")
public class Partidas implements Serializable {

	private static final long serialVersionUID = -239879180072303842L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pa_id;

	@Size(max=500)
	private String pa_descripcion;

	private Date pa_fecha_inicio;

	private Date pa_fecha_fin;

	@Size(max=100)
	private String pa_nombre;
	
	@Size(max=50)
	private String pa_alias_competidores;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USUARIOS_PARTIDAS", 
		joinColumns = @JoinColumn(name = "up_pa_id", referencedColumnName = "pa_id"), 
		inverseJoinColumns = @JoinColumn(name = "up_usu_id", referencedColumnName = "usu_id"))
	private Set<Usuarios> usuarios;

	@OneToMany(mappedBy="me_pa_id", cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Mensajes> listaMensajes;
	
	@OneToMany(mappedBy="ev_pa_id", cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Eventos> listaEventos;
	
	@OneToMany(mappedBy="co_pa_id", cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Competidores> listaCompetidores;
	
	@OneToMany(mappedBy="pp_pa_id", cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<PuntosPosicion> listaPuntosPosicion;
	
	public Long getPa_id() {
		return pa_id;
	}

	public void setPa_id(Long pa_id) {
		this.pa_id = pa_id;
	}

	public String getPa_descripcion() {
		return pa_descripcion;
	}

	public void setPa_descripcion(String pa_descripcion) {
		this.pa_descripcion = pa_descripcion;
	}

	public Date getPa_fecha_inicio() {
		return pa_fecha_inicio;
	}

	public void setPa_fecha_inicio(Date pa_fecha_inicio) {
		this.pa_fecha_inicio = pa_fecha_inicio;
	}

	public Date getPa_fecha_fin() {
		return pa_fecha_fin;
	}

	public void setPa_fecha_fin(Date pa_fecha_fin) {
		this.pa_fecha_fin = pa_fecha_fin;
	}

	public String getPa_nombre() {
		return pa_nombre;
	}

	public void setPa_nombre(String pa_nombre) {
		this.pa_nombre = pa_nombre;
	}

	public Set<Usuarios> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuarios> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Mensajes> getListaMensajes() {
		return listaMensajes;
	}

	public void setListaMensajes(List<Mensajes> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}

	public List<Eventos> getListaEventos() {
		return listaEventos;
	}

	public void setListaEventos(List<Eventos> listaEventos) {
		this.listaEventos = listaEventos;
	}

	public String getPa_alias_competidores() {
		return pa_alias_competidores;
	}

	public void setPa_alias_competidores(String pa_alias_competidores) {
		this.pa_alias_competidores = pa_alias_competidores;
	}

	public List<Competidores> getListaCompetidores() {
		return listaCompetidores;
	}

	public void setListaCompetidores(List<Competidores> listaCompetidores) {
		this.listaCompetidores = listaCompetidores;
	}

	public List<PuntosPosicion> getListaPuntosPosicion() {
		return listaPuntosPosicion;
	}

	public void setListaPuntosPosicion(List<PuntosPosicion> listaPuntosPosicion) {
		this.listaPuntosPosicion = listaPuntosPosicion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pa_id == null) ? 0 : pa_id.hashCode());
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
		Partidas other = (Partidas) obj;
		if (pa_id == null) {
			if (other.pa_id != null) {
				return false;
			}
		} else if (!pa_id.equals(other.pa_id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return pa_nombre;
	}

}
