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

public class FightServerTest {
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Rule
  public ServiceRule<FightServer> web = ServiceRule.startWithRandomPort(FightServer.class, new AbstractModule() {
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
    int result = new Shell().execute("./etc/junit_run_mocha.sh ./src/test/acceptance %d", web.getPort());
    assertThat(result).isZero();
  }

  public static void main(String[] args) throws IOException {
    new FightServer(new AbstractModule() {
      @Override
      protected void configure() {
        File root = new File("data");

        bindConstant().annotatedWith(Names.named("port")).to(8080);
        bind(Authenticator.class).to(FakeAuthenticator.class);
        bind(File.class).annotatedWith(named("planning.root")).toInstance(root);
      }
    }).start(8080);
  }
}
