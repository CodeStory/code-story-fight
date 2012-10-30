package controllers;

import com.google.inject.Singleton;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;

@Path("/static")
@Singleton
public class StaticResource extends AbstractResource {
  @GET
  @Path("{path : .*}/planning.js")
  @Produces("application/javascript;charset=UTF-8")
  public Response planning() {
    return concat("js/jquery.js", "js/jquery.cookie.js", "js/underscore.js", "js/hogan.js", "js/planning.js");
  }

  @GET
  @Path("{path : .*}/index.js")
  @Produces("application/javascript;charset=UTF-8")
  public Response index() {
    return concat("js/jquery.js", "js/jquery.countdown.js", "js/index.js");
  }

  @GET
  @Path("{path : .*}.css")
  @Produces("text/css;charset=UTF-8")
  public synchronized Response css(@PathParam("path") String path) throws IOException, LessException {
    File output = new File("target", path + ".css");
    new LessCompiler().compile(file(path + ".less"), output, false);

    return templatize(read(output));
  }

  @GET
  @Path("{path : .*}")
  public Response image(@PathParam("path") String path) {
    File file = file(path);
    String mimeType = new MimetypesFileTypeMap().getContentType(file);
    return ok(file, mimeType, file.lastModified());
  }
}
