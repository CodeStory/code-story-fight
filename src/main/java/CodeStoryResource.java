import auth.AuthenticationException;
import auth.Authenticator;
import auth.User;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.NotFoundException;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;
import twitter4j.TwitterException;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.*;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/")
@Singleton
public class CodeStoryResource {
  private Planning planning;
  private Users users;

  @Inject
  public CodeStoryResource(Planning planning, Users users) throws IOException {
    this.planning = planning;
    this.users = users;
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
      users.add(user);
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
