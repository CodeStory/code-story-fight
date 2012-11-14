import com.google.inject.AbstractModule;

import java.net.URL;

import static com.google.inject.name.Names.named;

public class JerseyModule extends AbstractModule {
	@Override
	protected void configure() {
		String planningUrl = "http://planning.code-story.net/planning.json";
		String votesUrl = "http://planning.code-story.net/starsPerTalk";

		try {
			bind(URL.class).annotatedWith(named("planningUrl")).toInstance(new URL(planningUrl));
			bind(URL.class).annotatedWith(named("vote")).toInstance(new URL(votesUrl));
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid urls");
		}
	}
}
