package auth;

import com.google.common.base.Throwables;
import config.PlanningServerModule;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.internal.http.HttpResponseCode;

public class TwitterCredentialValidator implements CredentialValidator {

  @Override
  public boolean isAuthenticated(User user) {
    final Twitter twitter = createTwitterWithUser(user);
    try {
      twitter.verifyCredentials();
    } catch (TwitterException e) {
      if (HttpResponseCode.UNAUTHORIZED == e.getStatusCode()) {
        return false;
      }
      Throwables.propagate(e);
    }
    return true;
  }

  protected Twitter createTwitterWithUser(User user) {
    return new TwitterFactory(
      PlanningServerModule.createCodeStoryConfigurationBuilderForTwitter()
        .setOAuthAccessToken(user.token)
        .setOAuthAccessTokenSecret(user.secret)
        .build()
    ).getInstance();
  }
}
