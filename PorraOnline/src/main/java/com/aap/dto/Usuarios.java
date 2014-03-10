package com.aap.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "USUARIOS")
public class Usuarios implements Serializable {

	private static final long serialVersionUID = 6267406834568235637L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long	   	usu_id;
	
	@Size(max=100)
	private String	   	usu_email;
	
	@Size(max=50)
	private String     	usu_username;
	
	@Size(max=100)
	private String     	usu_password;
	
	private Boolean    	usu_activo;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ROLES_USUARIOS",
    			joinColumns=@JoinColumn(name="ru_usu_id", referencedColumnName="usu_id"),
    			inverseJoinColumns=@JoinColumn(name="ru_rol_id", referencedColumnName="rol_id"))
	private Set<Roles> roles;

	public Long getUsu_id() {
		return usu_id;
	}
	public void setUsu_id(Long usu_id) {
		this.usu_id = usu_id;
	}
	public String getUsu_email() {
		return usu_email;
	}
	public void setUsu_email(String usu_email) {
		this.usu_email = usu_email;
	}
	public String getUsu_username() {
		return usu_username;
	}
	public void setUsu_username(String usu_username) {
		this.usu_username = usu_username;
	}
	public Set<Roles> getRoles() {
		return roles;
	}
	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}
	public void setUsu_password(String usu_password) {
		this.usu_password = usu_password;
	}
	public String getUsu_password() {
		return usu_password;
	}
	public void setUsu_activo(Boolean usu_activo) {
		this.usu_activo = usu_activo;
	}
	public Boolean getUsu_activo() {
		return usu_activo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usu_id == null) ? 0 : usu_id.hashCode());
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
		Usuarios other = (Usuarios) obj;
		if (usu_id == null) {
			if (other.usu_id != null)
				return false;
		} else if (!usu_id.equals(other.usu_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return usu_username;
	}
}