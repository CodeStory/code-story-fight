package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;
import planning.Planning;
import templating.ContentWithVariables;
import templating.Layout;
import templating.Template;
import templating.YamlFrontMatter;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/")
@Singleton
public class PlanningResource extends AbstractResource {
  private final Planning planning;

  @Inject
  public PlanningResource(Planning planning) {
    this.planning = planning;
  }

  @GET
  public Response redirectToIndex() {
    return seeOther("index.html");
  }

  @GET
  @Path("planning.json")
  @Produces("application/javascript;charset=UTF-8")
  public Response planning() {
    return ok(file("planning.json")); // TODO add small cache duration
  }

  @POST
  @Path("star")
  public void star(@CookieParam("userId") String userId, @FormParam("talkId") String talkId) {
    rejectUnauthenticated(userId);

    planning.star(userId, talkId);
  }

  @POST
  @Path("unstar")
  public void unstar(@CookieParam("userId") String userId, @FormParam("talkId") String talkId) {
    rejectUnauthenticated(userId);

    planning.unstar(userId, talkId);
  }

  @GET
  @Path("stars")
  @Produces("application/json;charset=UTF-8")
  public Iterable<String> stars(@CookieParam("userId") String userId) {
    rejectUnauthenticated(userId);

    return planning.stars(userId);
  }

  @GET
  @Path("{path : .*}/main.js")
  @Produces("application/javascript;charset=UTF-8")
  public Response main_script() {
    String body = read("js/jquery.js") + read("js/jquery.cookie.js") + read("js/underscore.js") + read("js/hogan.js") + read("js/codestory.js");

    return ok(body, file("js/codestory.js").lastModified());
  }

  @GET
  @Path("{path : .*}/index.js")
  @Produces("application/javascript;charset=UTF-8")
  public Response index_script() {
    String body = read("js/jquery.js") + read("js/jquery.countdown.js") + read("js/index.js");

    return ok(body, file("js/index.js").lastModified());
  }

  @GET
  @Path("{path : .*}.css")
  @Produces("text/css;charset=UTF-8")
  public synchronized Response style(@PathParam("path") String path) throws IOException, LessException {
    File output = new File("target", path + ".css");
    new LessCompiler().compile(file(path + ".less"), output, false);

    return templatize(read(output));
  }

  @GET
  @Path("{path : .*\\.html}")
  @Produces("text/html;charset=UTF-8")
  public Response html(@PathParam("path") String path) {
    return templatize(read(path));
  }

  private Response templatize(String text) {
    ContentWithVariables yamlContent = new YamlFrontMatter().parse(text);
    String content = yamlContent.getContent();
    Map<String, String> variables = yamlContent.getVariables();

    String layout = variables.get("layout");
    if (layout != null) {
      content = new Layout(read(layout)).apply(content);
    }

    String body = new Template().apply(content, variables);

    return ok(body);
  }

  @GET
  @Path("{path : .*}")
  public Response staticResource(@PathParam("path") String path) {
    File file = file(path);
    String mimeType = new MimetypesFileTypeMap().getContentType(file);

    return ok(file, mimeType, file.lastModified());
  }

  private static void rejectUnauthenticated(String userId) {
    if (userId == null) {
      throw new WebApplicationException(FORBIDDEN);
    }
  }
}
