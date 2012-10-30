package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;
import planning.Planning;

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

  @POST
  @Path("star")
  public void star(@CookieParam("userId") String userId, @FormParam("talkId") String talkId) {
    planning.star(rejectUnauthenticated(userId), talkId);
  }

  @POST
  @Path("unstar")
  public void unstar(@CookieParam("userId") String userId, @FormParam("talkId") String talkId) {
    planning.unstar(rejectUnauthenticated(userId), talkId);
  }

  @GET
  @Path("stars")
  @Produces("application/json;charset=UTF-8")
  public Iterable<String> stars(@CookieParam("userId") String userId) {
    return planning.stars(rejectUnauthenticated(userId));
  }

  @GET
  @Path("planning.json")
  @Produces("application/javascript;charset=UTF-8")
  public Response planning() {
    return ok(file("planning.json")); // TODO add small cache duration
  }

  @GET
  @Path("{path : .*}/planning.js")
  @Produces("application/javascript;charset=UTF-8")
  public Response main_script() {
    return concat("js/jquery.js", "js/jquery.cookie.js", "js/underscore.js", "js/hogan.js", "js/planning.js");
  }

  @GET
  @Path("{path : .*}/index.js")
  @Produces("application/javascript;charset=UTF-8")
  public Response index_script() {
    return concat("js/jquery.js", "js/jquery.countdown.js", "js/index.js");
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

  @GET
  @Path("{path : .*}")
  public Response staticResource(@PathParam("path") String path) {
    File file = file(path);
    String mimeType = new MimetypesFileTypeMap().getContentType(file);
    return ok(file, mimeType, file.lastModified());
  }

  private static String rejectUnauthenticated(String userId) {
    if (userId == null) {
      throw new WebApplicationException(FORBIDDEN);
    }
    return userId;
  }
}
