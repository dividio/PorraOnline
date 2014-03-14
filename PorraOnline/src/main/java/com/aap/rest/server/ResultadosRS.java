package com.aap.rest.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.dto.Bonificaciones;
import com.aap.dto.Eventos;
import com.aap.dto.Partidas;
import com.aap.dto.Pronosticos;
import com.aap.dto.PuntosPosicion;
import com.aap.dto.Resultados;
import com.aap.dto.Usuarios;
import com.aap.rest.exception.RestCustomException;

@Path("/resultados")
public class ResultadosRS extends AbstractFacade<Resultados> {
	
	private static final Logger log = LoggerFactory.getLogger(ResultadosRS.class);

	public ResultadosRS() {
		super(Resultados.class);
	}
	
	@Context
	private HttpServletRequest request;
	
	@PermitAll
	@POST
	@Path("{idEvento}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public List<Resultados> create(@PathParam("idEvento") Long idEvento, List<Resultados> listaNuevosResultados) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		Eventos evento = (Eventos) getSession().get(Eventos.class, idEvento);
		if(validaGuardarResultados(evento, usuario, listaNuevosResultados)) {
			Session session = getSession();
			List<Resultados> resultadosAntiguos = evento.getListaResultados();
			for(Resultados resultado:resultadosAntiguos) {
				session.delete(resultado);
			}
			resultadosAntiguos.clear();
			
			for(Resultados resultado:listaNuevosResultados) {
				Resultados nuevoResultado = new Resultados();
				nuevoResultado.setRe_posicion(resultado.getRe_posicion());
				nuevoResultado.setRe_co_id(resultado.getRe_co_id());
				nuevoResultado.setRe_ev_id(evento);
				nuevoResultado.setRe_bo_id(resultado.getRe_bo_id());
				nuevoResultado.setRe_pe_id(resultado.getRe_pe_id());
				session.save(nuevoResultado);
				resultadosAntiguos.add(nuevoResultado);
			}
			
			actualizarPuntuacionesEvento(evento);
			
			session.flush();
			
			return resultadosAntiguos;
		}
		return null;
	}
	
	private boolean validaGuardarResultados(Eventos evento, Usuarios usuario, List<Resultados> listaNuevosPronosticos) {
		if(evento == null) {
			throw new RestCustomException("El evento no existe.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		Date fechaActual = new Date();
		if(evento.getEv_fecha_limite_pronosticos().after(fechaActual)){
			throw new RestCustomException("No se pueden introducir los resultados del evento antes de que pase la fecha limite para los pronósticos.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		Partidas partida = evento.getEv_pa_id();
		if(!administradorPartida(partida, usuario)) {
			throw new RestCustomException("Sólo los administradores de la partida pueden modificar los resultados.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		//TODO: validaciones adicionales: bonificaciones pertenecen a partida, numero_pronosticos x bonificacion, no se repiten posiciones
		for(Resultados pronostico:listaNuevosPronosticos) {
			
		}
		
		
		return true;
	}
	
	private boolean administradorPartida(Partidas partida, Usuarios usuario) {
		if(partida != null && usuario != null && partida.getUsuarios() != null && partida.getAdministradores().contains(usuario)) {
			return true;
		}
		return false;
	}
	
	private void actualizarPuntuacionesEvento(Eventos evento) {
    	Session session = getSession();
    	
    	Map<Long, Map<Long, Resultados>> mapaBonificaciones = new HashMap<Long, Map<Long, Resultados>>();
    	for(Bonificaciones bonificacion:evento.getEv_pa_id().getListaBonificaciones()) {
    		Map<Long, Resultados> mapaPosiciones = new HashMap<Long, Resultados>();
    		mapaBonificaciones.put(bonificacion.getBo_id(), mapaPosiciones);
    	}
    	
    	List<Resultados> listaResultados = evento.getListaResultados();
    	for(Resultados resultado:listaResultados) {
    		if(resultado.getRe_bo_id() != null) {
    			mapaBonificaciones.get(resultado.getRe_bo_id().getBo_id()).put(resultado.getRe_co_id().getCo_id(), resultado);
    		}
    	}
    	
    	
    	List<Pronosticos> pronosticosUsuarios = session.createCriteria(Pronosticos.class)
    			.add(Restrictions.eq("pr_ev_id", evento))
    			.list();
    	for(Pronosticos pronostico:pronosticosUsuarios) {
    		Long puntos = Long.valueOf(0);
    		Long idCompetidor = pronostico.getPr_co_id().getCo_id();
    		Bonificaciones bonificacion = pronostico.getPr_bo_id();
    		Resultados resultado = mapaBonificaciones.get(bonificacion.getBo_id()).get(idCompetidor);
    		if(resultado != null) {
	    		Long posicionFinal = resultado.getRe_posicion();
	    		
	    		if(posicionFinal != null) {
	    			List<PuntosPosicion> listaPuntos = bonificacion.getListaPuntosPosicion();
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
	    		
	    		if(resultado.getRe_pe_id() != null){
	    			puntos += resultado.getRe_pe_id().getPe_puntos();
	    		}
    		}
    		
    		pronostico.setPr_puntos_conseguidos(puntos);
    		session.merge(pronostico);
    	}
    }

	@PermitAll
	@GET
	@Path("{idEvento}")
	@Produces({ "application/json" })
	public List<Resultados> findAll(@PathParam("idEvento") Long idEvento) {
		if(idEvento != null) {
			String hql = "select RE " +
					"from Resultados RE " +
					"join RE.re_ev_id EV " +
					"where EV.ev_id = :ID_EVENTO ";
			
			Query hqlQ = getSession().createQuery(hql);
			hqlQ.setLong("ID_EVENTO", idEvento);
			
			return hqlQ.list();
		}
		return new ArrayList<Resultados>();
	}

	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}