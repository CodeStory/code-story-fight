package auth;

import twitter4j.TwitterException;

import java.net.MalformedURLException;
import java.net.URL;

public interface Authenticator {
  URL getAuthenticateURL() throws TwitterException, MalformedURLException;

  User authenticate(String oauthVerifier) throws AuthenticationException;
}
