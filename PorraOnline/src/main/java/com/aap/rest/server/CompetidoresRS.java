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

import com.aap.dto.Competidores;
import com.aap.dto.Partidas;
import com.aap.dto.Usuarios;
import com.aap.rest.exception.RestCustomException;

@Path("/competidores")
public class CompetidoresRS extends AbstractFacade<Competidores> {
	
	private static final Logger log = LoggerFactory.getLogger(CompetidoresRS.class);

	public CompetidoresRS() {
		super(Competidores.class);
	}
	
	@Context
	private HttpServletRequest request;
	
	@PermitAll
	@POST
	@Path("partida/{id}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Competidores create(@PathParam("id") Long id, Competidores entity) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		Partidas partida = (Partidas) getSession().get(Partidas.class, id);
		if(validaGuardarCompetidor(partida, usuario, entity)) {
			

			entity.setCo_pa_id(partida);
			return super.create(entity);
		}
		return null;
	}
	
	private boolean validaGuardarCompetidor(Partidas partida, Usuarios usuario, Competidores evento) {
		if(evento == null) {
			throw new RestCustomException("No se ha indicado ningun competidor que guardar.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		if(!administradorPartida(partida, usuario)) {
			throw new RestCustomException("Sólo los administradores de la partida pueden guardar competidores.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		if(evento.getCo_nombre() == null || evento.getCo_nombre().isEmpty()) {
			throw new RestCustomException("Hace falta indicar el nombre del competidor.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		return true;
	}
	
	private boolean administradorPartida(Partidas partida, Usuarios usuario) {
		if(partida != null && usuario != null && partida.getAdministradores() != null && partida.getAdministradores().contains(usuario)) {
			return true;
		}
		return false;
	}

	@PermitAll
	@PUT
	@Override
	@Consumes({ "application/json" })
	public void edit(Competidores entity) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		Competidores competidor = (Competidores) getSession().get(Competidores.class, entity.getCo_id());
		competidor.setCo_nombre(entity.getCo_nombre());
		if(validaGuardarCompetidor(competidor.getCo_pa_id(), usuario, competidor)) {
			super.edit(competidor);
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
	public Competidores find(@PathParam("id") Long id) {
		return super.find(id);
	}
	
	@PermitAll
	@GET
	@Path("partida/{id}")
	@Produces({ "application/json" })
	public List<Competidores> findAll(@PathParam("id") Long id) {
		String hql = "select CO " +
				"from Competidores CO " +
				"join CO.co_pa_id PA " +
				"where PA.pa_id = :ID_PARTIDA " +
				"order by CO.co_nombre asc";
		Query hqlQ = getSession().createQuery(hql);
		hqlQ.setLong("ID_PARTIDA", id);
		
		return hqlQ.list();
	}

	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}