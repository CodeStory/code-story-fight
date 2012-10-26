package auth;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.net.URI;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Singleton
public class TwitterAuthenticator implements Authenticator {
  private final TwitterFactory twitterFactory;
  private final String callback;
  private final Map<String, RequestToken> oauthRequestByToken;

  @Inject
  public TwitterAuthenticator(TwitterFactory twitterFactory, @Named("oAuth.callback") String callback) {
    this.twitterFactory = twitterFactory;
    this.oauthRequestByToken = newHashMap();
    this.callback = Strings.emptyToNull(callback);
  }

  @Override
  public URI getAuthenticateURI() {
    Twitter twitter = twitterFactory.getInstance();
    try {
      RequestToken requestToken = twitter.getOAuthRequestToken(callback);
      oauthRequestByToken.put(requestToken.getToken(), requestToken);
      return URI.create(requestToken.getAuthenticationURL());
    } catch (TwitterException e) {
      throw new AuthenticationException(e);
    }
  }

  @Override
  public User authenticate(String oauthToken, String oauthVerifier) {
    Twitter twitter = twitterFactory.getInstance();
    try {
      AccessToken accessToken = twitter.getOAuthAccessToken(oauthRequestByToken.remove(oauthToken), oauthVerifier);
      return new User(accessToken.getUserId(), accessToken.getScreenName(), accessToken.getToken(), accessToken.getTokenSecret());
    } catch (TwitterException e) {
      throw new AuthenticationException(e);
    }
  }
}
