package controllers;

import com.google.inject.Singleton;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.net.URI;

@Path("/fakeauthenticate")
@Singleton
public class FakeAuthenticatorResource extends AbstractResource {
  @GET
  public Response fakeAuthenticate() {
    return Response.seeOther(URI.create("/authenticated?oauthToken=foo&oauthVerifier=bar")).build();
  }
}
