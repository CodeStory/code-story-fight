package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import planning.Planning;

import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

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
  @Path("{path : .*\\.html}")
  @Produces("text/html;charset=UTF-8")
  public Response html(@PathParam("path") String path) {
    return templatize(read(path));
  }

  private static String rejectUnauthenticated(String userId) {
    if (userId == null) {
      throw new WebApplicationException(FORBIDDEN);
    }
    return userId;
  }
}
