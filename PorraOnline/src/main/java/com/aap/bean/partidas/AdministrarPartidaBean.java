package com.aap.bean.partidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.aap.dto.Competidores;
import com.aap.dto.Eventos;
import com.aap.dto.Partidas;
import com.aap.dto.PuntosPosicion;
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
    
    private List<PuntosPosicion> listaPuntosPosicion = new ArrayList<PuntosPosicion>();

	private Competidores competidor = null;

	private Eventos evento = null;
	
	private PuntosPosicion puntosPosicion = null;
    
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
    
    public String nuevaPosicion() {
    	puntosPosicion = new PuntosPosicion();
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
    
    public String guardarPuntosPosicion() {
    	if(validaGuardarPuntosPosicion()) {
    		Session session = Contexts.getHibernateSession();
    		if(puntosPosicion.getPp_id() == null) {
    			puntosPosicion.setPp_pa_id(partida);
    			session.save(puntosPosicion);
    		} else {
    			session.merge(puntosPosicion);
    		}
    		session.flush();
    		cargarListaPuntosPosicion();
    		Contexts.addInfoMessage("Evento guardado correctamente.");
    	}
    	return null;
    }
    
    private boolean validaGuardarPuntosPosicion() {
    	if(puntosPosicion == null) {
    		Contexts.addErrorMessage("No hay ninguna posición seleccionada.");
    	} else {
    		Long posicion = puntosPosicion.getPp_posicion();
    		if(posicion == null) {
    			Contexts.addErrorMessage("Debe indicar una posición.");
    		} else {
    			Session session = Contexts.getHibernateSession();
    			String hql = "select count(*) " +
    					"from PuntosPosicion PP " +
    					"where PP.pp_posicion = :POSICION " +
    					"and PP.pp_pa_id = :PARTIDA " +
    					"and PP.pp_id != :ID";
    			Query hqlQ = session.createQuery(hql);
    			if(puntosPosicion.getPp_id() != null) {
    				hqlQ.setLong("ID", puntosPosicion.getPp_id());
    			} else {
    				hqlQ.setLong("ID", Long.valueOf(-1));
    			}
    			hqlQ.setParameter("PARTIDA", partida);
    			hqlQ.setLong("POSICION", posicion);
    			Long cont = (Long) hqlQ.uniqueResult();
    			if(cont.compareTo(Long.valueOf(0)) != 0) {
    				Contexts.addErrorMessage("La posición " + posicion + " ya está asignada.");
    			}
    		}
    		
    		if(puntosPosicion.getPp_puntos() == null) {
    			Contexts.addErrorMessage("Debe indicar los puntos para el que acierte.");
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
			cargarListaPuntosPosicion();
		}
	}
	
    private void cargarListaCompetidores() {
    	Session session = Contexts.getHibernateSession();
    	listaCompetidores = session.createCriteria(Competidores.class)
    			.add(Restrictions.eq("co_pa_id", partida))
    			.addOrder(Order.asc("co_nombre"))
    			.list();
    }
    
    private void cargarListaEventos() {
    	Session session = Contexts.getHibernateSession();
    	listaEventos = session.createCriteria(Eventos.class)
    			.add(Restrictions.eq("ev_pa_id", partida))
    			.addOrder(Order.asc("ev_fecha_evento"))
    			.list();
    }
    
    private void cargarListaPuntosPosicion() {
    	Session session = Contexts.getHibernateSession();
    	listaPuntosPosicion = session.createCriteria(PuntosPosicion.class)
    			.add(Restrictions.eq("pp_pa_id", partida))
    			.addOrder(Order.asc("pp_posicion"))
    			.list();
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

	public List<PuntosPosicion> getListaPuntosPosicion() {
		return listaPuntosPosicion;
	}

	public void setListaPuntosPosicion(List<PuntosPosicion> listaPuntosPosicion) {
		this.listaPuntosPosicion = listaPuntosPosicion;
	}

	public PuntosPosicion getPuntosPosicion() {
		return puntosPosicion;
	}

	public void setPuntosPosicion(PuntosPosicion puntosPosicion) {
		this.puntosPosicion = puntosPosicion;
	}

	
}
