package com.aap.dto;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
public class Roles implements Serializable {
	
	private static final long serialVersionUID = 2158679955170940588L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long   rol_id;
	
	private String rol_nombre;
	private Boolean rol_por_defecto;
	private Boolean rol_admin;
	private String rol_comentario;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ROLES_USUARIOS",
    			joinColumns=@JoinColumn(name="ru_rol_id", referencedColumnName="rol_id"),
    			inverseJoinColumns=@JoinColumn(name="ru_usu_id", referencedColumnName="usu_id"))
	private Set<Usuarios> usuarios;
    
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_padre")
	private Roles  rol_padre;
	
	@OneToMany(mappedBy = "rol_padre", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Roles> rol_hijos;
    
	public Long getRol_id() {
		return rol_id;
	}
	public void setRol_id(Long rol_id) {
		this.rol_id = rol_id;
	}
	public String getRol_nombre() {
		return rol_nombre;
	}
	public void setRol_nombre(String rol_nombre) {
		this.rol_nombre = rol_nombre;
	}
	public Set<Usuarios> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(Set<Usuarios> usuarios) {
		this.usuarios = usuarios;
	}	
	public Roles getRol_padre() {
		return rol_padre;
	}
	public void setRol_padre(Roles rol_padre) {
		this.rol_padre = rol_padre;
	}
	public Set<Roles> getRol_hijos() {
		return rol_hijos;
	}
	public void setRol_hijos(Set<Roles> rol_hijos) {
		this.rol_hijos = rol_hijos;
	}
	public Boolean getRol_por_defecto() {
		return rol_por_defecto;
	}
	public void setRol_por_defecto(Boolean rol_por_defecto) {
		this.rol_por_defecto = rol_por_defecto;
	}
	public Boolean getRol_admin() {
		return rol_admin;
	}
	public void setRol_admin(Boolean rol_admin) {
		this.rol_admin = rol_admin;
	}
	public String getRol_comentario() {
		return rol_comentario;
	}
	public void setRol_comentario(String rol_comentario) {
		this.rol_comentario = rol_comentario;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rol_id == null) ? 0 : rol_id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Roles other = (Roles) obj;
		if (rol_id == null) {
			if (other.rol_id != null)
				return false;
		} else if (!rol_id.equals(other.rol_id))
			return false;
		return true;
	}
	public String toString() {
		return rol_nombre;
	}
}