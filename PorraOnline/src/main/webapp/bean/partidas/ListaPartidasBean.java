package com.aap.bean.partidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Query;
import org.hibernate.Session;

import com.aap.dto.Partidas;
import com.aap.dto.Usuarios;
import com.aap.util.jsf.Contexts;

@ManagedBean
@ViewScoped
public class ListaPartidasBean implements Serializable {

    private static final long serialVersionUID = -4110764373104975536L;
	
    private List<Partidas> listaPartidas = new ArrayList<Partidas>();
    private List<Partidas> listaPartidasDisponibles = new ArrayList<Partidas>();
    private Partidas partida = null;
	
	@PostConstruct
	public void init() {
		cargarListaPartidas();
	}
	
	private void cargarListaPartidas() {
		Usuarios usuario = (Usuarios) Contexts.getSessionAttribute("usuario");
		
		if(usuario != null) {
			Session session = Contexts.getHibernateSession();
			String hql = "select PA " +
					"from Partidas PA " +
					"join PA.usuarios USU " +
					"where USU.usu_id = :ID_USUARIO";
			Query hqlQ = session.createQuery(hql);
			hqlQ.setLong("ID_USUARIO", usuario.getUsu_id());
			listaPartidas = hqlQ.list();
		}
	}
	
	public String mostrarPanelNuevaSuscripcion() {
		Usuarios usuario = (Usuarios) Contexts.getSessionAttribute("usuario");
		
		if(usuario != null) {
			Session session = Contexts.getHibernateSession();
			String hql = "select PA " +
					"from Partidas PA " +
					"where COALESCE(PA.pa_fecha_fin,'2013-31-12') >= :FECHA " +
					"and PA.pa_id not in (select PA1.pa_id " +
					"from Partidas PA1 " +
					"join PA1.usuarios USU " +
					"where USU.usu_id = :ID_USUARIO)";
			Query hqlQ = session.createQuery(hql);
			hqlQ.setLong("ID_USUARIO", usuario.getUsu_id());
			hqlQ.setDate("FECHA", new Date());
			listaPartidasDisponibles = hqlQ.list();
		}
		return null;
	}
	
	public String suscribirPartida() {
		if(partida != null) {
			Session session = Contexts.getHibernateSession();
			Usuarios usuario = (Usuarios) Contexts.getSessionAttribute("usuario");
			partida = (Partidas) session.get(Partidas.class, partida.getPa_id());
			partida.getUsuarios().add(usuario);
			session.merge(partida);
			session.flush();
			cargarListaPartidas();
			Contexts.addInfoMessage("Suscripción a la partida correcta.");
		} else {
			Contexts.addErrorMessage("No hay ninguna partida seleccionada.");
		}
		return null;
	}

	public List<Partidas> getListaPartidas() {
		return listaPartidas;
	}

	public void setListaPartidas(List<Partidas> listaPartidas) {
		this.listaPartidas = listaPartidas;
	}

	public List<Partidas> getListaPartidasDisponibles() {
		return listaPartidasDisponibles;
	}

	public void setListaPartidasDisponibles(List<Partidas> listaPartidasDisponibles) {
		this.listaPartidasDisponibles = listaPartidasDisponibles;
	}

	public Partidas getPartida() {
		return partida;
	}

	public void setPartida(Partidas partida) {
		this.partida = partida;
	}

	
}