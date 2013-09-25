package com.aap.rest.exception;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestCustomExceptionMapper implements ExceptionMapper<RestCustomException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(RestCustomException e) {
        return Response.status(e.getStatus()).
                entity(new ErrorResponseConverter(e.getMessage(), e.getReason(), e.getErrorCode())).
                type(headers.getMediaType()).
                build();
    }
}