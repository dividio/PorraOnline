package com.aap.util.hibernate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * SessionFilter initializes EntityManagerFactory and serves each request with
 * an EntityManager.
 * 
 */
public class EntityManagerFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(EntityManagerFilter.class);

	public void init(FilterConfig config) throws ServletException {
		HibernateUtil.getSessionFactory();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
	        FilterChain chain) throws ServletException, IOException {

		// create the entity manager
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		try {
			Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

			// store it as a request attribute
			request.setAttribute("session", hibernateSession);

			Transaction tx = null;
			try {
				// begin the transaction
				tx = hibernateSession.getTransaction();
				tx.begin();

				// continue processing
				chain.doFilter(request, response);

				tx = hibernateSession.getTransaction();

				Boolean abortarTransaccion = (Boolean) request.getAttribute("abortarTransaccion");
				if (abortarTransaccion != null && abortarTransaccion) {
					// System.out.println("rollback");
					tx.rollback();
				} else {
					// System.out.println("commit");
					tx.commit();
				}

			} catch (RuntimeException e) {
				// if something fails rollback the transaction and log the error
				log.error("Algo ha ido mal realizando la transacción de Hibernate.", e);
				tx.rollback();

			} finally {
				hibernateSession.close();
			}
		} catch (HibernateException e) {
			log.error("Error trabajando con la sesion de Hibernate.", e);
			throw new ServletException();
		}
	}

	public void destroy() {
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			if (sessionFactory != null) {
				Session session = sessionFactory.getCurrentSession();
				if (session.isOpen()) {
					session.close();
				}
				sessionFactory.close();
				sessionFactory = null;
			}
		} catch (HibernateException e) {
			log.error("Failed to close hibernate session in destroy() ", e);
		}
	}
}
