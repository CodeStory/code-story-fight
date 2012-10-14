package auth;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import twitter4j.TwitterException;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Boolean.TRUE;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class AuthenticatorTest {

  public static final String UNAUTHORIZED = "code-story.test.unauthorized";

  @BeforeClass
  public static void setupHTTPMock() {
    System.setProperty("java.protocol.handler.pkgs", "auth");
  }

  @Test
  public void should_generate_an_authentication_URL() throws Exception {
    Authenticator authenticator = new Authenticator();

    URL authenticateURL = authenticator.getAuthenticateURL();

    assertThat(authenticateURL).isEqualTo(new URL("http", "api.twitter.com", "/oauth/authenticate?oauth_token=NPcudxy0yU5T3tBzho7iCotZ3cnetKwcTIRlX0iwRl0"));
  }

  @Test
  public void should_authenticate() throws MalformedURLException, TwitterException {
    Authenticator authenticator = new Authenticator();
    authenticator.getAuthenticateURL();

    User user = authenticator.authenticate("oauthVerifierProvidedByCallBackAfterAuthenticateURL");

    assertThat(user.id).isEqualTo(7588892);
    assertThat(user.screenName).isEqualTo("seblm");
    assertThat(user.token).isEqualTo("7588892-kagSNqWge8gB1WwE3plnFsJHAZVfxWD7Vb57p0b4");
    assertThat(user.secret).isEqualTo("PbKfYqSryyeKDWz4ebtY3o5ogNLG11WJuZBc9fQrQo");
  }

  @Test(expected = AuthenticationException.class)
  public void should_raise_exception_when_authentication_fails() throws MalformedURLException, TwitterException {
    Authenticator authenticator = new Authenticator();
    authenticator.getAuthenticateURL();
    nextRequestWillBeUnauthorized();

    authenticator.authenticate("");
  }

  @Test
  public void should_restore_user() {
    Authenticator authenticator = new Authenticator(new User(7588892L, "seblm", "7588892-kagSNqWge8gB1WwE3plnFsJHAZVfxWD7Vb57p0b4", "PbKfYqSryyeKDWz4ebtY3o5ogNLG11WJuZBc9fQrQo"));

    boolean isAuthenticated = authenticator.isAuthenticated();

    assertThat(isAuthenticated).isTrue();
  }

  @Test
  public void should_not_restore_user() {
    Authenticator authenticator = new Authenticator(new User(33094L, "unknown", "33094-badToken", "badSecret"));
    nextRequestWillBeUnauthorized();

    boolean isAuthenticated = authenticator.isAuthenticated();

    assertThat(isAuthenticated).isFalse();
  }

  private void nextRequestWillBeUnauthorized() {
    System.setProperty(UNAUTHORIZED, TRUE.toString());
  }

  @After
  public void resetHttpResponseCode() {
    System.clearProperty(UNAUTHORIZED);
  }

  @AfterClass
  public static void resetDefaultHTTPHandler() {
    System.clearProperty("java.protocol.handler.pkgs");
  }
}
