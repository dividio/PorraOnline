package com.aap.rest.server;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.dto.Mensajes;
import com.aap.rest.exception.RestCustomException;

@Path("/mensajes")
public class MensajesRS extends AbstractFacade<Mensajes> {
	
	private static final Logger log = LoggerFactory.getLogger(MensajesRS.class);

	public MensajesRS() {
		super(Mensajes.class);
	}
	
	@Context
	private HttpServletRequest request;
	
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
	public Mensajes find(@PathParam("id") Long id) {
		String hql = "select ME " +
		             "from Mensajes ME " +
		             "where ME.me_id = :MENSAJE";
		Query hqlQ = getSession().createQuery(hql);
		hqlQ.setLong("MENSAJE", id);
		Mensajes elemento = (Mensajes) hqlQ.uniqueResult();
		if(elemento == null) {
			throw new RestCustomException("Elemento no encontrado", "Elemento no encontrado", Status.NOT_FOUND, RestCustomException.ERROR);
		}
		return elemento;
	}
	
	@PermitAll
	@GET
	@Produces({ "application/json" })
	public List<Mensajes> findAll() {
		return getSession().createCriteria(Mensajes.class)
				.addOrder(Order.desc("me_fecha"))
				.list();
	}
	
	@PermitAll
	@GET
	@Path("partida/{id}")
	@Produces({ "application/json" })
	public List<Mensajes> mensajesPartida(@PathParam("id") Long id) {
		String hql = "select ME "
				+ "from Mensajes ME "
				+ "join ME.me_pa_id PA "
				+ "where PA.pa_id = :ID_PARTIDA "
				+ "order by ME.me_fecha desc";
		Query hqlQ = getSession().createQuery(hql);
		hqlQ.setLong("ID_PARTIDA", id);
		return hqlQ.list();
		
	}

	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}