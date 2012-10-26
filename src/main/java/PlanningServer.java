import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import com.sun.net.httpserver.HttpServer;
import config.PlanningServerModule;
import controllers.FakeAuthenticatorResource;
import controllers.PlanningResource;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.io.IOException;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.inject.util.Modules.override;
import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS;
import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class PlanningServer {
  public static final String DEFAULT_PORT = "8080";

  private final Module[] modules;
  private HttpServer server;

  public PlanningServer(Module... modules) {
    this.modules = modules;
  }

  public static void main(String[] args) throws IOException {
    int port = parseInt(firstNonNull(System.getenv("PORT"), DEFAULT_PORT));

    new PlanningServer().start(port);
  }

  public void start(int port) throws IOException {
    ResourceConfig config = configuration();
    Injector injector = injector();
    IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(config, injector);

    System.out.println("Starting server on port: " + port);

    server = HttpServerFactory.create(format("http://localhost:%d/", port), config, ioc);
    server.start();
  }

  public void stop() {
    server.stop(0);
  }

  private ResourceConfig configuration() {
    DefaultResourceConfig config = new DefaultResourceConfig(
        JacksonJsonProvider.class,
        PlanningResource.class,
        FakeAuthenticatorResource.class);

    config.getProperties().put(PROPERTY_CONTAINER_REQUEST_FILTERS, GZIPContentEncodingFilter.class);
    config.getProperties().put(PROPERTY_CONTAINER_RESPONSE_FILTERS, GZIPContentEncodingFilter.class);

    return config;
  }

  private Injector injector() {
    return Guice.createInjector(override(new PlanningServerModule()).with(modules));
  }
}
