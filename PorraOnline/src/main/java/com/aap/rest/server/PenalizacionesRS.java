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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.dto.Penalizaciones;
import com.aap.rest.exception.RestCustomException;

@Path("/penalizaciones")
public class PenalizacionesRS extends AbstractFacade<Penalizaciones> {
	
	private static final Logger log = LoggerFactory.getLogger(PenalizacionesRS.class);

	public PenalizacionesRS() {
		super(Penalizaciones.class);
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
	public Penalizaciones find(@PathParam("id") Long id) {
		String hql = "select PE " +
		             "from Penalizaciones PE " +
		             "where PE.pe_id = :ID";
		Query hqlQ = getSession().createQuery(hql);
		hqlQ.setLong("ID", id);
		Penalizaciones elemento = (Penalizaciones) hqlQ.uniqueResult();
		if(elemento == null) {
			throw new RestCustomException("Elemento no encontrado", "Elemento no encontrado", Status.NOT_FOUND, RestCustomException.ERROR);
		}
		return elemento;
	}
	
	@PermitAll
	@GET
	@Path("partida/{idPartida}")
	@Produces({ "application/json" })
	public List<Penalizaciones> findAll(@PathParam("idPartida") Long idPartida) {
		String hql = "select PE " +
					"from Penalizaciones PE " +
					"join PE.pe_pa_id PA " +
					"where PA.pa_id = :ID_PARTIDA ";
		Query hqlQ = getSession().createQuery(hql);
		hqlQ.setLong("ID_PARTIDA", idPartida);
		return hqlQ.list();
	}

	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}