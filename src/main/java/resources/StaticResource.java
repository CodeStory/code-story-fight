package resources;

import com.google.inject.Singleton;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;

@Path("/static/{version : [^/]*}/")
@Singleton
public class StaticResource extends AbstractResource {
  @GET
  @Path("planning.js")
  @Produces("application/javascript;charset=UTF-8")
  public Response planning() {
    return concat("js/jquery.js", "js/purl.js", "js/jquery.scrollTo.min.js", "js/jquery.cookie.js", "js/underscore.js", "js/hogan.js", "js/planning.js");
  }

  @GET
  @Path("index.js")
  @Produces("application/javascript;charset=UTF-8")
  public Response index() {
    return concat("js/jquery.js", "js/index.js");
  }

  @GET
  @Path("{path : .*}.css")
  @Produces("text/css;charset=UTF-8")
  public synchronized Response css(@PathParam("path") String path) throws IOException, LessException {
    File output = new File("target", path + ".css");
    new LessCompiler().compile(file(path + ".less"), output, false);
    return ok(templatize(read(output)), output.lastModified());
  }

  @GET
  @Path("{path : .*\\.png}")
  @Produces("image/png")
  public Response png(@PathParam("path") String path) {
    return ok(file(path), file(path).lastModified());
  }

  @GET
  @Path("{path : .*\\.jpg}")
  @Produces("image/jpeg")
  public Response jpeg(@PathParam("path") String path) {
    return ok(file(path), file(path).lastModified());
  }
}
