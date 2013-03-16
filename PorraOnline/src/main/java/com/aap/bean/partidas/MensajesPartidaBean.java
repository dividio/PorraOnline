package com.aap.bean.partidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.aap.dto.Mensajes;
import com.aap.dto.Partidas;
import com.aap.dto.Usuarios;
import com.aap.util.jsf.Contexts;
import com.aap.util.jsf.FuncionesJSF;

@ManagedBean
@ViewScoped
public class MensajesPartidaBean implements Serializable {

    private static final long serialVersionUID = 8390445542162327520L;
	
    private List<Mensajes> listaMensajes = new ArrayList<Mensajes>();
    
    private Long idPartida;
    
    private Partidas partida = new Partidas();
    
    private Mensajes mensaje = new Mensajes();

    public String nuevoMensaje() {
    	mensaje = new Mensajes();
    	return null;
    }
    
    public String guardarNuevoMensaje() {
    	if(validaGuardarNuevoMensaje()) {
    		Usuarios usuario = (Usuarios) Contexts.getSessionAttribute("usuario");
    		
    		mensaje.setMe_fecha(new Date());
    		mensaje.setMe_pa_id(partida);
    		mensaje.setMe_usu_id(usuario);
    		
    		Session session = Contexts.getHibernateSession();
    		session.save(mensaje);
    		session.flush();
    		Contexts.addInfoMessage("Mensaje guardado correctamente.");
    		cargarListaMensajes();
    	}
    	return null;
    }
    
    private boolean validaGuardarNuevoMensaje() {
    	if(mensaje == null) {
    		Contexts.addErrorMessage("No hay ningún mensaje para guardar.");
    	} else if(mensaje.getMe_texto() == null || mensaje.getMe_texto().isEmpty()){
    		Contexts.addErrorMessage("Debe indicar un texto para el mensaje.");
    	}
    	return !FuncionesJSF.hayErrores();
    }
    
    public String cargarListaMensajes() {
    	if(partida != null && partida.getPa_id() != null) {
    		Session session = Contexts.getHibernateSession();
    		
    		listaMensajes = session.createCriteria(Mensajes.class)
    				.add(Restrictions.eq("me_pa_id", partida))
    				.addOrder(Order.desc("me_fecha"))
    				.list();
    	}
    	return null;
    }
    
	public List<Mensajes> getListaMensajes() {
		return listaMensajes;
	}

	public void setListaMensajes(List<Mensajes> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}

	public Long getIdPartida() {
		return idPartida;
	}

	public void setIdPartida(Long idPartida) {
		this.idPartida = idPartida;
		if(idPartida != null) {
			Session session = Contexts.getHibernateSession();
			Usuarios usuario = (Usuarios) Contexts.getSessionAttribute("usuario");
			if(usuario != null) {
				String hql = "select PA " +
						"from Partidas PA " +
						"join PA.usuarios USU " +
						"where USU.usu_id = :ID_USUARIO " +
						"and PA.pa_id = :ID_PARTIDA";
				Query hqlQ = session.createQuery(hql);
				hqlQ.setLong("ID_USUARIO", usuario.getUsu_id());
				hqlQ.setLong("ID_PARTIDA", idPartida);
				partida = (Partidas) hqlQ.uniqueResult();
				cargarListaMensajes();
			}
		}
	}

	public Partidas getPartida() {
		return partida;
	}

	public void setPartida(Partidas partida) {
		this.partida = partida;
	}

	public Mensajes getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensajes mensaje) {
		this.mensaje = mensaje;
	}

	
}