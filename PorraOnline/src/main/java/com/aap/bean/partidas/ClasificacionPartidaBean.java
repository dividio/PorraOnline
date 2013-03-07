package com.aap.bean.partidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.hibernate.Query;
import org.hibernate.Session;

import com.aap.dto.Partidas;
import com.aap.util.jsf.Contexts;

@ManagedBean
@RequestScoped
public class ClasificacionPartidaBean implements Serializable {

    private static final long serialVersionUID = -7385224742368662795L;
		
    private Long idPartida;
    
    private Partidas partida = new Partidas();
    
    private List<Object[]> clasificacion = new ArrayList<Object[]>();
    
    public Long getIdPartida() {
		return idPartida;
	}

	public void setIdPartida(Long idPartida) {
		this.idPartida = idPartida;
		if(idPartida != null) {
			Session session = Contexts.getHibernateSession();
			partida = (Partidas) session.get(Partidas.class, idPartida);
			cargarClasificacion();
		}
	}
	
	private void cargarClasificacion() {
		Session session = Contexts.getHibernateSession();
		String hql = "select USU.usu_username " +
						"from Partidas PA " +
						"join PA.usuarios USU " +
						"where PA.pa_id = :ID_PARTIDA " +
						"order by USU.usu_username";
		Query hqlQ = session.createQuery(hql);
		hqlQ.setLong("ID_PARTIDA", partida.getPa_id());
		clasificacion = hqlQ.list();
	}

	public Partidas getPartida() {
		return partida;
	}

	public void setPartida(Partidas partida) {
		this.partida = partida;
	}

	public List<Object[]> getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(List<Object[]> clasificacion) {
		this.clasificacion = clasificacion;
	}
	
	
}