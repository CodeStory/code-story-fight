import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.NotFoundException;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Charsets.UTF_8;

@Path("/")
@Singleton
public class CodeStoryResource {
  private Planning planning;

  @Inject
  public CodeStoryResource(Planning planning) throws IOException {
    this.planning = planning;
  }

  @GET
  public Response index() {
    return Response.seeOther(URI.create("planning.html")).build();
  }

  @POST
  @Path("register")
  public void register(@FormParam("login") String login, @FormParam("talkId") String talkId) {
    planning.register(login, talkId);
  }

  @POST
  @Path("unregister")
  public void unregister(@FormParam("login") String login, @FormParam("talkId") String talkId) {
    planning.unregister(login, talkId);
  }

  @GET
  @Path("registrations/{login}")
  @Produces("application/json;charset=UTF-8")
  public Iterable<String> myRegistrations(@PathParam("login") String login) {
    return planning.registrations(login);
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
    return readFile("hogan.js")
      + readFile("jquery.js")
      + readFile("underscore.js")
      + readFile("codestory.js");
  }

  @GET
  @Path("{path : .*}")
  public Response staticResource(@PathParam("path") String path) throws IOException {
    File file = file(path);
    String mimeType = new MimetypesFileTypeMap().getContentType(file);
    return Response.ok(file, mimeType).build();
  }

  static String readFile(String path) throws IOException {
    return Files.toString(file(path), UTF_8);
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
