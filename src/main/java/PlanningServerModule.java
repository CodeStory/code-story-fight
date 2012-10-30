import auth.Authenticator;
import auth.TwitterAuthenticator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;

import static com.google.inject.name.Names.named;

public class PlanningServerModule extends AbstractModule {
  @Override
  protected void configure() {
    bindConstant().annotatedWith(named("oAuth.callback")).to(env("OAUTH_CALLBACK", ""));
    bindConstant().annotatedWith(named("oAuth.key")).to(env("OAUTH_CONSUMER_KEY", ""));
    bindConstant().annotatedWith(named("oAuth.secret")).to(env("OAUTH_CONSUMER_SECRET", ""));
    bind(File.class).annotatedWith(named("planning.root")).toInstance(new File(env("PLANNING_ROOT", "data")));

    bind(Authenticator.class).to(TwitterAuthenticator.class);
  }

  @Provides
  private Configuration createConfiguration(@Named("oAuth.key") String key, @Named("oAuth.secret") String secret) {
    return new ConfigurationBuilder().setOAuthConsumerKey(key).setOAuthConsumerSecret(secret).build();
  }

  @Provides
  private TwitterFactory createTwitterFactory(Configuration config) {
    return new TwitterFactory(config);
  }

  private static String env(String name, String defaultValue) {
    String value = System.getenv(name);
    return (null != value) ? value : defaultValue;
  }
}
