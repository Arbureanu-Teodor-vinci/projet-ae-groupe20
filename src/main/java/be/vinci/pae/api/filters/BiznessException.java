package be.vinci.pae.api.filters;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * BiznessException for business logic exceptions -> status 412.
 */
public class BiznessException extends WebApplicationException {

  public BiznessException() {
    super(Response.status(Response.Status.PRECONDITION_FAILED)
        .build());
  }

  public BiznessException(String message) {
    super(Response.status(Response.Status.PRECONDITION_FAILED)
        .entity(message)
        .type("text/plain")
        .build());
  }

  public BiznessException(Throwable cause) {
    super(Response.status(Response.Status.PRECONDITION_FAILED)
        .entity(cause.getMessage())
        .type("text/plain")
        .build());
  }


}
