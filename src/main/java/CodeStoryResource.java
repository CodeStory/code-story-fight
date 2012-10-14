import auth.AuthenticationException;
import auth.Authenticator;
import auth.User;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.NotFoundException;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;
import twitter4j.TwitterException;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

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
    return Response.seeOther(URI.create("index.html")).build();
  }

  @GET
  @Path("/authenticate")
  public Response authenticate() throws MalformedURLException, TwitterException, URISyntaxException {
    return Response.seeOther(new Authenticator().getAuthenticateURL().toURI()).build();
  }

  @GET
  @Path("/authenticated")
  public Response authenticated(@QueryParam("oauth_token") String oauthToken, @QueryParam("oauth_verifier") String oauthVerifier) {
    try {
      User user = new Authenticator().authenticate(oauthVerifier);
      Users.add(user);
      return index();
    } catch (IllegalStateException e) {
      return index();
    } catch (AuthenticationException e) {
      return index();
    }
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
  @Path("{path : .*\\.less}.css")
  public File style(@PathParam("path") String path) throws IOException, LessException {
    File output = new File("target", path + ".css");
    new LessCompiler().compile(file(path), output, false);
    return output;
  }

  @GET
  @Path("codestory.js")
  @Produces("application/javascript;charset=UTF-8")
  public String javascript() throws IOException {
    return readFile("js/hogan.js")
      + readFile("js/jquery.js")
      + readFile("js/underscore.js")
      + readFile("js/codestory.js");
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
