package controllers;

import com.google.inject.Singleton;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Singleton
@Path("/fakeauthenticate")
public class FakeAuthenticatorResource extends AbstractResource {
  @GET
  public Response authenticate() {
    return seeOther("/user/authenticated?oauthToken=foo&oauthVerifier=bar");
  }
}
