package com.aap.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.aap.dto.Noticias;
import com.aap.util.jsf.Contexts;

@ManagedBean
@RequestScoped
public class MenuPrincipalBean implements Serializable {

	private static final long serialVersionUID = 6622640417115301875L;

	private List<Noticias> listaNoticias = new ArrayList<Noticias>();
	
	@PostConstruct
	public void init() {
		Session session = Contexts.getHibernateSession();
		listaNoticias = session.createCriteria(Noticias.class)
				.addOrder(Order.desc("no_fecha"))
				.list();
	}

	public List<Noticias> getListaNoticias() {
		return listaNoticias;
	}

	public void setListaNoticias(List<Noticias> listaNoticias) {
		this.listaNoticias = listaNoticias;
	}
}