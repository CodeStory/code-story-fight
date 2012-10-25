import auth.FakeAuthenticatorResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import com.sun.net.httpserver.HttpServer;
import config.PlanningServerModule;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.io.IOException;

import static com.google.common.base.Objects.firstNonNull;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class PlanningServer {
  public static final String DEFAULT_PORT = "8080";

  private HttpServer server;
  private final Module[] overridedModules;

  public static void main(String[] args) throws IOException {
    int port = parseInt(firstNonNull(System.getenv("PORT"), DEFAULT_PORT));

    new PlanningServer(new Module[0]).start(port);
  }

  public PlanningServer(Module[] overridedModules) {
    this.overridedModules = overridedModules;
  }

  public void start(int port) throws IOException {
    ResourceConfig config = configuration();
    Injector injector = Guice.createInjector(Modules.override(new PlanningServerModule()).with(overridedModules));
    IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(config, injector);

    System.out.println("Starting server on port: " + port);

    server = HttpServerFactory.create(format("http://localhost:%d/", port), config, ioc);
    server.start();
  }

  private ResourceConfig configuration() {
    return new DefaultResourceConfig(
      JacksonJsonProvider.class,
      PlanningResource.class,
      FakeAuthenticatorResource.class);
  }

  public void stop() {
    server.stop(0);
  }
}
