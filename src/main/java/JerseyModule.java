import com.google.inject.AbstractModule;

import java.net.URL;

import static com.google.inject.name.Names.named;

public class JerseyModule extends AbstractModule {
	@Override
	protected void configure() {
		try {
			String spec = "http://planning.code-story.net/planning.json";
			String spec1 = "http://planning.code-story.net/starsPerTalk";
			bind(URL.class).annotatedWith(named("planningUrl")).toInstance(new URL(spec));
			bind(URL.class).annotatedWith(named("vote")).toInstance(new URL(spec1));
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid urls");
		}
	}
}
