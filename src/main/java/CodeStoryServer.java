import com.sun.jersey.api.container.httpserver.HttpServerFactory;

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
    HttpServerFactory.create("http://localhost:8080/").start();
  }
}
