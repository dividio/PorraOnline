package com.aap.rest.server;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.dto.Usuarios;
import com.aap.rest.client.PersonaClientRS;
import com.aap.rest.client.VerificationResult;
import com.aap.rest.exception.ErrorResponseConverter;
import com.aap.rest.exception.RestCustomException;

@Path("/login")
public class LoginRS {
	
	private static final Logger log = LoggerFactory.getLogger(LoginRS.class);

	@Context
	private HttpServletRequest request;

	
	public LoginRS() {

	}
	
	@PermitAll
	@GET
	@Path("{assertion}")
	@Produces({ "application/json" })
	public VerificationResult login(@PathParam("assertion") String assertion) {
		PersonaClientRS personaClientRS = new PersonaClientRS();
		VerificationResult resultado = personaClientRS.verificarCredenciales(assertion);
		if(resultado != null) {
			Usuarios usuario = (Usuarios) getSession().createCriteria(Usuarios.class)
					.add(Restrictions.eq("usu_email",resultado.getEmail()))
					.uniqueResult();
			if(usuario != null) {
				request.getSession().setAttribute("usuario", usuario);
				resultado.setUsuario(usuario);
			}
		} else {
			ErrorResponseConverter error = personaClientRS.getError();
			throw new RestCustomException(error.getMessage(), error.getReason(), Status.FORBIDDEN, RestCustomException.ERROR);
		}
		
		return resultado;
	}

	
	protected Session getSession() {
		return (Session) request.getAttribute("session");
	}
}