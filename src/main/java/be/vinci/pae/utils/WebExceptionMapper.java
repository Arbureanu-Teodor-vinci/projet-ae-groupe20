package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Process service error messages sent to clients in an ExceptionMapper.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace();
    // If the exception is a WebApplicationException, we return the response
    if (exception instanceof WebApplicationException) {
      return Response.status(((WebApplicationException) exception).getResponse().getStatus())
          .entity(exception.getMessage())
          .build();
    }
    // If the exception is a IllegalStateException and the message is "Forbidden", we return a 403
    if (exception instanceof IllegalStateException
        && exception.getMessage().equals("Forbidden")) {
      return Response.status(Response.Status.FORBIDDEN)
          .entity("You are not the author")
          .build();
    }
    // If the exception is a NullPointerException and the message is "Not found", we return a 404
    if (exception instanceof NullPointerException
        && exception.getMessage().equals("Not found")) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(exception.getMessage())
          .build();
    }
    // Else we return a 500
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }

}
