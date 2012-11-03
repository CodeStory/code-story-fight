package resources;

import auth.AuthenticationException;
import auth.Authenticator;
import auth.User;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Singleton
@Path("/user")
public class AuthenticationResource extends AbstractResource {
  private static final int MAX_AGE = (int) TimeUnit.DAYS.toSeconds(7);

  private final Authenticator authenticator;

  @Inject
  public AuthenticationResource(Authenticator authenticator) {
    this.authenticator = authenticator;
  }

  @GET
  @Path("authenticate")
  public Response authenticate() {
    return seeOther(authenticator.getAuthenticateURI());
  }

  @GET
  @Path("authenticated")
  public Response authenticated(@QueryParam("oauth_token") String token, @QueryParam("oauth_verifier") String verifier) {
    try {
      User user = authenticator.authenticate(token, verifier);

      return redirectToPlanning().cookie(
          new NewCookie("userId", user.getId().toString(), "/", null, null, MAX_AGE, false),
          new NewCookie("screenName", user.getScreenName(), "/", null, null, MAX_AGE, false))
          .build();
    } catch (IllegalStateException e) {
      return seeOther("index.html");
    } catch (AuthenticationException e) {
      return seeOther("index.html");
    }
  }

  @GET
  @Path("logout")
  public Response logout(@CookieParam("userId") String userId) {
    return redirectToPlanning().cookie(
        new NewCookie("userId", null, "/", null, null, 0, false),
        new NewCookie("screenName", null, "/", null, null, 0, false))
        .build();
  }

  private static Response.ResponseBuilder redirectToPlanning() {
    return Response.seeOther(URI.create("planning.html"));
  }
}
