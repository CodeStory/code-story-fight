import auth.AuthenticationException;
import auth.Authenticator;
import auth.User;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.NotFoundException;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;
import twitter4j.TwitterException;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@Path("/fakeauthenticate")
@Singleton
public class FakeAuthenticatorResource {

  @GET
  public Response fakeAuthneticate() {
    return Response.seeOther(URI.create("/authenticated?oauthToken=foo&oauthVerifier=bar")).build();
  }

}
