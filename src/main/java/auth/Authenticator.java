package auth;

import java.net.URI;

public interface Authenticator {
  URI getAuthenticateURI() throws AuthenticationException;

  User authenticate(String aouthToken, String oauthVerifier) throws AuthenticationException;
}
