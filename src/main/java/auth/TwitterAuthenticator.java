package auth;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.internal.http.HttpResponseCode;

import java.net.MalformedURLException;
import java.net.URL;

public class TwitterAuthenticator implements Authenticator {
  private final Twitter twitter;
  private final String oAuthCallback;

  @Inject
  public TwitterAuthenticator(Twitter twitter, @Named("oAuth.callback") String oAuthCallback) {
    this.twitter = twitter;
    this.oAuthCallback = oAuthCallback;
  }

  @Override
  public URL getAuthenticateURL() throws AuthenticationException {
    try {
      return new URL(twitter.getOAuthRequestToken(oAuthCallback).getAuthenticationURL());
    } catch (MalformedURLException e) {
      throw new AuthenticationException(e);
    } catch (TwitterException e) {
      throw new AuthenticationException(e);
    }
  }

  @Override
  public User authenticate(String oauthVerifier) throws AuthenticationException {
    try {
      AccessToken oAuthAccessToken = twitter.getOAuthAccessToken(oauthVerifier);
      return new User(oAuthAccessToken.getUserId(), oAuthAccessToken.getScreenName(), oAuthAccessToken.getToken(), oAuthAccessToken.getTokenSecret());
    } catch (TwitterException e) {
      if (HttpResponseCode.UNAUTHORIZED == e.getStatusCode()) {
        throw new AuthenticationException(e);
      }
      throw Throwables.propagate(e);
    }
  }
}
