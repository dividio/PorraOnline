package com.aap.rest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.aap.rest.exception.HibernateExceptionMapper;
import com.aap.rest.exception.RestCustomExceptionMapper;
import com.aap.rest.server.LoginRS;
import com.aap.rest.server.LogoutRS;
import com.aap.rest.server.MensajesRS;
import com.aap.rest.server.NoticiasRS;
import com.aap.rest.server.PartidasRS;

@Provider
public class RESTApplication extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();
	
	public RESTApplication() {
		singletons.add(new NoticiasRS());
		singletons.add(new LoginRS());
		singletons.add(new LogoutRS());
		singletons.add(new PartidasRS());
		singletons.add(new MensajesRS());
		ResteasyProviderFactory pf = ResteasyProviderFactory.getInstance();
		pf.registerProvider(RestCustomExceptionMapper.class);
		pf.registerProvider(HibernateExceptionMapper.class);
		pf.registerProvider(SecurityInterceptor.class);
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}
}