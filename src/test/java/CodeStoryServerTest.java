import net.gageot.test.utils.Shell;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CodeStoryServerTest {
  @Before
  public void setUp() throws Exception {
    CodeStoryServer.main(null);
  }

  @Test
  public void should_show_homepage() {
    int result = new Shell().execute("coffee testHome.coffee");

    assertThat(result).isZero();
  }
}
