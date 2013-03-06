package com.aap.bean.partidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Session;

import com.aap.dto.Competidores;
import com.aap.dto.Eventos;
import com.aap.dto.Partidas;
import com.aap.util.jsf.Contexts;
import com.aap.util.jsf.FuncionesJSF;

@ManagedBean
@ViewScoped
public class AdministrarPartidaBean implements Serializable {

    private static final long serialVersionUID = 7989062756747525842L;

	private Long idPartida;
    
    private Partidas partida = new Partidas();
    
    private List<Competidores> listaCompetidores = new ArrayList<Competidores>();
    
    private List<Eventos> listaEventos = new ArrayList<Eventos>();

	private Competidores competidor = null;

	private Eventos evento = null;
    
    public String guardarPartida() {
    	if(validaGuardarPartida()) {
    		Session session = Contexts.getHibernateSession();
    		if(partida.getPa_id() == null) {
    			session.save(partida);
    		} else {
    			session.merge(partida);
    		}
    		session.flush();
    		Contexts.addInfoMessage("Partida guardada correctamente.");
    	}
    	return null;
    }
    
    private boolean validaGuardarPartida() {
    	if(partida == null) {
    		Contexts.addErrorMessage("No hay ninguna partida para guardar.");
    	} else {
    		if(partida.getPa_nombre() == null || partida.getPa_nombre().isEmpty()) {
    			Contexts.addErrorMessage("El nombre de la partida es obligatorio.");
    		}
    		if(partida.getPa_descripcion() == null || partida.getPa_descripcion().isEmpty()) {
    			Contexts.addErrorMessage("La descripción de la partida es obligatorio.");
    		}
    	}
    	return !FuncionesJSF.hayErrores();
    }
    
    public String nuevoCompetidor() {
    	competidor = new Competidores();
    	return null;
    }
    
    public String nuevoEvento() {
    	evento = new Eventos();
    	return null;
    }
    
    public String guardarCompetidor() {
    	if(validaGuardarCompetidor()) {
    		Session session = Contexts.getHibernateSession();
    		if(competidor.getCo_id() == null) {
    			competidor.setCo_pa_id(partida);
    			session.save(competidor);
    		} else {
    			session.merge(competidor);
    		}
    		session.flush();
    		cargarListaCompetidores();
    		Contexts.addInfoMessage("Competidor guardado correctamente.");
    	}
    	return null;
    }
    
    private boolean validaGuardarCompetidor() {
    	if(competidor == null) {
    		Contexts.addErrorMessage("No hay ningún competidor seleccionado.");
    	} else {
    		if(competidor.getCo_nombre() == null || competidor.getCo_nombre().isEmpty()) {
    			Contexts.addErrorMessage("Debe indicar un nombre para el competidor.");
    		}
    	}
    	return !FuncionesJSF.hayErrores();
    }
    
    public String guardarEvento() {
    	if(validaGuardarEvento()) {
    		Session session = Contexts.getHibernateSession();
    		if(evento.getEv_id() == null) {
    			evento.setEv_pa_id(partida);
    			session.save(evento);
    		} else {
    			session.merge(evento);
    		}
    		session.flush();
    		cargarListaEventos();
    		Contexts.addInfoMessage("Evento guardado correctamente.");
    	}
    	return null;
    }
    
    private boolean validaGuardarEvento() {
    	if(evento == null) {
    		Contexts.addErrorMessage("No hay ningún evento seleccionado.");
    	} else {
    		if(evento.getEv_nombre() == null || evento.getEv_nombre().isEmpty()) {
    			Contexts.addErrorMessage("Debe indicar un nombre para el evento.");
    		}
    	}
    	return !FuncionesJSF.hayErrores();
    }
    
	public void setIdPartida(Long idPartida) {
		this.idPartida = idPartida;
		if(idPartida != null) {
			Session session = Contexts.getHibernateSession();
			partida = (Partidas) session.get(Partidas.class, idPartida);
			cargarListaCompetidores();
			cargarListaEventos();
		}
	}
	
    private void cargarListaCompetidores() {
    	Session session = Contexts.getHibernateSession();
		partida = (Partidas) session.get(Partidas.class, idPartida);
    	listaCompetidores = partida.getListaCompetidores();
    }
    
    private void cargarListaEventos() {
    	Session session = Contexts.getHibernateSession();
		partida = (Partidas) session.get(Partidas.class, idPartida);
    	listaEventos = partida.getListaEventos();
    }

	public Long getIdPartida() {
		return idPartida;
	}
	
	public Partidas getPartida() {
		return partida;
	}

	public void setPartida(Partidas partida) {
		this.partida = partida;
	}

	public List<Competidores> getListaCompetidores() {
		return listaCompetidores;
	}

	public void setListaCompetidores(List<Competidores> listaCompetidores) {
		this.listaCompetidores = listaCompetidores;
	}

	public List<Eventos> getListaEventos() {
		return listaEventos;
	}

	public void setListaEventos(List<Eventos> listaEventos) {
		this.listaEventos = listaEventos;
	}

	public Competidores getCompetidor() {
		return competidor;
	}

	public void setCompetidor(Competidores competidor) {
		this.competidor = competidor;
	}

	public Eventos getEvento() {
		return evento;
	}

	public void setEvento(Eventos evento) {
		this.evento = evento;
	}

	
}
