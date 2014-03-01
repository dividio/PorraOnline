package com.aap.rest.server;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.dto.Partidas;
import com.aap.dto.Posiciones;

@Path("/clasificacion")
public class ClasificacionRS {
	
	private static final Logger log = LoggerFactory.getLogger(ClasificacionRS.class);

	public ClasificacionRS() {
	}
	
	@Context
	private HttpServletRequest request;
	
	@PermitAll
	@GET
	@Path("{id}")
	@Produces({ "application/json" })
	public List<Posiciones> general(@PathParam("id") Long id) {
		Session session = getSession();
		List<Posiciones> clasificacion = new ArrayList<Posiciones>();
		Partidas partida = (Partidas) session.get(Partidas.class, id);
		
		if(partida != null && partida.getPa_id() != null) {
			String hql = "select USU.usu_id, USU.usu_username, sum(coalesce(PR.pr_puntos_conseguidos,0)) " +
						"from Pronosticos PR " +
						"join PR.pr_usu_id USU " +
						"join PR.pr_ev_id EV " +
						"join EV.ev_pa_id PA " +
						"where PA.pa_id = :ID_PARTIDA " +
						"group by USU.usu_id, USU.usu_username " +
						"order by sum(coalesce(PR.pr_puntos_conseguidos,0)) desc, USU.usu_username ";
			Query hqlQ = session.createQuery(hql);
			hqlQ.setLong("ID_PARTIDA", partida.getPa_id());
			List<Object[]> datos = hqlQ.list();
			
			int cont = 1;
			for(Object[] puesto:datos) {
				Posiciones posicion = new Posiciones();
				
				posicion.setIdPartida(id);
				posicion.setPosicion(Long.valueOf(cont++));
				posicion.setUsu_id((Long)puesto[0]);
				posicion.setUsu_username((String)puesto[1]);
				posicion.setPuntos((Long)puesto[2]);
				
				clasificacion.add(posicion);
			}
		}
		return clasificacion;
	}
	
	@PermitAll
	@GET
	@Path("{id}/evento/{idEvento}")
	@Produces({ "application/json" })
	public List<Posiciones> evento(@PathParam("id") Long id, @PathParam("idEvento") Long idEvento) {
		Session session = getSession();
		List<Posiciones> clasificacion = new ArrayList<Posiciones>();
		Partidas partida = (Partidas) session.get(Partidas.class, id);
		
		if(partida != null && partida.getPa_id() != null && idEvento != null) {
			String hql = "select USU.usu_id, USU.usu_username, sum(coalesce(PR.pr_puntos_conseguidos,0)) " +
						"from Pronosticos PR " +
						"join PR.pr_usu_id USU " +
						"join PR.pr_ev_id EV " +
						"join EV.ev_pa_id PA " +
						"where PA.pa_id = :ID_PARTIDA " +
						"and EV.ev_id = :EVENTO " +
						"group by USU.usu_id, USU.usu_username " +
						"order by sum(coalesce(PR.pr_puntos_conseguidos,0)) desc, USU.usu_username ";
			Query hqlQ = session.createQuery(hql);
			hqlQ.setLong("ID_PARTIDA", partida.getPa_id());
			hqlQ.setLong("EVENTO", idEvento);
			List<Object[]> datos = hqlQ.list();
			
			int cont = 1;
			for(Object[] puesto:datos) {
				Posiciones posicion = new Posiciones();
				
				posicion.setIdPartida(id);
				posicion.setPosicion(Long.valueOf(cont++));
				posicion.setUsu_id((Long)puesto[0]);
				posicion.setUsu_username((String)puesto[1]);
				posicion.setPuntos((Long)puesto[2]);
				
				clasificacion.add(posicion);
			}
		}
		return clasificacion;
	}
	
	private Session getSession() {
		return (Session) request.getAttribute("session");
	}
}