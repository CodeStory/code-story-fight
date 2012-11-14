import net.gageot.test.utils.Shell;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FightServerTest {
	@Test
	public void should_test_home_page() throws Exception {
		FightServer.main(new String[0]);

		int resultCode = new Shell().execute("./etc/junit_run_mocha.sh ./src/test/acceptance/acceptance_test.coffee");
		assertThat(resultCode).isZero();
	}
}
