package be.vinci.pae.api.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * This annotation is used to specify the roles that are allowed to access a method or class.
 * It is used in conjunction with the AuthorizationRequestFilter to perform role-based access control.
 * 
 * @see AuthorizationRequestFilter
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
  /**
  * Returns an array of roles that are allowed to access the method or class that this annotation is applied to.
  * 
  * @return an array of roles
  */
  String[] rolesAllowed() default {};
}
