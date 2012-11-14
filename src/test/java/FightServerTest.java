import com.google.inject.AbstractModule;
import net.gageot.test.rules.ServiceRule;
import net.gageot.test.utils.Shell;
import org.junit.Rule;
import org.junit.Test;

import static com.google.inject.name.Names.named;
import static org.fest.assertions.Assertions.assertThat;

public class FightServerTest {
	@Rule
	public ServiceRule<FightServer> server = ServiceRule.startWithRandomPort(
		FightServer.class, new JerseyModuleForTest());

	@Test
	public void should_test_home_page() throws Exception {
		int resultCode = new Shell().execute("./etc/junit_run_mocha.sh ./src/test/acceptance/acceptance_test.coffee %d", server.getPort());
		assertThat(resultCode).isZero();
	}

	static class JerseyModuleForTest extends AbstractModule {
		@Override
		protected void configure() {
			bindConstant().annotatedWith(named("planningUrl")).to("planning.json");
			bindConstant().annotatedWith(named("vote")).to("starsPerTalk.json");
		}
	}
}
