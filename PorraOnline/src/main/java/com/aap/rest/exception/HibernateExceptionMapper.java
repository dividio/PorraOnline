package com.aap.rest.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.HibernateException;

@Provider
public class HibernateExceptionMapper implements ExceptionMapper<HibernateException> {

    @Context
    private HttpHeaders headers;
	
	@Context
	private HttpServletRequest request;

    @Override
	public Response toResponse(HibernateException e) {
		request.setAttribute("abortarTransaccion", Boolean.TRUE);
		return Response.status(e.hashCode()).
		        entity(new ErrorResponseConverter(e.getMessage(), e.getCause().getMessage(), 3)).
                type(headers.getMediaType()).
                build();
    }
}