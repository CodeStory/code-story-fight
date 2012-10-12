import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class CodeStoryServer {
  private HttpServer server;

  public static void main(String[] args) throws Exception {
    int port = parseInt(System.getenv("PORT"));
    System.out.println("PORT: " + port);
    new CodeStoryServer().start(port);
  }

  public void start(int port) throws IOException {
    ResourceConfig config = new DefaultResourceConfig(CodeStoryResource.class);
    server = HttpServerFactory.create(String.format("http://localhost:%d/", port), config);
    server.start();
  }

  public void stop() {
    server.stop(0);
  }
}
