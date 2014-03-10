package com.aap.rest.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.aap.dto.Eventos;
import com.aap.dto.Partidas;
import com.aap.dto.Pronosticos;
import com.aap.dto.Usuarios;
import com.aap.rest.exception.RestCustomException;

@Path("/pronosticos")
public class PronosticosRS extends AbstractFacade<Pronosticos> {
	
	private static final Logger log = LoggerFactory.getLogger(PronosticosRS.class);

	public PronosticosRS() {
		super(Pronosticos.class);
	}
	
	@Context
	private HttpServletRequest request;
	
	@PermitAll
	@POST
	@Path("{idEvento}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public List<Pronosticos> create(@PathParam("idEvento") Long idEvento, List<Pronosticos> listaNuevosPronosticos) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		Eventos evento = (Eventos) getSession().get(Eventos.class, idEvento);
		if(validaGuardarPronosticos(evento, usuario, listaNuevosPronosticos)) {
			Session session = getSession();
			List<Pronosticos> pronosticosAntiguos = session.createCriteria(Pronosticos.class)
					.add(Restrictions.eq("pr_ev_id", evento))
					.add(Restrictions.eq("pr_usu_id", usuario))
					.list();
			for(Pronosticos pronostico:pronosticosAntiguos) {
				session.delete(pronostico);
			}
			for(Pronosticos pronostico:listaNuevosPronosticos) {
				Pronosticos nuevoPronostico = new Pronosticos();
				nuevoPronostico.setPr_posicion(pronostico.getPr_posicion());
				nuevoPronostico.setPr_co_id(pronostico.getPr_co_id());
				nuevoPronostico.setPr_ev_id(evento);
				nuevoPronostico.setPr_usu_id(usuario);
				nuevoPronostico.setPr_bo_id(pronostico.getPr_bo_id());
				session.save(nuevoPronostico);
			}
			session.flush();
		}
		return null;
	}
	
	private boolean validaGuardarPronosticos(Eventos evento, Usuarios usuario, List<Pronosticos> listaNuevosPronosticos) {
		if(evento == null) {
			throw new RestCustomException("El evento no existe.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		Date fechaActual = new Date();
		if(!(evento.getEv_fecha_limite_pronosticos().after(fechaActual) && evento.getEv_fecha_inicio_pronosticos().before(fechaActual))) {
			throw new RestCustomException("No se puede modificar el pronóstico del evento.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		Partidas partida = evento.getEv_pa_id();
		if(!usuarioPartida(partida, usuario)) {
			throw new RestCustomException("Sólo los usuarios suscritos a la partida pueden realizar pronósticos.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		//TODO: validaciones adicionales: bonificaciones pertenecen a partida, numero_pronosticos x bonificacion, no se repiten posiciones
		for(Pronosticos pronostico:listaNuevosPronosticos) {
			
		}
		
		
		return true;
	}
	
	private boolean usuarioPartida(Partidas partida, Usuarios usuario) {
		if(partida != null && usuario != null && partida.getUsuarios() != null && partida.getUsuarios().contains(usuario)) {
			return true;
		}
		return false;
	}

	@PermitAll
	@GET
	@Path("{idEvento}/{idUsuario}")
	@Produces({ "application/json" })
	public List<Pronosticos> findAll(@PathParam("idEvento") Long idEvento, @PathParam("idUsuario") Long idUsuario) {
		if(idEvento != null && idUsuario != null) {
			Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
			boolean mismoUsuario = usuario != null && usuario.getUsu_id().compareTo(idUsuario) == 0;
			
			String hql = "select PR " +
					"from Pronosticos PR " +
					"join PR.pr_ev_id EV " +
					"join PR.pr_usu_id USU " +
					"where EV.ev_id = :ID_EVENTO " +
					"and USU.usu_id = :ID_USUARIO ";
			if(!mismoUsuario) {
				hql += "and EV.ev_fecha_limite_pronosticos < sysdate() ";
			}
			Query hqlQ = getSession().createQuery(hql);
			hqlQ.setLong("ID_EVENTO", idEvento);
			hqlQ.setLong("ID_USUARIO", idUsuario);
			
			return hqlQ.list();
		}
		return new ArrayList<Pronosticos>();
	}

	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}