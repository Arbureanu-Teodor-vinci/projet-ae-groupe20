package be.vinci.pae.api.filters;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;


/**
 * FatalException for SQLexceptions and token creation problems -> status 500.
 */
public class FatalException extends WebApplicationException {

  public FatalException() {
    super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .build());
  }

  public FatalException(String message) {
    super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(message)
        .type("text/plain")
        .build());
  }

  public FatalException(Throwable cause) {
    super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(cause.getMessage())
        .type("text/plain")
        .build());
  }

}
