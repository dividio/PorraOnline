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
		if(validaGuardarMensaje(id, usuario, entity)) {
			Partidas partida = (Partidas) getSession().get(Partidas.class, id);

			entity.setEv_pa_id(partida);
			return super.create(entity);
		}
		return null;
	}
	
	private boolean validaGuardarMensaje(Long idPartida, Usuarios usuario, Eventos evento) {
		if(!suscritoPartida(idPartida, usuario)) {
			throw new RestCustomException("Hace falta estar suscrito a la partida para crear eventos.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		return true;
	}
	
	private boolean suscritoPartida(Long idPartida, Usuarios usuario) {
		if(idPartida != null && usuario != null && usuario.getUsu_id() != null) {
			String hql = "select count(*) " +
						"from Partidas PA " +
						"join PA.usuarios USU " +
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

	@PUT
	@Override
	@Consumes({ "application/json" })
	public void edit(Eventos entity) {
		super.edit(entity);
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
	@Path("{id}")
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