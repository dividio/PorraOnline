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

import com.aap.dto.Noticias;
import com.aap.rest.exception.RestCustomException;

@Path("/noticias")
public class NoticiasRS extends AbstractFacade<Noticias> {
	
	private static final Logger log = LoggerFactory.getLogger(NoticiasRS.class);

	public NoticiasRS() {
		super(Noticias.class);
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
	public Noticias find(@PathParam("id") Long id) {
		String hql = "select NO " +
		             "from Noticias NO " +
		             "where NO.no_id = :NOTICIA";
		Query hqlQ = getSession().createQuery(hql);
		hqlQ.setLong("NOTICIA", id);
		Noticias elemento = (Noticias) hqlQ.uniqueResult();
		if(elemento == null) {
			throw new RestCustomException("Elemento no encontrado", "Elemento no encontrado", Status.NOT_FOUND, RestCustomException.ERROR);
		}
		return elemento;
	}
	
	@PermitAll
	@GET
	@Produces({ "application/json" })
	public List<Noticias> findAll() {
		return getSession().createCriteria(Noticias.class)
				.addOrder(Order.desc("no_fecha"))
				.list();
	}

	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}