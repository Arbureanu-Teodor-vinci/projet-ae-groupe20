package be.vinci.pae.api.filters;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * BiznessException for business logic exceptions -> status 412.
 */
public class BiznessException extends WebApplicationException {

  /**
   * Constructor.
   */
  public BiznessException() {
    super(Response.status(Response.Status.PRECONDITION_FAILED)
        .build());
  }

  /**
   * Constructor with message.
   *
   * @param message -> the message
   */
  public BiznessException(String message) {
    super(Response.status(Response.Status.PRECONDITION_FAILED)
        .entity(message)
        .type("text/plain")
        .build());
  }

  /**
   * Constructor with thrown exception cause.
   *
   * @param cause -> the cause
   */
  public BiznessException(Throwable cause) {
    super(Response.status(Response.Status.PRECONDITION_FAILED)
        .entity(cause.getMessage())
        .type("text/plain")
        .build());
  }


}
