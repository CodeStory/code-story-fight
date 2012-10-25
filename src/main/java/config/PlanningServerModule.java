package config;

import auth.Authenticator;
import auth.TwitterAuthenticator;
import com.google.inject.AbstractModule;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class PlanningServerModule extends AbstractModule {

  private static final String OAUTH_CONSUMER_KEY = "OAUTH_CONSUMER-KEY";
  private static final String OAUTH_CONSUMER_SECRET = "OAUTH_CONSUMER-SECRET";
  private static final String OAUTH_CALLBACK = "OAUTH_CALLBACK";

  @Override
  protected void configure() {
    TwitterFactory twitterFactory = new TwitterFactory(createCodeStoryConfigurationForTwitter());
    bind(Authenticator.class)
            .toInstance(new TwitterAuthenticator(twitterFactory.getInstance(), System.getenv(OAUTH_CALLBACK)));
  }

  private Configuration createCodeStoryConfigurationForTwitter() {
    return new ConfigurationBuilder()
            .setOAuthConsumerKey(getEnvironmentVariableOrFail(OAUTH_CONSUMER_KEY))
            .setOAuthConsumerSecret(getEnvironmentVariableOrFail(OAUTH_CONSUMER_SECRET))
            .build();
  }

  private String getEnvironmentVariableOrFail(String environmentVariableName) {
    String environmentVariableValue = System.getenv(environmentVariableName);
    if (environmentVariableValue == null) {
      addError("Please provide %s environment variable", environmentVariableName);
    }
    return environmentVariableValue;
  }

}
