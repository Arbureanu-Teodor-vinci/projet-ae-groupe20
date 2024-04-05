package be.vinci.pae.api.filters;

import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.userservices.UserDAO;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;

/**
 * Filter to check if the user is authorized to access the resource.
 */
@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();

  @Context
    private ResourceInfo resourceInfo;

  @Inject
  private UserDAO myUserDataService;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("A token is needed to access this resource").build());
    } else {
      DecodedJWT decodedToken = null;
      try {
        decodedToken = this.jwtVerifier.verify(token);
      } catch (Exception e) {
        throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
            .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
      }
      UserDTO authenticatedUser = myUserDataService.getOneUserByID(
          decodedToken.getClaim("user").asInt());
      if (authenticatedUser == null) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("You are forbidden to access this resource").build());
      } else {
        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> String.valueOf(authenticatedUser.getId());
            }

            @Override
            public boolean isUserInRole(String role) {
                return authenticatedUser.getRole().equals(role);
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });

        // Check if the user's role is allowed
        Authorize authorize = resourceInfo.getResourceMethod().getAnnotation(Authorize.class);
        if (authorize != null && !Arrays.asList(authorize.rolesAllowed()).contains(authenticatedUser.getRole())) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                .entity("You are forbidden to access this resource").build());
        }
      }
      
          
    }
  }
}
