package com.aap.bean;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.aap.dto.Usuarios;
import com.aap.util.jsf.Contexts;
import com.aap.util.jsf.FuncionesJSF;
import com.aap.util.security.AccesoPassword;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = -5263499638067297067L;

	private String username;
    
    private String password;
    
    private Boolean logueado = Boolean.FALSE;
    
    public String login() {
    	if(!logueado) {
    		Usuarios usuario = AccesoPassword.obtenerUsuario(username, password);
    		if(usuario != null) {
    			Contexts.getSession().setAttribute("usuario", usuario);
    			logueado = Boolean.TRUE;
    			password = null;
    		}
    	}
    	return null;
    }
    
    public String logout() {
    	Contexts.getSession().invalidate();
    	logueado = Boolean.FALSE;
    	password = null;
    	username = null;
    	return "menuPrincipal";
    }
    
    public String guardarNuevoUsuario() {
    	if(validaGuardarNuevoUsuario()) {
    		
    		try {
    			Usuarios usuario = new Usuarios();
        		usuario.setUsu_activo(true);
        		usuario.setUsu_username(username);
	            usuario.setUsu_password(AccesoPassword.obtenerSHA256(password));
	            Session session = Contexts.getHibernateSession();
	    		session.save(usuario);
	    		session.flush();
	    		
	    		Contexts.addInfoMessage("Usuario dado de alta correctamente.");
	    		login();
	    		
            } catch (NoSuchAlgorithmException e) {
	            Contexts.addErrorMessage("Error al guardar el usuario.");
            }
    	}
    	return null;
    }
    
    private boolean validaGuardarNuevoUsuario() {
    	if(username == null || username.isEmpty()) {
    		Contexts.addErrorMessage("Ha de indicar el nombre de usuario.");
    	} else if(password == null || password.isEmpty()) {
    		Contexts.addErrorMessage("Ha de indicar una contraseña");
    	} else {
    		Session session = Contexts.getHibernateSession();
    		Usuarios usuario = (Usuarios) session.createCriteria(Usuarios.class)
    				.add(Restrictions.eq("usu_username", username))
    				.uniqueResult();
    		
    		if(usuario != null) {
    			Contexts.addErrorMessage("Ya existe un usuario con el mismo nombre.");
    		}
    	}
    	return !FuncionesJSF.hayErrores();
    }
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getLogueado() {
		return logueado;
	}
	public void setLogueado(Boolean logueado) {
		this.logueado = logueado;
	}
	
	
}