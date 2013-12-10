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

import com.aap.dto.Eventos;
import com.aap.dto.Partidas;
import com.aap.dto.Pronosticos;
import com.aap.dto.Resultados;
import com.aap.dto.Usuarios;
import com.aap.util.ApplicationUtil;
import com.aap.util.jsf.Contexts;

@ManagedBean
@ViewScoped
public class PronosticosUsuarioBean implements Serializable {

    private static final long serialVersionUID = 1081888871484697707L;

	private Long idPartida;
	
	private Partidas partida = new Partidas();
	
	private Long idUsuario;
	
	private Usuarios usuario = new Usuarios();
    
    private Eventos evento = new Eventos();
    
    private Boolean eventoPasado = Boolean.FALSE;
    
    private Boolean hayResultados = Boolean.FALSE;
        
    private Pronosticos pronostico = new Pronosticos();
            
    private List<Pronosticos> listaPronosticos = new ArrayList<Pronosticos>();
    
    private List<Resultados> listaResultados = new ArrayList<Resultados>();
    
    private List<Eventos> listaEventos = new ArrayList<Eventos>();
    
    public String cargarListaEventos() {
    	if(listaEventos.isEmpty()) {
	    	Session session = Contexts.getHibernateSession();
	    	listaEventos = session.createCriteria(Eventos.class)
	    			.add(Restrictions.eq("ev_pa_id", partida))
	    			.addOrder(Order.asc("ev_fecha_evento"))
	    			.list();
    	}
    	return null;
    }
    
    public String cargarNuevoEvento() {
    	cargarEvento();
    	cargarPronosticosEvento();
    	cargarListaResultados();
    	return null;
    }    
    
    public Long getIdPartida() {
		return idPartida;
	}

	public void setIdPartida(Long idPartida) {
		this.idPartida = idPartida;
		if(ApplicationUtil.cambioPartida(partida, idPartida)) {
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
				cargarProximoEvento();
				cargarPronosticosEvento();
				cargarListaResultados();
			}
		}
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
		if(idUsuario != null) {
			Session session = Contexts.getHibernateSession();
			usuario = (Usuarios) session.get(Usuarios.class, idUsuario);
		}
	}
	
	private void cargarProximoEvento() {
		if(partida != null && partida.getPa_id() != null) {
			Session session = Contexts.getHibernateSession();
			String hql = "select EV " +
					"from Eventos EV " +
					"join EV.ev_pa_id PA " +
					"where PA.pa_id = :ID_PARTIDA " +
					"and EV.ev_fecha_evento = " +
					"(select min(EV1.ev_fecha_evento) " +
					"from Eventos EV1 " +
					"join EV1.ev_pa_id PA1 " +
					"where PA1.pa_id = :ID_PARTIDA " +
					"and EV1.ev_fecha_evento >= :FECHA)";
			Query hqlQ = session.createQuery(hql);
			hqlQ.setLong("ID_PARTIDA", partida.getPa_id());
			hqlQ.setDate("FECHA", new Date());
			
			evento = (Eventos) hqlQ.uniqueResult();
			cargarEvento();
		}
		
	}
	
	private void cargarEvento() {
		if(evento != null) {
			Date fechaFin = evento.getEv_fecha_limite_pronosticos();
			Date fechaActual = new Date();
			if(fechaFin.before(fechaActual)) {
				eventoPasado = Boolean.TRUE;
			} else {
				eventoPasado = Boolean.FALSE;
			}
		}
	}
	
	private void cargarPronosticosEvento() {
		if(eventoPasado && evento != null && evento.getEv_id() != null && usuario != null && usuario.getUsu_id() != null) {
			Session session = Contexts.getHibernateSession();
			listaPronosticos = session.createCriteria(Pronosticos.class)
					.add(Restrictions.eq("pr_ev_id", evento))
					.add(Restrictions.eq("pr_usu_id", usuario))
					.addOrder(Order.asc("pr_posicion"))
					.list();
		} else {
			listaPronosticos = new ArrayList<Pronosticos>();
		}
	}
	
	private void cargarListaResultados() {
		if(evento != null && evento.getEv_id() != null) {
			Session session = Contexts.getHibernateSession();
			listaResultados = new ArrayList<Resultados>();
			listaResultados = session.createCriteria(Resultados.class)
					.add(Restrictions.eq("re_ev_id", evento))
					.addOrder(Order.asc("re_posicion"))
					.list();
		} else {
			listaResultados = new ArrayList<Resultados>();
		}
		if(!listaResultados.isEmpty()) {
			hayResultados = Boolean.TRUE;
		} else {
			hayResultados = Boolean.FALSE;
		}
    }
	
	public Partidas getPartida() {
		return partida;
	}

	public void setPartida(Partidas partida) {
		this.partida = partida;
	}

	public Eventos getEvento() {
		return evento;
	}

	public void setEvento(Eventos evento) {
		this.evento = evento;
	}

	public List<Pronosticos> getListaPronosticos() {
		return listaPronosticos;
	}

	public void setListaPronosticos(List<Pronosticos> listaPronosticos) {
		this.listaPronosticos = listaPronosticos;
	}

	public Pronosticos getPronostico() {
		return pronostico;
	}

	public void setPronostico(Pronosticos pronostico) {
		this.pronostico = pronostico;
	}

	public List<Eventos> getListaEventos() {
		return listaEventos;
	}

	public void setListaEventos(List<Eventos> listaEventos) {
		this.listaEventos = listaEventos;
	}

	public List<Resultados> getListaResultados() {
		return listaResultados;
	}

	public void setListaResultados(List<Resultados> listaResultados) {
		this.listaResultados = listaResultados;
	}

	public Boolean getHayResultados() {
		return hayResultados;
	}

	public void setHayResultados(Boolean hayResultados) {
		this.hayResultados = hayResultados;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}

	public Boolean getEventoPasado() {
		return eventoPasado;
	}

	public void setEventoPasado(Boolean eventoPasado) {
		this.eventoPasado = eventoPasado;
	}
	
	
}