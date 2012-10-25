import auth.AuthenticationException;
import auth.Authenticator;
import auth.User;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.NotFoundException;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;
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
import javax.ws.rs.core.CacheControl;
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
  public PlanningResource(Planning planning, Users users, PlanningLoader planningLoader, Authenticator authenticator) {
    this.planning = planning;
    this.users = users;
    this.authenticator = authenticator;
    planningLoader.createTalks(planning, read("planning.json"));
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
  public Response authenticated(@QueryParam("oauth_verifier") String oauthVerifier) {
    try {
      User user = authenticator.authenticate(oauthVerifier);
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
  @Path("register")
  public void register(@CookieParam("userId") String userId, @FormParam("talkId") String talkId) {
    rejectUnauthenticated(userId);
    planning.register(userId, talkId);
  }

  @POST
  @Path("unregister")
  public void unregister(@CookieParam("userId") String userId, @FormParam("talkId") String talkId) {
    rejectUnauthenticated(userId);
    planning.unregister(userId, talkId);
  }

  @GET
  @Path("registrations")
  @Produces("application/json;charset=UTF-8")
  public Iterable<String> myRegistrations(@CookieParam("userId") String userId) {
    rejectUnauthenticated(userId);
    return planning.registrations(userId);
  }

  @GET
  @Path("main.js")
  @Produces("application/javascript")
  public String script() {
    return read("js/jquery.js") +
      read("js/jquery.cookie.js") +
      read("js/underscore.js") +
      read("js/hogan.js") +
      read("js/codestory.js");
  }

  @GET
  @Path("{path : .*\\.less}.css")
  public synchronized Response style(@PathParam("path") String path) throws IOException, LessException {
    File output = new File("target", path + ".css");
    new LessCompiler().compile(file(path), output, false);
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

    return Response.ok(body, "text/html").build();
  }

  @GET
  @Path("{path : .*}")
  public Response staticResource(@PathParam("path") String path) {
    return staticResource(file(path));
  }

  static Response.ResponseBuilder planning() {
    return Response.seeOther(URI.create("planning.html"));
  }

  static Response staticResource(File file) {
    String mimeType = new MimetypesFileTypeMap().getContentType(file);
    return Response.ok(file, mimeType).cacheControl(buildCacheControl()).lastModified(new Date()).build();
  }

  static CacheControl buildCacheControl() {
    CacheControl cacheControl = new CacheControl();
    // cacheControl.setMaxAge(3600); // 1 hour
    return cacheControl;
  }

  static String read(String path) {
    try {
      return Files.toString(file(path), Charsets.UTF_8);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  static File file(String path) {
    if (path.endsWith("/")) {
      throw new NotFoundException();
    }

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
