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
		
		int posicion = 1;
		clasificacion = new ArrayList<Object[]>();
		for(Object[] puesto:datos) {
			Object[] aux = new Object[4];
			aux[0] = puesto[0]; //USU.usu_id
			aux[1] = Long.valueOf(posicion++);
			aux[2] = puesto[1]; //USU.usu_username
			aux[3] = puesto[2]; //sum(coalesce(PR.pr_puntos_conseguidos,0))
			
			clasificacion.add(aux);
		}
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