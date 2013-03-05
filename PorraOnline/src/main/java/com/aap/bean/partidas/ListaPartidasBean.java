package com.aap.bean.partidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.hibernate.Query;
import org.hibernate.Session;

import com.aap.dto.Partidas;
import com.aap.dto.Usuarios;
import com.aap.util.jsf.Contexts;

@ManagedBean
@RequestScoped
public class ListaPartidasBean implements Serializable {

    private static final long serialVersionUID = -4110764373104975536L;
	
    private List<Partidas> listaPartidas = new ArrayList<Partidas>();
	
	@PostConstruct
	public void init() {
		
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

	public List<Partidas> getListaPartidas() {
		return listaPartidas;
	}

	public void setListaPartidas(List<Partidas> listaPartidas) {
		this.listaPartidas = listaPartidas;
	}

	
}