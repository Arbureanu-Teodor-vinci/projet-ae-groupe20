package be.vinci.pae.utils;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.api.filters.FatalException;
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

    // If the exception is a FatalException, we return a 500
    if (exception instanceof FatalException) {
      Logger.logEntry("FatalException: " + exception.getMessage(), exception, 3);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(exception.getMessage())
          .build();
    }

    // If the exception is a BiznessException, we return a 412
    if (exception instanceof BusinessException) {
      Logger.logEntry("BiznessException: " + exception.getMessage(), exception, 2);
      return Response.status(Response.Status.PRECONDITION_FAILED)
          .entity(exception.getMessage())
          .build();
    }

    // If the exception is a WebApplicationException, we return the response
    if (exception instanceof WebApplicationException) {
      Logger.logEntry("WebApplicationException: " + exception.getMessage(), exception, 2);
      return Response.status(((WebApplicationException) exception).getResponse().getStatus())
          .entity(exception.getMessage())
          .build();
    }
    // If the exception is a IllegalStateException and the message is "Forbidden", we return a 403
    if (exception instanceof IllegalStateException
        && exception.getMessage().equals("Forbidden")) {
      Logger.logEntry("Forbidden: " + exception.getMessage(), exception, 2);
      return Response.status(Response.Status.FORBIDDEN)
          .entity("You are not the author")
          .build();
    }
    // If the exception is a NullPointerException we return a 404
    if (exception instanceof NullPointerException) {
      Logger.logEntry("Not found: " + exception.getMessage(), exception, 2);
      return Response.status(Response.Status.NOT_FOUND)
          .entity(exception.getMessage())
          .build();
    }
    // Else we return a 500
    Logger.logEntry("Internal server error: " + exception.getMessage(), exception, 3);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }

}
