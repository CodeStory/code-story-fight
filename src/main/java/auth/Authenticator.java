package auth;

import com.google.common.base.Throwables;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.internal.http.HttpResponseCode;

import java.net.MalformedURLException;
import java.net.URL;

public class Authenticator {

  private final Twitter twitter;

  public Authenticator() {
    twitter = new TwitterFactory(createCodeStoryConfigurationBuilderForTwitter().build()).getInstance();
  }

  public Authenticator(final User user) {
    twitter = new TwitterFactory(
            createCodeStoryConfigurationBuilderForTwitter()
                    .setOAuthAccessToken(user.token)
                    .setOAuthAccessTokenSecret(user.secret)
                    .build()
    ).getInstance();
  }

  public URL getAuthenticateURL() throws TwitterException, MalformedURLException {
    return new URL(twitter.getOAuthRequestToken().getAuthenticationURL());
  }

  public User authenticate(String oauthVerifier) throws AuthenticationException {
    try {
      AccessToken oAuthAccessToken = twitter.getOAuthAccessToken(oauthVerifier);
      return new User(oAuthAccessToken.getUserId(), oAuthAccessToken.getScreenName(), oAuthAccessToken.getToken(), oAuthAccessToken.getTokenSecret());
    } catch (TwitterException e) {
      if (HttpResponseCode.UNAUTHORIZED == e.getStatusCode()) {
        throw new AuthenticationException();
      }
      throw new RuntimeException(e);
    }
  }

  public boolean isAuthenticated() {
    try {
      twitter.verifyCredentials();
      return true;
    } catch (TwitterException e) {
      if (HttpResponseCode.UNAUTHORIZED == e.getStatusCode()) {
        return false;
      }
      throw new RuntimeException(e);
    }
  }

  private static ConfigurationBuilder createCodeStoryConfigurationBuilderForTwitter() {
    return new ConfigurationBuilder()
            .setOAuthConsumerKey("tqbj29Swn22tquWwVNEbA")
            .setOAuthConsumerSecret("GBtZVX10lgEHvbMG8UPgX8n1pMFsyT6uETd97DjU");
  }

}
