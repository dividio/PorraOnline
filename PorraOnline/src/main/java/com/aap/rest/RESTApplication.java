package com.aap.rest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.aap.rest.exception.GeneralExceptionMapper;
import com.aap.rest.exception.HibernateExceptionMapper;
import com.aap.rest.exception.RestCustomExceptionMapper;
import com.aap.rest.server.ClasificacionRS;
import com.aap.rest.server.CompetidoresRS;
import com.aap.rest.server.EventosRS;
import com.aap.rest.server.LoginRS;
import com.aap.rest.server.LogoutRS;
import com.aap.rest.server.MensajesRS;
import com.aap.rest.server.NoticiasRS;
import com.aap.rest.server.PartidasRS;
import com.aap.rest.server.PenalizacionesRS;
import com.aap.rest.server.PronosticosRS;
import com.aap.rest.server.ResultadosRS;
import com.aap.rest.server.UsuariosRS;

@Provider
public class RESTApplication extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();
	
	public RESTApplication() {
		singletons.add(new NoticiasRS());
		singletons.add(new LoginRS());
		singletons.add(new LogoutRS());
		singletons.add(new PartidasRS());
		singletons.add(new MensajesRS());
		singletons.add(new EventosRS());
		singletons.add(new ClasificacionRS());
		singletons.add(new CompetidoresRS());
		singletons.add(new PronosticosRS());
		singletons.add(new UsuariosRS());
		singletons.add(new ResultadosRS());
		singletons.add(new PenalizacionesRS());
		ResteasyProviderFactory pf = ResteasyProviderFactory.getInstance();
		pf.registerProvider(RestCustomExceptionMapper.class);
		pf.registerProvider(HibernateExceptionMapper.class);
		pf.registerProvider(GeneralExceptionMapper.class);
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