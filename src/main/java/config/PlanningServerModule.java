package config;

import auth.Authenticator;
import auth.TwitterAuthenticator;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class PlanningServerModule extends AbstractModule {
  private static final String OAUTH_CONSUMER_KEY = "OAUTH_CONSUMER-KEY";
  private static final String OAUTH_CONSUMER_SECRET = "OAUTH_CONSUMER-SECRET";
  private static final String OAUTH_CALLBACK = "OAUTH_CALLBACK";

  @Override
  protected void configure() {
    bindConstant().annotatedWith(Names.named("oAuth.callback")).to(env(OAUTH_CALLBACK));
    bindConstant().annotatedWith(Names.named("oAuth.consumerKey")).to(env(OAUTH_CONSUMER_KEY));
    bindConstant().annotatedWith(Names.named("oAuth.consumerSecret")).to(env(OAUTH_CONSUMER_SECRET));

    bind(Authenticator.class).to(TwitterAuthenticator.class).asEagerSingleton();
  }

  @Provides
  private Twitter createCodeStoryConfigurationForTwitter(@Named("oAuth.consumerKey") String oAuthConsumerKey, @Named("oAuth.consumerSecret") String oAuthConsumerSecret) {
    Configuration config = new ConfigurationBuilder()
        .setOAuthConsumerKey(oAuthConsumerKey)
        .setOAuthConsumerSecret(oAuthConsumerSecret)
        .build();

    return new TwitterFactory(config).getInstance();
  }

  private String env(String name) {
    return Strings.nullToEmpty(System.getenv(name));
  }
}
