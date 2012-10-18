import auth.Authenticator;
import auth.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanningResourceTest {
  @Mock
  Planning planning;
  @Mock
  PlanningLoader planningLoader;
  @Mock
  Authenticator authenticator;
  @Mock
  Users users;

  @InjectMocks
  PlanningResource resource;

  @Test
  public void should_register_user_for_talk() {
    resource.register("user", "talkId");

    verify(planning).register("user", "talkId");
  }

  @Test
  public void should_unregister_user_for_talk() {
    resource.unregister("user", "talkId");

    verify(planning).unregister("user", "talkId");
  }

  @Test
  public void should_get_my_registrations() {
    when(planning.registrations("user")).thenReturn(Arrays.asList("talkId1", "talkId2"));

    Iterable<String> talkIds = resource.myRegistrations("user");

    assertThat(talkIds).containsOnly("talkId1", "talkId2");
  }

  @Test
  public void should_redirect_to_twitter() throws Exception {
    when(authenticator.getAuthenticateURL()).thenReturn(new URL("http://exemple.com/"));

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
    User user = new User(42L, "screenName", "token", "secret");
    when(authenticator.authenticate("oauthVerifier")).thenReturn(user);

    Response response = resource.authenticated("oauthToken", "oauthVerifier");

    verify(users).add(user);
    Map<String, List<Object>> metadata = assertRedirected(response, "planning.html");
    List<Object> cookies = metadata.get("Set-Cookie");
    assertThat(cookies).hasSize(1);
    Cookie userIdCookie = (Cookie) cookies.get(0);
    assertThat(userIdCookie.getName()).isEqualTo("userId");
    assertThat(userIdCookie.getValue()).isEqualTo("42");
  }
}
