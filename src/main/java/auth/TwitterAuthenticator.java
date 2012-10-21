package auth;

import com.google.inject.Inject;
import config.PlanningServerModule;
import com.google.common.base.Throwables;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.internal.http.HttpResponseCode;

import java.net.MalformedURLException;
import java.net.URL;

public class TwitterAuthenticator implements Authenticator {

  private final Twitter twitter;

  @Inject
  public TwitterAuthenticator(Twitter twitter) {
    this.twitter = twitter;
  }

  @Override
  public URL getAuthenticateURL() throws AuthenticationException {
    try {
      return new URL(twitter.getOAuthRequestToken().getAuthenticationURL());
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
