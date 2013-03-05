package com.aap.bean.partidas;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.hibernate.Session;

import com.aap.dto.Partidas;
import com.aap.util.jsf.Contexts;

@ManagedBean
@RequestScoped
public class ClasificacionPartidaBean implements Serializable {

    private static final long serialVersionUID = -7385224742368662795L;
		
    private Long idPartida;
    
    private Partidas partida = new Partidas();
    
    public Long getIdPartida() {
		return idPartida;
	}

	public void setIdPartida(Long idPartida) {
		this.idPartida = idPartida;
		if(idPartida != null) {
			Session session = Contexts.getHibernateSession();
			partida = (Partidas) session.get(Partidas.class, idPartida);
		}
	}

	public Partidas getPartida() {
		return partida;
	}

	public void setPartida(Partidas partida) {
		this.partida = partida;
	}
	
	
}