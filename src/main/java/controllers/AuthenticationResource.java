package controllers;

import auth.AuthenticationException;
import auth.Authenticator;
import auth.User;
import auth.Users;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.net.URI;

import static java.lang.Long.parseLong;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Singleton
@Path("/user")
public class AuthenticationResource extends AbstractResource {
  private final Users users;
  private final Authenticator authenticator;

  @Inject
  public AuthenticationResource(Users users, Authenticator authenticator) {
    this.users = users;
    this.authenticator = authenticator;
  }

  @GET
  @Path("authenticate")
  public Response authenticate() {
    return seeOther(authenticator.getAuthenticateURI());
  }

  @GET
  @Path("authenticated")
  public Response authenticated(@QueryParam("oauth_token") String oauthToken, @QueryParam("oauth_verifier") String oauthVerifier) {
    try {
      User user = authenticator.authenticate(oauthToken, oauthVerifier);
      users.add(user);

      return redirectToPlanning().cookie(new NewCookie("userId", user.getId().toString(), "/", null, null, 60 * 60 * 24 * 7, false)).build();
    } catch (IllegalStateException | AuthenticationException e) {
      return seeOther("index.html");
    }
  }

  @GET
  @Path("logout")
  public Response logout(@CookieParam("userId") String userId) {
    rejectUnauthenticated(userId);

    users.remove(parseLong(userId));

    return redirectToPlanning().cookie(new NewCookie("userId", null, "/", null, null, 0, false)).build();
  }

  private static Response.ResponseBuilder redirectToPlanning() {
    return Response.seeOther(URI.create("planning.html"));
  }

  private static void rejectUnauthenticated(String userId) {
    if (userId == null) {
      throw new WebApplicationException(FORBIDDEN);
    }
  }
}
