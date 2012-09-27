import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.File;
import java.io.IOException;

import static java.lang.Integer.parseInt;

@Path("/")
public class CodeStoryServer {
  private HttpServer server;

  @GET
  public File index() {
    return new File("index.html");
  }

  public static void main(String[] args) throws Exception {
    int port = parseInt(System.getenv("PORT"));
    new CodeStoryServer().start(port);
  }

  public void start(int port) throws IOException {
    ResourceConfig config = new DefaultResourceConfig(CodeStoryServer.class);
    server = HttpServerFactory.create(String.format("http://localhost:%d/", port), config);
    server.start();
  }

  public void stop() {
    server.stop(0);
  }
}
