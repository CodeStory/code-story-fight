import net.gageot.test.utils.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CodeStoryServerTest {
  private CodeStoryServer server;

  @Before
  public void setUp() throws Exception {
    server = new CodeStoryServer();
    server.start(8080);
  }

  @After
  public void tearDown() {
    server.stop();
  }

  @Test
  public void should_show_homepage() {
    int result = new Shell().execute("coffee testHome.coffee");

    assertThat(result).isZero();
  }
}
