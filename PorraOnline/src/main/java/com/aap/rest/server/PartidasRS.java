package com.aap.rest.server;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.aap.dto.Partidas;
import com.aap.dto.Usuarios;
import com.aap.rest.exception.RestCustomException;

@Path("/partidas")
public class PartidasRS extends AbstractFacade<Partidas> {
	
	private static final Logger log = LoggerFactory.getLogger(PartidasRS.class);

	public PartidasRS() {
		super(Partidas.class);
	}
	
	@Context
	private HttpServletRequest request;
	
	@PermitAll
	@POST
	@Override
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Partidas create(Partidas entity) {
		if(entity != null) {
			Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
			Set<Usuarios> administradores = new HashSet<Usuarios>();
			Set<Usuarios> usuarios = new HashSet<Usuarios>();
			administradores.add(usuario);
			usuarios.add(usuario);
			entity.setAdministradores(administradores);
			entity.setUsuarios(usuarios);
			if(validaGuardarPartida(usuario, entity)) {
				return super.create(entity);
			}
		}
		return null;
	}
	
	private boolean validaGuardarPartida(Usuarios usuario, Partidas partida) {
		if(partida == null) {
			throw new RestCustomException("No se ha indicado ninguna partida que guardar.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		if(partida.getPa_nombre() == null || partida.getPa_nombre().isEmpty()) {
			throw new RestCustomException("El nombre de la partida es obligatorio.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
		}
		if(!administradorPartida(partida, usuario)) {
			throw new RestCustomException("Sólo los administradores pueden guardar partidas.", "Prohibido", Status.FORBIDDEN, RestCustomException.ERROR);
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
	public void edit(Partidas entity) {
		if(entity != null) {
			Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
			Partidas partida = (Partidas) getSession().get(Partidas.class, entity.getPa_id());
			partida.setPa_descripcion(entity.getPa_descripcion());
			partida.setPa_fecha_fin(entity.getPa_fecha_fin());
			partida.setPa_fecha_inicio(entity.getPa_fecha_inicio());
			partida.setPa_nombre(entity.getPa_nombre());
			if(validaGuardarPartida(usuario, partida)) {
				super.edit(partida);
			}
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
	public Partidas find(@PathParam("id") Long id) {
		return super.find(id);
	}
	
	@PermitAll
	@GET
	@Path("{suscritas}/{enCurso}")
	@Produces({ "application/json" })
	public List<Partidas> findAll(@PathParam("suscritas") Boolean suscritas, @PathParam("enCurso") Boolean enCurso) {
		Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
		String hql = "select PA " +
					 "from Partidas PA ";
		
		if(suscritas && usuario != null) {
			hql += "join PA.usuarios USU ";
		}
		
		hql += "where 1=1 ";
		
		if(suscritas && usuario != null) {
			hql += "and USU.usu_id = :ID_USUARIO ";
		}
		
		if(enCurso) {
			hql += "and COALESCE(PA.pa_fecha_fin,'3013-31-12') >= sysdate() ";
		}
		hql += "order by PA.pa_fecha_inicio desc ";
		
		Query hqlQ = getSession().createQuery(hql);
		
		if(suscritas && usuario != null) {
			hqlQ.setLong("ID_USUARIO", usuario.getUsu_id());
		}
		return hqlQ.list();
	}

	@PermitAll
	@GET
	@Path("admin/{id}")
	@Produces({ "application/json" })
	public Boolean esAdmin(@PathParam("id") Long id) {
		Session session = getSession();
		
		Partidas partida = (Partidas) session.get(Partidas.class, id);
		if(partida != null) {
			Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
			if(usuario == null) {
				return false;
			} if(partida.getAdministradores().contains(usuario)) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new RestCustomException("No existe la partida.", "Incorrecto", Status.BAD_REQUEST, RestCustomException.ERROR);
		}
	}
	
	@PermitAll
	@GET
	@Path("suscripcion/{id}")
	@Produces({ "application/json" })
	public Boolean suscrito(@PathParam("id") Long id) {
		Session session = getSession();
		
		Partidas partida = (Partidas) session.get(Partidas.class, id);
		if(partida != null) {
			Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
			if(usuario == null) {
				return false;
			} if(partida.getUsuarios().contains(usuario)) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new RestCustomException("No existe la partida.", "Incorrecto", Status.BAD_REQUEST, RestCustomException.ERROR);
		}
	}
	
	@PermitAll
	@PUT
	@Path("suscripcion/{id}/{suscribir}")
	@Produces({ "application/json" })
	public Boolean suscribir(@PathParam("id") Long id, @PathParam("suscribir") Boolean suscribir) {
		Session session = getSession();
		
		Partidas partida = (Partidas) session.get(Partidas.class, id);
		if(partida != null && suscribir != null) {
			Usuarios usuario = (Usuarios) request.getSession().getAttribute("usuario");
			if(usuario == null) {
				throw new RestCustomException("El usuario no está registrado.", "Incorrecto", Status.FORBIDDEN, RestCustomException.ERROR);
			} else {
				if(suscribir) {
					if(!partida.getUsuarios().contains(usuario)) {
						partida.getUsuarios().add(usuario);
						session.merge(partida);
						session.flush();
						return true;
					} else {
						throw new RestCustomException("El usuario ya está suscrito a la partida.", "Incorrecto", Status.NOT_FOUND, RestCustomException.ERROR);
					}
				} else {
					if(partida.getUsuarios().contains(usuario)) {
						partida.getUsuarios().remove(usuario);
						session.merge(partida);
						session.flush();
						return true;
					} else {
						throw new RestCustomException("El usuario no está suscrito a la partida.", "Incorrecto", Status.NOT_FOUND, RestCustomException.ERROR);
					}
				}
			}
			
		} else {
			throw new RestCustomException("No existe la partida.", "Incorrecto", Status.BAD_REQUEST, RestCustomException.ERROR);
		}
	}
	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}