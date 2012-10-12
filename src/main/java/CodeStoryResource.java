import com.google.common.io.Files;
import com.sun.jersey.api.NotFoundException;
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
import java.net.URI;

import static com.google.common.base.Charsets.UTF_8;

@Path("/")
public class CodeStoryResource {
  @GET
  public Response index() {
    return Response.seeOther(URI.create("planning.html")).build();
  }

  @GET
  @Path("{path : .*\\.less}")
  public File style(@PathParam("path") String path) throws IOException, LessException {
    File output = new File("target", path + ".css");
    new LessCompiler().compile(file(path), output, false);
    return output;
  }

  @GET
  @Path("codestory.js")
  @Produces("application/javascript;charset=UTF-8")
  public String javascript() throws IOException {
    return Files.toString(file("hogan.js"), UTF_8) + //
      Files.toString(file("jquery.js"), UTF_8) + //
      Files.toString(file("codestory.js"), UTF_8);
  }

  @GET
  @Path("{path : .*}")
  public Response staticResource(@PathParam("path") String path) throws IOException {
    File file = file(path);
    String mimeType = new MimetypesFileTypeMap().getContentType(file);
    return Response.ok(file, mimeType).build();
  }

  static File file(String path) throws IOException {
    File root = new File("web");
    File file = new File(root, path);
    if (!file.exists() || !file.getCanonicalPath().startsWith(root.getCanonicalPath())) {
      throw new NotFoundException();
    }
    return file;
  }
}
