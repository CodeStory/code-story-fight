package auth;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import twitter4j.TwitterException;

import java.net.MalformedURLException;
import java.net.URL;

public class FakeAuthenticator implements Authenticator, CredentialValidator {

  private final int port;

  @Inject
  public FakeAuthenticator(@Named("port") int port) {
    this.port = port;
  }

  @Override
  public URL getAuthenticateURL() throws TwitterException, MalformedURLException {
    return new URL("http://localhost:" + port + "/fakeauthenticate");
  }

  @Override
  public User authenticate(String oauthVerifier) throws AuthenticationException {
    return new User(42L, "arnold", "ring", "girl");
  }

  @Override
  public boolean isAuthenticated(User user) {
    return true;
  }
}
