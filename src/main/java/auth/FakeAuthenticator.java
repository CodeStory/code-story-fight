package auth;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import twitter4j.TwitterException;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;

public class FakeAuthenticator implements Authenticator {
  private final int port;

  @Inject
  public FakeAuthenticator(@Named("port") int port) {
    this.port = port;
  }

  @Override
  public URL getAuthenticateURL() throws TwitterException, MalformedURLException {
    return new URL(format("http://localhost:%d/fakeauthenticate", port));
  }

  @Override
  public User authenticate(String oauthToken, String oauthVerifier) throws AuthenticationException {
    return new User(42L, "arnold", "ring", "girl");
  }
}
