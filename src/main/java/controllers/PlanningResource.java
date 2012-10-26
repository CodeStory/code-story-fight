package controllers;

import auth.AuthenticationException;
import auth.Authenticator;
import auth.User;
import auth.Users;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.NotFoundException;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;
import planning.Planning;
import templating.ContentWithVariables;
import templating.Layout;
import templating.Template;
import templating.YamlFrontMatter;
import twitter4j.TwitterException;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

import static java.lang.Long.parseLong;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/")
@Singleton
public class PlanningResource {
  private final Planning planning;
  private final Users users;
  private final Authenticator authenticator;

  @Inject
  public PlanningResource(Planning planning, Users users, Authenticator authenticator) {
    this.planning = planning;
    this.users = users;
    this.authenticator = authenticator;
  }

  @GET
  public Response index() {
    return Response.seeOther(URI.create("index.html")).build();
  }

  @GET
  @Path("/authenticate")
  public Response authenticate() throws MalformedURLException, TwitterException, URISyntaxException {
    return Response.seeOther(authenticator.getAuthenticateURL().toURI()).build();
  }

  @GET
  @Path("/authenticated")
  public Response authenticated(@QueryParam("oauth_token") String oauthToken, @QueryParam("oauth_verifier") String oauthVerifier) {
    try {
      User user = authenticator.authenticate(oauthToken, oauthVerifier);
      users.add(user);
      return planning().cookie(new NewCookie("userId", user.getId().toString(), "/", null, null, 60 * 60 * 24 * 7, false)).build();
    } catch (IllegalStateException e) {
      return index();
    } catch (AuthenticationException e) {
      return index();
    }
  }

  @GET
  @Path("/logout")
  public Response logout(@CookieParam("userId") String userId) {
    rejectUnauthenticated(userId);
    users.remove(parseLong(userId));
    return planning().cookie(new NewCookie("userId", null, "/", null, null, 0, false)).build();
  }

  private void rejectUnauthenticated(String userId) {
    if (userId == null) {
      throw new WebApplicationException(FORBIDDEN);
    }
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
  public Response script() {
    String body = read("js/jquery.js") +
      read("js/jquery.cookie.js") +
      read("js/underscore.js") +
      read("js/hogan.js") +
      read("js/codestory.js");

    Response.ResponseBuilder response = Response.ok(body, "application/javascript");
    return setCache(file("js/codestory.js"), response).build();
  }

  @GET
  @Path("{path : .*}.css")
  public synchronized Response style(@PathParam("path") String path) throws IOException, LessException {
    File output = new File("target", path + ".css");
    new LessCompiler().compile(file(path + ".less"), output, false);
    return staticResource(output);
  }

  @GET
  @Path("{path : .*\\.html}")
  public Response html(@PathParam("path") String path) {
    ContentWithVariables yamlContent = new YamlFrontMatter().parse(read(path));
    String content = yamlContent.getContent();
    Map<String, String> variables = yamlContent.getVariables();

    String layout = variables.get("layout");
    if (layout != null) {
      content = new Layout(read(layout)).apply(content);
    }

    String body = new Template().apply(content, variables);

    return Response.ok(body, "text/html;charset=UTF-8").build();
  }

  @GET
  @Path("{path : .*}")
  public Response staticResource(@PathParam("path") String path) {
    return staticResource(file(path));
  }

  private Response.ResponseBuilder planning() {
    return Response.seeOther(URI.create("planning.html"));
  }

  private Response staticResource(File file) {
    String mimeType = new MimetypesFileTypeMap().getContentType(file);

    Response.ResponseBuilder response = Response.ok(file, mimeType);
    return setCache(file, response).build();
  }

  private Response.ResponseBuilder setCache(File file, Response.ResponseBuilder response) {
    return response
        .lastModified(new Date(file.lastModified()))
        .expires(new Date(file.lastModified() + 1000L * 3600 * 24 * 30));
  }

  private String read(String path) {
    try {
      return Files.toString(file(path), Charsets.UTF_8);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private File file(String path) {
    if (path.endsWith("/")) {
      throw new NotFoundException();
    }

    // Remove version
    path = path.replaceFirst("version-[^/]*/", "");

    try {
      File root = new File("web");
      File file = new File(root, path);
      if (!file.exists() || !file.getCanonicalPath().startsWith(root.getCanonicalPath())) {
        throw new NotFoundException();
      }

      return file;
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }
}
