import com.google.inject.Guice;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import com.sun.net.httpserver.HttpServer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class PlanningServer {
  private HttpServer server;

  public static void main(String[] args) throws Exception {
    int port;
    try {
      port = parseInt(System.getenv("PORT"));
    } catch (NumberFormatException e) {
      port = 8080;
    }
    System.out.println("PORT: " + port);
    new PlanningServer().start(port);
  }

  public void start(int port) throws IOException {
    ResourceConfig config = new DefaultResourceConfig(JacksonJsonProvider.class, PlanningResource.class);
    IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(config, Guice.createInjector());

    server = HttpServerFactory.create(String.format("http://localhost:%d/", port), config, ioc);
    server.start();
  }

  public void stop() {
    server.stop(0);
  }
}
