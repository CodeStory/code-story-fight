package resources;

import auth.Authenticator;
import auth.User;
import auth.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationResourceTest {
  @Mock
  private Authenticator authenticator;
  @Mock
  private Users users;

  @InjectMocks
  private AuthenticationResource resource;

  @Test
  public void should_redirect_to_twitter() throws Exception {
    when(authenticator.getAuthenticateURI()).thenReturn(URI.create("http://exemple.com/"));

    Response response = resource.authenticate();

    assertRedirected(response, "http://exemple.com/");
  }

  private Map<String, List<Object>> assertRedirected(Response response, String location) {
    assertThat(response.getStatus()).isEqualTo(Response.Status.SEE_OTHER.getStatusCode());
    Map<String, List<Object>> metadata = response.getMetadata();
    assertThat(metadata).includes(entry("Location", newArrayList(URI.create(location))));
    return metadata;
  }

  @Test
  public void with_granted_user_should_authenticate_on_twitter_callback() throws Exception {
    User user = new User(42L, "arnold", "token", "secret");
    when(authenticator.authenticate("oauthToken", "oauthVerifier")).thenReturn(user);

    Response response = resource.authenticated("oauthToken", "oauthVerifier");

    verify(users).add(user);
    Map<String, List<Object>> metadata = assertRedirected(response, "planning.html");
    List<Object> cookies = metadata.get("Set-Cookie");
    assertThat(cookies).hasSize(2);
    Cookie userIdCookie = (Cookie) cookies.get(0);
    assertThat(userIdCookie.getName()).isEqualTo("userId");
    assertThat(userIdCookie.getValue()).isEqualTo("42");
    Cookie screenNameCookie = (Cookie) cookies.get(1);
    assertThat(screenNameCookie.getName()).isEqualTo("screenName");
    assertThat(screenNameCookie.getValue()).isEqualTo("arnold");
  }
}
