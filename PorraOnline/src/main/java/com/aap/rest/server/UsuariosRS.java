package com.aap.rest.server;

import java.util.Set;

import javax.annotation.security.PermitAll;
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

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.dto.Partidas;
import com.aap.dto.Usuarios;
import com.aap.rest.exception.RestCustomException;

@Path("/usuarios")
public class UsuariosRS extends AbstractFacade<Usuarios> {
	
	private static final Logger log = LoggerFactory.getLogger(UsuariosRS.class);

	public UsuariosRS() {
		super(Usuarios.class);
	}
	
	@Context
	private HttpServletRequest request;
	
	@PermitAll
	@POST
	@Override
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Usuarios create(Usuarios entity) {
		if(validaGuardarUsuario(entity)) {
			return super.create(entity);
		}

		return null;
	}
	
	private boolean validaGuardarUsuario(Usuarios entity) {
		Usuarios usuarioSession = (Usuarios) request.getSession().getAttribute("usuario");
		if(usuarioSession != null) {
			throw new RestCustomException("Cerrar la sesión para crear un nuevo usuario.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		if(entity == null) {
			throw new RestCustomException("No se ha indicado ningun usuario que guardar.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		return true;
	}
	
	@PermitAll
	@PUT
	@Override
	@Consumes({ "application/json" })
	public void edit(Usuarios entity) {
//		if(entity != null) {
//			Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
//			Partidas partida = (Partidas) getSession().get(Partidas.class, entity.getPa_id());
//			partida.setPa_descripcion(entity.getPa_descripcion());
//			partida.setPa_fecha_fin(entity.getPa_fecha_fin());
//			partida.setPa_fecha_inicio(entity.getPa_fecha_inicio());
//			partida.setPa_nombre(entity.getPa_nombre());
//			if(validaGuardarUsuario(usuario, partida)) {
//				super.edit(partida);
//			}
//		}
	}
	
	@PermitAll
	@GET
	@Path("{id}")
	@Produces({ "application/json" })
	public Usuarios find(@PathParam("id") Long id) {
		return super.find(id);
	}
	
	@PermitAll
	@GET
	@Path("partida/{idPartida}")
	@Produces({ "application/json" })
	public Set<Usuarios> findAll(@PathParam("idPartida") Long idPartida) {
		Partidas partida = (Partidas) getSession().get(Partidas.class, idPartida);
		if(partida != null) {
			return partida.getUsuarios();
		}
		return null;
	}
	
	@PermitAll
	@GET
	@Path("administradores/{idPartida}")
	@Produces({ "application/json" })
	public Set<Usuarios> administradores(@PathParam("idPartida") Long idPartida) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		Partidas partida = (Partidas) getSession().get(Partidas.class, idPartida);
		if(administradorPartida(partida, usuario)) {
			return partida.getAdministradores();
		}
		return null;
	}
	
	private boolean administradorPartida(Partidas partida, Usuarios usuario) {
		if(partida != null && usuario != null && partida.getAdministradores() != null && partida.getAdministradores().contains(usuario)) {
			return true;
		}
		return false;
	}

	@PermitAll
	@POST
	@Path("administradores/{idPartida}")
	@Consumes({ "application/json" })
	public void guardarAdministrador(@PathParam("idPartida") Long idPartida, Usuarios entity) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		Partidas partida = (Partidas) getSession().get(Partidas.class, idPartida);
		if(administradorPartida(partida, usuario)) {
			Usuarios nuevoAdmin = (Usuarios) getSession().get(Usuarios.class, entity.getUsu_id());
			partida.getAdministradores().add(nuevoAdmin);
			getSession().merge(partida);
		}
	}
	
	@PermitAll
	@DELETE
	@Path("administradores/{idPartida}/{idUsuario}")
	public void eliminarAdministrador(@PathParam("idPartida") Long idPartida, @PathParam("idUsuario") Long idUsuario) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		Partidas partida = (Partidas) getSession().get(Partidas.class, idPartida);
		if(administradorPartida(partida, usuario)) {
			Usuarios nuevoAdmin = (Usuarios) getSession().get(Usuarios.class, idUsuario);
			partida.getAdministradores().remove(nuevoAdmin);
			getSession().merge(partida);
		}
	}
	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}