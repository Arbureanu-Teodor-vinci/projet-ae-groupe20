package be.vinci.pae.api.filters;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * BusinessException for business logic exceptions -> status 412.
 */
public class BusinessException extends WebApplicationException {

  /**
   * Constructor.
   */
  public BusinessException() {
    super(Response.status(Response.Status.PRECONDITION_FAILED)
        .build());
  }

  /**
   * Constructor with message.
   *
   * @param message -> the message
   */
  public BusinessException(String message) {
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
  public BusinessException(Throwable cause) {
    super(Response.status(Response.Status.PRECONDITION_FAILED)
        .entity(cause.getMessage())
        .type("text/plain")
        .build());
  }


}
