import auth.Authenticator;
import auth.FakeAuthenticator;
import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import misc.ServiceRule;
import net.gageot.test.utils.Shell;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static com.google.inject.name.Names.named;
import static org.fest.assertions.Assertions.assertThat;

public class PlanningServerTest {
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Rule
  public ServiceRule<PlanningServer> web = ServiceRule.startWithRandomPort(PlanningServer.class, new AbstractModule() {
    @Override
    protected void configure() {
      File root;
      try {
        root = temporaryFolder.newFolder();
      } catch (IOException e) {
        throw Throwables.propagate(e);
      }

      bindConstant().annotatedWith(Names.named("port")).to(web.getPort());
      bind(Authenticator.class).to(FakeAuthenticator.class);
      bind(File.class).annotatedWith(named("planning.root")).toInstance(root);
    }
  });

  @Test
  public void should_show_homepage() {
    int result = new Shell().execute("./testHome.coffee %d", web.getPort());

    assertThat(result).isZero();
  }
}
