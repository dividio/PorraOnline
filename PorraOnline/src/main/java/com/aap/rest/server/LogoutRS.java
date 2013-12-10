package com.aap.rest.server;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/logout")
public class LogoutRS {
	
	private static final Logger log = LoggerFactory.getLogger(LogoutRS.class);

	@Context
	private HttpServletRequest request;

	
	public LogoutRS() {

	}
	
	@PermitAll
	@GET
	@Produces({ "application/json" })
	public void logout() {
		request.getSession().invalidate();
	}

	
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}