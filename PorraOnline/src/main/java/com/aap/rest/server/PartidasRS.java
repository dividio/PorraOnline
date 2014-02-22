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

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.dto.Partidas;
import com.aap.dto.Usuarios;

@Path("/partidas")
public class PartidasRS extends AbstractFacade<Partidas> {
	
	private static final Logger log = LoggerFactory.getLogger(PartidasRS.class);

	public PartidasRS() {
		super(Partidas.class);
	}
	
	@Context
	private HttpServletRequest request;
	
	@POST
	@Override
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Partidas create(Partidas entity) {
//		List<Mensajes> mensajes = validaCreate(entity);
//		if(mensajes == null || mensajes.isEmpty()) {
//			mensajes = null;
//			mensajes.clear();
			return super.create(entity);
//		} else {
//			throw new MyCustomException(mensajes.get(0).getMensaje(), "", Status.BAD_REQUEST, Mensajes.ERROR);
//		}
	}
	
//	private List<Mensajes> validaCreate(Partidas entity) {
//		List<Mensajes> mensajes = new ArrayList<Mensajes>();
//		if(entity == null) {
//			mensajes.add(new Mensajes(Mensajes.ERROR, "No hay ninguna provincia a guardar."));
//		} else if(entity.getProvincia() == null || entity.getProvincia().isEmpty()) {
//			mensajes.add(new Mensajes(Mensajes.ERROR, "Debe indicar el nombre de la provincia."));
//		}
//		return mensajes;
//	}
	
	@PUT
	@Override
	@Consumes({ "application/json" })
	public void edit(Partidas entity) {
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
	
	
	@Override
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}