package auth;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.net.URI;

import static java.lang.String.format;

public class FakeAuthenticator implements Authenticator {
  private final int port;

  @Inject
  public FakeAuthenticator(@Named("port") int port) {
    this.port = port;
  }

  @Override
  public URI getAuthenticateURI() {
    return URI.create(format("http://localhost:%d/fakeauthenticate", port));
  }

  @Override
  public User authenticate(String oauthToken, String oauthVerifier) {
    return new User(42L, "arnold", "ring", "girl");
  }
}
