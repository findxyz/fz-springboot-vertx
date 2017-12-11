package xyz.fz.springBootVertx.provider;

import xyz.fz.springBootVertx.model.Result;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper implements ExceptionMapper<RestException> {
    @Override
    public Response toResponse(RestException exception) {
        return Response.status(Response.Status.OK).entity(Result.ofMessage(exception.getMessage())).type("application/json").build();
    }
}
