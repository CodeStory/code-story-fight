import net.gageot.test.rules.ServiceRule;
import net.gageot.test.utils.Shell;
import org.junit.Rule;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FightServerTest {
	@Rule
	public ServiceRule<FightServer> server = ServiceRule.startWithRandomPort(FightServer.class);

	@Test
	public void should_test_home_page() throws Exception {
		int resultCode = new Shell().execute("./etc/junit_run_mocha.sh ./src/test/acceptance/acceptance_test.coffee %d", server.getPort());
		assertThat(resultCode).isZero();
	}
}
