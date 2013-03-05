package com.aap.bean.partidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.aap.dto.Mensajes;
import com.aap.dto.Partidas;
import com.aap.util.jsf.Contexts;

@ManagedBean
@RequestScoped
public class MensajesPartidaBean implements Serializable {

    private static final long serialVersionUID = 8390445542162327520L;
	
    private List<Mensajes> listaMensajes = new ArrayList<Mensajes>();
    
    private Long idPartida;
    
    private Partidas partida = new Partidas();

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
			partida = (Partidas) session.get(Partidas.class, idPartida);
			cargarListaMensajes();
		}
	}

	public Partidas getPartida() {
		return partida;
	}

	public void setPartida(Partidas partida) {
		this.partida = partida;
	}

	
}