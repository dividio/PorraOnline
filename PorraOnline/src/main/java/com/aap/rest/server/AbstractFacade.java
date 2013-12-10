package com.aap.rest.server;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.aap.rest.exception.RestCustomException;

public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

	protected abstract Session getSession();

	public T create(T entity) {
		Long id = (Long) getSession().save(entity);
		getSession().flush();
		return (T) getSession().get(entityClass, id);
    }

    public void edit(T entity) {
		getSession().merge(entity);
		getSession().flush();
    }

    public void remove(T entity) {
		if(entity != null) {
			getSession().delete(getSession().merge(entity));
			getSession().flush();
		}
    }

    public T find(Long id) {
		T elemento = (T) getSession().get(entityClass, (Serializable) id);
		if(elemento == null) {
			throw new RestCustomException("Elemento no encontrado", "Elemento no encontrado", Status.NOT_FOUND, RestCustomException.ERROR);
		}
		return elemento;
    }

    public List<T> findAll() {
		Criteria cq = getSession().createCriteria(entityClass);
		return cq.list();
    }

    public List<T> findRange(int[] range) {
		Criteria cq = getSession().createCriteria(entityClass);
		cq.setMaxResults(range[1] - range[0]);
		cq.setFirstResult(range[0]);
		return cq.list();
    }

	// public int count() {
	// javax.persistence.criteria.CriteriaQuery cq = getSession().getCriteriaBuilder().createQuery();
	// javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
	// cq.select(getSession().getCriteriaBuilder().count(rt));
	// javax.persistence.Query q = getSession().createQuery(cq);
	// return ((Long) q.getSingleResult()).intValue();
	// }
    
}
