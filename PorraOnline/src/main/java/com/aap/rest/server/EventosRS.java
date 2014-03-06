package com.aap.rest.server;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.dto.Eventos;
import com.aap.dto.Partidas;
import com.aap.dto.Usuarios;
import com.aap.rest.exception.RestCustomException;

@Path("/eventos")
public class EventosRS extends AbstractFacade<Eventos> {
	
	private static final Logger log = LoggerFactory.getLogger(EventosRS.class);

	public EventosRS() {
		super(Eventos.class);
	}
	
	@Context
	private HttpServletRequest request;
	
	@PermitAll
	@POST
	@Path("partida/{id}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Eventos create(@PathParam("id") Long id, Eventos entity) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		if(validaGuardarEvento(id, usuario, entity)) {
			Partidas partida = (Partidas) getSession().get(Partidas.class, id);

			entity.setEv_pa_id(partida);
			return super.create(entity);
		}
		return null;
	}
	
	private boolean validaGuardarEvento(Long idPartida, Usuarios usuario, Eventos evento) {
		if(evento == null) {
			throw new RestCustomException("No se ha indicado ningun evento que guardar.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		if(!administradorPartida(idPartida, usuario)) {
			throw new RestCustomException("Hace falta estar suscrito a la partida para crear eventos.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		if(evento.getEv_nombre() == null || evento.getEv_nombre().isEmpty()) {
			throw new RestCustomException("Hace falta indicar el nombre del evento.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		return true;
	}
	
	private boolean administradorPartida(Long idPartida, Usuarios usuario) {
		if(idPartida != null && usuario != null && usuario.getUsu_id() != null) {
			String hql = "select count(*) " +
						"from Partidas PA " +
						"join PA.administradores USU " +
						"where USU.usu_id = :ID_USUARIO " +
						"and PA.pa_id = :ID_PARTIDA ";
			Query hqlQ = getSession().createQuery(hql);
			hqlQ.setLong("ID_USUARIO", usuario.getUsu_id());
			hqlQ.setLong("ID_PARTIDA", idPartida);
			Long cont = (Long) hqlQ.uniqueResult();
			return (cont.compareTo(Long.valueOf(0)) != 0);
		}
		return false;
	}

	@PermitAll
	@PUT
	@Override
	@Consumes({ "application/json" })
	public void edit(Eventos entity) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		Eventos evento = (Eventos) getSession().get(Eventos.class, entity.getEv_id());
		evento.setEv_fecha_evento(entity.getEv_fecha_evento());
		evento.setEv_fecha_inicio_pronosticos(entity.getEv_fecha_inicio_pronosticos());
		evento.setEv_fecha_limite_pronosticos(entity.getEv_fecha_limite_pronosticos());
		evento.setEv_lugar(entity.getEv_lugar());
		evento.setEv_nombre(entity.getEv_nombre());
		evento.setEv_url_referencia(entity.getEv_url_referencia());
		if(validaGuardarEvento(evento.getEv_pa_id().getPa_id(), usuario, evento)) {
			super.edit(evento);
		}
	}
	
	@RolesAllowed("ADMIN")
	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") Long id) {
		super.remove(find(id));
	}
	
	@PermitAll
	@GET
	@Path("{id}")
	@Produces({ "application/json" })
	public Eventos find(@PathParam("id") Long id) {
		return super.find(id);
	}
	
	@PermitAll
	@GET
	@Path("ultimo/{id}")
	@Produces({ "application/json" })
	public Eventos ultimoEvento(@PathParam("id") Long id) {
		if(id != null) {
			Session session = getSession();
			String hql = "select EV " +
					"from Eventos EV " +
					"join EV.ev_pa_id PA " +
					"where PA.pa_id = :ID_PARTIDA " +
					"and EV.ev_fecha_evento = " +
					"(select max(EV1.ev_fecha_evento) " +
					"from Eventos EV1 " +
					"join EV1.ev_pa_id PA1 " +
					"where PA1.pa_id = :ID_PARTIDA " +
					"and EV1.ev_fecha_evento <= sysdate())";
			Query hqlQ = session.createQuery(hql);
			hqlQ.setLong("ID_PARTIDA", id);
			
			return (Eventos) hqlQ.uniqueResult();
		}
		return null;
	}
	
	@PermitAll
	@GET
	@Path("partida/{id}")
	@Produces({ "application/json" })
	public List<Eventos> findAll(@PathParam("id") Long id) {
		String hql = "select EV " +
				"from Eventos EV " +
				"join EV.ev_pa_id PA " +
				"where PA.pa_id = :ID_PARTIDA " +
				"order by EV.ev_fecha_evento asc";
		Query hqlQ = getSession().createQuery(hql);
		hqlQ.setLong("ID_PARTIDA", id);
		
		return hqlQ.list();
	}

	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}