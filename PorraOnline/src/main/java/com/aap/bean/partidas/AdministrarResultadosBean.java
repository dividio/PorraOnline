package com.aap.bean.partidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.aap.dto.Competidores;
import com.aap.dto.Eventos;
import com.aap.dto.Partidas;
import com.aap.dto.Pronosticos;
import com.aap.dto.PuntosPosicion;
import com.aap.dto.Resultados;
import com.aap.dto.Usuarios;
import com.aap.util.ApplicationUtil;
import com.aap.util.jsf.Contexts;

@ManagedBean
@ViewScoped
public class AdministrarResultadosBean implements Serializable {

    private static final long serialVersionUID = -1256949115644134639L;

	private Long idPartida;
    
    private Partidas partida = new Partidas();
    
    private Eventos evento = new Eventos();
    
    private Boolean hayCambios = Boolean.FALSE;
    
    private Boolean editable = Boolean.FALSE;
    
    private Boolean hayResultados = Boolean.FALSE;
        
    private Resultados resultado = new Resultados();
        
    List<Resultados> listaResultadosSinAsignar = new ArrayList<Resultados>();
        
    List<Resultados> listaResultados = new ArrayList<Resultados>();
    
    List<Eventos> listaEventos = new ArrayList<Eventos>();
    
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
    	cargarListaResultados();
    	cargarListaCompetidores();
    	return null;
    }
    
    public String guardarResultados() {
    	Session session = Contexts.getHibernateSession();
    	int posicion = 1;
    	for(Resultados resultado:listaResultados) {
    		resultado.setRe_posicion(Long.valueOf(posicion++));
    		if(resultado.getRe_id() == null || resultado.getRe_id().compareTo(Long.valueOf(0)) <= 0) {
    			resultado.setRe_id(null);
    			session.save(resultado);
    		} else {
    			session.merge(resultado);
    		}
    	}
    	for(Resultados resultado:listaResultadosSinAsignar) {
    		if(resultado.getRe_id() != null && resultado.getRe_id().compareTo(Long.valueOf(0)) > 0) {
    			session.delete(resultado);
    		}
    	}
    	
    	actualizarPuntuacionesEvento();
    	
    	session.flush();
    	hayCambios = Boolean.FALSE;
    	Contexts.addInfoMessage("El resultado ha sido guardado correctamente.");
    	return null;
    }
    
    private void actualizarPuntuacionesEvento() {
    	Session session = Contexts.getHibernateSession();
    	List<PuntosPosicion> listaPuntos = session.createCriteria(PuntosPosicion.class)
    			.add(Restrictions.eq("pp_pa_id", partida))
    			.addOrder(Order.asc("pp_posicion"))
    			.list();
    	Map<Long, Long> mapaPosiciones = new HashMap<Long, Long>();
    	for(Resultados resultado:listaResultados) {
    		mapaPosiciones.put(resultado.getRe_co_id().getCo_id(), resultado.getRe_posicion());
    	}
    	
    	List<Pronosticos> pronosticosUsuarios = session.createCriteria(Pronosticos.class)
    			.add(Restrictions.eq("pr_ev_id", evento))
    			.list();
    	for(Pronosticos pronostico:pronosticosUsuarios) {
    		Long puntos = Long.valueOf(0);
    		Long idCompetidor = pronostico.getPr_co_id().getCo_id();
    		Long posicionFinal = mapaPosiciones.get(idCompetidor);
    		
    		if(posicionFinal != null) {
        		int posicionPronostico = pronostico.getPr_posicion().intValue();
        		int posicionObtenida = posicionFinal.intValue();
    			if(listaPuntos.size() >= posicionObtenida) {
        			int diferencia = posicionPronostico-posicionObtenida;
        			diferencia = diferencia * Integer.signum(diferencia);
	    			PuntosPosicion puntosPosicion = listaPuntos.get(posicionObtenida-1);
	    			int nivelAproximacion = puntosPosicion.getPp_nivel_aproximacion().intValue();
	    			
	    			if(diferencia == 0) {
	    				puntos = puntosPosicion.getPp_puntos();
	    			} else if(diferencia <= nivelAproximacion) {
	    				puntos = puntosPosicion.getPp_puntos_aproximacion();
	    			}
    			}
    		}
    		pronostico.setPr_puntos_conseguidos(puntos);
    		session.merge(pronostico);
    	}
    	session.flush();
    }
    
    public String agregarResultado() {    	
    	listaResultados.add(resultado);
    	listaResultadosSinAsignar.remove(resultado);
    	
    	Long posicion = Long.valueOf(listaResultados.size());
    	resultado.setRe_posicion(posicion);
    	
    	hayCambios = Boolean.TRUE;
    	
    	return null;
    }
    
    public String subirResultado() {
    	int posicion = resultado.getRe_posicion().intValue();
    	if(posicion != 1) {
    		Resultados resultadoAnterior = listaResultados.get(posicion-2);
    		resultadoAnterior.setRe_posicion(Long.valueOf(posicion));
    		resultado.setRe_posicion(Long.valueOf(posicion-1));
    		listaResultados.set(posicion-2, resultado);
    		listaResultados.set(posicion-1, resultadoAnterior);
    		hayCambios = Boolean.TRUE;
    	}
    	
    	return null;
    }
    

    public String bajarResultado() {
    	int posicion = resultado.getRe_posicion().intValue();
    	if(posicion != listaResultados.size()) {
    		Resultados resultadoPosterior = listaResultados.get(posicion);
    		resultadoPosterior.setRe_posicion(Long.valueOf(posicion));
    		resultado.setRe_posicion(Long.valueOf(posicion+1));
    		listaResultados.set(posicion, resultado);
    		listaResultados.set(posicion-1, resultadoPosterior);
    		hayCambios = Boolean.TRUE;
    	}
    	
    	return null;
    }
    
    public String eliminarResultado() {
    	listaResultados.remove(resultado);
    	listaResultadosSinAsignar.add(resultado);
    	
    	int posicion = 1;
    	for(Resultados resultado:listaResultados) {
    		resultado.setRe_posicion(Long.valueOf(posicion++));
    	}
    	hayCambios = Boolean.TRUE;
    	
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
						"join PA.administradores USU " +
						"where USU.usu_id = :ID_USUARIO " +
						"and PA.pa_id = :ID_PARTIDA";
				Query hqlQ = session.createQuery(hql);
				hqlQ.setLong("ID_USUARIO", usuario.getUsu_id());
				hqlQ.setLong("ID_PARTIDA", idPartida);
				partida = (Partidas) hqlQ.uniqueResult();
				cargarProximoEvento();
				cargarListaResultados();
				cargarListaCompetidores();
			}
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
			Date fechaEvento = evento.getEv_fecha_evento();
			Date fechaActual = new Date();
			if(fechaEvento.before(fechaActual)) {
				editable = Boolean.TRUE;
			} else {
				editable = Boolean.FALSE;
			}
		}
	}
	
	private void cargarListaCompetidores() {
		listaResultadosSinAsignar = new ArrayList<Resultados>();
		if(partida != null && partida.getPa_id() != null && evento != null && evento.getEv_id() != null) {
	    	Session session = Contexts.getHibernateSession();
	    	String hql = "select CO " +
	    			"from Competidores CO " +
	    			"join CO.co_pa_id PA " +
	    			"where PA.pa_id = :ID_PARTIDA " +
	    			"and not exists (select CO1.co_id " +
	    			"from Resultados RE " +
	    			"join RE.re_co_id CO1 " +
	    			"where RE.re_ev_id = :EVENTO " +
	    			"and CO1.co_id = CO.co_id)";
	    	Query hqlQ = session.createQuery(hql);
	    	hqlQ.setLong("ID_PARTIDA", partida.getPa_id());
	    	hqlQ.setParameter("EVENTO", evento);
	    	List<Competidores> competidoresLibres = hqlQ.list();
	    	int indice = -1;
	    	for(Competidores competidor:competidoresLibres) {
	    		
	    		Resultados resultado = new Resultados();
	        	resultado.setRe_id(Long.valueOf(indice--));
	        	resultado.setRe_ev_id(evento);
	        	resultado.setRe_co_id(competidor);
	        	
	        	listaResultadosSinAsignar.add(resultado);
	    	}
		}
    }
	
	private void cargarListaResultados() {
		listaResultados = new ArrayList<Resultados>();
		if(evento != null && evento.getEv_id() != null) {
			Session session = Contexts.getHibernateSession();
			listaResultados = session.createCriteria(Resultados.class)
					.add(Restrictions.eq("re_ev_id", evento))
					.addOrder(Order.asc("re_posicion"))
					.list();
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

	public Boolean getHayCambios() {
		return hayCambios;
	}

	public void setHayCambios(Boolean hayCambios) {
		this.hayCambios = hayCambios;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
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

	public Resultados getResultado() {
		return resultado;
	}

	public void setResultado(Resultados resultado) {
		this.resultado = resultado;
	}

	public List<Resultados> getListaResultadosSinAsignar() {
		return listaResultadosSinAsignar;
	}

	public void setListaResultadosSinAsignar(List<Resultados> listaResultadosSinAsignar) {
		this.listaResultadosSinAsignar = listaResultadosSinAsignar;
	}
	
	
}