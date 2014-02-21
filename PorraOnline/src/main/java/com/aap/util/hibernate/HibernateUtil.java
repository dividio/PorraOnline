package com.aap.util.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

	private static final Logger log = LoggerFactory
	        .getLogger(HibernateUtil.class);

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			ConfiguracionAnotaciones.cargaAnotaciones(configuration);
			configuration.configure();

			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
					applySettings(configuration.getProperties());
			return configuration.buildSessionFactory(builder.build());
			// Create the SessionFactory from hibernate.cfg.xml
//			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
//			        .applySettings(configuration.getProperties())
//			        .getBootstrapServiceRegistry();
//			 configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			log.error("Initial SessionFactory creation failed.", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
