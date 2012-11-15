import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import net.gageot.test.rules.ServiceRule;
import net.gageot.test.utils.Shell;
import org.junit.Rule;
import org.junit.Test;

import java.net.URL;

import static com.google.inject.name.Names.named;
import static org.fest.assertions.Assertions.assertThat;

public class FightServerTest {
	@Rule
	public ServiceRule<FightServer> server = ServiceRule.startWithRandomPort(FightServer.class, new AbstractModule() {
		@Override
		protected void configure() {
			bind(URL.class).annotatedWith(named("planningUrl")).toInstance(Resources.getResource("planning.json"));
			bind(URL.class).annotatedWith(named("votesUrl")).toInstance(Resources.getResource("starsPerTalk.json"));
		}
	});

	@Test
	public void should_test_home_page() throws Exception {
		int resultCode = new Shell().execute("./etc/junit_run_mocha.sh ./src/test/acceptance/acceptance_test.coffee %d", server.getPort());

		assertThat(resultCode).isZero();
	}
}
