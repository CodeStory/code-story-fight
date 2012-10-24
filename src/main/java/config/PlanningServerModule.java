package config;

import auth.Authenticator;
import auth.TwitterAuthenticator;
import com.google.inject.AbstractModule;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class PlanningServerModule extends AbstractModule {

  private static final String O_AUTH_CONSUMER_KEY = "tqbj29Swn22tquWwVNEbA";

  private static final String O_AUTH_CONSUMER_SECRET = "GBtZVX10lgEHvbMG8UPgX8n1pMFsyT6uETd97DjU";

  @Override
  protected void configure() {
    TwitterFactory twitterFactory = new TwitterFactory(createCodeStoryConfigurationBuilderForTwitter().build());
    bind(Authenticator.class).toInstance(new TwitterAuthenticator(twitterFactory.getInstance()));
  }

  private static ConfigurationBuilder createCodeStoryConfigurationBuilderForTwitter() {
    return new ConfigurationBuilder()
      .setOAuthConsumerKey(O_AUTH_CONSUMER_KEY)
      .setOAuthConsumerSecret(O_AUTH_CONSUMER_SECRET);
  }
}
