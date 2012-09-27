import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.File;

@Path("/")
public class CodeStoryServer {
  @GET
  public File index() {
    return new File("index.html");
  }

  public static void main(String[] args) throws Exception {
    ResourceConfig config = new DefaultResourceConfig(CodeStoryServer.class);
    HttpServer server = HttpServerFactory.create("http://localhost:8080/", config);
    server.start();
  }
}
