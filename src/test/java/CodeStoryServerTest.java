import net.gageot.test.rules.ServiceRule;
import net.gageot.test.utils.Shell;
import org.junit.Rule;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CodeStoryServerTest {
  @Rule
  public ServiceRule<CodeStoryServer> web = ServiceRule.startWithRandomPort(CodeStoryServer.class);

  @Test
  public void should_show_homepage() {
    int result = new Shell().execute("coffee testHome.coffee %d", web.getPort());

    assertThat(result).isZero();
  }
}
