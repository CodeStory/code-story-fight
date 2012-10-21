import auth.Authenticator;
import auth.FakeAuthenticator;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import net.gageot.test.utils.Shell;
import org.junit.Rule;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class PlanningServerTest {
  @Rule
  public ServiceRule<PlanningServer> web = ServiceRule.startWithRandomPort(PlanningServer.class, new AbstractModule() {
    @Override
    protected void configure() {
      bind(Authenticator.class).to(FakeAuthenticator.class);
      bindConstant().annotatedWith(Names.named("port")).to(web.getPort());
    }
  });

  @Test
  public void should_show_homepage() {
    int result = new Shell().execute("./testHome.coffee %d", web.getPort());

    assertThat(result).isZero();
  }
}
