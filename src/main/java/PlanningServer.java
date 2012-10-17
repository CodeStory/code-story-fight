import com.google.inject.Guice;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import com.sun.net.httpserver.HttpServer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.io.IOException;

import static com.google.common.base.Objects.firstNonNull;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class PlanningServer {
  public static final String DEFAULT_PORT = "8080";

  private HttpServer server;

  public static void main(String[] args) throws IOException {
    int port = parseInt(firstNonNull(System.getenv("PORT"), DEFAULT_PORT));

    new PlanningServer().start(port);
  }

  public void start(int port) throws IOException {
    ResourceConfig config = configuration();
    IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(config, Guice.createInjector());

    System.out.println("Starting server on port: " + port);

    server = HttpServerFactory.create(format("http://localhost:%d/", port), config, ioc);
    server.start();
  }

  private ResourceConfig configuration() {
    return new DefaultResourceConfig(
      JacksonJsonProvider.class,
      PlanningResource.class);
  }

  public void stop() {
    server.stop(0);
  }
}
